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
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>



<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message code="label.document.manageInvoice.readCreditNote" />
        <small></small>
    </h1>
</div>
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/delete/${creditNote.externalId}" method="POST">
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
                        <spring:message code="label.document.manageInvoice.readCreditNote.confirmDelete" />
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

<div class="modal fade" id="closeModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${creditNote.externalId}/closecreditnote" method="POST">
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
                        <spring:message code="label.document.manageInvoice.readCreditNote.confirmClose" />
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <spring:message code="label.cancel" />
                    </button>
                    <button id="deleteButton" class="btn btn-primary" type="submit">
                        <spring:message code="label.close" />
                    </button>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<div class="modal fade" id="anullModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${creditNote.externalId}/anullcreditnote" method="POST">
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
                        <spring:message code="label.document.manageInvoice.readCreditNote.confirmAnull" />
                    </p>
                    <div class="form">
                        <div class="form-group row">
                            <div class="col-sm-4 control-label">
                                <spring:message code="label.CreditNote.annulledReason" />
                            </div>

                            <div class="col-sm-8">
                                <input id="CreditNote_anullReason" class="form-control" type="text" name="anullReason" required value='' />
                            </div>
                        </div>
                    </div>


                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <spring:message code="label.cancel" />
                    </button>
                    <button id="deleteButton" class="btn btn-danger" type="submit">
                        <spring:message code="label.annull" />
                    </button>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<%-- NAVIGATION --%>
<%-- NAVIGATION --%>
<form>
    <div class="well well-sm" style="display: inline-block">
        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
            href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${creditNote.debtAccount.externalId}"><spring:message
                code="label.event.back" /></a> &nbsp;|&nbsp;

        <c:if test="${creditNote.isPreparing() || creditNote.isClosed()}">
            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<a class=""
                href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/update/${creditNote.externalId}"><spring:message code="label.event.update" /></a>
		&nbsp;|&nbsp;
		</c:if>
        <c:if test="${creditNote.isPreparing()}">
            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp;<a class="" href="#" data-toggle="modal" data-target="#deleteModal"><spring:message
                    code="label.event.delete" /></a>  &nbsp;|&nbsp; 
			<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
            <a class="" href="#" data-toggle="modal" data-target="#closeModal"> <spring:message code="label.event.document.manageInvoice.closeCreditNote" />
            </a> &nbsp;|&nbsp; 
		</c:if>
        <c:if test="${creditNote.isClosed()}">
            <c:if test="${creditNote.openAmount > 0  }">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
                <a class="" href="#" data-toggle="modal" data-target="#anullModal"> <spring:message code="label.event.document.manageInvoice.anullCreditNote" />
                </a> &nbsp;|&nbsp;
            </c:if>
            <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class=""
                href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${creditNote.externalId}/exportintegrationfile"><spring:message
                    code="label.event.document.manageInvoice.exportIntegrationFile" /></a>

        </c:if>
    </div>
