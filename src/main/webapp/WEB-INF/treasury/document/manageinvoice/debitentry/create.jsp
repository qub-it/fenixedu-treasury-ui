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
		<spring:message code="label.document.manageInvoice.createDebitEntry" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitEntryBean.finantialDocument.externalId}"><spring:message
			code="label.event.back" /></a> |&nbsp;&nbsp;
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
				$scope.onProductChange = function(product, model) {
					$scope.postBack(model);
				}
			} ]);
</script>

<form name='form' method="post" class="form-horizontal" ng-app="angularAppDebitEntry" ng-controller="DebitEntryController"
	action='${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/create/${debitEntryBean.debtAccount.externalId}'>

	<input type="hidden" name="postback" value='${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/createpostback' /> <input name="bean" type="hidden"
		value="{{ object }}" />
	<div class="panel panel-default">
		<div class="panel-body">

			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.product" />
				</div>

				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<ui-select id="debitEntry_product" name="product" ng-model="$parent.object.product" theme="bootstrap" ng-disabled="disabled" on-select="onProductChange($product, $model)">
					<ui-select-match>{{$select.selected.text}}</ui-select-match> <ui-select-choices repeat="product.id as product in object.productDataSource | filter: $select.search">
					<span ng-bind-html="product.text | highlight: $select.search"></span> </ui-select-choices> </ui-select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.description" />
				</div>

				<div class="col-sm-10">
					<input id="debitEntry_description" class="form-control" type="text" ng-model="object.description" name="description" />
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
						<input id="debitEntry_amount" class="form-control currency" type="number" ng-model="object.amount" data-number-to-fixed="2" data-number-stepfactor="100" name="amount" />
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

			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DebitEntry.dueDate" />
				</div>

				<div class="col-sm-4">
					<input id="debitEntry_dueDate" class="form-control" type="date" ng-model="object.dueDate" name="dueDate" />
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
