<%@page import="org.fenixedu.treasury.ui.document.forwardpayments.ForwardPaymentController"%>
<%@page
    import="org.fenixedu.treasury.ui.accounting.managecustomer.DebtAccountController"%>
<%@page import="java.math.BigDecimal"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="pf" uri="http://example.com/placeFunctions"%>
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
<%-- ${portal.toolkit()} --%>
${portal.angularToolkit()}


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
    <h1><spring:message code="label.ForwardPaymentController.onlinePayment" /></h1>
    <h1><small><spring:message code="label.ForwardPaymentController.confirmPayment" /></small></h1>
    <div>
        <div class="well well-sm">
			<c:choose>
				<c:when test="${Boolean.TRUE.equals(forwardPaymentConfiguration.getOverrideFinantialInstitutionInfoHeader())}">
					<c:out value="${forwardPaymentConfiguration.finantialInstitutionInfoHeader.content}" escapeXml="false" />
				</c:when>
				<c:otherwise>
					<p>
						<strong><spring:message code="label.DebtAccount.finantialInstitution" />:</strong>
						<c:out value="${settlementNoteBean.debtAccount.finantialInstitution.name}" />
					</p>
					<p>
						<strong><spring:message code="label.FinantialInstitution.fiscalNumber" />:</strong>
						<c:out value="${settlementNoteBean.debtAccount.finantialInstitution.fiscalNumber}" />
					</p>
					<p>
						<strong><spring:message code="label.DebtAccount.finantialInstitution.address" />:</strong>
						<c:out value="${settlementNoteBean.debtAccount.finantialInstitution.address}" />,&nbsp;
						<c:out value="${settlementNoteBean.debtAccount.finantialInstitution.zipCode}" />&nbsp;-&nbsp;
						<c:out value="${settlementNoteBean.debtAccount.finantialInstitution.locality}" />,&nbsp;
						<pf:placeName place="${settlementNoteBean.debtAccount.finantialInstitution.country}" />
					</p>
					<p>
						<strong><spring:message code="label.FinantialInstitution.telephoneContact" />:</strong>
						<c:out value="${settlementNoteBean.debtAccount.finantialInstitution.telephoneContact}" />
					</p>
					<p>
						<strong><spring:message code="label.FinantialInstitution.email" />:</strong>
						<c:out value="${settlementNoteBean.debtAccount.finantialInstitution.email}" />
					</p>
				</c:otherwise>
			</c:choose>

            <p>&nbsp;</p>
            <p>
                <strong><spring:message code="label.DebtAccount.customer" />: </strong>
               	<c:out value='${settlementNoteBean.debtAccount.customer.businessIdentification} - ${settlementNoteBean.debtAccount.customer.name}' />
            </p>
            <p>
                <strong><spring:message code="label.Customer.fiscalNumber" />: </strong>
                <c:out value='${settlementNoteBean.debtAccount.customer.uiFiscalNumber}' />
            </p>
            <p>
            	<strong><spring:message code="label.Customer.address" />: </strong>
            	<c:out value='${settlementNoteBean.debtAccount.customer.address}' />
            </p>
        </div>
    </div>
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

<div>
    <p>
	        1. 
	        <spring:message code="label.ForwardPaymentController.chooseInvoiceEntries" />
	        <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span> 
        <strong>
	        2.
	        <spring:message code="label.ForwardPaymentController.confirmPayment" />
	        <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
        </strong>
	        3. 
	        <spring:message code="label.ForwardPaymentController.enterPaymentDetails" />
	        <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span> 
	        4.
	        <spring:message code="label.ForwardPaymentController.paymentConfirmation" />
    </p>
</div>

<script>
	angular.isUndefinedOrNull = function(val) {
		return angular.isUndefined(val) || val === null
	};
	angular
			.module('angularAppSettlementNote',
					[ 'ngSanitize', 'ui.select', 'bennuToolkit' ])
			.controller(
					'SettlementNoteController',
					[
							'$scope',
							function($scope) {
								$scope.object = ${settlementNoteBeanJson};
				                if($scope.object.previousStates[$scope.object.previousStates.length - 1] != 4) {
				                    $scope.object.previousStates.push(4);
				                }
								$scope.getTotal = function() {
									var total = 0;
									for (var i = 0; i < $scope.object.paymentEntries.length; i++) {
										total += parseFloat($scope.object.paymentEntries[i].paymentAmount);
									}
									return total.toFixed(2);
								}
							} ]);
</script>

<script type="text/javascript">
	function processSubmit(url) {
		$("#summaryForm").attr("action", url);
		$("#summaryForm").submit();
	}
</script>

