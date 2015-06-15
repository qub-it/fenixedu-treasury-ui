<%@page import="java.util.Collection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="pf"  uri="http://example.com/placeFunctions"%>

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

${portal.angularToolkit()} 

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<script src="${pageContext.request.contextPath}/webjars/angular-sanitize/1.3.11/angular-sanitize.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.css" />
<script src="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.js"></script>

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message
			code="label.administration.manageFinantialInstitution.updateFinantialInstitution" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
		class=""
		href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/read/${finantialInstitution.externalId}"><spring:message
			code="label.event.back" /></a> &nbsp;
</div>
<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">

		<c:forEach items="${warningMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>

<script>
    angular
            .module('angularAppFinantialInstitution', [ 'ngSanitize', 'ui.select' ])
            .controller(
                    'FinantialInstitutionController',
                    [
                            '$scope',
                            function($scope) {

                                $scope.object = angular
                                        .fromJson('${finantialInstitutionBeanJson}');

                                $scope.postBack = createAngularPostbackFunction($scope);

                                $scope.onCountryChange = function(country,
                                        model) {
                                    $scope.object.district = undefined;
                                    $scope.object.municipality = undefined;
                                    $scope.object.countries = undefined;
                                    $scope.postBack(model);
                                };

                                $scope.onDistrictChange = function(district,
                                        model) {
                                    $scope.object.municipality = undefined;
                                    $scope.object.countries = undefined;
                                    $scope.object.districts = undefined;
                                    $scope.postBack(model);
                                };

                                $scope.onMunicipalityChange = function(
                                        municipality, model) {
                                };

                                $scope.submitForm = function (model) {
                                    $scope.object.countries = undefined;
                                    $scope.object.districts = undefined;
                                    $scope.object.municipalities = undefined;
                                }
                            } ]);
</script>

