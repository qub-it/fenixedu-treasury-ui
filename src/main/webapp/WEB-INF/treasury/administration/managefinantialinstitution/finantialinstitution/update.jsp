<%@page import="org.fenixedu.treasury.domain.FinantialInstitution"%>
<%@page import="pt.ist.standards.geographic.Municipality"%>
<%@page import="pt.ist.standards.geographic.District"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="pt.ist.standards.geographic.Country"%>
<%@page import="java.util.Collection"%>
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

<% FinantialInstitution finantialInstitution = (FinantialInstitution) request.getAttribute("finantialInstitution"); %>
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
	action='${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/update'>

	<input type="hidden" name="postback"
		value='${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/updatepostback' />
	<div class="panel panel-default">
		<div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.FinantialInstitution.fiscalCountryRegion" />
                </div>
                <div class="col-sm-4">
                    <select id="finantialInstitution_fiscalCountryRegion"
                        class="js-example-basic-single" name="fiscalcountryregion">
                        <option value=""></option>
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
			<div class="form-group row">
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
			<div class="form-group row">
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
			<div class="form-group row">
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
	country_options = [
	    <%Collection<Country> countries = (Collection<Country>) request.getAttribute("finantialInstitution_country_options");
	      for(final Country c : countries) {%>
	      	{
	      		text : "<%=c.getLocalizedName(I18N.getLocale())%>",
	      		id : "<%=c.exportAsString()%>"
	      	},
	    <%}%>
	];
	district_options = [
	    <%Collection<District> districts = (Collection<District>) request.getAttribute("finantialInstitution_district_options");
	        for(final District d : districts) {%>
	      	{
	      		text : "<%=d.getLocalizedName(I18N.getLocale())%>",
	      		id : "<%=d.exportAsString()%>"
	      	},
	    <%}%>
	];
	municipality_options = [
	   <%Collection<Municipality> municipalities = (Collection<Municipality>) request.getAttribute("finantialInstitution_municipality_options");
	       for(final Municipality m : municipalities) {%>
	     	{
	     		text : "<%=m.getLocalizedName(I18N.getLocale())%>",
	     		id : "<%=m.exportAsString()%>",
	     	},
	   <%}%>
   	];
    fiscalCountryRegion_options = [
        <c:forEach items="${finantialInstitution_fiscalCountryRegion_options}" var="fiscalCountryRegion">
        <%-- Field access / formatting  here CHANGE_ME --%>
            {
                "id"   : "<c:out value='${fiscalCountryRegion.externalId}'/>",
                "text" : "<c:out value='${fiscalCountryRegion.name.content}'/>",
            },
        </c:forEach>
    ];  
	$("#finantialInstitution_country").select2(
		{
			data : country_options.sort(function(a,b) { return a.text.localeCompare(b.text) } ),
		}	  
	);
	$("#finantialInstitution_district").select2(
		{
			data : district_options.sort(function(a,b) { return a.text.localeCompare(b.text) } ),
		}	  
    );
	$("#finantialInstitution_municipality").select2(
		{
			data : municipality_options.sort(function(a,b) { return a.text.localeCompare(b.text) } ),
		}	  
	);
    $("#finantialInstitution_fiscalCountryRegion").select2(
        {
            data : fiscalCountryRegion_options.sort(function(a,b) { return a.text.localeCompare(b.text) } ),
        }     
    );	
	$("#finantialInstitution_country").select2().select2('val', '<%= finantialInstitution.getCountry().exportAsString() %>');
	$("#finantialInstitution_district").select2().select2('val', '<%= finantialInstitution.getDistrict().exportAsString() %>');
	$("#finantialInstitution_municipality").select2().select2('val', '<%= finantialInstitution.getMunicipality().exportAsString() %>');
    $("#finantialInstitution_fiscalCountryRegion").select2().select2('val', '${finantialInstitution.fiscalCountryRegion.externalId}');
});
</script>
