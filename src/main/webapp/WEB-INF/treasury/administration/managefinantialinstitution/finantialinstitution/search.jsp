<%@page import="java.util.Collection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
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
    <h1>
        <spring:message
            code="label.administration.manageFinantialInstitution.searchFinantialInstitution" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/create"><spring:message
            code="label.event.create" /></a> &nbsp;
</div>
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

<c:choose>
    <c:when test="${not empty searchfinantialinstitutionResultsDataSet}">
        <table id="searchfinantialinstitutionTable"
            class="table responsive table-bordered table-hover">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message
                            code="label.FinantialInstitution.code" /></th>
                    <th><spring:message
                            code="label.FinantialInstitution.fiscalNumber" /></th>
                    <th><spring:message
                            code="label.FinantialInstitution.name" /></th>                    
                    <th><%-- Operations Column --%></th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info" role="alert">
            <spring:message code="label.noResultsFound" />
        </div>
    </c:otherwise>
</c:choose>

<script>
	var searchfinantialinstitutionDataSet = [
		<c:forEach items="${searchfinantialinstitutionResultsDataSet}" var="searchResult">
			{
				"code" : "<c:out value='${searchResult.code}'/>",
				"fiscalnumber" : "<c:out value='${searchResult.fiscalNumber}'/>",
				"name" : "<c:out value='${searchResult.name}'/>",
				"actions" : "<a  class=\"btn btn-default btn-xs\" "
				              +" href=\"${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/search/view/${searchResult.externalId}\">"
				              +" <spring:message code='label.view'/></a>" 
			},
		</c:forEach>
    ];
	
    $(document).ready(function() {
        var table = $('#searchfinantialinstitutionTable').DataTable(
        	    {
        	        language : {
        	            url : "${datatablesI18NUrl}",			
        	        },
        	        "columns": [
        	            { data: 'code' },
        	            { data: 'fiscalnumber' },
        	            { data: 'name' },
        	            { data: 'actions' }			
        	        ],
        	        //CHANGE_ME adjust the actions column width if needed
        	        "columnDefs": [
        	            { "width": "70px", "targets": 3 } 
        	        ],
        	        "data" : searchfinantialinstitutionDataSet,
            		//Documentation: https://datatables.net/reference/option/dom
//             		"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
            		//"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
            		"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
            		//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
		        });
		table.columns.adjust().draw();
		$('#searchfinantialinstitutionTable tbody').on( 'click', 'tr', function () {
	        $(this).toggleClass('selected');
	    } );  
	}); 
</script>

