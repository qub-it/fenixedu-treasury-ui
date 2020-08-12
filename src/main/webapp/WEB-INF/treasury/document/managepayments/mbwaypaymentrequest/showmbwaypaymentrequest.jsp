<%@page import="org.fenixedu.treasury.ui.accounting.managecustomer.MbwayPaymentRequestController"%>
<%@page import="org.fenixedu.treasury.domain.settings.TreasurySettings"%>
<%@page import="org.fenixedu.treasury.ui.accounting.managecustomer.DebtAccountController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
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
        <spring:message code="label.document.managePayments.showMbwayPaymentRequest" />
        <small></small>
    </h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
        href="${pageContext.request.contextPath}${debtAccountUrl}"><spring:message
            code="label.event.back" /></a> &nbsp;
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

<script>
	angular
.module('angularApp',
		[ 'ngSanitize', 'ui.select', 'bennuToolkit' ])
.controller(
		'angularController',
		[
				'$scope',
				function($scope) {
					$scope.booleanvalues = [
							{
								name : '<spring:message code="label.no"/>',
								value : false
							},
							{
								name : '<spring:message code="label.yes"/>',
								value : true
							} ];

					$scope.object = angular.fromJson('${beanJson}');
					$scope.postBack = createAngularPostbackFunction($scope);

					//Begin here of Custom Screen business JS - code

					$scope.toggleDebitEntries = function toggleSelection(debitEntryId) {
						if($scope.object.selectedDebitEntries === undefined) {
							$scope.object.selectedDebitEntries = [];
						}
						
						var idx = $scope.object.selectedDebitEntries.indexOf(debitEntryId);
						
						// is currently selected
						if (idx > -1) {
						  $scope.object.selectedDebitEntries.splice(idx, 1);
						} else {
							// is newly selected
						  $scope.object.selectedDebitEntries.push(debitEntryId);
						}
						
						$scope.postBack();
					};
					
				} ]);
</script>

<div class="page-header">
    <div>
        <div class="well well-sm">
            <p>
                <strong><spring:message code="label.DebtAccount.finantialInstitution" />: </strong>
                <c:out value="${debtAccount.finantialInstitution.name}" />
            </p>
			<p>
				<strong><spring:message code="label.FinantialInstitution.fiscalNumber" />:</strong>
				<c:out value="${debtAccount.finantialInstitution.fiscalNumber}" />
			</p>
            <p>
                <strong><spring:message code="label.DebtAccount.finantialInstitution.address" />: </strong>
				<c:out value="${debtAccount.finantialInstitution.address}" />,&nbsp;
				<c:out value="${debtAccount.finantialInstitution.zipCode}" />&nbsp;-&nbsp;
				<c:out value="${debtAccount.finantialInstitution.locality}" />,&nbsp;
				<pf:placeName place="${debtAccount.finantialInstitution.country}" />
            </p>
			<p>
				<strong><spring:message code="label.FinantialInstitution.telephoneContact" />:</strong>
				<c:out value="${debtAccount.finantialInstitution.telephoneContact}" />
			</p>
			<p>
				<strong><spring:message code="label.FinantialInstitution.email" />:</strong>
				<c:out value="${debtAccount.finantialInstitution.email}" />
			</p>

            <p>&nbsp;</p>
            <p>
                <strong><spring:message code="label.DebtAccount.customer" />: </strong>
               	<c:out value='${debtAccount.customer.businessIdentification} - ${debtAccount.customer.name}' />
            </p>
            <p>
                <strong><spring:message code="label.Customer.fiscalNumber" />: </strong>
                <c:out value='${debtAccount.customer.uiFiscalNumber}' />
            </p>
            <p>
            	<strong><spring:message code="label.Customer.address" />: </strong>
            	<c:out value='${debtAccount.customer.address}' />
            </p>
        </div>
    </div>
</div>


<div class="alert alert-info" role="alert">
	<p><strong><spring:message code="label.MbwayPaymentRequest.created.request.with.success" /></strong></p>
	<p><strong><spring:message code="label.MbwayPaymentRequest.proceed.with.payment" /></strong></p>
</div>

<div class="panel panel-default">
	<div class="panel-body">
		<table class="table">
			<tbody>
				<tr>
					<th scope="row" class="col-xs-3"><spring:message code="label.MbwayPaymentRequest.creationDate" /></th>
					<td><c:out value='${mbwayPaymentRequest.requestDate.toString("yyyy-MM-dd HH:mm")}' /></td>
				</tr>
				<tr>
					<th scope="row" class="col-xs-3"><spring:message code="label.MbwayPaymentRequest.phoneNumber" /></th>
					<td><c:out value='${mbwayPaymentRequest.phoneNumber}' /></td>
				</tr>
				<tr>
					<th scope="row" class="col-xs-3"><spring:message code="label.MbwayPaymentRequest.payableAmount" /></th>
					<td><c:out value='${debtAccount.finantialInstitution.currency.getValueFor(mbwayPaymentRequest.payableAmount)}' /></td>
				</tr>
			</tbody>
		</table>
	</div>

</div>

<div class="alert alert-warn" role="alert">
	<p><strong><spring:message code="label.MbwayPaymentRequest.warning.payment.registration" /></strong></p>
</div>

<div style="text-align:center;">
	<img src="${pageContext.request.contextPath}/static/treasury/images/forwardpayments/netcaixa/mb.png" />
</div>

<script>
	$(document).ready(function() {});
</script>