<form name='form' method="post" class="form-horizontal"
	ng-app="angularAppFinantialInstitution" ng-controller="FinantialInstitutionController" ng-submit="submitForm($model)"
	action='${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/update/${finantialInstitution.externalId}'>

	<input type="hidden" name="postback"
		value='${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/updatepostback/${finantialInstitution.externalId}' />
    <input name="bean" type="hidden" value="{{ object }}" />

	<div class="panel panel-default">
		<div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.FinantialInstitution.fiscalCountryRegion" />
                </div>
                <div class="col-sm-4">
                    <ui-select id="finantialInstitution_fiscalCountryRegion"
                        ng-model="$parent.object.fiscalcountryregion" theme="bootstrap"
                        ng-disabled="disabled"> 
                        <ui-select-match>{{$select.selected.text}}</ui-select-match>
                        <ui-select-choices repeat="region.id as region in object.fiscalcountryregions| filter: $select.search">
                            <span ng-bind-html="region.text | highlight: $select.search"></span>
                        </ui-select-choices> 
                    </ui-select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.FinantialInstitution.currency" />
                </div>
                <div class="col-sm-4">
                    <ui-select id="finantialInstitution_currency"
                        ng-model="$parent.object.currency" theme="bootstrap"
                        ng-disabled="disabled"> 
                        <ui-select-match>{{$select.selected.text}}</ui-select-match>
                        <ui-select-choices repeat="currency.id as currency in object.currenciesDataSource| filter: $select.search">
                            <span ng-bind-html="currency.text | highlight: $select.search"></span>
                        </ui-select-choices> 
                    </ui-select>
                </div>
            </div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.code" />
				</div>

                <div class="col-sm-10">
                    <input id="finantialInstitution_code" class="form-control"
                        ng-model="object.code" type="text" required
                        value='<c:out value='${not empty param.code ? param.code : finantialInstitution.code }'/>' />
                </div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.fiscalNumber" />
				</div>

                <div class="col-sm-10">
                    <input id="finantialInstitution_fiscalNumber" class="form-control"
                        type="text" ng-model="object.fiscalNumber"
                        required
                        value='<c:out value='${not empty param.fiscalnumber ? param.fiscalnumber : finantialInstitution.fiscalNumber }'/>' />
                </div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.companyId" />
				</div>

                <div class="col-sm-10">
                    <input id="finantialInstitution_companyId" class="form-control"
                        ng-model="object.companyId" type="text"
                        value='<c:out value='${not empty param.companyid ? param.companyid : finantialInstitution.companyId }'/>' />
                </div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.name" />
				</div>

                <div class="col-sm-10">
                    <input id="finantialInstitution_name" class="form-control"
                        ng-model="object.name" type="text" required
                        value='<c:out value='${not empty param.name ? param.name : finantialInstitution.name }'/>' />
                </div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.companyName" />
				</div>

                <div class="col-sm-10">
                    <input id="finantialInstitution_companyName" class="form-control"
                        ng-model="object.companyName" type="text"
                        value='<c:out value='${not empty param.companyname ? param.companyname : finantialInstitution.companyName }'/>' />
                </div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.address" />
				</div>

                <div class="col-sm-10">
                    <input id="finantialInstitution_address" class="form-control"
                        ng-model="object.address" type="text" 
                        value='<c:out value='${not empty param.address ? param.address : finantialInstitution.address }'/>' />
                </div>
			</div>
			<div class="form-group row" id="finantialInstitution_country_div">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.country" />
				</div>

                <div class="col-sm-4">
                    <ui-select ng-model="$parent.object.country" on-select="onCountryChange($item, $model)" theme="bootstrap" ng-disabled="disabled"> 
                        <ui-select-match>{{$select.selected.text}}</ui-select-match>
                        <ui-select-choices repeat="country.id as country in object.countries | filter: $select.search"> 
                            <span ng-bind-html="country.text | highlight: $select.search"></span> 
                        </ui-select-choices> 
                    </ui-select>
                </div>
			</div>
			<div class="form-group row" id="finantialInstitution_district_div" ng-hide="object.districts === undefined || object.districts.length === 0">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.district" />
				</div>

                <div class="col-sm-4">
                    <ui-select ng-model="$parent.object.district" on-select="onDistrictChange($item, $model)" theme="bootstrap" ng-disabled="disabled"> 
                        <ui-select-match>{{$select.selected.text}}</ui-select-match>
                        <ui-select-choices repeat="district.id as district in object.districts | filter: $select.search">
                            <span ng-bind-html="district.text | highlight: $select.search"></span>
                        </ui-select-choices>
                    </ui-select>
                </div>
			</div>
			<div class="form-group row" id="finantialInstitution_municipality_div" ng-hide="object.municipalities === undefined || object.municipalities.length === 0">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.municipality" />
				</div>

                <div class="col-sm-4">
                    <ui-select ng-model="$parent.object.municipality" on-select="onMunicipalityChange($item, $model)" theme="bootstrap" ng-disabled="disabled"> 
                        <ui-select-match>{{$select.selected.text}}</ui-select-match>
                        <ui-select-choices repeat="municipality.id as municipality in object.municipalities | filter: $select.search">
                            <span ng-bind-html="municipality.text | highlight: $select.search"></span>
                        </ui-select-choices> 
                    </ui-select>
                </div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.locality" />
				</div>

                <div class="col-sm-10">
                    <input id="finantialInstitution_locality" class="form-control"
                        ng-model="object.locality" type="text"
                        value='<c:out value='${not empty param.locality ? param.locality : finantialInstitution.locality }'/>' />
                </div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.zipCode" />
				</div>

                <div class="col-sm-10">
                    <input id="finantialInstitution_zipCode" class="form-control"
                        ng-model="object.zipCode" type="text"
                        display="none"
                        value='<c:out value='${not empty param.zipcode ? param.zipcode : finantialInstitution.zipCode }'/>' />
                </div>
			</div>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button"
				value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<script>
$(document).ready(function() {
});
</script>
