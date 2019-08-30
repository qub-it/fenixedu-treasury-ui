package org.fenixedu.treasury.domain.document;

import static org.fenixedu.treasury.util.TreasuryConstants.divide;
import static org.fenixedu.treasury.util.TreasuryConstants.rationalVatRate;
import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.math.BigDecimal;
import java.util.Optional;

import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.Vat;
import org.fenixedu.treasury.domain.debt.DebtAccount;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.services.integration.erp.sap.SAPExporter;
import org.fenixedu.treasury.util.TreasuryConstants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.collect.Maps;

public class ReimbursementUtils {

    public static boolean isCreditNoteSettledWithPayment(final CreditNote creditNote) {
        return creditNote.getCreditEntries().flatMap(c -> c.getSettlementEntriesSet().stream())
                .filter(se -> !((SettlementNote) se.getFinantialDocument()).isReimbursement()).count() > 0;
    }

    public static boolean isCreditNoteForReimbursementMustBeClosedWithDebitNoteAndCreatedNew(final CreditEntry creditEntry) {
        final CreditNote creditNote = (CreditNote) creditEntry.getFinantialDocument();

        if (creditNote.isAnnulled()) {
            throw new TreasuryDomainException("error.ReimbursementUtils.creditNote.annulled");
        }

        if (creditNote.isPreparing()) {
            if (creditNote.getCreditEntries().flatMap(c -> c.getSettlementEntriesSet().stream()).count() > 0) {
                throw new TreasuryDomainException("error.ReimbursementUtils.creditNote.with.settlement.entries.already");
            }

            return false;
        }

        if (creditNote.isAdvancePayment()) {
            return false;
        }

        if (creditNote.isExportedInLegacyERP()) {
            return true;
        }
        
        if(creditNote.getDocumentNumberSeries().getSeries().isRegulationSeries() && 
                creditNote.getCloseDate().isBefore(SAPExporter.ERP_INTEGRATION_START_DATE)) {
            // ANIL 2017-05-04: This is applied to advanced payments in legacy ERP converted 
            // to regulation series with specific product
            return true;
        }

        if (creditNote.getCloseDate().isBefore(SAPExporter.ERP_INTEGRATION_START_DATE)) {
            throw new TreasuryDomainException(
                    "error.ReimbursementUtils.creditNote.marked.as.exportedInLegacyERP.but.close.date.not.conformant");
        }

        return isCreditNoteSettledWithPayment(creditNote);
    }

    public static CreditEntry closeWithDebitNoteAndCreateNewCreditNoteForReimbursement(final CreditEntry originalCreditEntry,
            final BigDecimal amountToReimburseWithVat) {
        final DateTime now = new DateTime();
        final DebtAccount debtAccount = originalCreditEntry.getDebtAccount();
        final FinantialInstitution finantialInstitution = debtAccount.getFinantialInstitution();
        final CreditNote originalCreditNote = (CreditNote) originalCreditEntry.getFinantialDocument();
        final Series series = Series.findUniqueDefault(finantialInstitution).get();

        final DebtAccount payorDebtAccount = originalCreditNote.getPayorDebtAccount();

        final DocumentNumberSeries debitNumberSeries =
                DocumentNumberSeries.find(FinantialDocumentType.findForDebitNote(), series);
        final DocumentNumberSeries creditNumberSeries =
                DocumentNumberSeries.find(FinantialDocumentType.findForCreditNote(), series);
        final DocumentNumberSeries settlementNumberSeries =
                DocumentNumberSeries.find(FinantialDocumentType.findForSettlementNote(), series);

        if (TreasuryConstants.isGreaterThan(amountToReimburseWithVat, originalCreditEntry.getOpenAmount())) {
            throw new TreasuryDomainException("error.ReimbursementUtils.amountToReimburse.greater.than.open.amount.of.credit");
        }

        if (originalCreditNote.isPreparing()) {
            originalCreditNote.closeDocument();
        }

        final Vat vat = originalCreditEntry.getVat();
        final BigDecimal amountToReimburseWithoutVat =
                divide(amountToReimburseWithVat, BigDecimal.ONE.add(rationalVatRate(originalCreditEntry)));

        final DebitNote compensationDebitNote = DebitNote.create(debtAccount, payorDebtAccount, debitNumberSeries, now,
                new LocalDate(), originalCreditNote.getUiDocumentNumber());
        final DebitEntry compensationDebitEntry = DebitEntry.create(Optional.of(compensationDebitNote), debtAccount, null, vat,
                amountToReimburseWithoutVat, new LocalDate(), Maps.newHashMap(), originalCreditEntry.getProduct(),
                treasuryBundle("label.ReimbursementUtils.compensation.debit.entry.description",
                        originalCreditEntry.getDescription()),
                BigDecimal.ONE, null, now);

        compensationDebitNote.closeDocument();

        settlementCompensation(originalCreditEntry, amountToReimburseWithVat, now, debtAccount, settlementNumberSeries,
                compensationDebitEntry);

        final CreditNote creditNoteToReimburse = CreditNote.create(debtAccount, creditNumberSeries, now,
                compensationDebitNote, originalCreditNote.getUiDocumentNumber());
        final CreditEntry creditEntryToReimburse =
                compensationDebitEntry.createCreditEntry(now, originalCreditEntry.getDescription(),
                        originalCreditNote.getDocumentObservations(), amountToReimburseWithoutVat, null, creditNoteToReimburse);

        return creditEntryToReimburse;
    }

    private static void settlementCompensation(final CreditEntry originalCreditEntry, final BigDecimal amountToReimburseWithVat,
            final DateTime now, final DebtAccount debtAccount, final DocumentNumberSeries settlementNumberSeries,
            final DebitEntry compensationDebitEntry) {
        final SettlementNote compensationSettlementNote =
                SettlementNote.create(debtAccount, settlementNumberSeries, now, now, null, null);

        SettlementEntry.create(compensationDebitEntry, compensationSettlementNote, amountToReimburseWithVat,
                compensationDebitEntry.getDescription(), now, false);
        SettlementEntry.create(originalCreditEntry, compensationSettlementNote, amountToReimburseWithVat,
                originalCreditEntry.getDescription(), now, false);
        
        compensationSettlementNote.closeDocument();
    }

}
