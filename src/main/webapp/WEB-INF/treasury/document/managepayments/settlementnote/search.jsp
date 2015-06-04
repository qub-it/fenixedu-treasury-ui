<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
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
        <spring:message
            code="label.document.managePayments.searchSettlementNote" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/document/managepayments/settlementnote/create"><spring:message
            code="label.event.create" /></a> &nbsp;|&nbsp;
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

<div class="panel panel-default">
    <form method="get" class="form-horizontal">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.SettlementNote.debtAccount" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="settlementNote_debtAccount"
                        class="js-example-basic-single"
                        name="debtaccount">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.SettlementNote.documentNumberSeries" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="settlementNote_documentNumberSeries"
                        class="js-example-basic-single"
                        name="documentnumberseries">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.SettlementNote.currency" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="settlementNote_currency"
                        class="js-example-basic-single" name="currency">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.SettlementNote.documentNumber" />
                </div>

                <div class="col-sm-10">
                    <input id="settlementNote_documentNumber"
                        class="form-control" type="text"
                        name="documentnumber"
                        value='<c:out value='${not empty param.documentnumber ? param.documentnumber : settlementNote.documentNumber }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.SettlementNote.documentDate" />
                </div>

                <div class="col-sm-4">
                    <input id="settlementNote_documentDate"
                        class="form-control" type="text"
                        name="documentdate" bennu-date
                        value='<c:out value='${not empty param.documentdate ? param.documentdate : settlementNote.documentDate }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.SettlementNote.documentDueDate" />
                </div>

                <div class="col-sm-4">
                    <input id="settlementNote_documentDueDate"
                        class="form-control" type="text"
                        name="documentduedate" bennu-date
                        value='<c:out value='${not empty param.documentduedate ? param.documentduedate : settlementNote.documentDueDate }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.SettlementNote.originDocumentNumber" />
                </div>

                <div class="col-sm-10">
                    <input id="settlementNote_originDocumentNumber"
                        class="form-control" type="text"
                        name="origindocumentnumber"
                        value='<c:out value='${not empty param.origindocumentnumber ? param.origindocumentnumber : settlementNote.originDocumentNumber }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.SettlementNote.state" />
                </div>

                <div class="col-sm-4">
                    <select id="settlementNote_state"
                        class="form-control" name="state">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME--%>
                        <c:forEach items="${stateValues}" var="field">
                            <option value='<c:out value='${field}'/>'><c:out
                                    value='${field}' /></option>
                        </c:forEach>
                    </select>
                    <script>
                    $("#settlementNote_state").val('<c:out value='${not empty param.state ? param.state : settlementNote.state }'/>');
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
    <c:when test="${not empty searchsettlementnoteResultsDataSet}">
        <table id="searchsettlementnoteTable"
            class="table responsive table-bordered table-hover">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message
                            code="label.SettlementNote.finantialDocumentType" /></th>
                    <th><spring:message
                            code="label.SettlementNote.debtAccount" /></th>
                    <th><spring:message
                            code="label.SettlementNote.documentNumberSeries" /></th>
                    <th><spring:message
                            code="label.SettlementNote.currency" /></th>
                    <th><spring:message
                            code="label.SettlementNote.documentNumber" /></th>
                    <th><spring:message
                            code="label.SettlementNote.documentDate" /></th>
                    <th><spring:message
                            code="label.SettlementNote.documentDueDate" /></th>
                    <th><spring:message
                            code="label.SettlementNote.originDocumentNumber" /></th>
                    <th><spring:message
                            code="label.SettlementNote.state" /></th>
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
                <span class="glyphicon glyphicon-exclamation-sign"
                    aria-hidden="true">&nbsp;</span>
                <spring:message code="label.noResultsFound" />
            </p>

        </div>

    </c:otherwise>
</c:choose>

