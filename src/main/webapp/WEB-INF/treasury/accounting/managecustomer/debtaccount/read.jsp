<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css" />

<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
${portal.angularToolkit()}
<%--${portal.toolkit()}--%>

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<script src="${pageContext.request.contextPath}/webjars/angular-sanitize/1.3.11/angular-sanitize.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.css" />
<script src="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.js"></script>


<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message code="label.accounting.manageCustomer.readDebtAccount" />
		<small></small>
	</h1>
</div>
<div class="modal fade" id="deleteModal">
	<div class="modal-dialog">
		<div class="modal-content">
			<form id="deleteForm" action="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/delete/${debtAccount.externalId}" method="POST">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">
						<spring:message code="label.confirmation" />
					</h4>
				</div>
				<div class="modal-body">
					<p>
						<spring:message code="label.accounting.manageCustomer.readDebtAccount.confirmDelete" />
					</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="label.close" />
					</button>
					<button id="deleteButton" class="btn btn-danger" type="submit">
						<spring:message code="label.delete" />
					</button>
				</div>
			</form>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/read/${debtAccount.customer.externalId}"><spring:message code="label.event.back" /></a>
	|&nbsp;&nbsp; <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createpayment"><spring:message
			code="label.event.accounting.manageCustomer.createPayment" /></a> &nbsp;|&nbsp; <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createdebtentry"><spring:message
			code="label.event.accounting.manageCustomer.createDebtEntry" /></a>&nbsp;|&nbsp; <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createdebitnote"><spring:message
			code="label.event.accounting.manageCustomer.createDebitNote" /></a> &nbsp;|&nbsp; <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createexemption"><spring:message
			code="label.event.accounting.manageCustomer.createExemption" /></a> &nbsp;|&nbsp; <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/readevent"><spring:message
			code="label.event.accounting.manageCustomer.readEvent" /></a>
</div>
<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">

		<c:forEach items="${warningMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>

	</div>
</c:if>

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">
			<spring:message code="label.details" />
		</h3>
	</div>
	<div class="panel-body">
		<form method="post" class="form-horizontal">
			<table class="table">
				<tbody>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message code="label.DebtAccount.customer" /></th>
						<td><c:out value='${debtAccount.customer.name}' /></td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message code="label.DebtAccount.finantialInstitution" /></th>
						<td><c:out value='${debtAccount.finantialInstitution.name}' /></td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message code="label.DebtAccount.balance" /></th>
						<td><c:out value="${debtAccount.obtainUITotalInDebt()}" /></td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message code="label.DebtAccount.pendingInterestAmount" /></th>
						<td><c:out value="${debtAccount.finantialIntitution.currency.getValueFor(debtAccount.calculatePendingInterestAmount())}" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>
