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
		<spring:message code="label.document.manageInvoice.updateDebitEntry" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;
	<c:if test="${ not empty debitEntry.finantialDocument}">
		<a class="" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitEntry.finantialDocument.externalId}"><spring:message
				code="label.event.back" /></a>
	</c:if>
	<c:if test="${ empty debitEntry.finantialDocument}">
		<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/read/${debitEntry.debtAccount.externalId}"><spring:message
				code="label.event.back" /></a>
	</c:if>

	|&nbsp;&nbsp;
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
	angular.module('angularAppDebitEntry', [ 'ngSanitize', 'ui.select' ])
			.controller('DebitEntryController', [ '$scope', function($scope) {

				$scope.object = angular.fromJson('${debitEntryBeanJson}');
				$scope.postBack = createAngularPostbackFunction($scope);

				//Begin here of Custom Screen business JS - code

			} ]);
</script>

<form name='form' method="post" class="form-horizontal" ng-app="angularAppDebitEntry" ng-controller="DebitEntryController"
	action='${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/update/${debitEntry.externalId}'>

	<input type="hidden" name="postback" value='${pageContext.request.contextPath}treasury/document/manageinvoice/debitentry/updatepostback/${debitEntry.externalId}' /> <input
		name="bean" type="hidden" value="{{ object }}" />

	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebtAccount.finantialInstitution" />
				</div>

				<div class="col-sm-10">
					<div class="form-control">
						<c:out value="${debitEntryBean.debtAccount.finantialInstitution.name}" />
					</div>
				</div>
			</div>

			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.debtAccount" />
				</div>

				<div class="col-sm-10">
					<div class="form-control">
						<c:out value="${debitEntryBean.debtAccount.customer.code} - ${debitEntryBean.debtAccount.customer.name}" />
					</div>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.finantialDocument" />
				</div>

				<div class="col-sm-10">
					<div class="form-control">
						<c:if test="${not empty debitEntry.finantialDocument}">
							<c:out value='${debitEntry.finantialDocument.uiDocumentNumber}' />
						</c:if>
						<c:if test="${empty debitEntry.finantialDocument}">
							<spring:message code="label.DebitEntry.debitentry.with.no.document" />
						</c:if>
					</div>
				</div>
			</div>

			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.product" />
				</div>

				<div class="col-sm-10">
					<div class="form-control">
						<c:out value="${debitEntryBean.product.name.content}" />
					</div>
				</div>
			</div>

			<!-- <div class="form-group row"> -->
			<%-- <div class="col-sm-2 control-label"><spring:message code="label.DebitEntry.product"/></div>  --%>

			<!-- <div class="col-sm-4"> -->
			<%-- Relation to side 1 drop down rendered in input --%>
			<!-- 		<ui-select id="debitEntry_product" class="form-control" name="product" ng-model="$parent.object.product" theme="bootstrap" ng-disabled="disabled" > -->
			<!--     						<ui-select-match >{{$select.selected.text}}</ui-select-match> -->
			<!--     						<ui-select-choices repeat="product.id as product in object.productDataSource | filter: $select.search"> -->
			<!--       							<span ng-bind-html="product.text | highlight: $select.search"></span> -->
			<!--     						</ui-select-choices> -->
			<!--   						</ui-select>				 -->
			<!-- 				</div> -->
			<!-- </div>		 -->



			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.description" />
				</div>

				<div class="col-sm-10">
					<input id="debitEntry_description" class="form-control" type="text" ng-model="object.description" name="description"
						value='<c:out value='${not empty param.description ? param.description : debitEntry.description }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.amount" />
				</div>

				<div class="col-sm-10">
					<div class="input-group">
						<div class="input-group-addon">
							<c:out value="${debitEntryBean.debtAccount.finantialInstitution.currency.symbol}" />
						</div>
						<input id="debitEntry_amount" class="form-control" type="text" ng-model="object.amount" name="amount" pattern="^[0-9]+(\.[0-9][0-9]?[0-9]?)?$" data-number-to-fixed="2"
							data-number-stepfactor="100" />
					</div>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.quantity" />
				</div>

				<div class="col-sm-10">
					<input id="debitEntry_quantity" class="form-control" type="text" ng-model="object.quantity" name="quantity" />
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

	});
</script>
