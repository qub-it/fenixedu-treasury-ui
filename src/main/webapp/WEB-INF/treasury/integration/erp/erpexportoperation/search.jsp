<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
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
            code="label.integration.erp.searchERPExportOperation" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/integration/erp/finantialdocument"><spring:message
            code="label.event.integration.erp.searchPendingDocuments" /></a>
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

<script type="text/javascript">
	function submitOptions(tableID, formID, attributeName) {
	array = $("#" + tableID).DataTable().rows(".selected")[0];	
	$("#" + formID).empty();
	if (array.length>0) {
		$.each(array,function(index, value) {
			externalId = $("#" + tableID).DataTable().row(value).data()["DT_RowId"];
			$("#" + formID).append("<input type='hidden' name='" + attributeName+ "' value='" + externalId + "'/>");
		});
		$("#" + formID).submit();
	}
	else
	{
		messageAlert('<spring:message code = "label.warning"/>','<spring:message code = "label.select.mustselect"/>');
	}
		
	}
</script>


<div class="panel panel-default">
    <form method="get" class="form-horizontal">
        <div class="panel-body">
                    <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.Vat.finantialInstitution" />
                </div>

                <div class="col-sm-10">
                    <select id="vat_finantialInstitution" class="js-example-basic-single" name="finantialinstitution">
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
    initSelect2("#vat_finantialInstitution",finantialInstitution_options, "<c:out value='${param.finantialinstitution}'/>"); //
</script>
                </div>
            </div>
        
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.ERPExportOperation.fromExecutionDate" />
                </div>
                <div class="col-sm-3">
                    <input id="eRPExportOperation_fromExecutionDate"
                        class="form-control" type="text"
                        name="fromexecutiondate" bennu-date
                        value='<c:out value='${param.fromexecutiondate }'/>' />
                </div>
            </div>
            <div class="form-group row">                
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.ERPExportOperation.toExecutionDate" />
                </div>
                <div class="col-sm-3">
                    <input id="eRPExportOperation_toExecutionDate"
                        class="form-control" type="text"
                        name="toexecutiondate" bennu-date
                        value='<c:out value='${param.toexecutiondate }'/>' />
                </div>
            </div>
            
            <div class="form-group row">                
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.FinantialDocument.documentNumber" />
                </div>
                <div class="col-sm-3">
                    <input id="eRPExportOperation_documentNumber"
                        class="form-control" type="text"
                        name="documentnumber" 
                        value='<c:out value='${param.documentnumber}'/>' />
                </div>
            </div>

            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.ERPExportOperation.success" />
                </div>

                <div class="col-sm-2">
                    <select id="eRPExportOperation_success"
                        name="success" class="form-control">
                        <option></option>
                        <option value="false"><spring:message
                                code="label.no" /></option>
                        <option value="true"><spring:message
                                code="label.yes" /></option>
                    </select>
                    <script>
		$("#eRPExportOperation_success").val('<c:out value='${not empty param.success ? param.success : eRPExportOperation.success }'/>');
	</script>
                </div>
            </div>
        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button"
                value="<spring:message code="label.search" />" />
        </div>
    </form>
</div>


<c:choose>
    <c:when test="${not empty searcherpexportoperationResultsDataSet}">
        <table id="searcherpexportoperationTable"
            class="table responsive table-bordered table-hover" width="100%">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message
                            code="label.ERPExportOperation.finantialInstitution" /></th>
                    <th><spring:message
                            code="label.ERPExportOperation.executionDate" /></th>
                    <th><spring:message
                            code="label.ERPExportOperation.success" /></th>
                    <%--                     <th><spring:message --%>
                    <%--                             code="label.ERPExportOperation.corrected" /></th> --%>
                    <%--                     <th><spring:message --%>
                    <%--                             code="label.ERPExportOperation.creator" /></th> --%>
                    <%-- Operations Column --%>
                    <th></th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
        <form id="deleteMultiple"
            action="${pageContext.request.contextPath}/treasury/integration/erp/erpexportoperation/deletemultiple/"
            style="display: none;" method="POST"></form>

        <button id="deleteMultiple" type="button"
            onClick="javascript:submitOptions('searcherpexportoperationTable', 'deleteMultiple', 'operations')">
            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp;
            <spring:message
                code='label.deleteMultiple' />
        </button>

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
	var searcherpexportoperationDataSet = [
			<c:forEach items="${searcherpexportoperationResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
				"creator" : "<c:out value='${searchResult.versioningCreator}'/>",
				"executiondate" : "<c:out value='${searchResult.executionDate.toString("YYYY-MM-dd HH:mm:ss")}'/>",
"finantialinstitution" : "<c:out value='${searchResult.finantialInstitution.name}'/>",
"success" : "<c:if test="${searchResult.success}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.success}"><spring:message code="label.false" /></c:if>",
// "corrected" : "<c:if test="${searchResult.corrected}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.corrected}"><spring:message code="label.false" /></c:if>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/integration/erp/erpexportoperation/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" 
			},
            </c:forEach>
    ];
	
	$(document).ready(function() {

	


		var table = $('#searcherpexportoperationTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
		            { data: 'finantialinstitution' },
			{ data: 'executiondate' },
			{ data: 'success' },
// 			{ data: 'corrected' },
//			{ data: 'creator' },
			{ data: 'actions',className:"all" }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 3 } 
		             ],
		"data" : searcherpexportoperationDataSet,
		//Documentation: https://datatables.net/reference/option/dom
//"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"        	
        }
		});
		table.columns.adjust().draw();
		
		  $('#searcherpexportoperationTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

