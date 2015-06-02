<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
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
        <spring:message code="label.document.manageInvoice.readCreditEntry" />
        <small></small>
    </h1>
</div>
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditentry/delete/${creditEntry.externalId}" method="POST">
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
                        <spring:message code="label.document.manageInvoice.readCreditEntry.confirmDelete" />
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
    <c:if test="${not empty creditEntry.finantialDocument }">
        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
            href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${creditEntry.finantialDocument.externalId}"><spring:message
                code="label.document.manageInvoice.event.backToCreditNote" /></a>&nbsp|&nbsp; 
    </c:if>
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
        href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${creditEntry.debtAccount.externalId}"><spring:message
            code="label.document.manageInvoice.readDebitEntry.event.backToDebtAccount" /></a> &nbsp;|&nbsp;
    <c:if test="${empty creditEntry.finantialDocument ||  creditEntry.finantialDocument.isPreparing()}">
        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp;<a class="" href="#" data-toggle="modal" data-target="#deleteModal"><spring:message
                code="label.event.delete" /></a> &nbsp;|&nbsp; <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<a class=""
            href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditentry/update/${creditEntry.externalId}"><spring:message code="label.event.update" /></a>
    &nbsp;|&nbsp;
    </c:if>
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
                        <th scope="row" class="col-xs-3"><spring:message code="label.InvoiceEntry.debtAccount" /></th>
                        <td><c:out value='${creditEntry.debtAccount.customer.code} - ${creditEntry.debtAccount.customer.name}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.FinantialDocumentEntry.finantialDocument" /></th>
                        <td><c:if test="${not empty creditEntry.finantialDocument}">
                                <c:out value='${creditEntry.finantialDocument.uiDocumentNumber}' />
                            </c:if> <c:if test="${empty creditEntry.finantialDocument}">
                                <span class="label label-warning"> <spring:message code="label.CreditEntry.creditEntry.with.no.document" />
                                </span>
                            </c:if></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.FinantialDocumentEntry.entryDate" /></th>
                        <td><c:out value='${creditEntry.entryDateTime}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditEntry.debitNote" /></th>
                        <td><c:if test="${not empty creditEntry.debitEntry}">
                        <a href ="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${creditEntry.debitEntry.finantialDocument.externalId}">
                                <c:out value='${creditEntry.debitEntry.finantialDocument.uiDocumentNumber}' /></a>
                            </c:if> </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.InvoiceEntry.description" /></th>
                        <td><c:out value='${creditEntry.product.code} - ${creditEntry.description}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.InvoiceEntry.quantity" /></th>
                        <td><c:out value='${creditEntry.quantity}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditEntry.amount" /></th>
                        <td><c:out value='${creditEntry.currency.getValueFor(creditEntry.amount)}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditEntry.vat" /></th>
                        <td><c:out value='${creditEntry.vat.taxRate} % ' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.CreditEntry.totalAmount" /></th>
                        <td><c:out value='${creditEntry.currency.getValueFor(creditEntry.totalAmount)}' /></td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>

<script>
    $(document).ready(function() {

    });
</script>