<script>
	var searchsettlementnoteDataSet = [
			<c:forEach items="${searchsettlementnoteResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"finantialdocumenttype" : "<c:out value='${searchResult.finantialDocumentType}'/>",
"debtaccount" : "<c:out value='${searchResult.debtAccount}'/>",
"documentnumberseries" : "<c:out value='${searchResult.documentNumberSeries}'/>",
"currency" : "<c:out value='${searchResult.currency}'/>",
"documentnumber" : "<c:out value='${searchResult.documentNumber}'/>",
// "documentdate" : "<c:out value='${searchResult.documentDate}'/>",
"documentdate" : "<joda:format value='${searchResult.documentDate}' style='S-' />",
// "documentduedate" : "<c:out value='${searchResult.documentDueDate}'/>",
"documentduedate" : "<joda:format value='${searchResult.documentDueDate}' style='S-' />",
"origindocumentnumber" : "<c:out value='${searchResult.originDocumentNumber}'/>",
"state" : "<c:out value='${searchResult.state}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/document/managepayments/settlementnote/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" },
            </c:forEach>
    ];
	
$(document).ready(function() {
	<%-- Block for providing debtAccount options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	debtAccount_options = [
		<c:forEach items="${SettlementNote_debtAccount_options}" var="element"> 
			{
				text :"<c:out value='${element.finantialInstitution.code} - ${element.customer.code} - ${element.customer.name}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	$("#settlementNote_debtAccount").select2(
		{
			data : debtAccount_options,
		} 
	);	    
    <%-- If it's not from parameter change param.debtAccount to whatever you need (it's the externalId already) --%>
	$("#settlementNote_debtAccount").select2().select2('val', '<c:out value='${param.debtAccount}'/>');
	<%-- End block for providing debtAccount options --%>
	<%-- Block for providing documentNumberSeries options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	documentNumberSeries_options = [
		<c:forEach items="${SettlementNote_documentNumberSeries_options}" var="element"> 
			{
				text :"<c:out value='${element.series.code} - ${element.series.name.content}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	$("#settlementNote_documentNumberSeries").select2(
		{
			data : documentNumberSeries_options,
		}	  
	);	    
	<%-- If it's not from parameter change param.documentNumberSeries to whatever you need (it's the externalId already) --%>
	$("#settlementNote_documentNumberSeries").select2().select2('val', '<c:out value='${param.documentNumberSeries}'/>');
	<%-- End block for providing documentNumberSeries options --%>
	<%-- Block for providing currency options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	currency_options = [
		<c:forEach items="${SettlementNote_currency_options}" var="element"> 
			{
				text :"<c:out value='${element.code} - ${element.name.content}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	$("#settlementNote_currency").select2(
		{
			data : currency_options,
		}	  
    );		    
    <%-- If it's not from parameter change param.currency to whatever you need (it's the externalId already) --%>
    $("#settlementNote_currency").select2().select2('val', '<c:out value='${param.currency}'/>');
	<%-- End block for providing currency options --%>
	var table = $('#searchsettlementnoteTable').DataTable({
		language : {
			  url : "${datatablesI18NUrl}",			
	    },
	    "columns": [
    		{ data: 'finantialdocumenttype' },
    		{ data: 'debtaccount' },
    		{ data: 'documentnumberseries' },
    		{ data: 'currency' },
    		{ data: 'documentnumber' },
    		{ data: 'documentdate' },
    		{ data: 'documentduedate' },
    		{ data: 'origindocumentnumber' },
    		{ data: 'state' },
    		{ data: 'actions' }		
    	],
    	//CHANGE_ME adjust the actions column width if needed
    	"columnDefs": [
            { "width": "54px", "targets": 9 } 
        ],
	    "data" : searchsettlementnoteDataSet,
    	//Documentation: https://datatables.net/reference/option/dom
        //"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
        "dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
        //"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
        //"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/static/treasury/swf/copy_csv_xls_pdf.swf"
        }
	});
	table.columns.adjust().draw();	
	$('#searchsettlementnoteTable tbody').on( 'click', 'tr', function () {
	    $(this).toggleClass('selected');
	} );
		  
}); 
</script>

