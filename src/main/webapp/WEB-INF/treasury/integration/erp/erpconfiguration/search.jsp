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
	<h1><spring:message code="label.integration.erp.searchERPConfiguration" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/integration/erp/erpconfiguration/create"   ><spring:message code="label.event.create" /></a>
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


<!-- 
<div class="panel panel-default">
<form method="get" class="form-horizontal">
<div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.ERPConfiguration.paymentsIntegrationSeries"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="eRPConfiguration_paymentsIntegrationSeries" class="js-example-basic-single" name="paymentsintegrationseries">
		 <option value=""></option> empty option remove it if you don't want to have it or give it a label CHANGE_ME 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.ERPConfiguration.finantialInstitution"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="eRPConfiguration_finantialInstitution" class="js-example-basic-single" name="finantialinstitution">
		 <option value=""></option> empty option remove it if you don't want to have it or give it a label CHANGE_ME 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.ERPConfiguration.code"/></div> 

<div class="col-sm-10">
	<input id="eRPConfiguration_code" class="form-control" type="text" name="code"  value='<c:out value='${not empty param.code ? param.code : eRPConfiguration.code }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.ERPConfiguration.externalURL"/></div> 

<div class="col-sm-10">
	<input id="eRPConfiguration_externalURL" class="form-control" type="text" name="externalurl"  value='<c:out value='${not empty param.externalurl ? param.externalurl : eRPConfiguration.externalURL }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.ERPConfiguration.username"/></div> 

<div class="col-sm-10">
	<input id="eRPConfiguration_username" class="form-control" type="text" name="username"  value='<c:out value='${not empty param.username ? param.username : eRPConfiguration.username }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.ERPConfiguration.password"/></div> 

<div class="col-sm-10">
	<input id="eRPConfiguration_password" class="form-control" type="text" name="password"  value='<c:out value='${not empty param.password ? param.password : eRPConfiguration.password }'/>' />
</div>	
</div>		
</div>
<div class="panel-footer">
	<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.search" />"/>
</div>
</form>
</div>
 -->

<c:choose>
	<c:when test="${not empty searcherpconfigurationResultsDataSet}">
		<table id="searcherpconfigurationTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.ERPConfiguration.paymentsIntegrationSeries"/></th>
<th><spring:message code="label.ERPConfiguration.finantialInstitution"/></th>
<th><spring:message code="label.ERPConfiguration.code"/></th>
<th><spring:message code="label.ERPConfiguration.externalURL"/></th>
<th><spring:message code="label.ERPConfiguration.username"/></th>
<th><spring:message code="label.ERPConfiguration.password"/></th>
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
	var searcherpconfigurationDataSet = [
			<c:forEach items="${searcherpconfigurationResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"paymentsintegrationseries" : "<c:out value='${searchResult.paymentsIntegrationSeries.name.content}'/>",
"finantialinstitution" : "<c:out value='${searchResult.finantialInstitution.name}'/>",
"code" : "<c:out value='${searchResult.code}'/>",
"externalurl" : "<c:out value='${searchResult.externalURL}'/>",
"username" : "<c:out value='${searchResult.username}'/>",
"password" : "<c:out value='${searchResult.password}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/integration/erp/erpconfiguration/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" 
			},
            </c:forEach>
    ];
	
	$(document).ready(function() {

	<%-- Block for providing paymentsIntegrationSeries options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
// 	paymentsIntegrationSeries_options = [
// 		<c:forEach items="${ERPConfiguration_paymentsIntegrationSeries_options}" var="element"> 
// 			{
// 				text :"<c:out value='${element}'/>", 
// 				id : "<c:out value='${element.externalId}'/>"
// 			},
// 		</c:forEach>
// 	];
	
// 	$("#eRPConfiguration_paymentsIntegrationSeries").select2(
// 		{
// 			data : paymentsIntegrationSeries_options,
// 		}	  
// 		    );
		    
		    
// 		    $("#eRPConfiguration_paymentsIntegrationSeries").select2().select2('val', '<c:out value='${param.paymentsIntegrationSeries}'/>');
// 	finantialInstitution_options = [
// 		<c:forEach items="${ERPConfiguration_finantialInstitution_options}" var="element"> 
// 			{
// 				text :"<c:out value='${element}'/>", 
// 				id : "<c:out value='${element.externalId}'/>"
// 			},
// 		</c:forEach>
// 	];
	
// 	$("#eRPConfiguration_finantialInstitution").select2(
// 		{
// 			data : finantialInstitution_options,
// 		}	  
// 		    );
		    
		    
// 		    $("#eRPConfiguration_finantialInstitution").select2().select2('val', '<c:out value='${param.finantialInstitution}'/>');
	
	


		var table = $('#searcherpconfigurationTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'paymentsintegrationseries' },
			{ data: 'finantialinstitution' },
			{ data: 'code' },
			{ data: 'externalurl' },
			{ data: 'username' },
			{ data: 'password' },
			{ data: 'actions' }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 6 } 
		             ],
		"data" : searcherpconfigurationDataSet,
		//Documentation: https://datatables.net/reference/option/dom
"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
// "dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"        	
        }
		});
		table.columns.adjust().draw();
		
		  $('#searcherpconfigurationTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

