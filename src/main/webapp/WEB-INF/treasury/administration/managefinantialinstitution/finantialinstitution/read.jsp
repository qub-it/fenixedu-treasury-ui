
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="pf" uri="http://example.com/placeFunctions"%>

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
<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.tableTools.min.js"></script>
<link href="${pageContext.request.contextPath}/static/treasury/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/treasury/js/select2.min.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/bootbox.min.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.administration.manageFinantialInstitution.readFinantialInstitution" />
        <small></small>
    </h1>
</div>
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">
                    <spring:message code="label.confirmation" />
                </h4>
            </div>
            <div class="modal-body">
                <p>
                    <spring:message
                        code="label.administration.manageFinantialInstitution.readFinantialInstitution.confirmDelete"
                        arguments='${finantialInstitution.name }' />
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                    data-dismiss="modal">
                    <spring:message code="label.close" />
                </button>
                <a class="btn btn-danger"
                    href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/delete/${finantialInstitution.externalId}">
                    <spring:message code="label.delete" />
                </a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/"><spring:message
            code="label.event.back" /></a> |&nbsp;&nbsp; <span
        class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp;<a
        class="" href="#" data-toggle="modal" data-target="#deleteModal"><spring:message
            code="label.event.delete" /></a> |&nbsp;&nbsp; <span
        class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/update/${finantialInstitution.externalId}"><spring:message
            code="label.event.update" /></a> |&nbsp;&nbsp;
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
                                code="label.FinantialInstitution.fiscalCountryRegion" /></th>
                        <td><c:out
                                value='${finantialInstitution.fiscalCountryRegion.name.content}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.code" /></th>
                        <td><c:out
                                value='${finantialInstitution.code}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.fiscalNumber" /></th>
                        <td><c:out
                                value='${finantialInstitution.fiscalNumber}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.companyId" /></th>
                        <td><c:out
                                value='${finantialInstitution.companyId}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.name" /></th>
                        <td><c:out
                                value='${finantialInstitution.name}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.companyName" /></th>
                        <td><c:out
                                value='${finantialInstitution.companyName}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.address" /></th>
                        <td><c:out
                                value='${finantialInstitution.address}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.country" /></th>
                        <td><pf:placeName
                                place="${finantialInstitution.country}" />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.district" /></th>
                        <td><pf:placeName
                                place="${finantialInstitution.district}" />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.municipality" /></th>
                        <td><pf:placeName
                                place="${finantialInstitution.municipality}" />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.locality" /></th>
                        <td><c:out
                                value='${finantialInstitution.locality}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.zipCode" /></th>
                        <td><c:out
                                value='${finantialInstitution.zipCode}' />
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>

<!-- Finantial Entity section -->
<h2>
    <spring:message code="label.administration.manageFinantialInstitution.searchFinantialEntity" />
</h2>

<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
    &nbsp;
    <a class=""
       href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialentity/create?finantialInstitutionId=${finantialInstitution.externalId }">
           <spring:message code="label.event.create" />
    </a> 
    |&nbsp;&nbsp;
</div>
<c:choose>
    <c:when
        test="${not empty finantialInstitution.finantialEntitiesSet}">
        <table id="searchfinantialentityTable"
            class="table responsive table-bordered table-hover">
            <thead>
                <tr>
                    <th><spring:message
                            code="label.FinantialEntity.name" /></th>
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
            <spring:message code="label.noResultsFound" />
        </div>
    </c:otherwise>
</c:choose>

<!-- Documents Series section -->
<h2>
    <spring:message code="label.administration.manageFinantialInstitution.searchSeries" />
</h2>

<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
    &nbsp;
    <a class=""
       href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/series/create?finantialInstitutionId=${finantialInstitution.externalId }">
           <spring:message code="label.event.create" />
    </a> 
    |&nbsp;&nbsp;
</div>
<c:choose>
    <c:when test="${not empty finantialInstitution.seriesSet}">
        <table id="searchseriesTable"
            class="table responsive table-bordered table-hover">
            <thead>
                <tr>
                    <th><spring:message code="label.Series.code" /></th>
                    <th><spring:message code="label.Series.name" /></th>
                    <th><spring:message
                            code="label.Series.externSeries" /></th>
                    <th><spring:message
                            code="label.Series.defaultSeries" /></th>
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
            <spring:message code="label.noResultsFound" />
        </div>
    </c:otherwise>
</c:choose>

<script>
var searchfinantialentityDataSet = [
    <c:forEach items="${finantialInstitution.finantialEntitiesSet}" var="searchResult">
    {
        "name" : "<c:out value='${searchResult.name.content}'/>",
        "actions" :
             " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialentity/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>"
           + " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialentity/search/edit/${searchResult.externalId}\"><spring:message code='label.edit'/></a>"             
    },
    </c:forEach>
];

var searchseriesDataSet = [
    <c:forEach items="${finantialInstitution.seriesSet}" var="searchResult">
    {
        "code" : "<c:out value='${searchResult.code}'/>",
        "name" : "<c:out value='${searchResult.name.content}'/>",
        "externSeries" : <c:if test="${searchResult.externSeries}">
                              "<spring:message code='label.true' />"
                          </c:if>
                          <c:if test="${not searchResult.externSeries}">
                              "<spring:message code='label.false' />"
                          </c:if>,
        "defaultSeries" :<c:if test="${searchResult.defaultSeries}">
                              "<spring:message code='label.Series.defaultSeries' />"
                          </c:if>
                          <c:if test="${not searchResult.defaultSeries}">
                              "<a href=\"${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/series/search/editDefault/${searchResult.externalId}\"><spring:message code='label.Series.defaultSeries'/></a>"
                          </c:if>,
        "actions" :
             " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/series/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>"
           + " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/series/search/edit/${searchResult.externalId}\"><spring:message code='label.edit'/></a>"             
    },
    </c:forEach>
];



$(document).ready(function() {
    var table = $('#searchfinantialentityTable').DataTable({
    	language : {
    	    url : "${datatablesI18NUrl}",           
    	},
        "columns": [
            { data: 'name' },
            { data: 'actions' }
        ],
        //CHANGE_ME adjust the actions column width if needed
        "columnDefs": [
            { "width": "105px", "targets": 1 } 
        ],
        "data" : searchfinantialentityDataSet,
        //Documentation: https://datatables.net/reference/option/dom
        //"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
        //"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
        "dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
        //"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
    });
    var table2 = $('#searchseriesTable').DataTable({
        language : {
            url : "${datatablesI18NUrl}",           
        },
        "columns": [
            { data: 'code' },
            { data: 'name' },
            { data: 'externSeries' },
            { data: 'defaultSeries' },
            { data: 'actions' }                    
        ],
        //CHANGE_ME adjust the actions column width if needed
        "columnDefs": [
            { "width": "70px", "targets": 4 } 
        ],
        "data" : searchseriesDataSet,
        //Documentation: https://datatables.net/reference/option/dom
        "dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
        //"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
        //"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
        //"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
    });
    
    table.columns.adjust().draw();
    $('#searchfinantialentityTable tbody').on( 
	  	'click', 
		'tr', 
		function () {
		    $(this).toggleClass('selected');
        } 
	);    
    table.columns.adjust().draw();
    $('#searchseriesTable tbody').on( 
        'click', 
        'tr', 
        function () {
            $(this).toggleClass('selected');
        } 
    );  
}); 
</script>
