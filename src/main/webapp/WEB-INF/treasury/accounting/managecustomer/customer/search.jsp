<%@page import="org.fenixedu.treasury.domain.accesscontrol.TreasuryAccessControl"%>
<%@page import="org.fenixedu.treasury.ui.accounting.managecustomer.AdhocCustomerController"%>
<%@page import="org.fenixedu.treasury.ui.accounting.managecustomer.CustomerController"%>
<%@page import="org.fenixedu.treasury.ui.accounting.managecustomer.DebtAccountController"%>
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
        <spring:message code="label.accounting.manageCustomer.searchCustomer" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}<%= AdhocCustomerController.CREATE_URL %>"><spring:message
            code="label.event.create" /></a> &nbsp;
    <%
        if (TreasuryAccessControl.getInstance().isBackOfficeMember()) {
    %>
    | &nbsp; <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class=""
        href="${pageContext.request.contextPath}<%= DebtAccountController.SEARCHOPENDEBTACCOUNTS_URL %>"><spring:message
            code="label.event.accounting.managecustomer.search.debtaccounts.with.pending.values" /></a> &nbsp;
    <%
        }
    %>
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
                    <spring:message code="label.Series.finantialInstitution" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="finantialinstitution" class="js-example-basic-single" name="finantialInstitution">
                        <option value="">&nbsp;</option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                    <script>
            		<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
            		var finantialinstitution_options = [
            			<c:forEach items="${finantialinstitution_options}" var="element"> 
            				{
            					text :"<c:out value='${element.name}'/>", 
            					id : "<c:out value='${element.externalId}'/>"
            				},
            			</c:forEach>
            		];
            
            		//Init Select2_Options
            		initSelect2("#finantialinstitution",finantialinstitution_options, "<c:out value='${param.finantialinstitution}'/>");
            
            		</script>
                </div>

            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.Customer.customerType" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="customer_customerType" class="js-example-basic-single" name="customertype">
                        <option value="">&nbsp;</option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.Customer" />
                </div>

                <div class="col-sm-10">
                    <input id="customer_name" class="form-control" type="text" name="customer" value='<c:out value='${param.customer}'/>' />
                </div>
            </div>
        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.search" />" />
        </div>
    </form>
</div>


<c:if test="${limit_exceeded}">
    <div class="alert alert-warning" role="alert">

        <p>
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
            <spring:message code="label.limitexceeded" arguments="${searchcustomerResultsDataSet.size()};${searchcustomerResultsDataSet_totalCount}" argumentSeparator=";"
                htmlEscape="false" />
        </p>

    </div>
</c:if>
<c:choose>
    <c:when test="${not empty searchcustomerResultsDataSet}">
        <table id="searchcustomerTable" class="display table  table-bordered table-hover responsive" width="100%">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message code="label.Customer.code" /></th>
                    <th><spring:message code="label.Customer.name" /></th>
                    <th><spring:message code="label.Customer.fiscalNumber" /></th>
                    <th><spring:message code="label.Customer.identificationNumber" /></th>
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

	var searchcustomerDataSet = [
			<c:forEach items="${searchcustomerResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
<c:if test='${searchResult.isPersonCustomer() }'>
"code" : "<c:out value='${searchResult.businessIdentification}'/>",
</c:if>
<c:if test='${searchResult.isAdhocCustomer() }'>
"code" : "<c:out value='${searchResult.code}'/>",
</c:if>
"name" : "<c:out value='${searchResult.name}'/>",
"fiscalnumber" : "<c:out value='${searchResult.fiscalNumber}'/>",
"identificationnumber" : "<c:out value='${searchResult.identificationNumber}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" 
			},
            </c:forEach>
    ];
	
	$(document).ready(function() {
		customerType_options = [
		                        <c:forEach items="${Customer_customerType_options}" var="element"> 
		                            {
		                                text :"<c:out value='${element.name.content}'/>", 
		                                id : "<c:out value='${element.externalId}'/>"
		                            },
		                        </c:forEach>
		                    ];
		                    
		                    $("#customer_customerType").select2(
		                        {
		                            data : customerType_options,
		                        }     
		                            );
		                            
		                            <%-- If it's not from parameter change param.productGroup to whatever you need (it's the externalId already) --%>
		                            $("#customer_customerType").select2().select2('val', '<c:out value='${param.customertype}'/>');


		
		var table = $('#searchcustomerTable').DataTable({language : {
			url : "${datatablesI18NUrl}",	
			responsive: true
		},
		
		"columns": [
			{ data: 'code' },
			{ data: 'name' },
			{ data: 'fiscalnumber' },
			{ data: 'identificationnumber' },
			{ data: 'actions',className:"all" }
			
		],
		"order": [[ 1, "asc" ]],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 4 } 
		             ],
		"data" : searchcustomerDataSet,
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
		
		  $('#searchcustomerTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );

			$("#finantialinstitution").select2().select2('val', '${param.finantialInstitution}');
		  
	}); 
</script>

