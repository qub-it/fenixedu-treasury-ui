package org.fenixedu.treasury.services.integration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import oecd.standardauditfile_tax.pt_1.AddressStructure;
import oecd.standardauditfile_tax.pt_1.AddressStructurePT;
import oecd.standardauditfile_tax.pt_1.AuditFile;
import oecd.standardauditfile_tax.pt_1.Header;
import oecd.standardauditfile_tax.pt_1.MovementTax;
import oecd.standardauditfile_tax.pt_1.OrderReferences;
import oecd.standardauditfile_tax.pt_1.SAFTPTMovementTaxType;
import oecd.standardauditfile_tax.pt_1.SAFTPTSourceBilling;
import oecd.standardauditfile_tax.pt_1.SourceDocuments;
import oecd.standardauditfile_tax.pt_1.SourceDocuments.MovementOfGoods;
import oecd.standardauditfile_tax.pt_1.SourceDocuments.WorkingDocuments.WorkDocument;
import oecd.standardauditfile_tax.pt_1.Tax;
import oecd.standardauditfile_tax.pt_1.TaxTableEntry;

import org.fenixedu.bennu.io.domain.GenericFile;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.Vat;
import org.fenixedu.treasury.domain.VatExemptionReason;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.document.FinantialDocument;
import org.fenixedu.treasury.domain.document.FinantialDocumentEntry;
import org.fenixedu.treasury.domain.document.InvoiceEntry;
import org.fenixedu.treasury.domain.integration.ExportOperation;
import org.fenixedu.treasury.domain.integration.OperationFile;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

// ******************************************************************************************************************************
// http://info.portaldasfinancas.gov.pt/NR/rdonlyres/3B4FECDB-2380-45D7-9019-ABCA80A7E99E/0/Comunicacao_Dados_Doc_Transporte.pdf
// http://info.portaldasfinancas.gov.pt/NR/rdonlyres/15D18787-8AA9-4060-90D5-79F168A927A4/0/Portaria_11922009.pdf
// (Documento Original)
// http://dre.pt/pdf1sdip/2012/11/22700/0672406740.pdf (Adenda para os
// Documentos de Transporte)
// ******************************************************************************************************************************
public class SAFTExporter {

	private static Logger logger = LoggerFactory.getLogger(SAFTExporter.class);
	// public final static String SAFT_HEADER_VERSION_1_02_01 = "1.02_01";
	public final static String SAFT_HEADER_VERSION_1_03_01 = "1.03_01";

	private void generateSaft(FinantialInstitution institution,
			DateTime fromDate, DateTime toDate, ExportOperation operation,
			List<? extends FinantialDocument> allDocuments,
			Boolean generateAllCustomersAndProducts) throws Exception {

		// Build SAFT-AuditFile
		AuditFile auditFile = new AuditFile();
		// ThreadInformation information =
		// SaftThreadRegister.retrieveCurrentThreadInformation();

		// Build SAFT-HEADER (Chapter 1 in AuditFile)
		Header header = this.createSAFTHeader(fromDate, toDate, institution,
				SAFT_HEADER_VERSION_1_03_01);
		// SetHeader
		auditFile.setHeader(header);

		// Build Master-Files
		oecd.standardauditfile_tax.pt_1.AuditFile.MasterFiles masterFiles = new oecd.standardauditfile_tax.pt_1.AuditFile.MasterFiles();

		// SetMasterFiles
		auditFile.setMasterFiles(masterFiles);

		// Build SAFT-MovementOfGoods (Customer and Products are built inside)
		// ProductsTable (Chapter 2.4 in AuditFile)
		List<oecd.standardauditfile_tax.pt_1.Product> productList = masterFiles
				.getProduct();
		Map<String, oecd.standardauditfile_tax.pt_1.Product> productMap = new HashMap<String, oecd.standardauditfile_tax.pt_1.Product>();
		Set<String> productCodes = new HashSet<String>();

		// ClientsTable (Chapter 2.2 in AuditFile)
		List<oecd.standardauditfile_tax.pt_1.Customer> customerList = masterFiles
				.getCustomer();
		Map<String, oecd.standardauditfile_tax.pt_1.Customer> customerMap = new HashMap<String, oecd.standardauditfile_tax.pt_1.Customer>();

		// Readd All Products and Clients if needed
		if (generateAllCustomersAndProducts) {
			logger.info("Reading all Customers and Products in Store "
					+ institution.getCode());

			Set<Customer> allCustomers = new HashSet<Customer>();
			for (DebtAccount debt : institution.getDebtAccountsSet()) {
				allCustomers.add(debt.getCustomer());
			}
			Set<Product> allProducts = institution.getAvailableProductsSet();

			// Update the Total Objects Count
			// information.setTotalCounter(allCustomers.size() +
			// allProducts.size() + allDocuments.size() * 10);

			int i = 0;
			for (Customer customer : allCustomers) {
				oecd.standardauditfile_tax.pt_1.Customer saftCustomer = this
						.convertCustomerToSAFTCustomer(customer);
				// information.setCurrentCounter(information.getCurrentCounter()
				// + 1);
				customerMap.put(saftCustomer.getCustomerID(), saftCustomer);
				i++;
				if (i % 100 == 0) {
					logger.info("Processing " + i + "/" + allCustomers.size()
							+ " Customers in Store " + institution.getCode());
				}
			}

			i = 0;
			for (Product product : allProducts) {
				if (!productCodes.contains(product.getCode())) {
					oecd.standardauditfile_tax.pt_1.Product saftProduct = this
							.convertProductToSAFTProduct(product);
					productCodes.add(product.getCode());
					productMap.put(saftProduct.getProductCode(), saftProduct);
				}

				i++;
				if (i % 100 == 0) {
					logger.info("Processing " + i + "/" + allProducts.size()
							+ " Products in Store " + institution.getCode());
				}

				// information.setCurrentCounter(information.getCurrentCounter()
				// + 1);
			}
		} else {
			// information.setTotalCounter(allDocuments.size() * 10);
			// Update the Total Objects Count
			// information.setCurrentCounter(0);
		}

		// TaxTable (Chapter 2.5 in AuditFile)
		oecd.standardauditfile_tax.pt_1.TaxTable taxTable = new oecd.standardauditfile_tax.pt_1.TaxTable();
		masterFiles.setTaxTable(taxTable);

		for (Vat vat : institution.getVatsSet()) {
			taxTable.getTaxTableEntry().add(
					this.convertVATtoTaxTableEntry(vat, institution));
		}

		// Set MovementOfGoods in SourceDocuments(AuditFile)
		oecd.standardauditfile_tax.pt_1.SourceDocuments sourceDocuments = new oecd.standardauditfile_tax.pt_1.SourceDocuments();
		auditFile.setSourceDocuments(sourceDocuments);

		SourceDocuments.SalesInvoices invoices = new SourceDocuments.SalesInvoices();

		SourceDocuments.WorkingDocuments workingDocuments = new SourceDocuments.WorkingDocuments();

		MovementOfGoods movementOfGoods = new MovementOfGoods();
		BigInteger sumOfNumberMovementLines = BigInteger.ZERO;
		BigDecimal sumOfTotalQuantityIssued = BigDecimal.ZERO;
		BigInteger numberOfWorkingDocuments = BigInteger.ZERO;
		BigDecimal totalDebitOfWorkingDocuments = BigDecimal.ZERO;
		BigDecimal totalCreditOfWorkingDocuments = BigDecimal.ZERO;

		invoices.setNumberOfEntries(BigInteger.ZERO);
		invoices.setTotalCredit(BigDecimal.ZERO);
		invoices.setTotalDebit(BigDecimal.ZERO);

		// persistenceSupport.disableAutoFlush();

		int i = 0;
		for (FinantialDocument document : allDocuments) {
			if (Boolean.TRUE.equals(document.getClosed())) {
				try {
					// if (SAFTDocumentType.MOVEMENT_OF_GOODS().equals(
					// document.getFinantialDocumentType().get
					// .getDocumentType().getSAFTType())) {
					// StockMovement mov = convertToSAFTStockMovement(
					// document, customerMap, productMap);
					// movementOfGoods.getStockMovement().add(mov);
					// // AcumulateValues
					// sumOfNumberMovementLines = sumOfNumberMovementLines
					// .add(BigInteger.valueOf(mov.getLine().size()));
					// for
					// (oecd.standardauditfile_tax.pt_1.SourceDocuments.MovementOfGoods.StockMovement.Line
					// line : mov
					// .getLine()) {
					// sumOfTotalQuantityIssued = sumOfTotalQuantityIssued
					// .add(line.getQuantity());
					// }
					//
					// Update Totals of MovementOfGoods
					movementOfGoods
							.setNumberOfMovementLines(sumOfNumberMovementLines);
					movementOfGoods
							.setTotalQuantityIssued(sumOfTotalQuantityIssued);

					sourceDocuments.setMovementOfGoods(movementOfGoods);
					// }
					if (document.getDocumentNumberSeries().getSeries()
							.getCertificated() == true) {
						// SourceDocuments.SalesInvoices.Invoice invoice =
						// convertToSAFTInvoice(
						// document, customerMap, productMap);
						//
						// invoices.getInvoice().add(invoice);
						// // AcumulateValues
						// invoices.setNumberOfEntries(invoices
						// .getNumberOfEntries().add(BigInteger.ONE));
						// invoices.setTotalCredit(invoices.getTotalDebit().add(
						// invoice.getDocumentTotals().getNetTotal()));
						// sourceDocuments.setSalesInvoices(invoices);
					} else if (document.getDocumentNumberSeries().getSeries()
							.getCertificated() == false) {
						WorkDocument workDocument = convertToSAFTWorkDocument(
								document, customerMap, productMap);
						workingDocuments.getWorkDocument().add(workDocument);

						// AcumulateValues
						numberOfWorkingDocuments = numberOfWorkingDocuments
								.add(BigInteger.ONE);
						totalCreditOfWorkingDocuments = totalCreditOfWorkingDocuments
								.add(workDocument.getDocumentTotals()
										.getNetTotal());

						// Update Totals of Workingdocuments
						workingDocuments
								.setNumberOfEntries(numberOfWorkingDocuments);
						workingDocuments
								.setTotalCredit(totalCreditOfWorkingDocuments);
						workingDocuments
								.setTotalDebit(totalDebitOfWorkingDocuments);

						sourceDocuments.setWorkingDocuments(workingDocuments);
					} else {
						logger.error("Error processing document "
								+ document.getUiDocumentNumber()
								+ ": No SAFT Document Type defined.");
					}

					// Update Counter
					// information.setCurrentCounter(information
					// .getCurrentCounter() + 10);
					// if (i % 10 == 0) {
					// try {
					// persistenceSupport.flush();
					// persistenceSupport.clearSession();
					// } catch (Throwable t) {
					// logger.error("Something gone wrong flushing state",
					// t);
					// }
					// logger.info("Processing " + i + "/"
					// + saleDocuments.size() + " documents in Store "
					// + storeToProcess.getCode());
					// }
					i++;

				} catch (Exception ex) {
					// persistenceSupport.flush();
					logger.error("Error processing document "
							+ document.getUiDocumentNumber() + ": "
							+ ex.getMessage());
					throw ex;
				}
			} else {
				logger.info("Ignoring document "
						+ document.getUiDocumentNumber()
						+ " because is not closed yet.");
			}

		}

		// persistenceSupport.enableAutoFlush();

		// Update the Customer Table in SAFT
		for (oecd.standardauditfile_tax.pt_1.Customer customer : customerMap
				.values()) {
			customerList.add(customer);
		}

		// Update the Product Table in SAFT
		for (oecd.standardauditfile_tax.pt_1.Product product : productMap
				.values()) {
			productList.add(product);
		}

		String xml = ExportAuditFileToXML(auditFile);

		writeSaftFile(xml, operation);
		logger.info("SAFT File export concluded with success.");
	}

