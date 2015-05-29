<%@page
	import="org.fenixedu.treasury.ui.document.manageinvoice.DebitNoteController"%>
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

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.administration.document.manageDocumentNumberSeries.readDocumentNumberSeries" />
		<small></small>
	</h1>
</div>
<div class="modal fade" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">
    <form id ="deleteForm" action="${pageContext.request.contextPath}/treasury/administration/document/managedocumentnumberseries/documentnumberseries/delete/${documentNumberSeries.externalId}"   method="POST">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><spring:message code="label.confirmation"/></h4>
      </div>
      <div class="modal-body">
        <p><spring:message code = "label.administration.document.manageDocumentNumberSeries.readDocumentNumberSeries.confirmDelete"/></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "label.close"/></button>
        <button id="deleteButton" class ="btn btn-danger" type="submit"> <spring:message code = "label.delete"/></button>
      </div>
      </form>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/administration/document/managedocumentnumberseries/documentnumberseries/"  ><spring:message code="label.event.back" /></a>
&nbsp;|&nbsp;				<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp;<a class="" href="#" data-toggle="modal"
data-target="#deleteModal"><spring:message code="label.event.delete" /></a>
				&nbsp;|&nbsp;
	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/administration/document/managedocumentnumberseries/documentnumberseries/update/${documentNumberSeries.externalId}"  ><spring:message code="label.event.update" /></a>
&nbsp;|
<c:if test="${ documentNumberSeries.finantialDocumentType.type == 'DEBIT_NOTE'}">		 
&nbsp;<span
		class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a
		class=""
		href="${pageContext.request.contextPath}<%=DebitNoteController.CREATE_URL %>?documentnumberseries=${documentNumberSeries.externalId}"><spring:message
			code="label.event.createdebitnote" /></a>
</c:if>
</div>
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

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title"><spring:message code="label.details"/></h3>
	</div>
	<div class="panel-body">
<form method="post" class="form-horizontal">
<table class="table">
		<tbody>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.Series.finantialInstitution"/></th> 
	<td>
		<c:out value='${documentNumberSeries.series.finantialInstitution.name}'/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.DocumentNumberSeries.series"/></th> 
	<td>
		<c:out value='${documentNumberSeries.series.name.content}'/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.DocumentNumberSeries.finantialDocumentType"/></th> 
	<td>
		<c:out value='${documentNumberSeries.finantialDocumentType.name.content}'/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.DocumentNumberSeries.counter"/></th> 
	<td>
		<c:out value='${documentNumberSeries.counter}'/>
	</td> 
</tr>
</tbody>
</table>
</form>
</div>
</div>

<h3><spring:message code="label.Series.FinantialDocumentNotes"></spring:message></h3>

<c:choose>
	<c:when test="${not empty documentNumberSeries.finantialDocumentsSet}">
		<table id="documentsTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.DocumentNumberSeries.FinantialDocumentType"/></th>
<th><spring:message code="label.Customer.TotalDocuments"/></th>
<%-- Operations Column --%>
					<th></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${documentNumberSeries.finantialDocumentsSet}" var="document">
			<tr>
			<td>
			<c:out value="${document.documentNumber}"/>					
			</td>
<!-- 			<td> -->
<%--  			<c:out value="${documentNumberSeries.sequenceNumber}"/>	  --%>
<!-- 			</td> -->
<!-- 			<td> -->
<!-- <!--  ACTIONS -->				 -->
<%--  			<a  class="btn btn-default btn-xs" href="${pageContext.request.contextPath}<%=DocumentNumberSeriesController.READ_URL%>${documentNumberSeries.externalId}"><spring:message code='label.view'/></a> --%>
<!-- 			</td> -->
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

<script>



	
	
	$(document).ready(function() {

	


		var table = $('#documentsTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 2 } 
		             ],
		//Documentation: https://datatables.net/reference/option/dom
//"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
//"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"
        }
		});
		table.columns.adjust().draw();
		
		  $('#documentsTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );

	}); 
</script>
