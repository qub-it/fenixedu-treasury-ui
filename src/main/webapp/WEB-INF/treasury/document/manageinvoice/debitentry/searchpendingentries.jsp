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
            code="label.document.manageInvoice.searchPendingEntries" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/read/${debitNote.externalId}"><spring:message
            code="label.event.back" /></a> &nbsp;
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

<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">
            <spring:message code="label.FinantialDocumentEntry.document" />
        </h3>
    </div>

    <table class="table">
        <tbody>
            <tr>
                <th scope="row" class="col-xs-3"><spring:message
                        code="label.DebtAccount.finantialInstitution" /></th>
                <td><c:out
                        value='${debitNote.debtAccount.finantialInstitution.name}' /></td>
            </tr>
            <tr>
                <th scope="row" class="col-xs-3"><spring:message
                        code="label.DebtAccount.customer" /></th>
                <td><c:out
                        value='${debitNote.debtAccount.customer.businessIdentification} - ${debitNote.debtAccount.customer.name}' /></td>
            </tr>
            <tr>
                <th scope="row" class="col-xs-3"><spring:message
                        code="label.DebitNote.documentNumber" /></th>
                <td><c:out value='${debitNote.uiDocumentNumber}' /></td>
            </tr>
            <tr>
                <th scope="row" class="col-xs-3"><spring:message
                        code="label.DebitNote.documentDate" /></th>
                <td><joda:format value='${debitNote.documentDate}' style='SS'/></td>
            </tr>
        </tbody>
    </table>
</div>

</br>
</br>
<c:choose>
    <c:when test="${not empty searchpendingentriesResultsDataSet}">
        <table id="searchpendingentriesTable"
            class="table responsive table-bordered table-hover">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message
                            code="label.DebitEntry.entryDateTime" /></th>
                    <th><spring:message
                            code="label.DebitEntry.quantity" /></th>
                    <th><spring:message
                            code="label.DebitEntry.description" /></th>
                    <th><spring:message
                            code="label.DebitEntry.amount" /></th>
                    <th><spring:message code="label.DebitEntry.vat" /></th>
                    <%-- Operations Column --%>
                    <th></th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
        <form id="addentries"
            action="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/searchpendingentries/addentries?debitNote=${debitNote.externalId}"
            style="display: none;" method="POST"></form>

        <button id="addEntryButton" type="button"
            onClick="javascript:submitOptions('searchpendingentriesTable', 'addentries', 'debitEntrys')">
            <spring:message
                code='label.document.manageInvoice.searchPendingEntries.addEntries' />
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
	var searchpendingentriesDataSet = [
			<c:forEach items="${searchpendingentriesResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"datetime" : "<joda:format value='${searchResult.entryDateTime}' style='SS'/>",
				"quantity" : "<c:out value='${searchResult.quantity}'/>",
"description" : "<c:out value='${searchResult.description}'/>",
"amount" : "<c:out value='${searchResult.currency.getValueFor(searchResult.totalAmount)}'/>",
"vat" : "<c:out value='${searchResult.vat.taxRate}'/> %",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/document/manageinvoice/debitentry/searchpendingentries/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" 
			},
            </c:forEach>
    ];
	
	$(document).ready(function() {

	

		var table = $('#searchpendingentriesTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'datetime' },
			{ data: 'quantity' },
			{ data: 'description' },
			{ data: 'amount' },
			{ data: 'vat' },
			{ data: 'actions' }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 4 } 
		             ],
		"data" : searchpendingentriesDataSet,
		//Documentation: https://datatables.net/reference/option/dom
//"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
//"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"        	
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchpendingentriesTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

