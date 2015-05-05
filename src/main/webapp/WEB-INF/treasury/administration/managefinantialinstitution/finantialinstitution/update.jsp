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

<link
	href="//cdn.datatables.net/responsive/1.0.4/css/dataTables.responsive.css"
	rel="stylesheet" />
<script
	src="//cdn.datatables.net/responsive/1.0.4/js/dataTables.responsive.js"></script>
<link
	href="//cdn.datatables.net/tabletools/2.2.3/css/dataTables.tableTools.css"
	rel="stylesheet" />
<script
	src="//cdn.datatables.net/tabletools/2.2.3/js/dataTables.tableTools.min.js"></script>
<link
	href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/css/select2.min.css"
	rel="stylesheet" />
<script
	src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/js/select2.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

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
angular.module('changeExample', []).controller('ExampleController', ['$scope', function($scope) {
	$scope.country="uninitialized";
	$scope.district="uninitialized";
	$scope.municipality="uninitialized";
	
	$scope.change = function(newValue, oldValue) {
		var form = $('form[name="' + $scope.form.$name + '"]');
		
		if(oldValue !== "uninitialized" && newValue !== oldValue) {
			form.attr("action", form.find('input[name="postback"]').attr('value'));
			form.submit();
		}
	};
}]);
</script>

<form name='form' method="post" class="form-horizontal"
	ng-app="changeExample" ng-controller="ExampleController"
	action='${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/update/${finantialInstitution.externalId}'>

	<input type="hidden" name="postback"
		value='${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/updatepostback/${finantialInstitution.externalId}' />
	<div class="panel panel-default">
		<div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.FinantialInstitution.fiscalCountryRegion" />
                </div>
                <div class="col-sm-4">
                    <select id="finantialInstitution_fiscalCountryRegion"
                        class="js-example-basic-single" name="fiscalcountryregion">
                    </select>
                </div>
            </div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.code" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_code" class="form-control"
						type="text" name="code"
						value='<c:out value='${not empty param.code ? param.code : finantialInstitution.code }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.fiscalNumber" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_fiscalNumber" class="form-control"
						type="text" name="fiscalnumber"
						value='<c:out value='${not empty param.fiscalnumber ? param.fiscalnumber : finantialInstitution.fiscalNumber }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.companyId" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_companyId" class="form-control"
						type="text" name="companyid"
						value='<c:out value='${not empty param.companyid ? param.companyid : finantialInstitution.companyId }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.name" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_name" class="form-control"
						type="text" name="name"
						value='<c:out value='${not empty param.name ? param.name : finantialInstitution.name }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.companyName" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_companyName" class="form-control"
						type="text" name="companyname"
						value='<c:out value='${not empty param.companyname ? param.companyname : finantialInstitution.companyName }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.address" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_address" class="form-control"
						type="text" name="address"
						value='<c:out value='${not empty param.address ? param.address : finantialInstitution.address }'/>' />
				</div>
			</div>
			<div class="form-group row" id="finantialInstitution_country_div">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.country" />
				</div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="finantialInstitution_country"
                        class="js-example-basic-single" name="country" ng-model="country"
                        ng-change="change(country, '{{country}}')">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                </div>
			</div>
			<div class="form-group row" id="finantialInstitution_district_div">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.district" />
				</div>

                <div class="col-sm-4">
                    <select id="finantialInstitution_district"
                        class="js-example-basic-single" name="district"
                        ng-model="district" ng-change="change(district, '{{district}}')">
                        <option value=""></option>
                    </select>
                </div>
			</div>
			<div class="form-group row" id="finantialInstitution_municipality_div">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.municipality" />
				</div>

				<div class="col-sm-4">
					<select id="finantialInstitution_municipality"
						class="js-example-basic-single" name="municipality"
						ng-model="municipality">
						<option value=""></option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.locality" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_locality" class="form-control"
						type="text" name="locality"
						value='<c:out value='${not empty param.locality ? param.locality : finantialInstitution.locality }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialInstitution.zipCode" />
				</div>

				<div class="col-sm-10">
					<input id="finantialInstitution_zipCode" class="form-control"
						type="text" name="zipcode"
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
    fiscalCountryRegion_options = [
        <c:forEach items="${finantialInstitution_fiscalCountryRegion_options}" var="fiscalCountryRegion">
        {
            "id"   : "<c:out value='${fiscalCountryRegion.externalId}'/>",
            "text" : "<c:out value='${fiscalCountryRegion.name.content}'/>",
        },
        </c:forEach>
    ];
    country_options = [
        <c:forEach items="${finantialInstitution_country_options}" var="country">
        {
            "id"   : "<pf:placeCode place='${country}'/>",
            "text" : "<pf:placeName place='${country}'/>",
        },
        </c:forEach>
    ];
    district_options = [
        <c:forEach items="${finantialInstitution_district_options}" var="district">
        {
            "id"   : "<pf:placeCode place='${district}'/>",
            "text" : "<pf:placeName place='${district}'/>",
        },
        </c:forEach>
    ];
    municipality_options = [
        <c:forEach items="${finantialInstitution_municipality_options}" var="municipality">
        {
            "id"   : "<pf:placeCode place='${municipality}'/>",
            "text" : "<pf:placeName place='${municipality}'/>",
        },
        </c:forEach>
    ];
    var sortFunction = function(a,b) { return a.text.localeCompare(b.text) };
	$("#finantialInstitution_country").select2(
		{
			data : country_options.sort( sortFunction ),
		}	  
	);
	$("#finantialInstitution_district").select2(
		{
			data : district_options.sort( sortFunction ),
		}	  
    );
	$("#finantialInstitution_municipality").select2(
		{
			data : municipality_options.sort( sortFunction ),
		}	  
	);
    $("#finantialInstitution_fiscalCountryRegion").select2(
        {
            data : fiscalCountryRegion_options.sort( sortFunction ),
        }     
    );
    <c:set var="savedCountry"><pf:placeCode place='${finantialInstitution.country}'/></c:set>
    <c:set var="savedDistrict"><pf:placeCode place='${finantialInstitution.district}'/></c:set>
    <c:set var="savedMunicipality"><pf:placeCode place='${finantialInstitution.municipality}'/></c:set>
	$("#finantialInstitution_country").select2().select2('val', '${not empty param.country ? param.country : savedCountry}');
 	$("#finantialInstitution_district").select2().select2('val', '${not empty param.country ? param.country : savedDistrict}');
 	$("#finantialInstitution_municipality").select2().select2('val', '${not empty param.country ? param.country : savedMunicipality}');
    $("#finantialInstitution_fiscalCountryRegion").select2().select2('val', '${finantialInstitution.fiscalCountryRegion.externalId}');
    if (district_options.length == 0) {
        $("#finantialInstitution_district_div").hide();
    } 
    if (municipality_options.length == 0) {
        $("#finantialInstitution_municipality_div").hide();
    } 
});
</script>
