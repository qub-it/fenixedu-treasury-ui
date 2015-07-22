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
        <spring:message code="label.document.managePayments.createPaymentCodeInDebitNote" />
        <small></small>
    </h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
        href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${paymentReferenceCodeBean.debitNote.externalId}"><spring:message
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
			.module('angularAppPaymentReferenceCode',
					[ 'ngSanitize', 'ui.select', 'bennuToolkit' ])
			.controller(
					'PaymentReferenceCodeController',
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

								$scope.object = angular
										.fromJson('${paymentReferenceCodeBeanJson}');
								$scope.postBack = createAngularPostbackFunction($scope);

								//Begin here of Custom Screen business JS - code

								$scope.onPoolChange = function(pool, model) {
									$scope.postBack(model);
								};
							} ]);
</script>

<form name='form' method="post" class="form-horizontal" ng-app="angularAppPaymentReferenceCode" ng-controller="PaymentReferenceCodeController"
    action='${pageContext.request.contextPath}/treasury/document/managepayments/paymentreferencecode/createpaymentcodeindebitnote'>

    <input type="hidden" name="postback" value='${pageContext.request.contextPath}/treasury/document/managepayments/paymentreferencecode/createpaymentcodeindebitnotepostback' /> <input
        name="bean" type="hidden" value="{{ object }}" />
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.finantialInstitution" />
                </div>

                <div class="col-sm-10">
                    <input class="col-sm-6" type="text" value="<c:out value='${paymentReferenceCodeBean.debitNote.debtAccount.finantialInstitution.name}'/>" disabled />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.DebtAccount.customer" />
                </div>

                <div class="col-sm-10">
                    <input class="col-sm-6" type="text"
                        value="<c:out value='${paymentReferenceCodeBean.debitNote.debtAccount.customer.businessIdentification} - ${paymentReferenceCodeBean.debitNote.debtAccount.customer.name}'/>"
                        disabled />
                </div>
            </div>
            <div class="form-group row ">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.DebitNote.documentNumber" />
                </div>

                <div class="col-sm-4">
                    <input class="col-sm-12" type="text" value="<c:out value='${paymentReferenceCodeBean.debitNote.uiDocumentNumber}'/>" disabled />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentReferenceCode.paymentCodePool" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <ui-select id="paymentReferenceCode_paymentCodePool" name="paymentcodepool" on-select="onPoolChange($item, $model)" ng-model="$parent.object.paymentCodePool"
                        theme="bootstrap" ng-disabled="disabled"> <ui-select-match>{{$select.selected.text}}</ui-select-match> <ui-select-choices
                        repeat="paymentCodePool.id as paymentCodePool in object.paymentCodePoolDataSource | filter: $select.search"> <span
                        ng-bind-html="paymentCodePool.text | highlight: $select.search"></span> </ui-select-choices> </ui-select>
                </div>
            </div>
            <div class="form-group row" >
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentReferenceCode.useCustomPaymentAmount" />
                </div>
                <div class="col-sm-4">
                         <input type="checkbox" ng-model="object.useCustomPaymentAmount" />
                </div>
            </div>

            <div class="form-group row" ng-show=" object.usePaymentAmountWithInterests == false ">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentReferenceCode.payableAmount" />
                </div>
                <div class="col-sm-10">
                    <div class="input-group">
                        <div class="input-group-addon">
                            <c:out value="${paymentReferenceCodeBean.debitNote.debtAccount.finantialInstitution.currency.symbol}" />
                        </div>
                        <input  class="col-sm-4" type="text" ng-model="object.paymentAmount" ng-readonly="object.useCustomPaymentAmount == false" />&nbsp <input type="checkbox" ng-model="object.usePaymentAmountWithInterests" />
                        <spring:message code="label.PaymentReferenceCode.usePaymentAmountWithInterests" />
                    </div>
                </div>
            </div>

            
            <div class="form-group row" ng-show=" object.usePaymentAmountWithInterests == true ">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentReferenceCode.payableAmount" />
                </div>
                <div class="col-sm-10">
                    <div class="input-group">
                        <div class="input-group-addon">
                            <c:out value="${paymentReferenceCodeBean.debitNote.debtAccount.finantialInstitution.currency.symbol}" />
                        </div>
                        <input  class="col-sm-4" type="text" ng-model="object.paymentAmountWithInterst" ng-readonly="object.useCustomPaymentAmount == false" />&nbsp <input type="checkbox"
                            ng-model="object.usePaymentAmountWithInterests" />
                        <spring:message code="label.PaymentReferenceCode.usePaymentAmountWithInterests" />
                    </div>
                </div>
            </div>
            <div class="form-group row" ng-hide="angular.isUndefined(object.isPoolVariableTimeWindow) || object.isPoolVariableTimeWindow == false ">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentReferenceCode.beginDate" />
                </div>

                <div class="col-sm-4">
                    <input type="text" bennu-date="object.beginDate" />
                </div>
            </div>
            <div class="form-group row " ng-hide="angular.isUndefined(object.isPoolVariableTimeWindow) || object.isPoolVariableTimeWindow == false ">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentReferenceCode.endDate" />
                </div>

                <div class="col-sm-4">
                    <input type="text" bennu-date="object.endDate" />
                </div>
            </div>



        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />" />
        </div>
    </div>
</form>

<script>
	$(document).ready(function() {

		// Put here the initializing code for page
	});
</script>
