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
            code="label.administration.payments.sibs.managePaymentCodePool.searchPaymentCodePool" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/create"><spring:message
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

<!-- 
<div class="panel panel-default">
    <form method="get" class="form-horizontal">
        <div class="panel-body">

            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.PaymentCodePool.finantialInstitution" />
                </div>

                <div class="col-sm-4">
                    <select id="paymentCodePool_finantialInstitution"
                        class="js-example-basic-single"
                        name="finantialinstitution">
                        <option value="">&nbsp;</option>
                    </select>
                </div>
            </div>
            
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.name"/></div> 
<div class="col-sm-10">
	<input id="paymentCodePool_name" class="form-control" type="text" name="name"  value='<c:out value='${not empty param.name ? param.name : paymentCodePool.name }'/>' />
</div>	
</div>	
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.entityReferenceCode"/></div> 
<div class="col-sm-10">
	<input id="paymentCodePool_entityReferenceCode" class="form-control" type="text" name="entityreferencecode"  value='<c:out value='${not empty param.entityreferencecode ? param.entityreferencecode : paymentCodePool.entityReferenceCode }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.minReferenceCode"/></div> 
<div class="col-sm-10">
	<input id="paymentCodePool_minReferenceCode" class="form-control" type="text" name="minreferencecode"  value='<c:out value='${not empty param.minreferencecode ? param.minreferencecode : paymentCodePool.minReferenceCode }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.maxReferenceCode"/></div> 
<div class="col-sm-10">
	<input id="paymentCodePool_maxReferenceCode" class="form-control" type="text" name="maxreferencecode"  value='<c:out value='${not empty param.maxreferencecode ? param.maxreferencecode : paymentCodePool.maxReferenceCode }'/>' />
</div>	
</div>	
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.minAmount"/></div> 

<div class="col-sm-10">
	<input id="paymentCodePool_minAmount" class="form-control" type="text" name="minamount"  value='<c:out value='${not empty param.minamount ? param.minamount : paymentCodePool.minAmount }'/>' />
</div>	
</div>	 
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.maxAmount"/></div> 

<div class="col-sm-10">
	<input id="paymentCodePool_maxAmount" class="form-control" type="text" name="maxamount"  value='<c:out value='${not empty param.maxamount ? param.maxamount : paymentCodePool.maxAmount }'/>' />
</div>	
</div>	
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.validFrom"/></div> 

<div class="col-sm-4">
	<input id="paymentCodePool_validFrom" class="form-control" type="text" name="validfrom"  bennu-datetime value = '<c:out value='${not empty param.validfrom ? param.validfrom : paymentCodePool.validFrom }'/>' />
</div>
</div>	
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.validTo"/></div> 

<div class="col-sm-4">
	<input id="paymentCodePool_validTo" class="form-control" type="text" name="validto"  bennu-datetime value = '<c:out value='${not empty param.validto ? param.validto : paymentCodePool.validTo }'/>' />
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.active"/></div> 

<div class="col-sm-2">
<select id="paymentCodePool_active" name="active" class="form-control">
<option value="">&nbsp;</option> 
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#paymentCodePool_active").val('<c:out value='${not empty param.active ? param.active : paymentCodePool.active }'/>');
	</script>	
</div>
</div>	
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.PaymentCodePool.useCheckDigit"/></div> 

<div class="col-sm-2">
<select id="paymentCodePool_useCheckDigit" name="usecheckdigit" class="form-control">
<option value="">&nbsp;</option> 
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>$("#paymentCodePool_useCheckDigit").val('<c:out value='${not empty param.usecheckdigit ? param.usecheckdigit : paymentCodePool.useCheckDigit }'/>');</script>	
</div>
</div> 	
</div>
        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button"
                value="<spring:message code="label.search" />" />
        </div>
    </form>
</div>
-->

<c:choose>
    <c:when test="${not empty searchpaymentcodepoolResultsDataSet}">
        <table id="searchpaymentcodepoolTable"
            class="table responsive table-bordered table-hover" width="100%">
            <thead>
                <tr>
                    <%--!!!  Field names here --%>
                    <th><spring:message
                            code="label.PaymentCodePool.finantialInstitution" /></th>
                    <th><spring:message
                            code="label.PaymentCodePool.name" /></th>
                    <th><spring:message
                             code="label.PaymentCodePool.active" /></th> 
                    <th><spring:message
                            code="label.PaymentCodePool.entityReferenceCode" /></th>
                    <th><spring:message
                            code="label.PaymentCodePool.minReferenceCode" /></th>
                    <th><spring:message
                            code="label.PaymentCodePool.maxReferenceCode" /></th>
                    <th><spring:message
                            code="label.PaymentCodePool.validFrom" /></th>
                    <th><spring:message
                            code="label.PaymentCodePool.validTo" /></th>
<%--                     <th><spring:message --%>
<%--                             code="label.PaymentCodePool.useCheckDigit" /></th> --%>
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
"entityreferencecode" : "<c:out value='${searchResult.entityReferenceCode}'/>",
"minreferencecode" : "<c:out value='${searchResult.minReferenceCode}'/>",
"maxreferencecode" : "<c:out value='${searchResult.maxReferenceCode}'/>",
"validfrom" : "<c:out value='${searchResult.validFrom}'/>",
"validto" : "<c:out value='${searchResult.validTo}'/>",
 "active" : "<c:if test="${searchResult.active}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.active}"><spring:message code="label.false" /></c:if>",
// "usecheckdigit" : "<c:if test="${searchResult.useCheckDigit}"><spring:message code="label.true" /></c:if><c:if test="${not searchResult.useCheckDigit}"><spring:message code="label.false" /></c:if>",
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
// 			{ data: 'usecheckdigit' },
			{ data: 'actions',className:"all" }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 8 } 
		             ],
		"data" : searchpaymentcodepoolDataSet,
		//Documentation: https://datatables.net/reference/option/dom
"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
// "dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
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