<div class="panel panel-primary ">
    <div class="panel-heading">
        <h3 class="panel-title">
            <spring:message code="label.ForwardPaymentController.debitEntries.to.confirm" />
        </h3>
    </div>
    <div class="panel-body">
    
		<div class="alert alert-warning" role="alert">
			<h5>
				<spring:message code="label.ForwardPaymentController.debitEntries.confirm.and.pay" />
			</h5>
		</div>
		
		<c:if test="${warningBeforeRedirectionPage != null}">
			<jsp:include page="${warningBeforeRedirectionPage}" />
		</c:if>

        <table id="debitNoteTable"
            class="table responsive table-bordered table-hover" width="100%">
            <thead>
                <tr>
                    <th><spring:message code="label.DebitEntry.documentNumber" /></th>
                    <th><spring:message code="label.DebitEntry.description" /></th>
                    <th><spring:message code="label.DebitEntry.dueDate" /></th>
                    <th><spring:message code="label.DebitEntry.vat" /></th>
                    <th><spring:message code="label.DebitEntry.amountWithVat" /></th>
                </tr>
            </thead>
            <tbody>
                <c:set var="debitNoteDate"
                    value='${settlementNoteBean.debitNoteDate.toString("yyyy-MM-dd")}' />
                <c:forEach items="${ settlementNoteBean.debitEntries }" var="debitEntryBean">
                    <c:if test="${ debitEntryBean.included}">
                        <tr>
                            <td>
								<c:if test="${debitEntryBean.isForDebitEntry() && not empty debitEntryBean.debitEntry.finantialDocument}">
									<c:out value="${ debitEntryBean.debitEntry.finantialDocument.uiDocumentNumber }" />
								</c:if>
                            </td>
                            <td>
								<c:choose>
									<c:when test="${debitEntryBean.isForInstallment()}">
										<p><c:out value="${debitEntryBean.installment.description.content}" /></p>
										<ul style="list-style-type: none;">
										<c:forEach items="${debitEntryBean.installment.sortedInstallmentEntries}" var="entry">
											<li><em><c:out value="${entry.debitEntry.description}" /></em></li>
										</c:forEach>
										</ul>
									</c:when>
									<c:otherwise>
										<c:out value="${ debitEntryBean.description }" />							
									</c:otherwise>
								</c:choose>
                            </td>
                            <td>
                            	<c:choose>
                            		<c:when test="${debitEntryBean.isForDebitEntry()}">
                            			<c:out value="${ debitEntryBean.debitEntry.dueDate }" />
                            		</c:when>
                            		<c:when test="${debitEntryBean.isForInstallment()}">
		                            	<c:out value='${debitEntryBean.installment.dueDate}' />
                            		</c:when>
                            	</c:choose>
                            </td>
                            <td>
                            	<c:if test="${debitEntryBean.isForDebitEntry()}">
                            		<c:out value="${ settlementNoteBean.debtAccount.finantialInstitution.currency.getValueWithScale(debitEntryBean.debitEntry.vat.taxRate) }" />
                            	</c:if>
                            </td>
                            <td><c:out value="${ settlementNoteBean.debtAccount.finantialInstitution.currency.getValueFor(debitEntryBean.settledAmount) }" /></td>
                        </tr>
                    </c:if>
                </c:forEach>
                <c:forEach
                    items="${ settlementNoteBean.virtualDebitEntries }"
                    var="interestEntryBean">
                    <c:if test="${ interestEntryBean.included  }">
                        <tr>
                            <td>---</td>
                            <td><c:out value="${ interestEntryBean.description }" />
                            </td>
                            <td><c:out value='${ debitNoteDate }' />
                            </td>
                            <td>0.00</td>
                            <td><c:out
                                    value="${ settlementNoteBean.debtAccount.finantialInstitution.currency.getValueFor( interestEntryBean.settledAmount ) }" />
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </tbody>
        </table>
        
    	<c:if test="${paymentInStateOfPostPaymentAndPayedOnPlatformWarningMessage}">
			<div class="alert alert-warning" role="alert">
				<h5>
					<strong>
						<spring:message code="label.ForwardPaymentController.paymentInStateOfPostPaymentAndPayedOnPlatformWarning.message" />
					</strong>
				</h5>
			</div>
    	</c:if>
        
    </div>
	<div class="panel-footer">
		 <p align="right">
			<strong><spring:message code="label.document.managepayments.settlementnote.paymentTotal" /></strong>:
			${settlementNoteBean.debtAccount.finantialInstitution.currency.getValueFor( settlementNoteBean.debtAmountWithVat ) }
		 </p>
	</div>
</div>

<form id='summaryForm' name='form' method="post" class="form-horizontal"
    ng-app="angularAppSettlementNote"
    ng-controller="SettlementNoteController"
    action='${pageContext.request.contextPath}${summaryUrl}'>

    <input name="bean" type="hidden" value="{{ object }}" />

    <div class="panel-footer">
		<a href="${pageContext.request.contextPath}${chooseInvoiceEntriesUrl}${settlementNoteBean.debtAccount.externalId}/${settlementNoteBean.digitalPaymentPlatform.externalId}"
			class="btn btn-default">
			<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"> </span>
			&nbsp;
			<spring:message code="label.event.back" />
		</a>
		<button type="button" class="btn btn-primary"
            onClick="javascript:processSubmit('${pageContext.request.contextPath}${summaryUrl}')">

            <spring:message code="label.event.accounting.manageCustomer.doForwardPayment" />
			&nbsp;<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        </button>
    </div>
</form>

<c:if test="${forwardPaymentConfiguration.isLogosPageDefined()}">
	<jsp:include page="${logosPage}" /> 
</c:if>
