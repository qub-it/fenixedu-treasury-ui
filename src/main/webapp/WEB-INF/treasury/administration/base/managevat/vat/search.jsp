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

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message code="label.administration.base.manageVat.searchVat" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/administration/base/managevat/vat/create"><spring:message
			code="label.event.create" /></a> |&nbsp;&nbsp;
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

<div class="panel panel-default">
	<form method="get" class="form-horizontal">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Vat.vatType" />
				</div>

				<div class="col-sm-10">
					<select id="vat_vatType" class="js-example-basic-single" name="vatType">
						<option value=""></option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
					</select>
					<script>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	var vatType_options = [
	<c:forEach items="${vatTypeList}" var="element">   // THIS _FIELD_NAME__options must be added in the Controller.java 
	{
		text :"<c:out value='${element.name.content}'/>",  //Format the Output for the HTML Option
		id : "<c:out value='${element.externalId}'/>" //Define the ID for the HTML Option
	},
	</c:forEach>
	];

//Init Select2Options
	initSelect2("#vat_vatType",vatType_options, "<c:out value='${param.vatType}'/>"); //
</script>
				</div>
			</div>


			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Vat.finantialInstitution" />
				</div>

				<div class="col-sm-10">
					<select id="vat_finantialInstitution" class="js-example-basic-single" name="finantialInstitution">
						<option value=""></option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
					</select>
					<script>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	var finantialInstitution_options = [
	<c:forEach items="${finantialInstitutionList}" var="element">   // THIS _FIELD_NAME__options must be added in the Controller.java 
	{
		text :"<c:out value='${element.name}'/>",  //Format the Output for the HTML Option
		id : "<c:out value='${element.externalId}'/>" //Define the ID for the HTML Option
	},
	</c:forEach>
	];

//Init Select2Options
	initSelect2("#vat_finantialInstitution",finantialInstitution_options, "<c:out value='${param.finantialInstitution}'/>"); //
</script>
				</div>
			</div>



			<!-- <div class="form-group row"> -->
			<%-- <div class="col-sm-2 control-label"><spring:message code="label.Vat.endDate"/></div>  --%>

			<!-- <div class="col-sm-4"> -->
			<!-- 	<input id="vat_endDate" class="form-control" type="text" name="enddate"  bennu-datetime  -->
			<%-- 	value = '<c:out value='${not empty param.enddate ? param.enddate : vat.endDate }'/>' /> --%>
			<!-- </div> -->
			<!-- </div>		 -->
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.search" />" />
		</div>
	</form>
</div>

<c:choose>
	<c:when test="${not empty searchvatResultsDataSet}">
		<table id="searchvatTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
					<th><spring:message code="label.Vat.finantialInstitution" /></th>
					<th><spring:message code="label.Vat.vatType" /></th>
					<th><spring:message code="label.Vat.taxRate" /></th>
					<th><spring:message code="label.Vat.beginDate" /></th>
					<th><spring:message code="label.Vat.endDate" /></th>
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

			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
				<spring:message code="label.noResultsFound" />
			</p>

		</div>

	</c:otherwise>
</c:choose>

<script>
	var searchvatDataSet = [
			<c:forEach items="${searchvatResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
				"finantialinstitution" : "<c:out value='${searchResult.finantialInstitution.name}'/>",
				"vattype" : "<c:out value='${searchResult.vatType.name.content}'/>",
				"taxrate" : "<c:out value='${searchResult.taxRate} %' />",
"begindate" : "<c:out value='${searchResult.beginDate.toString(\'yyyy-MM-dd\')}'/>",
"enddate" : "<c:out value='${searchResult.endDate.toString(\'yyyy-MM-dd\')}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/base/managevat/vat/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" },
            </c:forEach>
    ];
	
	$(document).ready(function() {

	


		var table = $('#searchvatTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
					{ data: 'finantialinstitution' },
					{ data: 'vattype' },
		            { data: 'taxrate' },
			{ data: 'begindate' },
			{ data: 'enddate' },
			{ data: 'actions' }
			
		],
		"columnDefs": [
			       		//54
			       		//128
			       		               { "width": "54px", "targets": 5 } 
			       		             ],
		"data" : searchvatDataSet,
		//Documentation: https://datatables.net/reference/option/dom
//"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
//"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/static/treasury/swf/copy_csv_xls_pdf.swf"
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchvatTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

