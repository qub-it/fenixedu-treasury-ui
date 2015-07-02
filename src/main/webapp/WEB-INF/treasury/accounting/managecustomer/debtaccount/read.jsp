<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="datatables"
    uri="http://github.com/dandelion/datatables"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<spring:url var="datatablesUrl"
    value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl"
    value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl"
    value="/CSS/dataTables/dataTables.bootstrap.min.css" />

<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl"
    value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />
<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
${portal.angularToolkit()}
<%--${portal.toolkit()}--%>

<link
    href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link
    href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link
    href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css"
    rel="stylesheet" />
<script
    src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript"
    src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script
    src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<script
    src="${pageContext.request.contextPath}/webjars/angular-sanitize/1.3.11/angular-sanitize.js"></script>
<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.css" />
<script
    src="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.js"></script>


<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.accounting.manageCustomer.readDebtAccount" />
        <small></small>
    </h1>
</div>
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm"
                action="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/delete/${debtAccount.externalId}"
                method="POST">
                <div class="modal-header">
                    <button type="button" class="close"
                        data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">
                        <spring:message code="label.confirmation" />
                    </h4>
                </div>
                <div class="modal-body">
                    <p>
                        <spring:message
                            code="label.accounting.manageCustomer.readDebtAccount.confirmDelete" />
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                        data-dismiss="modal">
                        <spring:message code="label.close" />
                    </button>
                    <button id="deleteButton" class="btn btn-danger"
                        type="submit">
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
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/read/${debtAccount.customer.externalId}"><spring:message
            code="label.event.back" /></a> &nbsp; |&nbsp;<span
        class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createpayment"><spring:message
            code="label.event.accounting.manageCustomer.createPayment" /></a>
    &nbsp;|&nbsp; <span class="glyphicon glyphicon-cog"
        aria-hidden="true"></span>&nbsp;<a class=""
        href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createreimbursement"><spring:message
            code="label.event.accounting.manageCustomer.createReimbursement" /></a>
    <c:if test='${not debtAccount.getClosed() }'>
 &nbsp;|&nbsp;
        <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a
            class=""
            href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createdebtentry"><spring:message
                code="label.event.accounting.manageCustomer.createDebtEntry" /></a>&nbsp;|&nbsp; <span
            class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a
            class=""
            href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createdebitnote"><spring:message
                code="label.event.accounting.manageCustomer.createDebitNote" /></a> &nbsp;|&nbsp; <span
            class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a
            class=""
            href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/createcreditnote"><spring:message
                code="label.event.accounting.manageCustomer.createCreditNote" /></a> &nbsp;|&nbsp;
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp; <a
            class=""
            href="${pageContext.request.contextPath}/academictreasury/createdebts/operations/${debtAccount.externalId}">
            <spring:message
                code="label.event.accounting.manageCustomer.createDebt" />&nbsp;
        </a>
    </c:if>
    |&nbsp;<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;
    <a class=""
        href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debtAccount.externalId}/readevent">
        <spring:message
            code="label.event.accounting.manageCustomer.readEvent" />
    </a>

</div>
<c:if test="${not empty infoMessages}">
    <div class="alert alert-info" role="alert">

        <c:forEach items="${infoMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon glyphicon-ok-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>

    </div>
</c:if>
<c:if test="${not empty warningMessages}">
    <div class="alert alert-warning" role="alert">

        <c:forEach items="${warningMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
            </p>
        </c:forEach>

    </div>
</c:if>
<c:if test="${not empty errorMessages}">
    <div class="alert alert-danger" role="alert">

        <c:forEach items="${errorMessages}" var="message">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span> ${message}
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
                    <c:if test='${ debtAccount.getClosed() }'>
                        <tr>
                            <th scope="row" class="col-xs-3"><spring:message
                                    code="label.DebtAccount.closed" /></th>
                            <td><span class="label label-warning"><spring:message
                                        code="warning.DebtAccount.is.closed" /></span></td>
                        </tr>
                    </c:if>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.Customer.fiscalNumber" /></th>
                        <td><c:out
                                value='${debtAccount.customer.fiscalNumber}' /></td>
                    </tr>

                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.DebtAccount.customer" /></th>
                        <td><c:out
                                value='${debtAccount.customer.businessIdentification}' />
                            - <c:out
                                value='${debtAccount.customer.name}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.DebtAccount.finantialInstitution" /></th>
                        <td><c:out
                                value='${debtAccount.finantialInstitution.name}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.DebtAccount.balance" /></th>
                        <td><c:out
                                value="${debtAccount.finantialInstitution.currency.getValueFor(debtAccount.totalInDebt)}" />
                            <c:if test="${debtAccount.totalInDebt < 0 }">
                                <span class="label label-warning">
                                    <spring:message
                                        code="label.DebtAccount.customerHasAmountToRehimburse" />
                                </span>
                            </c:if></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.DebtAccount.pendingInterestAmount" /></th>
                        <td><c:out
                                value="${debtAccount.finantialInstitution.currency.getValueFor(debtAccount.calculatePendingInterestAmount())}" /></td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>
