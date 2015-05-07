<%@page import="org.fenixedu.treasury.domain.FinantialInstitution"%>
<%@page import="pt.ist.standards.geographic.Municipality"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Locale"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="org.fenixedu.bennu.core.util.CoreConfiguration"%>
<%@page import="pt.ist.standards.geographic.District"%>
<%@page import="pt.ist.standards.geographic.Country"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>

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


<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
${portal.angularToolkit()}
<%-- ${portal.toolkit()}--%>

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


<%
    FinantialInstitution finantialInstitution = (FinantialInstitution) request
					.getAttribute("finantialInstitution");
%>

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message
			code="label.administration.manageFinantialInstitution.createFinantialInstitution" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
		class=""
		href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution2/"><spring:message
			code="label.event.back" /></a> |&nbsp;&nbsp;
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
			.module('changeExample', [ 'ngSanitize', 'ui.select' ])
			.controller(
					'ExampleController',
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
									$scope.postBack(model);
								};

								$scope.onDistrictChange = function(district,
										model) {
									$scope.object.municipality = undefined;
									$scope.postBack(model);
								};

								$scope.onMunicipalityChange = function(
										municipality, model) {
								};

							} ]);
</script>
<form name='form' method="post" class="form-horizontal"
	ng-app="changeExample" ng-controller="ExampleController"
	action='${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution2/create'>

	<input type="hidden" name="postback"
		value='${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution2/createpostback' />

	<input name="bean" type="hidden" value="{{ object }}" />

	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.FinantialInstitution.fiscalCountryRegion" />
				</div>
				<div class="col-sm-4">
					<ui-select id="finantialInstitution_fiscalCountryRegion"
						ng-model="$parent.object.fiscalcountryregion" theme="bootstrap"
						ng-disabled="disabled"> <ui-select-match>{{$select.selected.text}}</ui-select-match>
					<ui-select-choices
						repeat="region.id as region in object.fiscalcountryregions| filter: $select.search">
					<span ng-bind-html="region.text | highlight: $select.search"></span>
					</ui-select-choices> </ui-select>

				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.currency" />
				</div>
				<div class="col-sm-4">
					<ui-select id="finantialInstitution_currency"
						ng-model="$parent.object.currency" theme="bootstrap"
						ng-disabled="disabled"> <ui-select-match>{{$select.selected.text}}</ui-select-match>
					<ui-select-choices
						repeat="currency.id as currency in object.currenciesDataSource| filter: $select.search">
					<span ng-bind-html="currency.text | highlight: $select.search"></span>
					</ui-select-choices> </ui-select>

				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.code" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_code" class="form-control"
						ng-model="object.code" type="text" name="code" required
						value='<c:out value='${not empty param.code ? param.code : finantialInstitution.code }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.fiscalNumber" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_fiscalNumber" class="form-control"
						type="text" name="fiscalnumber" ng-model="object.fiscalNumber"
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
						ng-model="object.companyId" type="text" name="companyid"
						value='<c:out value='${not empty param.companyid ? param.companyid : finantialInstitution.companyId }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.name" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_name" class="form-control"
						ng-model="object.name" type="text" name="name" required
						value='<c:out value='${not empty param.name ? param.name : finantialInstitution.name }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.companyName" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_companyName" class="form-control"
						ng-model="object.companyName" type="text" name="companyname"
						value='<c:out value='${not empty param.companyname ? param.companyname : finantialInstitution.companyName }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.address" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_address" class="form-control"
						ng-model="object.address" type="text" name="address"
						value='<c:out value='${not empty param.address ? param.address : finantialInstitution.address }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.country" />
				</div>
				<div class="col-sm-4">
					<ui-select ng-model="$parent.object.country"
						on-select="onCountryChange($item, $model)" theme="bootstrap"
						ng-disabled="disabled"> <ui-select-match>{{$select.selected.text}}</ui-select-match>
					<ui-select-choices
						repeat="country.id as country in object.countries | filter: $select.search">
					<span ng-bind-html="country.text | highlight: $select.search"></span>
					</ui-select-choices> </ui-select>
				</div>

			</div>
			<div class="form-group row"
				ng-hide="angular.isUndefinedOrNull(object.districts) || object.districts.length==0">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.district" />
				</div>
				<div class="col-sm-4">
					<ui-select ng-model="$parent.object.district"
						on-select="onDistrictChange($item, $model)" theme="bootstrap"
						ng-disabled="disabled"> <ui-select-match>{{$select.selected.text}}</ui-select-match>
					<ui-select-choices
						repeat="district.id as district in object.districts | filter: $select.search">
					<span ng-bind-html="district.text | highlight: $select.search"></span>
					</ui-select-choices> </ui-select>
				</div>
			</div>
			<div class="form-group row"
				ng-hide="angular.isUndefinedOrNull(object.municipalities) || object.municipalities.length==0">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.municipality" />
				</div>

				<div class="col-sm-4">
					<ui-select ng-model="$parent.object.municipality"
						on-select="onMunicipalityChange($item, $model)" theme="bootstrap"
						ng-disabled="disabled"> <ui-select-match>{{$select.selected.text}}</ui-select-match>
					<ui-select-choices
						repeat="municipality.id as municipality in object.municipalities | filter: $select.search">
					<span ng-bind-html="municipality.text | highlight: $select.search"></span>
					</ui-select-choices> </ui-select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.locality" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_locality" class="form-control"
						ng-model="object.locality" type="text" name="locality"
						value='<c:out value='${not empty param.locality ? param.locality : finantialInstitution.locality }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.zipCode" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_zipCode" class="form-control"
						ng-model="object.zipCode" type="text" name="zipcode"
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
