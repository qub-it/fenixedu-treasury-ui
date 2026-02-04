<%@page import="org.fenixedu.treasury.services.accesscontrol.TreasuryAccessControlAPI"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/select/1.2.0/js/dataTables.select.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
<script type="text/javascript" src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
<script type="text/javascript" src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.dataTables.min.css" />


<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css" />

<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%-- ${portal.angularToolkit()} --%>
${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/omnis.js"></script>

<script src="${pageContext.request.contextPath}/webjars/angular-sanitize/1.3.11/angular-sanitize.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.css" />
<script src="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.js"></script>


<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.administration.payments.sibs.managePaymentCodePool.searchPaymentCodePool" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>

<%
    if (TreasuryAccessControlAPI.isManager(org.fenixedu.treasury.util.TreasuryConstants.getAuthenticatedUsername())) {
%>

<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/create"><spring:message
            code="label.event.create" /></a> &nbsp;
</div>

<%
    }
%>

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


<c:choose>
    <c:when test="${not empty searchpaymentcodepoolResultsDataSet}">
        <table id="searchpaymentcodepoolTable"
            class="table responsive table-bordered table-hover" width="100%">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message code="label.PaymentCodePool.finantialInstitution" /></th>
                    <th><spring:message code="label.PaymentCodePool.name" /></th>
                    <th><spring:message code="label.PaymentCodePool.active" /></th> 
                    <th><spring:message code="label.PaymentCodePool.entityReferenceCode" /></th>
                    <th><spring:message code="label.PaymentCodePool.minReferenceCode" /></th>
                    <th><spring:message code="label.PaymentCodePool.maxReferenceCode" /></th>
                    <th><spring:message code="label.PaymentCodePool.validFrom" /></th>
                    <th><spring:message code="label.PaymentCodePool.validTo" /></th>
                    <th><spring:message code="label.PaymentCodePool.useCheckDigit" /></th>
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
	var searchpaymentcodepoolDataSet = [
			<c:forEach items="${searchpaymentcodepoolResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
				"finantialinstitution" : "<c:out value='${searchResult.finantialInstitution.name}'/>",
				"name" : "<c:out value='${searchResult.name}'/>",
				"active" : "<c:if test="${searchResult.active}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.active}"><spring:message code="label.false" /></c:if>",
				"entityreferencecode" : "<c:out value='${searchResult.entityReferenceCode}'/>",
				"minreferencecode" : "<c:out value='${searchResult.minReferenceCode}'/>",
				"maxreferencecode" : "<c:out value='${searchResult.maxReferenceCode}'/>",
				"validfrom" : "<c:out value='${searchResult.validFrom}'/>",
				"validto" : "<c:out value='${searchResult.validTo}'/>",
				 "usecheckdigit" : "<c:if test="${searchResult.useCheckDigit}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.useCheckDigit}"><spring:message code="label.false" /></c:if>",
				 "actions" :
				  " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                 "" 
			},
            </c:forEach>
    ];
	
	$(document).ready(function() {

	<%-- Block for providing finantialInstitution options
	CHANGE_ME INSERT YOUR FORMAT FOR element 
	finantialInstitution_options = [
		<c:forEach items="${finantialInstitutionList}" var="element"> 
			{
				text :"<c:out value='${element.name}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#paymentCodePool_finantialInstitution").select2(
		{
			data : finantialInstitution_options,
		}	  
		    );
		    
		    If it's not from parameter change param.finantialInstitution to whatever you need (it's the externalId already)
		    $("#paymentCodePool_finantialInstitution").select2().select2('val', '<c:out value='${param.finantialInstitution}'/>');
	End block for providing finantialInstitution options --%>
	


		var table = $('#searchpaymentcodepoolTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'finantialinstitution' },
			{ data: 'name' },
            { data: 'active' },
			{ data: 'entityreferencecode' },
			{ data: 'minreferencecode' },
			{ data: 'maxreferencecode' },
			{ data: 'validfrom' },
			{ data: 'validto' },
 			{ data: 'usecheckdigit' },
			{ data: 'actions',className:"all" }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 8 } 
		             ],
		"data" : searchpaymentcodepoolDataSet,
		//Documentation: https://datatables.net/reference/option/dom
        // "dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
        //"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
        //"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        dom: '<"col-sm-5"l><"col-sm-3"f><"col-sm-3"B>rtip', //FilterBox = YES && ExportOptions = YES
        buttons: [
            'copyHtml5',
            'excelHtml5',
            'csvHtml5',
            'pdfHtml5'
        ],
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/swf/copy_csv_xls_pdf.swf"        	
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchpaymentcodepoolTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