	private WorkDocument convertToSAFTWorkDocument(
			FinantialDocument document,
			Map<String, oecd.standardauditfile_tax.pt_1.Customer> baseCustomers,
			Map<String, oecd.standardauditfile_tax.pt_1.Product> baseProducts) {
		WorkDocument workDocument = new WorkDocument();

		// Find the Customer in BaseCustomers
		oecd.standardauditfile_tax.pt_1.Customer customer = null;

		if (baseCustomers.containsKey(document.getDebtAccount().getCustomer()
				.getCode())) {
			customer = baseCustomers.get(document.getDebtAccount()
					.getCustomer().getCode());
		} else {
			// If not found, create a new one and add it to baseCustomers
			customer = convertCustomerToSAFTCustomer(document.getDebtAccount()
					.getCustomer());
			baseCustomers.put(customer.getCustomerID(), customer);
		}

		// MovementDate
		DatatypeFactory dataTypeFactory;
		try {
			dataTypeFactory = DatatypeFactory.newInstance();
			DateTime documentDate = document.getWhenCreated();

			// SystemEntryDate
			workDocument.setSystemEntryDate(dataTypeFactory
					.newXMLGregorianCalendar(documentDate.getYear(),
							documentDate.getMonthOfYear(),
							documentDate.getDayOfMonth(),
							documentDate.getHourOfDay(),
							documentDate.getMinuteOfHour(),
							documentDate.getSecondOfMinute(), 0,
							DatatypeConstants.FIELD_UNDEFINED));

			workDocument.setWorkDate(dataTypeFactory
					.newXMLGregorianCalendarDate(documentDate.getYear(),
							documentDate.getMonthOfYear(),
							documentDate.getDayOfMonth(),
							DatatypeConstants.FIELD_UNDEFINED));

			// DocumentNumber
			workDocument.setDocumentNumber(document.getUiDocumentNumber());

			// CustomerID
			workDocument.setCustomerID(document.getDebtAccount().getCustomer()
					.getCode());

			// DocumentStatus
			/*
			 * Deve ser preenchido com: ?N? ? Normal; Texto 1 ?T? ? Por conta de
			 * terceiros; ?A? ? Documento anulado.
			 */
			SourceDocuments.WorkingDocuments.WorkDocument.DocumentStatus status = new SourceDocuments.WorkingDocuments.WorkDocument.DocumentStatus();
			status.setWorkStatus("N");
			status.setWorkStatusDate(workDocument.getSystemEntryDate());
			// status.setReason("");
			// Utilizador responsável pelo estado atual do docu-mento.
			status.setSourceID(document.getUserChanged());
			// Deve ser preenchido com:
			// 'P' - Documento produzido na aplicacao;
			status.setSourceBilling(SAFTPTSourceBilling.P);

			workDocument.setDocumentStatus(status);

			// DocumentTotals
			SourceDocuments.WorkingDocuments.WorkDocument.DocumentTotals docTotals = new SourceDocuments.WorkingDocuments.WorkDocument.DocumentTotals();
			docTotals.setGrossTotal(document.getTotalValue().setScale(2,
					RoundingMode.HALF_EVEN));
			docTotals.setNetTotal(document.getTotalNetValue().setScale(2,
					RoundingMode.HALF_EVEN));
			docTotals.setTaxPayable(document.getTotalValue()
					.subtract(document.getTotalNetValue())
					.setScale(2, RoundingMode.HALF_EVEN));
			workDocument.setDocumentTotals(docTotals);

			// TODO: EACCode
			// mov.setEACCode(value);

			// Hash
			// if (!StringUtils.isEmpty(document.getDigitalSignature())) {
			// workDocument.setHash(document.getDigitalSignature());
			// } else {
			// workDocument.setHash(" ");
			// }

			// HashControl
			// if (!StringUtils.isEmpty(document.getDigitalSignatureControl()))
			// {
			// workDocument.setHashControl(document
			// .getDigitalSignatureControl());
			// } else {
			// workDocument.setHashControl(" ");
			// }

			// WorkType
			/*
			 * Deve ser preenchido com: Texto 2 "DC" — Documentos emitidos que
			 * sejam suscetiveis de apresentacao ao cliente para conferencia de
			 * entrega de mercadorias ou da prestacao de servicos. "FC" — Fatura
			 * de consignacao nos termos do artigo 38º do codigo do IVA.
			 */
			workDocument.setWorkType("DC");

			// Period
			/*
			 * Per?odo contabil?stico (Period) . . . . . . . . . . Deve ser
			 * indicado o n?mero do m?s do per?odo de tributa??o, de ?1? a ?12?,
			 * contado desde a data do in?cio. Pode ainda ser preenchido com
			 * ?13?, ?14?, ?15? ou ?16? para movimentos efectuados no ?ltimo m?s
			 * do per?odo de tributa??o, relacionados com o apuramento do
			 * resultado. Ex.: movimentos de apuramentos de invent?rios,
			 * deprecia??es, ajustamentos ou apuramentos de resultados.
			 */
			workDocument.setPeriod(document.getWhenCreated().getMonthOfYear());

			// ShipFrom
			// ShippingPointStructure shipFrom = new ShippingPointStructure();
			// Store documentStore =
			// orderNote.getVisit().getDailyRecord().getSalesman().getCurrentWorkingStore();
			// AddressStructure storeAddress = null;
			// if
			// (!documentStore.getPartyContacts(PhysicalAddress.class).isEmpty())
			// {
			// storeAddress =
			// convertToSAFTAddressStructure(documentStore.getPartyContacts(PhysicalAddress.class).get(0));
			// } else {
			// throw new DomainException("Address in Store not defined");
			// }
			// shipFrom.setAddress(storeAddress);
			// // shipFrom.setDeliveryDate(mov.getMovementStartTime());
			// mov.setShipFrom(shipFrom);

			// // ShipTo
			// ShippingPointStructure shipTo = new ShippingPointStructure();
			// shipTo.setAddress(customer.getBillingAddress());
			// // shipTo.setDeliveryDate(mov.getMovementEndTime());
			// mov.setShipTo(shipTo);

			// TODO: SourceBilling
			// mov.setSourceBilling(value);

			// SourceID
			/*
			 * C?digo do utilizador que registou o movimento (SourceID).
			 */
			workDocument.setSourceID(document.getUserCreated());

		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TransactionId
		/*
		 * O preenchimento é obrigatório, no caso de se tratar de um sistema
		 * integrado que inclua inventário permanente em que o campo 1.4. –
		 * Sistema con-tabilístico (TaxAccountingBasis) = I. Texto 70 Deve ser
		 * indicada a chave única da tabela 3 — Mo-vimentos contabilísticos
		 * (GeneralLedgerEntries) onde foi lançado este documento de movimento
		 * de stocks, respeitando a regra aí definida para o campo 3.4.3.1 —
		 * Chave única do movimento contabilístico (TransactionID).
		 */
		// mov.setTransactionID(mov.getMovementDate().toString() + " " +
		// mov.getSourceID() + " " + document.getDocumentNumber()
		// + " ");

		List<oecd.standardauditfile_tax.pt_1.SourceDocuments.WorkingDocuments.WorkDocument.Line> productLines = workDocument
				.getLine();

		// Process individual
		BigInteger i = BigInteger.ONE;
		for (FinantialDocumentEntry docLine : document
				.getFinantialDocumentEntriesSet()) {
			InvoiceEntry orderNoteLine = (InvoiceEntry) docLine;
			oecd.standardauditfile_tax.pt_1.SourceDocuments.WorkingDocuments.WorkDocument.Line line = convertToSAFTWorkDocumentLine(
					orderNoteLine, baseProducts);

			// LineNumber
			line.setLineNumber(i);

			// Add to productLines
			i = i.add(BigInteger.ONE);
			productLines.add(line);
		}

		return workDocument;
	}

	private oecd.standardauditfile_tax.pt_1.SourceDocuments.WorkingDocuments.WorkDocument.Line convertToSAFTWorkDocumentLine(
			InvoiceEntry entry,
			Map<String, oecd.standardauditfile_tax.pt_1.Product> baseProducts) {
		oecd.standardauditfile_tax.pt_1.Product currentProduct = null;

		Product product = entry.getProduct();

		if (product.getCode() != null
				&& baseProducts.containsKey(product.getCode())) {
			currentProduct = baseProducts.get(product.getCode());
		} else {
			currentProduct = convertProductToSAFTProduct(product);
			baseProducts.put(currentProduct.getProductCode(), currentProduct);
		}
		XMLGregorianCalendar documentDateCalendar = null;
		try {
			DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
			DateTime documentDate = entry.getFinantialDocument()
					.getWhenCreated();
			documentDateCalendar = dataTypeFactory.newXMLGregorianCalendarDate(
					documentDate.getYear(), documentDate.getMonthOfYear(),
					documentDate.getDayOfMonth(),
					DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		oecd.standardauditfile_tax.pt_1.SourceDocuments.WorkingDocuments.WorkDocument.Line line = new oecd.standardauditfile_tax.pt_1.SourceDocuments.WorkingDocuments.WorkDocument.Line();

		// DebitAmount
		// line.setDebitAmount(productLine.getNetPrice().setScale(2,
		// RoundingMode.HALF_EVEN));

		// 4 – Nas guias de remessa (output de produtos) os valores das linhas
		// são registados em creditamount, e não debitamount
		// CreditAmount
		line.setCreditAmount(entry.getAmount().setScale(2,
				RoundingMode.HALF_EVEN));

		// Description
		line.setDescription(currentProduct.getProductDescription());

		if (!Strings.isNullOrEmpty(entry.getFinantialDocument()
				.getOriginDocumentNumber())) {
			List<OrderReferences> orderReferences = line.getOrderReferences();
			OrderReferences reference = new OrderReferences();
			reference.setOriginatingON(entry.getFinantialDocument()
					.getOriginDocumentNumber());

			reference.setOrderDate(documentDateCalendar);
			orderReferences.add(reference);
		}

		// ProductCode
		line.setProductCode(currentProduct.getProductCode());

		// ProductDescription
		line.setProductDescription(currentProduct.getProductDescription());

		// Quantity
		line.setQuantity(entry.getQuantity());

		// SettlementAmount
		line.setSettlementAmount(BigDecimal.ZERO);

		// Tax
		line.setTax(getSAFTWorkingDocumentsTax(product, entry.getVat()));

		line.setTaxPointDate(documentDateCalendar);

		// TaxExemptionReason
		/*
		 * Motivo da isen??o de imposto (TaxExemptionReason). Campo de
		 * preenchimento obrigat?rio, quando os campos percentagem da taxa de
		 * imposto (TaxPercentage) ou montante do imposto (TaxAmount) s?o iguais
		 * a zero. Deve ser referido o preceito legal aplic?vel. . . . . . . . .
		 * . Texto 60
		 */
		if (line.getTax().getTaxPercentage() == BigDecimal.ZERO) {
			Vat vat = entry.getVat();

			if (vat.getVatExemptionReason() != null) {
				line.setTaxExemptionReason(vat.getVatExemptionReason()
						.getCode()
						+ "-"
						+ vat.getVatExemptionReason().getName());
			} else {
				// HACK : DEFAULT
				// line.setTaxExemptionReason(VatExemptionReason.M1().getCode()
				// + "-" + VatExemptionReason.M1().getDescription());
			}
		}

		// UnitOfMeasure
		line.setUnitOfMeasure(product.getUnitOfMeasure().getContent());
		// UnitPrice
		line.setUnitPrice(entry.getAmount().setScale(2, RoundingMode.HALF_EVEN));

		return line;
	}

	private Tax getSAFTWorkingDocumentsTax(Product product, Vat vat) {
		Tax tax = new Tax();

		// VatType vat = product.getVatType();
		// Tax-TaxCode

		if (vat.getVatType().getCode().equals("4")) {
			tax.setTaxCode("INT");
		} else if (vat.getVatType().getCode().equals("2")) {
			tax.setTaxCode("RED");
		} else if (vat.getVatType().getCode().equals("3")) {
			tax.setTaxCode("NOR");
		} else if (vat.getVatType().getCode().equals("5")) {
			tax.setTaxCode("ISE");
		} else if (vat.getVatType().getCode().equals("6")) {
			tax.setTaxCode("ISE");
		} else if (vat.getVatType().getCode().equals("1")) {
			tax.setTaxCode("ISE");
		}

		// TODO: Tax-TaxCountryRegion
		tax.setTaxCountryRegion("PT");

		// Tax-TaxPercentage
		tax.setTaxPercentage(vat.getTaxRate());

		// Tax-TaxType
		tax.setTaxType("IVA");

		return tax;
	}

	private TaxTableEntry convertVATtoTaxTableEntry(Vat vat,
			FinantialInstitution finantialInstitution) {
		TaxTableEntry entry = new TaxTableEntry();
		entry.setTaxType("IVA");
		entry.setTaxCode(vat.getVatType().getName().getContent());
		if (finantialInstitution.getFiscalNumber() != null) {
			entry.setTaxCountryRegion(finantialInstitution
					.getFiscalCountryRegion().getFiscalCode());
			entry.setDescription(finantialInstitution.getFiscalCountryRegion()
					.getName().getContent()
					+ "-" + vat.getVatType().getName().getContent());
		} else {
			entry.setTaxCountryRegion("PT");
			entry.setDescription("");
		}
		entry.setTaxCode(vat.getVatType().getName().getContent());
		entry.setTaxPercentage(vat.getTaxRate());

		if (Strings.isNullOrEmpty(entry.getDescription())) {
			entry.setDescription(entry.getTaxCode() + "_"
					+ entry.getTaxCountryRegion());
		}
		return entry;
	}

	private Header createSAFTHeader(DateTime startDate, DateTime endDate,
			FinantialInstitution finantialInstitution, String auditVersion) {

		Header header = new Header();
		DatatypeFactory dataTypeFactory;
		try {

			dataTypeFactory = DatatypeFactory.newInstance();

			// AuditFileVersion
			header.setAuditFileVersion(auditVersion);

			// BusinessName - Nome da Empresa
			header.setBusinessName(finantialInstitution.getCompanyName());
			header.setCompanyName(finantialInstitution.getName());

			// CompanyAddress
			AddressStructurePT companyAddress = null;
			companyAddress = convertAddressToAddressPT(
					finantialInstitution.getAddress(),
					finantialInstitution.getZipCode(),
					finantialInstitution.getDistrictSubdivision(),
					finantialInstitution.getAddress());
			header.setCompanyAddress(companyAddress);

			// CompanyID
			/*
			 * Obtem -se pela concatena??o da conservat?ria do registo comercial
			 * com o n?mero do registo comercial, separados pelo car?cter
			 * espa?o. Nos casos em que n?o existe o registo comercial, deve ser
			 * indicado o NIF.
			 */
			header.setCompanyID(finantialInstitution
					.getComercialRegistrationCode());

			// CurrencyCode
			/*
			 * 1.11 * C?digo de moeda (CurrencyCode) . . . . . . . Preencher com
			 * ?EUR?
			 */
			// if (finantialInstitution != null && headquarter.getParameters()
			// != null
			// && headquarter.getParameters().getDefaultCurrency() != null) {
			// Currency currency = headquarter.getParameters()
			// .getDefaultCurrency();
			// header.setCurrencyCode(currency.getTypeValue());
			// } else {
			header.setCurrencyCode("EUR");
			// }

			// DateCreated
			DateTime now = new DateTime();
			XMLGregorianCalendar xmlCreateDate = dataTypeFactory
					.newXMLGregorianCalendarDate(now.getYear(),
							now.getMonthOfYear(), now.getDayOfMonth(),
							DatatypeConstants.FIELD_UNDEFINED);
			header.setDateCreated(xmlCreateDate);

			// Email
			// header.setEmail(StringUtils.EMPTY);

			// EndDate
			header.setEndDate(dataTypeFactory.newXMLGregorianCalendarDate(
					endDate.getYear(), endDate.getMonthOfYear(),
					endDate.getDayOfMonth(), DatatypeConstants.FIELD_UNDEFINED));

			// Fax
			// header.setFax(StringUtils.EMPTY);

			// FiscalYear
			/*
			 * Utilizar as regras do c?digo do IRC, no caso de per?odos
			 * contabil?sticos n?o coincidentes com o ano civil. (Ex: per?odo de
			 * tributa??o de 01 -10 -2008 a 30 -09 -2009 corresponde FiscalYear
			 * 2008). Inteiro 4
			 */
			header.setFiscalYear(endDate.getYear());

			// Ir obter a data do ?ltimo
			// documento(por causa de submeter em janeiro, documentos de
			// dezembro)

			// HeaderComment
			// header.setHeaderComment(org.apache.commons.lang.StringUtils.EMPTY);

			// ProductCompanyTaxID
			// Preencher com o NIF da entidade produtora do software
			header.setProductCompanyTaxID(SaftConfig.PRODUCT_COMPANY_TAX_ID());

			// ProductID
			/*
			 * 1.16 * Nome do produto (ProductID). . . . . . . . . . . Nome do
			 * produto que gera o SAF -T (PT) . . . . . . . . . . . Deve ser
			 * indicado o nome comercial do software e o da empresa produtora no
			 * formato ?Nome produto/nome empresa?.
			 */
			header.setProductID(SaftConfig.PRODUCT_ID());

			// Product Version
			header.setProductVersion(SaftConfig.PRODUCT_VERSION());

			// SoftwareCertificateNumber
			header.setSoftwareCertificateNumber(BigInteger.valueOf(SaftConfig
					.SOFTWARE_CERTIFICATE_NUMBER()));

			// StartDate
			header.setStartDate(dataTypeFactory.newXMLGregorianCalendarDate(
					startDate.getYear(), startDate.getMonthOfYear(),
					startDate.getDayOfMonth(),
					DatatypeConstants.FIELD_UNDEFINED));

			// TaxAccountingBasis
			/*
			 * Deve ser preenchido com: contabilidade; facturao; ?I? ? dados
			 * integrados de factura??o e contabilidade; ?S? ? autofactura??o;
			 * ?P? ? dados parciais de factura??o
			 */
			header.setTaxAccountingBasis("P");

			// TaxEntity
			/*
			 * Identifica??o do estabelecimento (TaxEntity) No caso do ficheiro
			 * de factura??o dever? ser especificado a que estabelecimento diz
			 * respeito o ficheiro produzido, se aplic?vel, caso contr?rio,
			 * dever? ser preenchido com a especifica??o ?Global?. No caso do
			 * ficheiro de contabilidade ou integrado, este campo dever? ser
			 * preenchido com a especifica??o ?Sede?. Texto 20
			 */
			header.setTaxEntity("Global");

			// TaxRegistrationNumber
			/*
			 * N?mero de identifica??o fiscal da empresa
			 * (TaxRegistrationNumber). Preencher com o NIF portugu?s sem
			 * espa?os e sem qualquer prefixo do pa?s. Inteiro 9
			 */
			try {
				header.setTaxRegistrationNumber(Integer
						.parseInt(finantialInstitution.getFiscalNumber()));
			} catch (Exception ex) {
				throw new RuntimeException("Invalid Fiscal Number.");
			}

			// TODO: Telephone
			// header.setTelephone(finantialInstitution.get);

			// TODO: Website
			// header.setWebsite(finantialInstitution.getEmailContact());

			return header;
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private AddressStructurePT convertAddressToAddressPT(String addressDetail,
			String zipCode, String zipCodeRegion, String street) {
		AddressStructurePT companyAddress;
		companyAddress = new AddressStructurePT();
		companyAddress.setCountry("PT");
		companyAddress.setAddressDetail(Splitter.fixedLength(60)
				.splitToList(addressDetail).get(0));
		companyAddress.setCity(Splitter.fixedLength(49)
				.splitToList(zipCodeRegion).get(0));
		companyAddress.setPostalCode(zipCode);
		companyAddress.setRegion(zipCodeRegion);
		companyAddress.setStreetName(Splitter.fixedLength(49)
				.splitToList(street).get(0));
		return companyAddress;
	}

	private String ExportAuditFileToXML(AuditFile auditFile) {
		try {
			final String cleanXMLAnotations = "xsi:type=\"xs:string\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"";
			final String cleanXMLAnotations2 = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
			final String cleanDateTimeMiliseconds = ".000<";
			final String cleanStandaloneAnnotation = "standalone=\"yes\"";

			final JAXBContext jaxbContext = JAXBContext
					.newInstance(AuditFile.class);
			Marshaller marshaller = jaxbContext.createMarshaller();

			StringWriter writer = new StringWriter();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "Windows-1252");
			marshaller.marshal(auditFile, writer);

			Charset charset = Charset.forName("Windows-1252");

			String xml = new String(charset.encode(writer.toString()).array(),
					"Windows-1252");
			xml = xml.replace(cleanXMLAnotations, "");
			xml = xml.replace(cleanXMLAnotations2, "");
			xml = xml.replace(cleanDateTimeMiliseconds, "<");
			xml = xml.replace(cleanStandaloneAnnotation, "");

			try {
				MessageDigest md = MessageDigest.getInstance("SHA1");
				md.update(("SALTING WITH QUB:" + xml).getBytes("Windows-1252"));
				byte[] output = md.digest();
				String digestAscii = bytesToHex(output);
				xml = "<!-- QUB-IT (remove this line,add the qubSALT, save with Windows-1252 encode): "
						+ digestAscii + " -->\n" + xml;
			} catch (Exception ex) {

			}
			return xml;
		} catch (JAXBException e) {
			return org.apache.commons.lang.StringUtils.EMPTY;
		} catch (UnsupportedEncodingException jex) {
			return org.apache.commons.lang.StringUtils.EMPTY;
		}
	}

	public static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}

	// private StockMovement convertToSAFTStockMovement(SaleNoteDocument
	// document,
	// Map<String, oecd.standardauditfile_tax.pt_1.Customer> baseCustomers,
	// Map<String, oecd.standardauditfile_tax.pt_1.Product> baseProducts) {
	//
	// StockMovement mov = new StockMovement();
	//
	// // Find the Customer in BaseCustomers
	// oecd.standardauditfile_tax.pt_1.Customer customer = null;
	//
	// if
	// (baseCustomers.containsKey(document.getVisit().getCustomerStore().getCode()))
	// {
	// customer =
	// baseCustomers.get(document.getVisit().getCustomerStore().getCode());
	// } else {
	// // If not found, create a new one and add it to baseCustomers
	// customer = convertToSAFTCustomer(document.getVisit().getCustomerStore());
	// baseCustomers.put(customer.getCustomerID(), customer);
	// }
	//
	// // MovementDate
	// DatatypeFactory dataTypeFactory;
	// try {
	// dataTypeFactory = DatatypeFactory.newInstance();
	// DateTime documentDate = document.getWhenCreated();
	//
	// // SystemEntryDate
	// mov.setSystemEntryDate(dataTypeFactory.newXMLGregorianCalendar(documentDate.getYear(),
	// documentDate.getMonthOfYear(),
	// documentDate.getDayOfMonth(), documentDate.getHourOfDay(),
	// documentDate.getMinuteOfHour(),
	// documentDate.getSecondOfMinute(), 0, DatatypeConstants.FIELD_UNDEFINED));
	//
	// mov.setMovementDate(dataTypeFactory.newXMLGregorianCalendarDate(documentDate.getYear(),
	// documentDate.getMonthOfYear(), documentDate.getDayOfMonth(),
	// DatatypeConstants.FIELD_UNDEFINED));
	//
	// // DocumentNumber
	// mov.setDocumentNumber(document.getUiDocumentNumber());
	//
	// // CustomerID
	// mov.setCustomerID(document.getVisit().getCustomerStore().getCode());
	//
	// // MovementStartTime
	// mov.setMovementStartTime(mov.getSystemEntryDate());
	//
	// // MovementEndTime
	// mov.setMovementEndTime(mov.getSystemEntryDate());
	//
	// // DocumentStatus
	// /*
	// * Deve ser preenchido com: ?N? ? Normal; Texto 1 ?T? ? Por conta de
	// * terceiros; ?A? ? Documento anulado.
	// */
	// SourceDocuments.MovementOfGoods.StockMovement.DocumentStatus status = new
	// SourceDocuments.MovementOfGoods.StockMovement.DocumentStatus();
	// status.setMovementStatus("N");
	// status.setMovementStatusDate(mov.getSystemEntryDate());
	// // status.setReason("");
	// // Utilizador responsável pelo estado atual do docu-mento.
	// status.setSourceID(document.getVisit().getDailyRecord().getSalesman().getCode());
	// // Deve ser preenchido com:
	// // 'P' - Documento produzido na aplicacao;
	// status.setSourceBilling(SAFTPTSourceBilling.P);
	//
	// mov.setDocumentStatus(status);
	//
	// // DocumentTotals
	// DocumentTotals docTotals = new DocumentTotals();
	// docTotals.setGrossTotal(document.getTotalValue().setScale(2,
	// RoundingMode.HALF_EVEN));
	// docTotals.setNetTotal(document.getTotalNetValue().setScale(2,
	// RoundingMode.HALF_EVEN));
	// docTotals.setTaxPayable(document.getTotalValue().subtract(document.getTotalNetValue())
	// .setScale(2, RoundingMode.HALF_EVEN));
	// mov.setDocumentTotals(docTotals);
	//
	// // TODO: EACCode
	// // mov.setEACCode(value);
	//
	// // Hash
	// if (!StringUtils.isEmpty(document.getDigitalSignature())) {
	// mov.setHash(document.getDigitalSignature());
	// } else {
	// mov.setHash(" ");
	// }
	//
	// // HashControl
	// if (!StringUtils.isEmpty(document.getDigitalSignatureControl())) {
	// mov.setHashControl(document.getDigitalSignatureControl());
	// } else {
	// mov.setHashControl(" ");
	// }
	//
	// // mov.setMovementComments("");
	//
	// // MovementType
	// /*
	// * Deve ser preenchido com: ?GR? ? Guia de remessa; ?GT? ? Guia de
	// * transporte; ?GA? ? Guia de movimenta??o de ativos pr?prios; ?GC?
	// * ? Guia de consigna??o; ?GD? ? Guia ou nota de devolu??o efetuada
	// * pelo cliente
	// */
	// mov.setMovementType("GR");
	//
	// // Period
	// /*
	// * Per?odo contabil?stico (Period) . . . . . . . . . . Deve ser
	// * indicado o n?mero do m?s do per?odo de tributa??o, de ?1? a ?12?,
	// * contado desde a data do in?cio. Pode ainda ser preenchido com
	// * ?13?, ?14?, ?15? ou ?16? para movimentos efectuados no ?ltimo m?s
	// * do per?odo de tributa??o, relacionados com o apuramento do
	// * resultado. Ex.: movimentos de apuramentos de invent?rios,
	// * deprecia??es, ajustamentos ou apuramentos de resultados.
	// */
	// mov.setPeriod(document.getWhenCreated().getMonthOfYear());
	//
	// // ShipFrom
	// ShippingPointStructure shipFrom = new ShippingPointStructure();
	// Store documentStore =
	// document.getVisit().getDailyRecord().getSalesman().getCurrentWorkingStore();
	// AddressStructure storeAddress = null;
	// if (!documentStore.getPartyContacts(PhysicalAddress.class).isEmpty()) {
	// storeAddress =
	// convertToSAFTAddressStructure(documentStore.getPartyContacts(PhysicalAddress.class).get(0));
	// } else {
	// throw new DomainException("Address in Store not defined");
	// }
	// shipFrom.setAddress(storeAddress);
	// // shipFrom.setDeliveryDate(mov.getMovementStartTime());
	// mov.setShipFrom(shipFrom);
	//
	// // ShipTo
	// ShippingPointStructure shipTo = new ShippingPointStructure();
	// shipTo.setAddress(customer.getBillingAddress());
	// // shipTo.setDeliveryDate(mov.getMovementEndTime());
	// mov.setShipTo(shipTo);
	//
	// // TODO: SourceBilling
	// // mov.setSourceBilling(value);
	//
	// // SourceID
	// /*
	// * C?digo do utilizador que registou o movimento (SourceID).
	// */
	// mov.setSourceID(document.getVisit().getDailyRecord().getSalesman().getFullCode());
	//
	// // SupplierID
	// mov.setSupplierID(null);
	//
	// } catch (DatatypeConfigurationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// // TransactionId
	// /*
	// * O preenchimento é obrigatório, no caso de se tratar de um sistema
	// * integrado que inclua inventário permanente em que o campo 1.4. –
	// * Sistema con-tabilístico (TaxAccountingBasis) = I. Texto 70 Deve ser
	// * indicada a chave única da tabela 3 — Mo-vimentos contabilísticos
	// * (GeneralLedgerEntries) onde foi lançado este documento de movimento
	// * de stocks, respeitando a regra aí definida para o campo 3.4.3.1 —
	// * Chave única do movimento contabilístico (TransactionID).
	// */
	// // mov.setTransactionID(mov.getMovementDate().toString() + " " +
	// // mov.getSourceID() + " " + document.getDocumentNumber()
	// // + " ");
	//
	// List<oecd.standardauditfile_tax.pt_1.SourceDocuments.MovementOfGoods.StockMovement.Line>
	// productLines = mov.getLine();
	//
	// // Process individual
	// BigInteger i = BigInteger.ONE;
	// for (DocumentLine docLine : document.getLines()) {
	// ProductLine saleNoteLine = (ProductLine) docLine;
	// oecd.standardauditfile_tax.pt_1.SourceDocuments.MovementOfGoods.StockMovement.Line
	// line = convertToSAFTStockMovementLine(
	// saleNoteLine, baseProducts);
	//
	// // LineNumber
	// line.setLineNumber(i);
	//
	// // Add to productLines
	// i = i.add(BigInteger.ONE);
	// productLines.add(line);
	// }
	//
	// return mov;
	// }

	// private SourceDocuments.SalesInvoices.Invoice
	// convertToSAFTInvoice(SaleNoteDocument document,
	// Map<String, oecd.standardauditfile_tax.pt_1.Customer> baseCustomers,
	// Map<String, oecd.standardauditfile_tax.pt_1.Product> baseProducts) {
	//
	// SourceDocuments.SalesInvoices.Invoice invoice = new
	// SourceDocuments.SalesInvoices.Invoice();
	//
	// // Find the Customer in BaseCustomers
	// CustomerStore movementCustomer = document.getVisit().getCustomerStore();
	// oecd.standardauditfile_tax.pt_1.Customer customer = null;
	//
	// if (baseCustomers.containsKey(movementCustomer.getCode())) {
	// customer = baseCustomers.get(movementCustomer.getCode());
	// } else {
	// // If not found, create a new one and add it to baseCustomers
	// customer = convertToSAFTCustomer(document.getVisit().getCustomerStore());
	// baseCustomers.put(customer.getCustomerID(), customer);
	// }
	//
	// // MovementDate
	// DatatypeFactory dataTypeFactory;
	// try {
	// dataTypeFactory = DatatypeFactory.newInstance();
	// DateTime documentDate = document.getWhenCreated();
	//
	// // SystemEntryDate
	// invoice.setSystemEntryDate(dataTypeFactory.newXMLGregorianCalendar(documentDate.getYear(),
	// documentDate.getMonthOfYear(), documentDate.getDayOfMonth(),
	// documentDate.getHourOfDay(),
	// documentDate.getMinuteOfHour(), documentDate.getSecondOfMinute(), 0,
	// DatatypeConstants.FIELD_UNDEFINED));
	//
	// invoice.setInvoiceDate(dataTypeFactory.newXMLGregorianCalendarDate(documentDate.getYear(),
	// documentDate.getMonthOfYear(), documentDate.getDayOfMonth(),
	// DatatypeConstants.FIELD_UNDEFINED));
	//
	// // DocumentNumber
	// invoice.setInvoiceNo(document.getUiDocumentNumber());
	//
	// // CustomerID - Uses Code
	// invoice.setCustomerID(document.getVisit().getCustomerStore().getCode());
	//
	// // MovementStartTime
	// invoice.setMovementStartTime(invoice.getSystemEntryDate());
	//
	// // MovementEndTime
	// invoice.setMovementEndTime(invoice.getSystemEntryDate());
	//
	// // SpecialRegimes
	//
	// SpecialRegimes specialRegimes = new SpecialRegimes();
	// // Deve ser preenchido com "1" se respeitar a autofaturação e
	// // com "0" (zero) no caso contrário
	// specialRegimes.setSelfBillingIndicator(0);
	// // Indicador da existência de adesão ao regime de IVA de Caixa.
	// // Inteiro
	// // Deve ser preenchido com "1" se houver adesão e com "0"
	// // (zero) no caso contrário.
	// specialRegimes.setCashVATSchemeIndicator(0);
	//
	// // Deve ser preenchido com "1" se respeitar a faturação emitida
	// // em nome e por conta de terceiros e com "0" (zero) no caso
	// // contrário.
	// specialRegimes.setThirdPartiesBillingIndicator(0);
	//
	// invoice.setSpecialRegimes(specialRegimes);
	//
	// // DocumentStatus
	// /*
	// * Deve ser preenchido com: ?N? ? Normal; Texto 1 ?T? ? Por conta de
	// * terceiros; ?A? ? Documento anulado.
	// */
	// SourceDocuments.SalesInvoices.Invoice.DocumentStatus status = new
	// SourceDocuments.SalesInvoices.Invoice.DocumentStatus();
	// // status.setReason("");
	// // Utilizador responsável pelo estado atual do docu-mento.
	// status.setSourceID(document.getVisit().getDailyRecord().getSalesman().getCode());
	// // Deve ser preenchido com:
	// // 'P' - Documento produzido na aplicacao;
	// status.setSourceBilling(SAFTPTSourceBilling.P);
	// status.setInvoiceStatus("N");
	// status.setInvoiceStatusDate(invoice.getSystemEntryDate());
	//
	// invoice.setDocumentStatus(status);
	//
	// // DocumentTotals
	// SourceDocuments.SalesInvoices.Invoice.DocumentTotals docTotals = new
	// SourceDocuments.SalesInvoices.Invoice.DocumentTotals();
	// docTotals.setGrossTotal(document.getTotalValue().setScale(2,
	// RoundingMode.HALF_EVEN));
	// docTotals.setNetTotal(document.getTotalNetValue().setScale(2,
	// RoundingMode.HALF_EVEN));
	// docTotals.setTaxPayable(document.getTotalValue().subtract(
	// document.getTotalNetValue().setScale(2, RoundingMode.HALF_EVEN)));
	// invoice.setDocumentTotals(docTotals);
	//
	// // Hash
	// if (!StringUtils.isEmpty(document.getDigitalSignature())) {
	// invoice.setHash(document.getDigitalSignature());
	// } else {
	// invoice.setHash(" ");
	// }
	//
	// // HashControl
	// if (!StringUtils.isEmpty(document.getDigitalSignatureControl())) {
	// invoice.setHashControl(document.getDigitalSignatureControl());
	// } else {
	// invoice.setHashControl(" ");
	// }
	//
	// // mov.setMovementComments("");
	//
	// // MovementType
	// invoice.setInvoiceType("FT");
	//
	// // Period
	// /*
	// * Per?odo contabil?stico (Period) . . . . . . . . . . Deve ser
	// * indicado o n?mero do m?s do per?odo de tributa??o, de ?1? a ?12?,
	// * contado desde a data do in?cio. Pode ainda ser preenchido com
	// * ?13?, ?14?, ?15? ou ?16? para movimentos efectuados no ?ltimo m?s
	// * do per?odo de tributa??o, relacionados com o apuramento do
	// * resultado. Ex.: movimentos de apuramentos de invent?rios,
	// * deprecia??es, ajustamentos ou apuramentos de resultados.
	// */
	// invoice.setPeriod(document.getWhenCreated().getMonthOfYear());
	//
	// // ShipFrom
	// ShippingPointStructure shipFrom = new ShippingPointStructure();
	// // shipFrom.setAddress(value);
	// // shipFrom.setDeliveryDate(mov.getMovementStartTime());
	// invoice.setShipFrom(shipFrom);
	//
	// // ShipTo
	// ShippingPointStructure shipTo = new ShippingPointStructure();
	// shipTo.setAddress(customer.getBillingAddress());
	// // shipTo.setDeliveryDate(mov.getMovementEndTime());
	// invoice.setShipTo(shipTo);
	//
	// // TODO: SourceBilling
	// // mov.setSourceBilling(value);
	//
	// // SourceID
	// /*
	// * C?digo do utilizador que registou o movimento (SourceID).
	// */
	// invoice.setSourceID(document.getVisit().getDailyRecord().getSalesman().getFullCode());
	//
	// } catch (DatatypeConfigurationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// // TransactionId
	// /*
	// * O preenchimento é obrigatório, no caso de se tratar de um sistema
	// * integrado que inclua inventário permanente em que o campo 1.4. –
	// * Sistema con-tabilístico (TaxAccountingBasis) = I. Texto 70 Deve ser
	// * indicada a chave única da tabela 3 — Mo-vimentos contabilísticos
	// * (GeneralLedgerEntries) onde foi lançado este documento de movimento
	// * de stocks, respeitando a regra aí definida para o campo 3.4.3.1 —
	// * Chave única do movimento contabilístico (TransactionID).
	// */
	// // mov.setTransactionID(mov.getMovementDate().toString() + " " +
	// // mov.getSourceID() + " " + document.getDocumentNumber()
	// // + " ");
	//
	// List<SourceDocuments.SalesInvoices.Invoice.Line> productLines =
	// invoice.getLine();
	//
	// // Process individual
	// BigInteger i = BigInteger.ONE;
	// for (DocumentLine docLine : document.getLines()) {
	// ProductLine saleNoteLine = (ProductLine) docLine;
	// SourceDocuments.SalesInvoices.Invoice.Line line =
	// convertToSAFTInvoiceLine(saleNoteLine, baseProducts);
	//
	// // LineNumber
	// line.setLineNumber(i);
	//
	// // Add to productLines
	// i = i.add(BigInteger.ONE);
	// productLines.add(line);
	// }
	//
	// return invoice;
	// }

	// private SourceDocuments.SalesInvoices.Invoice.Line
	// convertToSAFTInvoiceLine(ProductLine productLine,
	// Map<String, oecd.standardauditfile_tax.pt_1.Product> baseProducts) {
	// oecd.standardauditfile_tax.pt_1.Product currentProduct = null;
	//
	// ProductInstance productInstance = productLine.getProductInstance();
	// Product product = productInstance.getProduct();
	//
	// if (product.getSapCode() != null &&
	// baseProducts.containsKey(product.getSapCode())) {
	// currentProduct = baseProducts.get(product.getSapCode());
	// } else if (baseProducts.containsKey(product.getBaseCode())) {
	// currentProduct = baseProducts.get(product.getBaseCode());
	// } else {
	// currentProduct = convertToSAFTProduct(product);
	// baseProducts.put(currentProduct.getProductCode(), currentProduct);
	// }
	// XMLGregorianCalendar documentGregorianDate = null;
	//
	// try {
	// DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
	// DateTime documentDate = productLine.getDocument().getWhenCreated();
	// documentGregorianDate =
	// dataTypeFactory.newXMLGregorianCalendarDate(documentDate.getYear(),
	// documentDate.getMonthOfYear(), documentDate.getDayOfMonth(),
	// DatatypeConstants.FIELD_UNDEFINED);
	// } catch (DatatypeConfigurationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// SourceDocuments.SalesInvoices.Invoice.Line line = new
	// SourceDocuments.SalesInvoices.Invoice.Line();
	//
	// // The line total should be in creditamount, regarding email from AT
	// // 4 – Nas guias de remessa (output de produtos) os valores das linhas
	// // são registados em creditamount, e não debitamount
	// // DebitAmount
	// // line.setDebitAmount(productLine.getNetPrice().setScale(2,
	// // RoundingMode.HALF_EVEN));
	//
	// // CreditAmount
	// line.setCreditAmount(productLine.getNetPrice().setScale(2,
	// RoundingMode.HALF_EVEN));
	//
	// line.setTaxPointDate(documentGregorianDate);
	//
	// // Description
	// line.setDescription(currentProduct.getProductDescription());
	//
	// if
	// (!StringUtils.isEmpty(productLine.getDocument().getDocumentSourceReferenceNumber()))
	// {
	// List<OrderReferences> orderReferences = line.getOrderReferences();
	// OrderReferences reference = new OrderReferences();
	// reference.setOriginatingON(productLine.getDocument().getDocumentSourceReferenceNumber());
	// reference.setOrderDate(documentGregorianDate);
	//
	// orderReferences.add(reference);
	// }
	//
	// // ProductCode
	// line.setProductCode(currentProduct.getProductCode());
	//
	// // ProductDescription
	// line.setProductDescription(currentProduct.getProductDescription());
	//
	// // Quantity
	// line.setQuantity(productLine.getQuantity().multiply(productInstance.getUnitNumber()));
	//
	// // SettlementAmount
	// line.setSettlementAmount(BigDecimal.ZERO);
	//
	// Store store =
	// productLine.getDocument().getVisit().getDailyRecord().getStore();
	// ProductStore productInStore = productInstance.getProductStore(store);
	//
	// // Tax
	// line.setTax(getSAFTInvoiceTax(productInstance.getProductStore(store)));
	//
	// // TaxExemptionReason
	// /*
	// * Motivo da isen??o de imposto (TaxExemptionReason). Campo de
	// * preenchimento obrigat?rio, quando os campos percentagem da taxa de
	// * imposto (TaxPercentage) ou montante do imposto (TaxAmount) s?o iguais
	// * a zero. Deve ser referido o preceito legal aplic?vel. . . . . . . . .
	// * . Texto 60
	// */
	// if (line.getTax().getTaxAmount() == BigDecimal.ZERO ||
	// line.getTax().getTaxPercentage() == BigDecimal.ZERO) {
	// VAT vat =
	// productLine.getProductInstance().getProductStore(store).getVat();
	// VatExemptionReason reason = vat.getVatExemptionReason();
	// if (reason == null) {
	// reason =
	// VatExemptionReason.findByCode(SolutionConfig.DOCUMENTS.DEFAULT_VAT_EXEMPTION_REASON());
	// }
	// line.setTaxExemptionReason(reason.getCode() + "-" +
	// reason.getDescription());
	// }
	// // UnitOfMeasure
	// line.setUnitOfMeasure(productInstance.getUIUnitOfMeasure());
	//
	// // UnitPrice
	// line.setUnitPrice(productLine.getUnitPrice().setScale(2,
	// RoundingMode.HALF_EVEN));
	//
	// return line;
	// }
	//
	// private Line convertToSAFTStockMovementLine(ProductLine productLine,
	// Map<String, oecd.standardauditfile_tax.pt_1.Product> baseProducts) {
	// oecd.standardauditfile_tax.pt_1.Product currentProduct = null;
	//
	// ProductInstance productInstance = productLine.getProductInstance();
	// Product product = productInstance.getProduct();
	//
	// if (product.getSapCode() != null &&
	// baseProducts.containsKey(product.getSapCode())) {
	// currentProduct = baseProducts.get(product.getSapCode());
	// } else if (baseProducts.containsKey(product.getBaseCode())) {
	// currentProduct = baseProducts.get(product.getBaseCode());
	// } else {
	// currentProduct = convertToSAFTProduct(product);
	// baseProducts.put(currentProduct.getProductCode(), currentProduct);
	// }
	//
	// Line line = new Line();
	//
	// // DebitAmount
	// // line.setDebitAmount(productLine.getNetPrice().setScale(2,
	// // RoundingMode.HALF_EVEN));
	//
	// // 4 – Nas guias de remessa (output de produtos) os valores das linhas
	// // são registados em creditamount, e não debitamount
	// // CreditAmount
	// line.setCreditAmount(productLine.getNetPrice().setScale(2,
	// RoundingMode.HALF_EVEN));
	//
	// // Description
	// line.setDescription(currentProduct.getProductDescription());
	//
	// if
	// (!StringUtils.isEmpty(productLine.getDocument().getDocumentSourceReferenceNumber()))
	// {
	// List<OrderReferences> orderReferences = line.getOrderReferences();
	// OrderReferences reference = new OrderReferences();
	// reference.setOriginatingON(productLine.getDocument().getDocumentSourceReferenceNumber());
	//
	// try {
	// DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
	// DateTime documentDate = productLine.getDocument().getWhenCreated();
	// reference.setOrderDate(dataTypeFactory.newXMLGregorianCalendarDate(documentDate.getYear(),
	// documentDate.getMonthOfYear(), documentDate.getDayOfMonth(),
	// DatatypeConstants.FIELD_UNDEFINED));
	// } catch (DatatypeConfigurationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// orderReferences.add(reference);
	// }
	//
	// // ProductCode
	// line.setProductCode(currentProduct.getProductCode());
	//
	// // ProductDescription
	// line.setProductDescription(currentProduct.getProductDescription());
	//
	// // Quantity
	// line.setQuantity(productLine.getQuantity().multiply(productInstance.getUnitNumber()));
	//
	// // SettlementAmount
	// line.setSettlementAmount(BigDecimal.ZERO);
	//
	// // Tax
	// Store store =
	// productLine.getDocument().getVisit().getDailyRecord().getStore();
	// line.setTax(getSAFTMovementTax(productInstance.getProductStore(store)));
	//
	// // TaxExemptionReason
	// /*
	// * Motivo da isen??o de imposto (TaxExemptionReason). Campo de
	// * preenchimento obrigat?rio, quando os campos percentagem da taxa de
	// * imposto (TaxPercentage) ou montante do imposto (TaxAmount) s?o iguais
	// * a zero. Deve ser referido o preceito legal aplic?vel. . . . . . . . .
	// * . Texto 60
	// */
	// if (line.getTax().getTaxPercentage() == BigDecimal.ZERO) {
	// VAT vat =
	// productLine.getProductInstance().getProductStore(store).getVat();
	// if (vat.getVatExemptionReason() != null) {
	// line.setTaxExemptionReason(vat.getVatExemptionReason().getCode() + "-"
	// + vat.getVatExemptionReason().getDescription());
	// } else {
	// // HACK : TABACO
	// line.setTaxExemptionReason(VatExemptionReason.M11().getCode() + "-" +
	// VatExemptionReason.M11().getDescription());
	// }
	// }
	//
	// // UnitOfMeasure
	// line.setUnitOfMeasure(productInstance.getUIUnitOfMeasure());
	// // UnitPrice
	// line.setUnitPrice(productLine.getUnitPrice().setScale(2,
	// RoundingMode.HALF_EVEN));
	//
	// return line;
	// }
	//
	private oecd.standardauditfile_tax.pt_1.Customer convertCustomerToSAFTCustomer(
			Customer customer) {
		oecd.standardauditfile_tax.pt_1.Customer c = new oecd.standardauditfile_tax.pt_1.Customer();

		// AccountID
		/*
		 * Deve ser indicada a respectiva conta corrente do cliente no plano de
		 * contas da contabilidade, caso esteja definida. Caso contr?rio dever?
		 * ser preenchido com a designa??o ?Desconhecido?.
		 */
		c.setAccountID("Desconhecido");

		// BillingAddress
		// List<PhysicalAddress> addresses = customer
		// .getPartyContacts(PhysicalAddress.class);
		// if (addresses.size() > 0) {
		// c.setBillingAddress(convertToSAFTAddressStructure(addresses.get(0)));
		// } else {
		// PhysicalAddress addr = new PhysicalAddress();
		c.setBillingAddress(convertAddressToSAFTAddress(customer.getAddress(),
				customer.getZipCode(), customer.getDistrictSubdivision(),
				customer.getAddress()));
		// }

		// CompanyName
		c.setCompanyName(customer.getName());

		// Contact
		c.setContact(customer.getName());

		// CustomerID
		c.setCustomerID(customer.getCode());

		// CustomerTaxID
		if (Strings.isNullOrEmpty(customer.getFiscalNumber())) {
			c.setCustomerTaxID(Customer.DEFAULT_FISCAL_NUMBER);
		} else {
			c.setCustomerTaxID(customer.getFiscalNumber());
		}
		// Email
		// c.setEmail("");

		// Fax
		// c.setFax("");

		// SelfBillingIndicator
		/*
		 * Indicador da exist?ncia de acordo de autofactura??o entre o cliente e
		 * o fornecedor. Deve ser preenchido com ?1? se houver acordo e com ?0?
		 * (zero) no caso contr?rio.
		 */
		c.setSelfBillingIndicator(0);

		// Telephone
		// c.setTelephone("");

		// WebSite
		// c.setWebsite("");

		return c;
	}

	private AddressStructure convertAddressToSAFTAddress(String addressDetail,
			String zipCode, String zipCodeRegion, String street) {
		AddressStructure companyAddress;
		companyAddress = new AddressStructure();
		companyAddress.setCountry("PT");
		companyAddress.setAddressDetail(Splitter.fixedLength(60)
				.splitToList(addressDetail).get(0));
		companyAddress.setCity(Splitter.fixedLength(49)
				.splitToList(zipCodeRegion).get(0));
		companyAddress.setPostalCode(zipCode);
		companyAddress.setRegion(zipCodeRegion);
		companyAddress.setStreetName(Splitter.fixedLength(49)
				.splitToList(street).get(0));
		return companyAddress;
	}

	private oecd.standardauditfile_tax.pt_1.Product convertProductToSAFTProduct(
			Product product) {
		oecd.standardauditfile_tax.pt_1.Product p = new oecd.standardauditfile_tax.pt_1.Product();

		// ProductCode
		p.setProductCode(product.getCode());

		// ProductDescription
		p.setProductDescription(product.getName().getContent());

		// ProductGroup
		if (product.getProductGroup() != null) {
			p.setProductGroup(product.getProductGroup().getName().getContent());
		}

		// ProductNumberCode
		p.setProductNumberCode(p.getProductCode());

		// ProductType
		/*
		 * Deve ser preenchido com: ?P? ? produtos; ?S? ? servi?os; ?O? ? outros
		 * (ex: portes debitados); ?I? ? impostos, taxas e encargos parafiscais
		 * (excepto IVA e IS que dever?o ser reflectidos na tabela de impostos ?
		 * TaxTable). Texto 1
		 */
		p.setProductType("S");

		return p;
	}

	private MovementTax getSAFTMovementTax(Product product, Vat vat) {
		MovementTax tax = new MovementTax();

		if (vat.getVatType().getCode().equals("RED")) {
			tax.setTaxCode("INT");
		} else if (vat.getVatType().getCode().equals("2")) {
			tax.setTaxCode("RED");
		} else if (vat.getVatType().getCode().equals("3")) {
			tax.setTaxCode("NOR");
		} else if (vat.getVatType().getCode().equals("5")) {
			tax.setTaxCode("ISE");
		} else if (vat.getVatType().getCode().equals("6")) {
			tax.setTaxCode("ISE");
		} else if (vat.getVatType().getCode().equals("1")) {
			tax.setTaxCode("ISE");
		}

		// TODO: Tax-TaxCountryRegion
		tax.setTaxCountryRegion("PT");

		// Tax-TaxPercentage
		tax.setTaxPercentage(vat.getTaxRate());

		// Tax-TaxType
		tax.setTaxType(SAFTPTMovementTaxType.IVA);

		return tax;
	}

	private Tax getSAFTInvoiceTax(Product product, Vat vat) {
		Tax tax = new Tax();
		// Tax-TaxCode

		if (vat.getVatType().getCode().equals("RED")) {
			tax.setTaxCode("INT");
		} else if (vat.getVatType().getCode().equals("2")) {
			tax.setTaxCode("RED");
		} else if (vat.getVatType().getCode().equals("3")) {
			tax.setTaxCode("NOR");
		} else if (vat.getVatType().getCode().equals("5")) {
			tax.setTaxCode("ISE");
		} else if (vat.getVatType().getCode().equals("6")) {
			tax.setTaxCode("ISE");
		} else if (vat.getVatType().getCode().equals("1")) {
			tax.setTaxCode("ISE");
		}

		// TODO: Tax-TaxCountryRegion
		tax.setTaxCountryRegion("PT");

		// Tax-TaxPercentage
		tax.setTaxPercentage(vat.getTaxRate());

		// Tax-TaxType
		tax.setTaxType("IVA");

		return tax;
	}

	public static ExportOperation exportFullSAFT(
			FinantialInstitution finantialInstitution, DateTime fromDate,
			DateTime toDate, String username, Boolean includeMovements) {

		ExportOperation operation = createSaftExportOperation(
				finantialInstitution, fromDate, toDate, username);
		try {
			SAFTExporter saftExporter = new SAFTExporter();
			List<FinantialDocument> documents = finantialInstitution
					.getExportableDocuments(fromDate, toDate);
			logger.info("Collecting " + documents.size()
					+ " documents to export to store "
					+ finantialInstitution.getCode());
			saftExporter.generateSaft(finantialInstitution, fromDate, toDate,
					operation, documents, true);

		} catch (Throwable t) {
			writeError(operation, t);
		}
		return operation;
	}

	private static void writeError(ExportOperation operation, Throwable t) {
		StringWriter out = new StringWriter();
		PrintWriter writer = new PrintWriter(out);
		t.printStackTrace(writer);
		operation.setProcessed(true);
		operation.setErrorLog(out.toString());
	}

	private static ExportOperation createSaftExportOperation(
			FinantialInstitution institution, DateTime fromDate,
			DateTime toDate, String username) {
		return ExportOperation.create(new DateTime(), false, false, "");
		// return ExportOperation.create(username, institution, false, null,
		// java.util.Collections.<SaftDocumentReport> emptyList(),
		// fromDate, toDate, type, null);
	}

	@Atomic
	private void writeSaftFile(String content, ExportOperation operation) {
		byte[] bytes = null;
		try {
			bytes = content.getBytes("Windows-1252");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String fileName = operation.getFinantialInstitution().getFiscalNumber()
				+ "_" + operation.getExecutionDate().toString("ddMMyyyy_hhmm")
				+ ".xml";
		OperationFile binaryStream = new OperationFile(fileName, bytes);
		operation.setFile(binaryStream);
	}

	// @Atomic
	// private void createSaftFile(BinaryStream binaryStream, Long operationID)
	// {
	// SaftFileExportOperation operation = SaftFileExportOperation
	// .readById(operationID);
	// long length = binaryStream.getContent().length;
	// SaftFile.create(
	// operation.getStoreCode() + "-" + System.currentTimeMillis()
	// + ".xml", length, "application/octet-stream",
	// binaryStream, operation);
	// operation.setProcessed(true);
	// }

	public static void exportMovementOfGoods(String username,
			FinantialInstitution institution, DateTime fromDate, DateTime toDate) {

		// if (!deliveryNotesNotValidated.isEmpty()) {
		List<FinantialDocument> pendingDocuments = institution
				.findPendingDocumentsNotExported(fromDate, toDate);

		ExportOperation operation = createSaftExportOperation(institution,
				fromDate, toDate, username);
		try {
			SAFTExporter saftExporter = new SAFTExporter();
			saftExporter.generateSaft(institution, fromDate, toDate, operation,
					pendingDocuments, false);

		} catch (Throwable t) {
			writeError(operation, t);
		}
		// }
	}
}
