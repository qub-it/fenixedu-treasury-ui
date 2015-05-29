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

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>						
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message
			code="label.administration.document.manageDocumentNumberSeries.createDocumentNumberSeries" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
		class=""
		href="${pageContext.request.contextPath}/treasury/administration/document/managedocumentnumberseries/documentnumberseries/"><spring:message
			code="label.event.back" /></a> &nbsp;|&nbsp;
</div>
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
				<div class="col-sm-2 control-label">
					<spring:message code="label.DocumentNumberSeries.counter" />
				</div>

				<div class="col-sm-10">
					<input id="documentNumberSeries_counter" class="form-control"
						type="text" name="counter"
						value='<c:out value='${not empty param.counter ? param.counter : documentNumberSeries.counter }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DocumentNumberSeries.series" />
				</div>

				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="documentNumberSeries_series"
						class="js-example-basic-single" name="series">
						<option value=""></option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.DocumentNumberSeries.finantialDocumentType" />
				</div>

				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="documentNumberSeries_finantialDocumentType"
						class="js-example-basic-single" name="finantialdocumenttype">
						<option value=""></option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
					</select>
				</div>
			</div>
			<%-- 
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.DocumentNumberSeries.bennu" />
				</div>
				<div class="col-sm-4">					
					<select id="documentNumberSeries_bennu"
						class="js-example-basic-single" name="bennu">
						<option value=""></option>
						
					</select>
				</div>
			 --%>

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

		<%-- Block for providing series options --%>
		<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
		series_options = [
			<c:forEach items="${DocumentNumberSeries_series_options}" var="element"> 
				{
					text : "<c:out value='${element.name}'/>",  
					id : "<c:out value='${element.externalId}'/>"
				},
			</c:forEach>
		];
		
		$("#documentNumberSeries_series").select2(
			{
				data : series_options,
			}	  
	    );
	    
	    
	    
	    $("#documentNumberSeries_series").select2().select2('val', '<c:out value='${param.series}'/>');
	
		<%-- End block for providing series options --%>
		<%-- Block for providing finantialDocumentType options --%>
		<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
		finantialDocumentType_options = [
			<c:forEach items="${DocumentNumberSeries_finantialDocumentType_options}" var="element"> 
				{
					text : "<c:out value='${element.name}'/>",  
					id : "<c:out value='${element.externalId}'/>"
				},
			</c:forEach>
		];
		
		$("#documentNumberSeries_finantialDocumentType").select2(
			{
				data : finantialDocumentType_options,
			}	  
	    );
	    
	    
	    
	    $("#documentNumberSeries_finantialDocumentType").select2().select2('val', '<c:out value='${param.finantialdocumenttype}'/>');
	
		<%-- End block for providing bennu options --%>
	
	
	});
</script>