<h2>Conta Corrente</h2>
<div id="content">
	<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">

		<li class="active"><a href="#pending" data-toggle="tab">Docs. Pendentes</a></li>
		<li><a href="#details" data-toggle="tab">Extracto</a></li>
		<li><a href="#payments" data-toggle="tab">Pagamentos</a></li>
		<li><a href="#exemptions" data-toggle="tab">Isencoes</a></li>
	</ul>
	<div id="my-tab-content" class="tab-content">
		<div class="tab-pane active" id="pending">
			<!--             <h3>Docs. Pendentes</h3> -->
			<p></p>
			<c:choose>
				<c:when test="${not empty pendingDocumentsDataSet}">
					<datatables:table id="pendingDocuments" row="pendingEntry" data="${pendingDocumentsDataSet}" cssClass="table table-bordered table-hover" cdn="false" cellspacing="2">
						<datatables:column>
							<datatables:columnHead>
								<spring:message code="label.InvoiceEntry.finantialDocument" />
							</datatables:columnHead>
							<c:if test="${not empty pendingEntry.finantialDocument }">
								<c:if test="${pendingEntry.isDebitNoteEntry() }">
									<a href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${pendingEntry.finantialDocument.externalId}"> <c:out
											value="${pendingEntry.finantialDocument.uiDocumentNumber}" /></a>
								</c:if>
								<c:if test="${pendingEntry.isCreditNoteEntry() }">
									<a href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${pendingEntry.finantialDocument.externalId}"> <c:out
											value="${pendingEntry.finantialDocument.uiDocumentNumber}" /></a>
								</c:if>
							</c:if>
							<c:if test="${empty pendingEntry.finantialDocument }">
							---
							</c:if>
						</datatables:column>
						<datatables:column cssStyle="width:10%;align:right">
							<datatables:columnHead>
								<spring:message code="label.InvoiceEntry.description" />
							</datatables:columnHead>
							<c:out value="${pendingEntry.description}" />
						</datatables:column>
						<datatables:column>
							<datatables:columnHead>
								<spring:message code="label.InvoiceEntry.date" />
							</datatables:columnHead>
							<c:out value="${pendingEntry.entryDateTime}" />
						</datatables:column>
						<%-- 						<datatables:column> --%>
						<%-- 							<datatables:columnHead> --%>
						<%-- 								<spring:message code="label.Invoice.debitAmount" /> --%>
						<%-- 							</datatables:columnHead> --%>
						<!-- 							<div align=right> -->
						<%-- 								<c:out value="${pendingEntry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.debitAmount)}" /> --%>
						<!-- 							</div> -->
						<%-- 						</datatables:column> --%>
						<datatables:column>
							<datatables:columnHead>
								<c:if test="${pendingEntry.isCreditNoteEntry() }">-</c:if>
								<spring:message code="label.InvoiceEntry.totalAmount" />
							</datatables:columnHead>
							<div align=right>
								<c:if test="${pendingEntry.isCreditNoteEntry() }">-</c:if>
								<c:out value="${pendingEntry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.totalAmount)}" />
							</div>
						</datatables:column>
						<datatables:column>
							<datatables:columnHead>
								<c:if test="${pendingEntry.isCreditNoteEntry() }">-</c:if>
								<spring:message code="label.InvoiceEntry.openAmount" />
							</datatables:columnHead>
							<div align=right>
								<c:if test="${pendingEntry.isCreditNoteEntry() }">-</c:if>
								<c:out value="${pendingEntry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.openAmount)}" />
							</div>
						</datatables:column>
						<datatables:column>
							<form method="get" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/read/${pendingEntry.externalId}">
								<button type="submit" class="btn btn-default btn-xs">
									<spring:message code="label.view" />
								</button>
							</form>
							<%-- 				<form method="post" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}/deleteentry/${debitEntry.externalId}"> --%>
							<!-- 					<button type="submit" class="btn btn-default btn-xs"> -->
							<!-- 						<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp; -->
							<%-- 						<spring:message code="label.event.document.manageInvoice.deleteEntry" /> --%>
							<!-- 					</button> -->
							<!-- 				</form> -->
						</datatables:column>
					</datatables:table>
					<script>
						createDataTables('pendingDocuments', false, false,
								false, "${pageContext.request.contextPath}",
								"${datatablesI18NUrl}");
					</script>
				</c:when>
				<c:otherwise>
					<div class="alert alert-warning" role="alert">

						<p>
							<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
							<spring:message code="label.noResultsFound" />
						</p>

					</div>

				</c:otherwise>
			</c:choose>
		</div>
		<div class="tab-pane" id="details">
			<!--             <h3>Extracto</h3> -->
			<p></p>
			<c:choose>
				<c:when test="${not empty allDocumentsDataSet}">
					<datatables:table id="allDocuments" row="entry" data="${allDocumentsDataSet}" cssClass="table table-bordered table-hover" cdn="false" cellspacing="2">
						<datatables:column>
							<datatables:columnHead>
								<spring:message code="label.InvoiceEntry.finantialDocument" />
							</datatables:columnHead>
							<c:if test="${not empty entry.finantialDocument }">
								<a href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${entry.finantialDocument.externalId}"> <c:out
										value="${entry.finantialDocument.uiDocumentNumber}" />
							</c:if>
							<c:if test="${empty entry.finantialDocument }">
							---
							</c:if>
						</datatables:column>
						<datatables:column cssStyle="width:10%;align:right">
							<datatables:columnHead>
								<spring:message code="label.InvoiceEntry.description" />
							</datatables:columnHead>
							<c:out value="${entry.description}" />
						</datatables:column>
						<datatables:column>
							<datatables:columnHead>
								<spring:message code="label.InvoiceEntry.date" />
							</datatables:columnHead>
							<c:out value="${entry.entryDateTime}" />
						</datatables:column>
						<datatables:column>
							<datatables:columnHead>
								<spring:message code="label.Invoice.totalAmount" />
							</datatables:columnHead>
							<div align=right>
								<c:out value="${entry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.totalAmount)}" />
							</div>
						</datatables:column>
						<%-- 						<datatables:column> --%>
						<%-- 							<datatables:columnHead> --%>
						<%-- 								<spring:message code="label.InvoiceEntry.creditAmount" /> --%>
						<%-- 							</datatables:columnHead> --%>
						<!-- 							<div align=right> -->
						<%-- 								<c:out value="${entry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.creditAmount)}" /> --%>
						<!-- 							</div> -->
						<%-- 						</datatables:column> --%>
						<datatables:column>
							<datatables:columnHead>
								<spring:message code="label.InvoiceEntry.openAmount" />
							</datatables:columnHead>
							<div align=right>
								<c:out value="${entry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.openAmount)}" />
							</div>
						</datatables:column>
						<datatables:column>
							<form method="get" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/read/${entry.externalId}">
								<button type="submit" class="btn btn-default btn-xs">
									<spring:message code="label.view" />
								</button>
							</form>
							<%-- 				<form method="post" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}/deleteentry/${debitEntry.externalId}"> --%>
							<!-- 					<button type="submit" class="btn btn-default btn-xs"> -->
							<!-- 						<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp; -->
							<%-- 						<spring:message code="label.event.document.manageInvoice.deleteEntry" /> --%>
							<!-- 					</button> -->
							<!-- 				</form> -->
						</datatables:column>
					</datatables:table>
					<script>
						createDataTables('allDocuments', false, false, false,
								"${pageContext.request.contextPath}",
								"${datatablesI18NUrl}");
					</script>
				</c:when>
				<c:otherwise>
					<div class="alert alert-warning" role="alert">

						<p>
							<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
							<spring:message code="label.noResultsFound" />
						</p>

					</div>

				</c:otherwise>
			</c:choose>
		</div>
		<div class="tab-pane" id="payments">
			<!--             <h3>Pagamentos</h3> -->
			<p></p>
			<c:choose>
				<c:when test="${not empty paymentsDataSet}">
					<table id="paymentsTable" class="table responsive table-bordered table-hover">
						<thead>
							<tr>
								<%--!!!  Field names here --%>
								<th><spring:message code="label.accounting.manageCustomer.readCustomer.debtItems" /></th>
								<th><spring:message code="label.accounting.manageCustomer.readCustomer.dueDate" /></th>
								<%-- 								<th><spring:message code="label.accounting.manageCustomer.readCustomer.debitAmount" /></th> --%>
								<th><spring:message code="label.accounting.manageCustomer.readCustomer.totalAmount" /></th>
								<th><spring:message code="label.accounting.manageCustomer.readCustomer.pendingAmount" /></th>
								<%-- Operations Column --%>
								<th></th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="alert alert-warning" role="alert">

						<p>
							<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
							<spring:message code="label.noResultsFound" />
						</p>

					</div>

				</c:otherwise>
			</c:choose>
		</div>
		<div class="tab-pane" id="exemptions">
			<!--             <h3>Isencoes</h3> -->
			<p></p>
			<c:choose>
				<c:when test="${not empty exemptionsDataSet}">
					<table id="exemptionsTable" class="table responsive table-bordered table-hover">
						<thead>
							<tr>
								<%--!!!  Field names here --%>
								<th><spring:message code="label.accounting.manageCustomer.readCustomer.debtItems" /></th>
								<th><spring:message code="label.accounting.manageCustomer.readCustomer.dueDate" /></th>
								<%-- 								<th><spring:message code="label.accounting.manageCustomer.readCustomer.debitAmount" /></th> --%>
								<th><spring:message code="label.accounting.manageCustomer.readCustomer.totalAmount" /></th>
								<th><spring:message code="label.accounting.manageCustomer.readCustomer.pendingAmount" /></th>
								<%-- Operations Column --%>
								<th></th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="alert alert-warning" role="alert">

						<p>
							<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
							<spring:message code="label.noResultsFound" />
						</p>

					</div>

				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {

		//Enable Bootstrap Tabs
		$('#tabs').tab();

		var tablePayments = $('#paymentsTable').DataTable({
			language : {
				url : "${datatablesI18NUrl}",
			},
			//CHANGE_ME adjust the actions column width if needed
			"columnDefs" : [
			//54
			{
				"width" : "54px",
				"targets" : 4
			} ],
			"dom" : '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
		});

		tablePayments.columns.adjust().draw();

		var tableExemptions = $('#exemptionsTable').DataTable({
			language : {
				url : "${datatablesI18NUrl}",
			},
			"columnDefs" : [
			//54
			{
				"width" : "54px",
				"targets" : 4
			} ],
			//Documentation: https://datatables.net/reference/option/dom
			"dom" : '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
		});

		table.columns.adjust().draw();

	});
