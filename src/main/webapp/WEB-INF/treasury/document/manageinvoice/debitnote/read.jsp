<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="datatables"
	uri="http://github.com/dandelion/datatables"%>

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

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message code="label.document.manageInvoice.readDebitNote" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managedebtentry/invoiceentry/"  ><spring:message code="label.event.back" /></a>|&nbsp;&nbsp;
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/read"  ><spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;	<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}/addentry"  ><spring:message code="label.event.document.manageInvoice.addEntry" /></a>	|&nbsp;&nbsp;
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
								code="label.DebitNote.documentDate" /></th>
						<td><c:out value='${debitNote.documentDate}' /></td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message
								code="label.DebitNote.documentDueDate" /></th>
						<td><c:out value='${debitNote.documentDueDate}' /></td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message
								code="label.DebitNote.documentNumber" /></th>
						<td><c:out value='${debitNote.documentNumber}' /></td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message
								code="label.DebitNote.originDocumentNumber" /></th>
						<td><c:out value='${debitNote.originDocumentNumber}' /></td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3"><spring:message
								code="label.DebitNote.state" /></th>
						<td><c:out value='${debitNote.state}' /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>

<p> </p>
<p> </p>
<h2><spring:message code="label.DebitNote.debitEntries" /></h2>

<c:choose>
	<c:when test="${not empty debitNote.debitEntriesSet}">
		<datatables:table id="debitEntries" row="debitEntry" data="${debitNote.debitEntriesSet}" cssClass="table responsive table-bordered table-hover" cdn="false" cellspacing="2">
			<datatables:column cssStyle="width:10%">
				<datatables:columnHead ><spring:message code="label.DebitNote.documentDate" /></datatables:columnHead>
				<c:out value="${debitEntry}" /> - XPTO 
			</datatables:column>
			<datatables:column >
				<datatables:columnHead ><spring:message code="label.DebitNote.documentDate" /></datatables:columnHead>
				<c:out value="${debitEntry}" />
			</datatables:column>
			<datatables:column title="City 3">
				<datatables:columnHead ><spring:message code="label.DebitNote.documentDate" /></datatables:columnHead>
				<c:out value="${debitEntry}" />
	<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}/updateentry/"${debitEntry.externalId}  ><spring:message code="label.event.document.manageInvoice.updateEntry" /></a>	|&nbsp;&nbsp;
	<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}/deleteentry/"${debitEntry.externalId}  ><spring:message code="label.event.document.manageInvoice.deleteEntry" /></a>	
			</datatables:column>
		</datatables:table>
 		<script>
 		createDataTables('debitEntries',false,false,true,"${pageContext.request.contextPath}","${datatablesI18NUrl}");
 		</script> 
	</c:when>
	<c:otherwise>
		<div class="alert alert-warning" role="alert">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign"
					aria-hidden="true">&nbsp;</span>
				<spring:message code="label.noResultsFound" />
			</p>
		</div>

	</c:otherwise>
</c:choose>



<script>
	$(document).ready(function() {

	});
</script>
