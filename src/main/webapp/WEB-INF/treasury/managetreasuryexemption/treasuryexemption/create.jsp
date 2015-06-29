<%@page import="org.fenixedu.treasury.ui.accounting.managecustomer.TreasuryEventController"%>
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
			code="label.manageTreasuryExemption.createTreasuryExemption" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
	&nbsp;<a
		href="${pageContext.request.contextPath}<%= TreasuryEventController.READ_URL %>/${treasuryEvent.externalId}">
			<spring:message code="label.event.back" />
		</a> 
	&nbsp;|&nbsp;
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

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.TreasuryExemption.treasuryExemptionType" />
				</div>

				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="treasuryExemption_treasuryExemptionType"
						class="js-example-basic-single" name="treasuryexemptiontype">
						<option value="">&nbsp;</option>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.TreasuryExemption.exemptByPercentage" />
				</div>

				<div class="col-sm-2">
					<select id="treasuryExemption_exemptByPercentage"
						name="exemptbypercentage" class="form-control">
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
						$("#treasuryExemption_exemptByPercentage").select2().select2('val', '<c:out value='${not empty param.exemptbypercentage ? param.exemptbypercentage : treasuryExemption.exemptByPercentage }'/>');
					</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.TreasuryExemption.valueToExempt" />
				</div>

				<div class="col-sm-10">
					<input id="treasuryExemption_valueToExempt" class="form-control"
						type="text" name="valuetoexempt"
						value='<c:out value='${not empty param.valuetoexempt ? param.valuetoexempt : treasuryExemption.valueToExempt }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.TreasuryExemption.product" />
				</div>

				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="treasuryExemption_product"
						class="js-example-basic-single" name="product">
						<option value="">&nbsp;</option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.TreasuryExemption.reason" />
				</div>

				<div class="col-sm-10">
					<input id="treasuryExemption_reason" class="form-control"
						type="text" name="reason"
						value='<c:out value='${not empty param.reason ? param.reason : treasuryExemption.reason }'/>' />
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button"
				value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<script>
$(document).ready(function() {

	<%-- Block for providing treasuryExemptionType options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	treasuryExemptionType_options = [
		<c:forEach items="${TreasuryExemption_treasuryExemptionType_options}" var="element"> 
			{
				text : "<c:out value='${element.name.content}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#treasuryExemption_treasuryExemptionType").select2(
		{
			data : treasuryExemptionType_options,
		}	  
    );
    
    
    
    $("#treasuryExemption_treasuryExemptionType").select2().select2('val', '<c:out value='${param.treasuryexemptiontype}'/>');

	<%-- End block for providing treasuryExemptionType options --%>
	<%-- Block for providing product options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	product_options = [
		<c:forEach items="${TreasuryExemption_product_options}" var="element"> 
			{
				text : "<c:out value='${element.name.content}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#treasuryExemption_product").select2(
		{
			data : product_options,
		}	  
    );
    
    
    
    $("#treasuryExemption_product").select2().select2('val', '<c:out value='${param.product}'/>');

	<%-- End block for providing product options --%>

	});
</script>
