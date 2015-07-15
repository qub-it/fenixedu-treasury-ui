<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>



<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.integration.erp.searchFinantialDocument" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/integration/erp/erpexportoperation/"  ><spring:message code="label.event.back" /></a>&nbsp;|&nbsp;
    <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/integration/erp/finantialdocument/search/forceintegrationexport/?finantialinstitution=${param.finantialinstitution}"  ><spring:message code="label.event.integration.erp.forceIntegrationExport" /></a>
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

<div class="panel panel-default">
<form method="get" class="form-horizontal">
<div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.Series.finantialInstitution"/></div> 

<div class="col-sm-4">
    <%-- Relation to side 1 drop down rendered in input --%>
         <select id="finantialDocument_finantialInstitution" class="js-example-basic-single" name="finantialinstitution">
        </select>
                </div>
</div>      
</div>
<div class="panel-footer">
    <input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.search" />"/>
</div>
</form>
</div>




<c:choose>
	<c:when test="${not empty searchfinantialdocumentResultsDataSet}">
		<table id="searchfinantialdocumentTable" class="table responsive table-bordered table-hover" width="100%">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.FinantialDocument.documentNumber"/></th>
<th><spring:message code="label.FinantialDocument.documentDate"/></th>
<th><spring:message code="label.FinantialDocument.state"/></th>
<%-- Operations Column --%>
					<th></th>
				</tr>
			</thead>
			<tbody>
				
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
	var searchfinantialdocumentDataSet = [
			<c:forEach items="${searchfinantialdocumentResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"documentnumber" : "<c:out value='${searchResult.uiDocumentNumber}'/>",
"documentdate" : "<c:out value='${searchResult.documentDate.toString("YYYY-MM-dd")}'/>",
"state" : "<c:out value='${searchResult.state.descriptionI18N.content}'/>",
"actions" :
" <a target=\"#\" class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/integration/erp/finantialdocument/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" 
			},
            </c:forEach>
    ];
	
	$(document).ready(function() {

		<%-- Block for providing finantialInstitution options --%>
	    <%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	    finantialInstitution_options = [
	        <c:forEach items="${FinantialDocument_finantialInstitution_options}" var="element"> 
	            {
	                text :"<c:out value='${element.name}'/>", 
	                id : "<c:out value='${element.externalId}'/>"
	            },
	        </c:forEach>
	    ];
	    
	    $("#finantialDocument_finantialInstitution").select2(
	        {
	            data : finantialInstitution_options,
	        }     
	            );
	            
	            <%-- If it's not from parameter change param.finantialInstitution to whatever you need (it's the externalId already) --%>
	            <c:if test ='${not empty param.finantialinstitution}'>
	            $("#finantialDocument_finantialInstitution").select2().select2('val', '<c:out value='${param.finantialinstitution}'/>');
	            </c:if>
                <c:if test ='${not empty finantialinstitution}'>
                $("#finantialDocument_finantialInstitution").select2().select2('val', '<c:out value='${finantialinstitution}'/>');
                </c:if>
	    <%-- End block for providing finantialDocumentType options --%>


		var table = $('#searchfinantialdocumentTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'documentnumber' },
			{ data: 'documentdate' },
			{ data: 'state' },
			{ data: 'actions',className="all" }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 3 } 
		             ],
		"data" : searchfinantialdocumentDataSet,
		//Documentation: https://datatables.net/reference/option/dom
"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
//"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"        	
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchfinantialdocumentTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