</form>


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
                        <th scope="row" class="col-xs-3"><spring:message code="label.Series.finantialInstitution" /></th>
                        <td><c:out value='${creditNote.documentNumberSeries.series.finantialInstitution.name}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.DebtAccount.customer" /></th>
                        <td><c:out value='${creditNote.debtAccount.customer.code} - ${creditNote.debtAccount.customer.name}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditNote.documentNumber" /></th>
                        <td><c:out value='${creditNote.uiDocumentNumber}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditNote.documentDate" /></th>
                        <td><c:out value='${creditNote.documentDate}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditNote.originDocumentNumber" /></th>
                        <td><c:out value='${creditNote.originDocumentNumber}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditNote.debitNote" /></th>
                        <td><c:if test="${not creditNote.debitNote}">
                                <span class="label label-warning"> <spring:message code="label.document.manageinvoice.creditnote.without.debitnote" />
                                </span>
                            </c:if> <c:if test="${not empty creditNote.debitNote}">

                                <a href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${creditNote.getDebitNote().externalId}"><c:out
                                        value='${creditNote.getDebitNote().uiDocumentNumber}' /></a>
                            </c:if></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditNote.state" /></th>
                        <td><c:if test="${creditNote.isAnnulled()}">
                                <span class="label label-danger">
                            </c:if> <c:if test="${creditNote.isPreparing() }">
                                <span class="label label-warning">
                            </c:if> <c:if test="${creditNote.isClosed()}">
                                <span class="label label-primary">
                            </c:if> <c:out value='${creditNote.state.descriptionI18N.content}' /> </span></td>
                    </tr>
                    <c:if test="${creditNote.isAnnulled()}">
                        <tr>
                            <th scope="row" class="col-xs-3"><spring:message code="label.CreditNote.annulledReason" /></th>
                            <td><c:out value='${creditNote.annulledReason}' /></td>
                        </tr>
                    </c:if>

                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.DebitNote.totalNetAmount" /></th>
                        <td><c:out value='${creditNote.debtAccount.finantialInstitution.currency.getValueFor(creditNote.totalNetAmount)}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.DebitNote.totalVatAmount" /></th>
                        <td><c:out value='${creditNote.debtAccount.finantialInstitution.currency.getValueFor(creditNote.totalVatAmount)}' /></td>
                    </tr>

                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.DebitNote.totalAmount" /></th>
                        <td><c:out value='${creditNote.debtAccount.finantialInstitution.currency.getValueFor(creditNote.totalAmount)}' /></td>
                    </tr>

                    <c:if test="${not empty creditNote.relatedSettlementEntries }">
                        <tr>
                            <th scope="row" class="col-xs-3"><spring:message code="label.DebitNote.relatedSettlementEntries" /></th>
                            <td>
                                <ul>
                                    <c:forEach var="settlementEntry" items="${creditNote.relatedSettlementEntries}">
                                        <li><c:out
                                                value='${settlementEntry.finantialDocument.uiDocumentNumber} - ${ settlementEntry.debtAcccount.finantialInstitution.currency.getValueFor(settlementEntry.amount)}' /></li>
                                    </c:forEach>
                                </ul>
                            </td>
                        </tr>
                    </c:if>

                </tbody>
            </table>
        </form>
    </div>
</div>
<h2>
    <spring:message code="label.CreditNote.creditEntries" />
</h2>

<!-- NAVIGATION -->
<c:if test="${creditNote.isPreparing()}">
    <div class="well well-sm" style="display: inline-block">
        <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a
            href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${creditNote.externalId}/addentry"><spring:message
                code="label.event.document.manageInvoice.addCreditEntry" /></a> &nbsp;|
    </div>
</c:if>
<c:choose>
    <c:when test="${not empty creditNote.creditEntriesSet}">
        <datatables:table id="creditEntries" row="creditEntry" data="${creditNote.creditEntriesSet}" cssClass="table responsive table-bordered table-hover" cdn="false"
            cellspacing="2">
            <datatables:column cssStyle="width:10%">
                <datatables:columnHead>
                    <spring:message code="label.InvoiceEntry.quantity" />
                </datatables:columnHead>
                <c:out value="${creditEntry.quantity}" />
            </datatables:column>
            <datatables:column>
                <datatables:columnHead>
                    <spring:message code="label.CreditEntry.description" />
                </datatables:columnHead>
                <c:out value="${creditEntry.description}" />
            </datatables:column>
            <datatables:column cssStyle="width:10%">
                <datatables:columnHead>
                    <spring:message code="label.CreditEntry.amount" />
                </datatables:columnHead>
                <c:out value="${creditEntry.currency.getValueFor(creditEntry.totalAmount)}" />
            </datatables:column>
            <datatables:column cssStyle="width:10%">
                <datatables:columnHead>
                    <spring:message code="label.CreditEntry.vat" />
                </datatables:columnHead>
                <c:out value="${creditEntry.vat.taxRate} %" />
            </datatables:column>
            <datatables:column cssStyle="width:10%">
                <form method="get" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditentry/read/${creditEntry.externalId}">
                    <button type="submit" class="btn btn-default btn-xs">
                        <spring:message code="label.view" />
                    </button>
                </form>
            </datatables:column>
        </datatables:table>
        <script>
									createDataTables(
											'creditEntries',
											false,
											false,
											false,
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

<script>
	$(document).ready(function() {

	});
</script>
