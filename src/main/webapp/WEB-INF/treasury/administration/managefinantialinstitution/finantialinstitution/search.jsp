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

<link href="//cdn.datatables.net/responsive/1.0.4/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="//cdn.datatables.net/responsive/1.0.4/js/dataTables.responsive.js"></script>
<link href="//cdn.datatables.net/tabletools/2.2.3/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="//cdn.datatables.net/tabletools/2.2.3/js/dataTables.tableTools.min.js"></script>
<link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/css/select2.min.css" rel="stylesheet" />
<script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/js/select2.min.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.administration.manageFinantialInstitution.searchFinantialInstitution" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/create"   ><spring:message code="label.event.create" /></a>
|&nbsp;&nbsp;</div>
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



<div class="panel panel-default">
<form method="get" class="form-horizontal">
<div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.code"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_code" class="form-control" type="text" name="code"  value='<c:out value='${not empty param.code ? param.code : finantialInstitution.code }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.fiscalNumber"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_fiscalNumber" class="form-control" type="text" name="fiscalnumber"  value='<c:out value='${not empty param.fiscalnumber ? param.fiscalnumber : finantialInstitution.fiscalNumber }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.companyId"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_companyId" class="form-control" type="text" name="companyid"  value='<c:out value='${not empty param.companyid ? param.companyid : finantialInstitution.companyId }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.name"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_name" class="form-control" type="text" name="name"  value='<c:out value='${not empty param.name ? param.name : finantialInstitution.name }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.companyName"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_companyName" class="form-control" type="text" name="companyname"  value='<c:out value='${not empty param.companyname ? param.companyname : finantialInstitution.companyName }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.address"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_address" class="form-control" type="text" name="address"  value='<c:out value='${not empty param.address ? param.address : finantialInstitution.address }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.country"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_country" class="form-control" type="text" name="country"  value='<c:out value='${not empty param.country ? param.country : finantialInstitution.country }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.district"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_district" class="form-control" type="text" name="district"  value='<c:out value='${not empty param.district ? param.district : finantialInstitution.district }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.municipality"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_municipality" class="form-control" type="text" name="municipality"  value='<c:out value='${not empty param.municipality ? param.municipality : finantialInstitution.municipality }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.locality"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_locality" class="form-control" type="text" name="locality"  value='<c:out value='${not empty param.locality ? param.locality : finantialInstitution.locality }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialInstitution.zipCode"/></div> 

<div class="col-sm-10">
	<input id="finantialInstitution_zipCode" class="form-control" type="text" name="zipcode"  value='<c:out value='${not empty param.zipcode ? param.zipcode : finantialInstitution.zipCode }'/>' />
</div>	
</div>		
</div>
<div class="panel-footer">
	<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.search" />"/>
</div>
</form>
</div>


<c:choose>
	<c:when test="${not empty searchfinantialinstitutionResultsDataSet}">
		<table id="searchfinantialinstitutionTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.FinantialInstitution.code"/></th>
<th><spring:message code="label.FinantialInstitution.fiscalNumber"/></th>
<th><spring:message code="label.FinantialInstitution.companyId"/></th>
<th><spring:message code="label.FinantialInstitution.name"/></th>
<th><spring:message code="label.FinantialInstitution.companyName"/></th>
<th><spring:message code="label.FinantialInstitution.address"/></th>
<th><spring:message code="label.FinantialInstitution.country"/></th>
<th><spring:message code="label.FinantialInstitution.district"/></th>
<th><spring:message code="label.FinantialInstitution.municipality"/></th>
<th><spring:message code="label.FinantialInstitution.locality"/></th>
<th><spring:message code="label.FinantialInstitution.zipCode"/></th>
<%-- Operations Column --%>
					<th></th>
				</tr>
			</thead>
			<tbody>
				
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
				<div class="alert alert-info" role="alert">
					
					<spring:message code="label.noResultsFound"/>
					
				</div>	
		
	</c:otherwise>
</c:choose>

<script>
	var searchfinantialinstitutionDataSet = [
			<c:forEach items="${searchfinantialinstitutionResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"code" : "<c:out value='${searchResult.code}'/>",
"fiscalnumber" : "<c:out value='${searchResult.fiscalNumber}'/>",
"companyid" : "<c:out value='${searchResult.companyId}'/>",
"name" : "<c:out value='${searchResult.name}'/>",
"companyname" : "<c:out value='${searchResult.companyName}'/>",
"address" : "<c:out value='${searchResult.address}'/>",
"country" : "<c:out value='${searchResult.country}'/>",
"district" : "<c:out value='${searchResult.district}'/>",
"municipality" : "<c:out value='${searchResult.municipality}'/>",
"locality" : "<c:out value='${searchResult.locality}'/>",
"zipcode" : "<c:out value='${searchResult.zipCode}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" },
            </c:forEach>
    ];
	
	$(document).ready(function() {

	


		var table = $('#searchfinantialinstitutionTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'code' },
			{ data: 'fiscalnumber' },
			{ data: 'companyid' },
			{ data: 'name' },
			{ data: 'companyname' },
			{ data: 'address' },
			{ data: 'country' },
			{ data: 'district' },
			{ data: 'municipality' },
			{ data: 'locality' },
			{ data: 'zipcode' },
			{ data: 'actions' }
			
		],
		"data" : searchfinantialinstitutionDataSet,
		//Documentation: https://datatables.net/reference/option/dom
//"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "//cdn.datatables.net/tabletools/2.2.3/swf/copy_csv_xls_pdf.swf"
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchfinantialinstitutionTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

