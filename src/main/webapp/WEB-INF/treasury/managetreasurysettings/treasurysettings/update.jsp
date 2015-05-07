<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

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
	<h1><spring:message code="label.manageTreasurySettings.updateTreasurySettings" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;
	<a class="" href="${pageContext.request.contextPath}/treasury/managetreasurysettings/treasurysettings/read" >
		<spring:message code="label.event.back" />
	</a>
|&nbsp;&nbsp;</div>
<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">
		
		<c:forEach items="${infoMessages}" var="message"> 
			<p> <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
						${message}
					</p>
		</c:forEach>
		
	</div>	
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">
		
		<c:forEach items="${warningMessages}" var="message"> 
			<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
						${message}
					</p>
		</c:forEach>
		
	</div>	
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">
		
		<c:forEach items="${errorMessages}" var="message"> 
			<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
						${message}
					</p>
		</c:forEach>
		
	</div>	
</c:if>

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label"><spring:message code="label.TreasurySettings.defaultCurrency"/></div> 
				
				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="treasurySettings_defaultCurrency" class="js-example-basic-single" name="defaultcurrency">
						<option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
					</select>
				</div>
			</div>		

			<div class="form-group row">
				<div class="col-sm-2 control-label"><spring:message code="label.TreasurySettings.defaultVatType"/></div> 
				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="treasurySettings_defaultVatType" class="js-example-basic-single" name="defaultvattype">
						<option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
					</select>
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />"/>
		</div>
	</div>
</form>

<script>
$(document).ready(function() {

	<%-- Block for providing defaultCurrency options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	defaultCurrency_options = [
		<c:forEach items="${TreasurySettings_defaultCurrency_options}" var="element"> 
			{
				text : "<c:out value='${element.name.content}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#treasurySettings_defaultCurrency").select2(
		{
			data : defaultCurrency_options,
		}	  
		    );
		    
		    
    $("#treasurySettings_defaultCurrency").select2().select2('val', '<c:out value='${not empty param.defaultcurrency ? param.defaultcurrency : treasurySettings.defaultCurrency.externalId }'/>');
    <%-- End block for providing defaultCurrency options --%>
	
	
	<%-- Block for providing defaultVatType options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	defaultVatType_options = [
		<c:forEach items="${TreasurySettings_defaultVatType_options}" var="element"> 
			{
				text : "<c:out value='${element.name.content}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#treasurySettings_defaultVatType").select2( {data : defaultVatType_options});	  
		    
	$("#treasurySettings_defaultVatType").select2().select2('val', '<c:out value='${not empty param.defaultvattype ? param.defaultvattype : treasurySettings.defaultVatType.externalId }'/>');
	
});   
	
</script>
