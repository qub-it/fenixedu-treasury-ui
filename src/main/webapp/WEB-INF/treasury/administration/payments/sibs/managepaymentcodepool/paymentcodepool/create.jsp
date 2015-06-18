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

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>



<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message code="label.administration.payments.sibs.managePaymentCodePool.createPaymentCodePool" />
        <small></small>
    </h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
        href="${pageContext.request.contextPath}/treasury/administration/payments/sibs/managepaymentcodepool/paymentcodepool/"><spring:message code="label.event.back" /></a>
    &nbsp;
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

<form method="post" class="form-horizontal">
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.finantialInstitution" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="paymentCodePool_finantialInstitution" class="js-example-basic-single" name="finantialinstitution" required>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.name" />
                </div>

                <div class="col-sm-10">
                    <input id="paymentCodePool_name" class="form-control" type="text" name="name"
                        value='<c:out value='${not empty param.name ? param.name : paymentCodePool.name }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.entityReferenceCode" />
                </div>

                <div class="col-sm-4">
                    <input id="paymentCodePool_entityReferenceCode" class="form-control" type="text" name="entityreferencecode"
                        value='<c:out value='${not empty param.entityreferencecode ? param.entityreferencecode : paymentCodePool.entityReferenceCode }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.minReferenceCode" />
                </div>

                <div class="col-sm-4">
                    <input id="paymentCodePool_minReferenceCode" class="form-control" type="text" name="minreferencecode"
                        value='<c:out value='${not empty param.minreferencecode ? param.minreferencecode : paymentCodePool.minReferenceCode }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.maxReferenceCode" />
                </div>

                <div class="col-sm-4">
                    <input id="paymentCodePool_maxReferenceCode" class="form-control" type="text" name="maxreferencecode"
                        value='<c:out value='${not empty param.maxreferencecode ? param.maxreferencecode : paymentCodePool.maxReferenceCode }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.minAmount" />
                </div>

                <div class="col-sm-4">
                    <input id="paymentCodePool_minAmount" class="form-control" type="text" name="minamount" pattern="[0-9]+(\.[0-9][0-9]?[0-9]?)?"
                        value='<c:out value='${not empty param.minamount ? param.minamount : paymentCodePool.minAmount }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.maxAmount" />
                </div>

                <div class="col-sm-4">
                    <input id="paymentCodePool_maxAmount" class="form-control" type="text" name="maxamount" pattern="[0-9]+(\.[0-9][0-9]?[0-9]?)?"
                        value='<c:out value='${not empty param.maxamount ? param.maxamount : paymentCodePool.maxAmount }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.validFrom" />
                </div>

                <div class="col-sm-4">
                    <input id="paymentCodePool_validFrom" class="form-control" type="text" name="validfrom" bennu-date
                        value='<c:out value='${not empty param.validfrom ? param.validfrom : paymentCodePool.validFrom }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.validTo" />
                </div>

                <div class="col-sm-4">
                    <input id="paymentCodePool_validTo" class="form-control" type="text" name="validto" bennu-date
                        value='<c:out value='${not empty param.validto ? param.validto : paymentCodePool.validTo }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.active" />
                </div>

                <div class="col-sm-2">
                    <select id="paymentCodePool_active" name="active" class="form-control">
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                        <option value="false"><spring:message code="label.no" /></option>
                        <option value="true"><spring:message code="label.yes" /></option>
                    </select>
                    <script>
		$("#paymentCodePool_active").val('<c:out value='${not empty param.active ? param.active : paymentCodePool.active }'/>');
	</script>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.useCheckDigit" />
                </div>

                <div class="col-sm-2">
                    <select id="paymentCodePool_useCheckDigit" name="usecheckdigit" class="form-control">
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                        <option value="false"><spring:message code="label.no" /></option>
                        <option value="true"><spring:message code="label.yes" /></option>
                    </select>
                    <script>
		$("#paymentCodePool_useCheckDigit").val('<c:out value='${not empty param.usecheckdigit ? param.usecheckdigit : paymentCodePool.useCheckDigit }'/>');
	</script>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.useAmountToValidateCheckDigit" />
                </div>

                <div class="col-sm-2">
                    <select id="paymentCodePool_useAmountToValidateCheckDigit" name="useamounttovalidatecheckdigit" class="form-control">
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
                        <option value="false"><spring:message code="label.no" /></option>
                        <option value="true"><spring:message code="label.yes" /></option>
                    </select>
                    <script>
		$("#paymentCodePool_useAmountToValidateCheckDigit").val('<c:out value='${not empty param.useamounttovalidatecheckdigit ? param.useamounttovalidatecheckdigit : paymentCodePool.useAmountToValidateCheckDigit }'/>');
	</script>
                </div>
            </div>

            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.paymentMethod" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="paymentCodePool_paymentMethod" class="js-example-basic-single" name="paymentmethod">
                    </select>
                </div>
            </div>

            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.PaymentCodePool.documentSeriesForPayments" />
                </div>

                <div class="col-sm-4">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="paymentCodePool_documentSeriesForPayments" class="js-example-basic-single" name="documentnumberseries">
                    </select>
                </div>
            </div>



        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />" />
        </div>
    </div>
</form>

<script>
$(document).ready(function() {
    <%-- Block for providing paymentMethod options --%>
    <%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
    paymentMethod_options = [
        <c:forEach items="${PaymentCodePool_paymentMethod_options}" var="element"> 
            {
                text : "<c:out value='${element.name.content}'/>",  
                id : "<c:out value='${element.externalId}'/>"
            },
        </c:forEach>
    ];
    $("#paymentCodePool_paymentMethod").select2(
        {
            data : paymentMethod_options,
        }     
    );
    $("#paymentCodePool_paymentMethod").select2().select2('val', '<c:out value='${param.paymentmethod}'/>');
    <%-- End block for providing paymentMethod options --%>

	<%-- Block for providing finantialInstitution options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	finantialInstitution_options = [
		<c:forEach items="${finantialInstitutionList}" var="element"> 
			{
				text : "<c:out value='${element.name}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	$("#paymentCodePool_finantialInstitution").select2(
		{
			data : finantialInstitution_options,
		}	  
    );
    $("#paymentCodePool_finantialInstitution").select2().select2('val', '<c:out value='${param.finantialinstitution}'/>');
	<%-- End block for providing finantialInstitution options --%>

	
	   <%-- Block for providing documentSeriesForPayments options --%>
	    <%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	    documentSeriesForPayments_options = [
	        <c:forEach items="${PaymentCodePool_documentSeriesForPayments_options}" var="element"> 
	            {
	                text : "<c:out value='${element.series.finantialInstitution.name} - ${element.series.name.content}'/>",  
	                id : "<c:out value='${element.externalId}'/>"
	            },
	        </c:forEach>
	    ];
	    $("#paymentCodePool_documentSeriesForPayments").select2(
	        {
	            data : documentSeriesForPayments_options,
	        }     
	    );
	    $("#paymentCodePool_documentSeriesForPayments").select2().select2('val', '<c:out value='${param.documentseriesforpayments}'/>');
	    <%-- End block for providing documentSeriesForPayments options --%>

	});
</script>
