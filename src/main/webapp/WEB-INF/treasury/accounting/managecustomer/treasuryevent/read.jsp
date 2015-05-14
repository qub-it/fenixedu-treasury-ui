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

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

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



<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message
			code="label.viewCustomerTreasuryEvents.readTreasuryEvent" />
		<small></small>
	</h1>
</div>
<!-- <div class="modal fade" id="deleteModal"> -->
<!--   <div class="modal-dialog"> -->
<!--     <div class="modal-content"> -->
<%--     <form id ="deleteForm" action="${pageContext.request.contextPath}/treasury/accounting/managecustomer/treasuryevent/delete/${treasuryEvent.externalId}"   method="POST"> --%>
<!--       <div class="modal-header"> -->
<!--         <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> -->
<%--         <h4 class="modal-title"><spring:message code="label.confirmation"/></h4> --%>
<!--       </div> -->
<!--       <div class="modal-body"> -->
<%--         <p><spring:message code = "label.viewCustomerTreasuryEvents.readTreasuryEvent.confirmDelete"/></p> --%>
<!--       </div> -->
<!--       <div class="modal-footer"> -->
<%--         <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "label.close"/></button> --%>
<%--         <button id="deleteButton" class ="btn btn-danger" type="submit"> <spring:message code = "label.delete"/></button> --%>
<!--       </div> -->
<!--       </form> -->
<!--     </div>/.modal-content -->
<!--   </div>/.modal-dialog -->
<!-- </div>/.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
		class=""
		href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/treasuryevent/?debtAccount=${treasuryEvent.debtAccount.externalId}"><spring:message
			code="label.event.back" /></a> |&nbsp;&nbsp;
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

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">
			<spring:message code="label.details" />
		</h3>
	</div>
	<div class="panel-body">
		<form method="post" class="form-horizontal">
			<table class="table">
				<tbody>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message
								code="label.TreasuryEvent.description" /></th>
						<td><c:out value='${treasuryEvent.description.content}' /></td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message
								code="label.TreasuryEvent.propertiesJsonMap" /></th>
						<td><c:out value='${treasuryEvent.propertiesJsonMap}' /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>
<th><spring:message code="label.TreasuryEvent.allDebitEntries"/></th>
<div class="tab-pane" id="allDebitEntries">
	<p></p>
	<c:choose>
	<c:when test="${not empty allDebitEntriesDataSet}">
	<table id="allDebitEntriesTable" class="table responsive table-bordered table-hover">
		<thead>
			<tr>
			<%--!!!  Field names here --%>
			<th><spring:message code="label.TreasuryEvent.allDebitEntries.documentNumber"/></th>
			<th><spring:message code="label.TreasuryEvent.allDebitEntries.dueDate"/></th>
			<th><spring:message code="label.TreasuryEvent.allDebitEntries.description"/></th>
			<th><spring:message code="label.TreasuryEvent.allDebitEntries.amount"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${allDebitEntriesDataSet}" var="debitEntry">
				<tr>
					<td>
						<p><span>${debitEntry.finantialDocument.finantialDocumentNumber}</span></p>
					</td>
					<td>
						<p align=center><span>${debitEntry.dueDate}</span></p>
					</td>
					<td>
						<p><span>${debitEntry.description}</span></p>
					</td>
					<td>
						<p align=right><span>${debitEntry.amount} &#128;</span></p>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</c:when>
	<c:otherwise>
	<div class="alert alert-warning" role="alert">
		
		<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>			<spring:message code="label.noResultsFound" /></p>
		
	</div>	
	
	</c:otherwise>
	</c:choose>
</div>

<script>
$(document).ready(function() {
	var tableAllDebitEntries = $('#allDebitEntriesTable').DataTable({language : {
		url : "${datatablesI18NUrl}",			
	},
	"columns": [
		{ data: 'documentNumber' },
		{ data: 'dueDate' },
		{ data: 'description' },		
		{ data: 'amount' }
		
	],
	"columnDefs": [
		{ "width": "10%", "targets": 0 },
		{ "width": "10%", "targets": 1 },
		{ "width": "60%", "targets": 2 },
		{ "width": "20%", "targets": 3 }
		
	],
	"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
	});
	tableAllDebitEntries.columns.adjust().draw();
	
	});
</script>