<h2>Conta Corrente</h2>
<div id="content">
    <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">

        <li class="active"><a href="#pending" data-toggle="tab"><spring:message
                    code="label.DebtAccount.pendingDocumentEntries" /></a></li>
        <li><a href="#details" data-toggle="tab"><spring:message
                    code="label.DebtAccount.allDocumentEntries" /></a></li>
        <li><a href="#payments" data-toggle="tab"><spring:message
                    code="label.DebtAccount.payments" /></a></li>
        <li><a href="#exemptions" data-toggle="tab"><spring:message
                    code="label.DebtAccount.exemptions" /></a></li>
    </ul>
    <div id="my-tab-content" class="tab-content">
        <div class="tab-pane active" id="pending">
            <!--             <h3>Docs. Pendentes</h3> -->
            <p></p>
            <c:choose>
                <c:when test="${not empty pendingDocumentsDataSet}">
                    <datatables:table id="pendingDocuments"
                        row="pendingEntry"
                        data="${pendingDocumentsDataSet}"
                        cssClass="table table-bordered table-hover"
                        cdn="false" cellspacing="2">
                        <datatables:column
                            cssStyle="width:80px;align:right">
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.date" />
                            </datatables:columnHead>
                            <c:out
                                value='${pendingEntry.entryDateTime.toString("YYYY-MM-dd")}' />
                            <%--                             <joda:format value="${pendingEntry.entryDateTime}" style="S-" /> --%>
                        </datatables:column>

                        <datatables:column cssStyle="width:100px;">
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.finantialDocument" />
                            </datatables:columnHead>
                            <c:if
                                test="${not empty pendingEntry.finantialDocument }">
                                <c:if
                                    test="${pendingEntry.isDebitNoteEntry() }">
                                    <a target="_blank" 
                                        href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${pendingEntry.finantialDocument.externalId}">
                                        <c:out
                                            value="${pendingEntry.finantialDocument.uiDocumentNumber}" />
                                    </a>
                                </c:if>
                                <c:if
                                    test="${pendingEntry.isCreditNoteEntry() }">
                                    <a target="_blank" 
                                        href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${pendingEntry.finantialDocument.externalId}">
                                        <c:out
                                            value="${pendingEntry.finantialDocument.uiDocumentNumber}" />
                                    </a>
                                </c:if>
                            </c:if>
                            <c:if
                                test="${empty pendingEntry.finantialDocument }">
							---
							</c:if>
                        </datatables:column>
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.description" />
                            </datatables:columnHead>
                            <c:out value="${pendingEntry.description}" />
                        </datatables:column>
                        <datatables:column
                            cssStyle="width:15%;align:right">
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.totalAmount" />
                            </datatables:columnHead>
                            <div align=right>
                                <c:if
                                    test="${pendingEntry.isCreditNoteEntry() }">-</c:if>
                                <c:out
                                    value="${pendingEntry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.totalAmount)}" />
                            </div>
                        </datatables:column>
                        <datatables:column
                            cssStyle="width:15%;align:right">
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.openAmount" />
                            </datatables:columnHead>
                            <div align=right>
                                <c:if
                                    test="${pendingEntry.isCreditNoteEntry() }">-</c:if>
                                <c:out
                                    value="${pendingEntry.debtAccount.finantialInstitution.currency.getValueFor(pendingEntry.openAmount)}" />
                            </div>
                        </datatables:column>
                        <datatables:column>
                            <c:if
                                test="${pendingEntry.isDebitNoteEntry() }">
                                <a class="btn btn-default btn-xs"
                                    href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/read/${pendingEntry.externalId}">
                            </c:if>
                            <c:if
                                test="${pendingEntry.isCreditNoteEntry() }">
                                <a class="btn btn-default btn-xs"
                                    href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditentry/read/${pendingEntry.externalId}">
                            </c:if>
                            <spring:message code="label.view" />
                            </a>
                        </datatables:column>
                    </datatables:table>
                    <script>
																					createDataTables(
																							'pendingDocuments',
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
                            <span
                                class="glyphicon glyphicon-exclamation-sign"
                                aria-hidden="true">&nbsp;</span>
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
                    <datatables:table id="allDocuments" row="entry"
                        data="${allDocumentsDataSet}"
                        cssClass="table table-bordered table-hover"
                        cdn="false" cellspacing="2">
                        <datatables:column cssStyle="width:80px">
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.date" />
                            </datatables:columnHead>
                            <c:out
                                value='${entry.entryDateTime.toString("YYYY-MM-dd")}' />
                            <%--                             <joda:format value="${entry.entryDateTime}" style="S-" /> --%>
                        </datatables:column>
                        <datatables:column cssStyle="width:100px;">
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.finantialDocument" />
                            </datatables:columnHead>
                            <c:if
                                test="${not empty entry.finantialDocument }">
                                <c:if
                                    test="${entry.isDebitNoteEntry() }">
                                    <a target="_blank" 
                                        href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${entry.finantialDocument.externalId}">
                                        <c:out
                                            value="${entry.finantialDocument.uiDocumentNumber}" />
                                    </a>
                                </c:if>
                                <c:if
                                    test="${entry.isCreditNoteEntry() }">
                                    <a target="_blank" 
                                        href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${entry.finantialDocument.externalId}">
                                        <c:out
                                            value="${entry.finantialDocument.uiDocumentNumber}" />
                                    </a>
                                </c:if>
                            </c:if>
                            <c:if
                                test="${empty entry.finantialDocument }">
                            ---
                            </c:if>
                        </datatables:column>
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.description" />
                            </datatables:columnHead>
                            <c:out value="${entry.description}" />
                        </datatables:column>
                        <datatables:column cssStyle="width:110px">
                            <datatables:columnHead>
                                <spring:message
                                    code="label.Invoice.totalAmount" />
                            </datatables:columnHead>
                            <div align=right>
                                <c:if
                                    test="${entry.isCreditNoteEntry() }">-</c:if>
                                <c:out
                                    value="${entry.debtAccount.finantialInstitution.currency.getValueFor(entry.totalAmount)}" />
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
                        <datatables:column
                            cssStyle="width:110px;align:right">
                            <datatables:columnHead>
                                <spring:message
                                    code="label.InvoiceEntry.openAmount" />
                            </datatables:columnHead>
                            <div align=right>
                                <c:if
                                    test="${entry.isCreditNoteEntry() }">-</c:if>
                                <c:out
                                    value="${entry.debtAccount.finantialInstitution.currency.getValueFor(entry.openAmount)}" />
                            </div>
                        </datatables:column>
                        <datatables:column>
                            <c:if test="${entry.isDebitNoteEntry() }">
                                <form method="get"
                                    action="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/read/${entry.externalId}">
                            </c:if>
                            <c:if test="${entry.isCreditNoteEntry() }">
                                <form method="get"
                                    action="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditentry/read/${entry.externalId}">
                            </c:if>
                            <button type="submit"
                                class="btn btn-default btn-xs">
                                <spring:message code="label.view" />
                            </button>
                            <%-- 				<form method="post" action="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}/deleteentry/${debitEntry.externalId}"> --%>
                            <!-- 					<button type="submit" class="btn btn-default btn-xs"> -->
                            <!-- 						<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp; -->
                            <%-- 						<spring:message code="label.event.document.manageInvoice.deleteEntry" /> --%>
                            <!-- 					</button> -->
                            </form>
                        </datatables:column>
                    </datatables:table>
                    <script>
																					createDataTables(
																							'allDocuments',
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
                            <span
                                class="glyphicon glyphicon-exclamation-sign"
                                aria-hidden="true">&nbsp;</span>
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
                    <datatables:table id="paymentsDataSet" row="payment"
                        data="${paymentsDataSet}"
                        cssClass="table table-bordered table-hover"
                        cdn="false" cellspacing="2">
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message
                                    code="label.FinantialDocument.documentDate" />
                            </datatables:columnHead>
                            <%--                             <c:out value="${payment.documentDate}" /> --%>
                            <joda:format value="${payment.documentDate}"
                                style="S-" />
                        </datatables:column>
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message
                                    code="label.SettlementEntry.finantialDocument" />
                            </datatables:columnHead>
                            <a target="_blank" 
                                href="${pageContext.request.contextPath}/treasury/document/managepayments/settlementnote/read/${payment.externalId}">
                                <c:out
                                    value="${payment.uiDocumentNumber}" />
                        </datatables:column>
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message
                                    code="label.SettlementNote.settlementEntries" />
                            </datatables:columnHead>
                            <ul>
                                <c:forEach var="settlementEntry"
                                    items="${payment.settlemetEntriesSet}">
                                    <c:if
                                        test="${settlementEntry.invoiceEntry.isDebitNoteEntry() }">
                                        <li><c:out
                                                value="[ ${payment.currency.getValueFor(settlementEntry.amount)} ] ${settlementEntry.description}" /></li>
                                    </c:if>
                                    <c:if
                                        test="${settlementEntry.invoiceEntry.isCreditNoteEntry() }">
                                        <li><c:out
                                                value="[ -${payment.currency.getValueFor(settlementEntry.amount)} ] ${settlementEntry.description}    " /></li>
                                    </c:if>
                                </c:forEach>
                                <c:if
                                    test='${not empty payment.advancedPaymentCreditNote }'>
                                    <c:forEach
                                        var="advancedPaymentEntry"
                                        items="${payment.advancedPaymentCreditNote.creditEntriesSet}">
                                        <li><c:out
                                                value="[ -${payment.currency.getValueFor(advancedPaymentEntry.amount)} ] ${advancedPaymentEntry.description}    " /></li>
                                    </c:forEach>
                                </c:if>
                            </ul>
                        </datatables:column>
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message
                                    code="label.SettlementNote.paymentEntries" />
                            </datatables:columnHead>
                            <ul>
                                <c:forEach var="paymentEntry"
                                    items="${payment.paymentEntriesSet}">
                                    <li><c:out
                                            value="[ ${payment.currency.getValueFor(paymentEntry.payedAmount)} ] ${paymentEntry.paymentMethod.name.content} " /></li>
                                </c:forEach>
                                <c:if
                                    test="${not empty payment.reimbursementEntriesSet }">
                                    <span class="label label-warning" ><spring:message
                                    code="FinantialDocumentTypeEnum.REIMBURSEMENT_NOTE" /></span>
                                    <c:forEach var="reimbursementEntry"
                                        items="${payment.reimbursementEntriesSet}">
                                        <li><c:out
                                                value="[ ${payment.currency.getValueFor(reimbursementEntry.reimbursedAmount)} ] ${reimbursementEntry.paymentMethod.name.content} " /></li>
                                    </c:forEach>
                                </c:if>
                            </ul>
                        </datatables:column>
                        <datatables:column>
                            <form method="get"
                                action="${pageContext.request.contextPath}/treasury/document/managepayments/settlementnote/read/${payment.externalId}">
                                <button type="submit"
                                    class="btn btn-default btn-xs">
                                    <spring:message code="label.view" />
                                </button>
                            </form>
                        </datatables:column>
                    </datatables:table>
                    <script>
																					createDataTables(
																							'paymentsDataSet',
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
                            <span
                                class="glyphicon glyphicon-exclamation-sign"
                                aria-hidden="true">&nbsp;</span>
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
                <c:when test="${not empty exemptionDataSet}">
                    <datatables:table id="exemptionsDataSet" row="exemption" data="${exemptionDataSet}" cssClass="table table-bordered table-hover" cdn="false" cellspacing="2">
                        <datatables:column >
                            <datatables:columnHead>
                                <spring:message code="label.TreasuryExemption.treasuryEvent" />
                            </datatables:columnHead>
                            ${exemption.treasuryEvent.description.content}
                        </datatables:column>
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message code="label.TreasuryExemption.product" />
                            </datatables:columnHead>
                            ${exemption.product.name.content}
                        </datatables:column>
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message code="label.TreasuryExemption.valueToExempt" />
                            </datatables:columnHead>
                            ${exemption.treasuryEvent.debtAccount.finantialInstitution.currency.getValueFor(exemption.valueToExempt)}
                        </datatables:column>
                        <datatables:column>
                            <datatables:columnHead>
                                <spring:message code="label.TreasuryExemption.reason" />
                            </datatables:columnHead>
                            ${exemption.reason}
                        </datatables:column>
                    </datatables:table>
                    <script>
																					createDataTables(
																							'exemptionsDataSet',
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
                            <span
                                class="glyphicon glyphicon-exclamation-sign"
                                aria-hidden="true">&nbsp;</span>
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
</script>