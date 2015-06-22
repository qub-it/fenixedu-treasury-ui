<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="datatables"
    uri="http://github.com/dandelion/datatables"%>
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
            code="label.administration.base.manageGlobalInterestRate.searchGlobalInterestRate" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/base/manageglobalinterestrate/globalinterestrate/create"><spring:message
            code="label.event.create" /></a> &nbsp;
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



<!-- <div class="panel panel-default"> -->
<!-- <form method="get" class="form-horizontal"> -->
<!-- <div class="panel-body"> -->
<!-- <div class="form-group row"> -->
<%-- <div class="col-sm-2 control-label"><spring:message code="label.GlobalInterestRate.year"/></div>  --%>

<!-- <div class="col-sm-10"> -->
<%-- 	<input id="globalInterestRate_year" class="form-control" type="text" name="year"  value='<c:out value='${not empty param.year ? param.year : globalInterestRate.year }'/>' /> --%>
<!-- </div>	 -->
<!-- </div>		 -->
<!-- <div class="form-group row"> -->
<%-- <div class="col-sm-2 control-label"><spring:message code="label.GlobalInterestRate.description"/></div>  --%>

<!-- <div class="col-sm-10"> -->
<%-- 	<input id="globalInterestRate_description" class="form-control" type="text" name="description"  bennu-localized-string value='${not empty param.description ? param.description : "{}" } '/>  --%>
<!-- </div> -->
<!-- </div>		 -->
<!-- <div class="form-group row"> -->
<%-- <div class="col-sm-2 control-label"><spring:message code="label.GlobalInterestRate.rate"/></div>  --%>

<!-- <div class="col-sm-10"> -->
<%-- 	<input id="globalInterestRate_rate" class="form-control" type="text" name="rate"  value='<c:out value='${not empty param.rate ? param.rate : globalInterestRate.rate }'/>' /> --%>
<!-- </div>	 -->
<!-- </div>		 -->
<!-- </div> -->
<!-- <div class="panel-footer"> -->
<%-- 	<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.search" />"/> --%>
<!-- </div> -->
<!-- </form> -->
<!-- </div> -->


<div class="tab-pane" id="allGlobalInterestRates">
    <p></p>
    <c:choose>
        <c:when
            test="${not empty searchglobalinterestrateResultsDataSet}">
            <datatables:table
                id="allsearchglobalinterestrateResultsTable"
                row="globalInterestRate"
                data="${searchglobalinterestrateResultsDataSet}"
                cssClass="table responsive table-bordered table-hover"
                cdn="false" cellspacing="2">
                <datatables:column cssStyle="width:10%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.GlobalInterestRate.year" />
                    </datatables:columnHead>
                    <c:out value="${globalInterestRate.year}" />
                </datatables:column>
                <datatables:column cssStyle="width:60%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.GlobalInterestRate.description" />
                    </datatables:columnHead>
                    <c:out
                        value="${globalInterestRate.description.content}" />
                </datatables:column>
                <datatables:column cssStyle="width:10%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.GlobalInterestRate.rate" />
                    </datatables:columnHead>
                    <c:out value="${globalInterestRate.rate}" />&#37;
				</datatables:column>
                <datatables:column cssStyle="width:20%">
                    <a class="btn btn-default btn-xs"
                        href="${pageContext.request.contextPath}/treasury/administration/base/manageglobalinterestrate/globalinterestrate/search/view/${globalInterestRate.externalId}"><spring:message
                            code="label.view" /></a>
                </datatables:column>
            </datatables:table>
            <script>
													createDataTables(
															'allsearchglobalinterestrateResultsTable',
															true,
															false,
															true,
															"${pageContext.request.contextPath}",
															"${datatablesI18NUrl}");
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
</div>

<script>
	// 	var searchglobalinterestrateDataSet = [
	// 			<c:forEach items="${searchglobalinterestrateResultsDataSet}" var="searchResult">
	// 				{
	// 				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
	// "year" : "<c:out value='${searchResult.year}'/>",
	// "description" : "<c:out value='${searchResult.description.content}'/>",
	// "rate" : "<c:out value='${searchResult.rate}'/>&#37;",
	// "actions" :
	// " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/base/manageglobalinterestrate/globalinterestrate/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
	//                 "" 
	// 			},
	//             </c:forEach>
	//     ];

	// 	$(document).ready(function() {

	// 		var table = $('#searchglobalinterestrateTable').DataTable({language : {
	// 			url : "${datatablesI18NUrl}",			
	// 		},
	// 		"columns": [
	// 			{ data: 'year' },
	// 			{ data: 'description' },
	// 			{ data: 'rate' },
	// 			{ data: 'actions' }

	// 		],
	// 		//CHANGE_ME adjust the actions column width if needed
	// 		"columnDefs": [
	// 		//54
	// 		               { "width": "54px", "targets": 3 } 
	// 		             ],
	// 		"data" : searchglobalinterestrateDataSet,
	// 		//Documentation: https://datatables.net/reference/option/dom
	// //"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
	// "dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
	// //"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
	// //"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
	//         "tableTools": {
	//             "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"        	
	//         }
	// 		});
	// 		table.columns.adjust().draw();

	// 		  $('#searchglobalinterestrateTable tbody').on( 'click', 'tr', function () {
	// 		        $(this).toggleClass('selected');
	// 		    } );

	// 	});
</script>

