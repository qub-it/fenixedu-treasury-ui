<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
${portal.angularToolkit()} 
<%--${portal.toolkit()}--%>

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
	<h1><spring:message code="label.administration.base.manageFixedTariff.updateFixedTariff" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/administration/base/manageproduct/product/read/${fixedTariff.product.externalId}" ><spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;</div>
	<c:if test="${not empty infoMessages}">
				<div class="alert alert-info" role="alert">
					
					<c:forEach items="${infoMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty warningMessages}">
				<div class="alert alert-warning" role="alert">
					
					<c:forEach items="${warningMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty errorMessages}">
				<div class="alert alert-danger" role="alert">
					
					<c:forEach items="${errorMessages}" var="message"> 
						<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
  							${message}
  						</p>
					</c:forEach>
					
				</div>	
			</c:if>

<script>

angular.module('angularAppFixedTariff', ['ngSanitize', 'ui.select']).controller('FixedTariffController', ['$scope', function($scope) {
	$scope.booleanvalues= [
	                    {name: '<spring:message code="label.no"/>',    value: false},
	                    {name: '<spring:message code="label.yes"/>',        value: true}
	                  ];

 	$scope.object=angular.fromJson('${fixedTariffBeanJson}');
	$scope.postBack = createAngularPostbackFunction($scope); 

	//Begin here of Custom Screen business JS - code
 	
}]);
</script>

<form name='form' method="post" class="form-horizontal"
	ng-app="angularAppFixedTariff" ng-controller="FixedTariffController"
	action='${pageContext.request.contextPath}/treasury/administration/base/managefixedtariff/fixedtariff/update/${fixedTariff.externalId}'>

	<input type="hidden" name="postback"
		value='${pageContext.request.contextPath}treasury/administration/base/managefixedtariff/fixedtariff/updatepostback/${fixedTariff.externalId}' />
		
	<input name="bean" type="hidden" value="{{ object }}" />

<div class="panel panel-default">
  <div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.amount"/></div> 

<div class="col-sm-10">
	<input id="fixedTariff_amount" class="form-control" type="text" ng-model="object.amount" name="amount"  value='<c:out value='${not empty param.amount ? param.amount : fixedTariff.amount }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.applyInterests"/></div> 

<div class="col-sm-2">
<script>
</script>
<select id="fixedTariff_applyInterests" name="applyinterests" class="form-control" ng-model="object.applyInterests" ng-options="bvalue.value as bvalue.name for bvalue in booleanvalues">
</select>
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.beginDate"/></div> 

 <div class="col-sm-4">
	<input id="fixedTariff_beginDate"  class="form-control" type="date" name="begindate"   
	ng-model="object.beginDate"/>
</div> 
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.endDate"/></div> 

 <div class="col-sm-4">
	<input id="fixedTariff_endDate" class="form-control" type="date" name="enddate"  ng-model="object.endDate" />
</div> 
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.finantialEntity"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		<ui-select id="fixedTariff_finantialEntity"  name="finantialentity" ng-model="$parent.object.finantialEntity" theme="bootstrap" ng-disabled="disabled" >
    						<ui-select-match >{{$select.selected.text}}</ui-select-match>
    						<ui-select-choices repeat="finantialEntity.id as finantialEntity in object.finantialEntityDataSource | filter: $select.search">
      							<span ng-bind-html="finantialEntity.text | highlight: $select.search"></span>
    						</ui-select-choices>
  						</ui-select>				
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.dueDateCalculationType"/></div> 

<div class="col-sm-4">
		<ui-select id="fixedTariff_dueDateCalculationType"  name="duedatecalculationtype" ng-model="$parent.object.dueDateCalculationType" theme="bootstrap" ng-disabled="disabled" >
    						<ui-select-match >{{$select.selected.text}}</ui-select-match>
    						<ui-select-choices repeat="dueDateCalculationType.id as dueDateCalculationType in object.dueDateCalculationTypeDataSource | filter: $select.search">
      							<span ng-bind-html="dueDateCalculationType.text | highlight: $select.search"></span>
    						</ui-select-choices>
  						</ui-select>				
</div>
</div>		
<div class="form-group row" ng-show="object.dueDateCalculationType == 'FIXED_DATE'">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.fixedDueDate"/></div> 

 <div class="col-sm-4">
	<input id="fixedTariff_fixedDueDate"  type="text" name="fixedduedate"  ng-model="object.fixedDueDate" />
</div> 
</div>		
<div class="form-group row" ng-show="object.dueDateCalculationType == 'DAYS_AFTER_CREATION'">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.numberOfDaysAfterCreationForDueDate"/></div> 

<div class="col-sm-10">
	<input id="fixedTariff_numberOfDaysAfterCreationForDueDate" class="form-control" type="text" ng-model="object.numberOfDaysAfterCreationForDueDate" name="numberofdaysaftercreationforduedate"  value='<c:out value='${not empty param.numberofdaysaftercreationforduedate ? param.numberofdaysaftercreationforduedate : fixedTariff.numberOfDaysAfterCreationForDueDate }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FixedTariff.vatType"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		<ui-select id="fixedTariff_vatType"  name="vattype" ng-model="$parent.object.vatType" theme="bootstrap" ng-disabled="disabled" >
    						<ui-select-match >{{$select.selected.text}}</ui-select-match>
    						<ui-select-choices repeat="vatType.id as vatType in object.vatTypeDataSource | filter: $select.search">
      							<span ng-bind-html="vatType.text | highlight: $select.search"></span>
    						</ui-select-choices>
  						</ui-select>				
				</div>
</div>		
  </div>
  <div class="panel-footer">
		<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />"/>
	</div>
</div>
</form>

<script>
$(document).ready(function() {

	});
</script>
