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
	<h1>
		<spring:message
			code="label.administration.document.manageFinantialDocumentType.createFinantialDocumentType" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
		class=""
		href="${pageContext.request.contextPath}/treasury/administration/document/managefinantialdocumenttype/finantialdocumenttype/"><spring:message
			code="label.event.back" /></a> |&nbsp;&nbsp;
</div>
<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">

		<c:forEach items="${warningMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p>${message}</p>
		</c:forEach>

	</div>
</c:if>

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialDocumentType.type" />
				</div>

				<div class="col-sm-4">
					<select id="finantialDocumentType_type" class="form-control"
						name="type" required>
						<option value=""></option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME--%>
						<c:forEach items="${typeValues}" var="field">
							<option value='<c:out value='${field}'/>'><c:out
									value='${field}' /></option>
						</c:forEach>
					</select>
					<script>
		$("#finantialDocumentType_type").val('<c:out value='${not empty param.type ? param.type : finantialDocumentType.type }'/>');
	</script>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialDocumentType.code" />
				</div>

				<div class="col-sm-10">
					<input id="finantialDocumentType_code" class="form-control"
						type="text" name="code"
						value='<c:out value='${not empty param.code ? param.code : finantialDocumentType.code }'/>'
						required />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialDocumentType.name" />
				</div>

				<div class="col-sm-10">
					<input id="finantialDocumentType_name" class="form-control"
						type="text" name="name" bennu-localized-string
						value='${not empty param.name ? param.name : "{}" } ' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message
						code="label.FinantialDocumentType.documentNumberSeriesPrefix" />
				</div>

				<div class="col-sm-10">
					<input id="finantialDocumentType_documentNumberSeriesPrefix"
						class="form-control" type="text" name="documentnumberseriesprefix"
						value='<c:out value='${not empty param.documentnumberseriesprefix ? param.documentnumberseriesprefix : finantialDocumentType.documentNumberSeriesPrefix }'/>' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.FinantialDocumentType.invoice" />
				</div>

				<div class="col-sm-2">
					<select id="finantialDocumentType_invoice" name="invoice"
						class="form-control">
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
		$("#finantialDocumentType_invoice").val('<c:out value='${not empty param.invoice ? param.invoice : finantialDocumentType.invoice }'/>');
	</script>
				</div>
			</div>
			<%--	
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.FinantialDocumentType.bennu"/></div> 

<div class="col-sm-4">
			 <select id="finantialDocumentType_bennu" class="js-example-basic-single" name="bennu">
		 <option value=""></option>  
		</select>
				</div>
</div>	
--%>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button"
				value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<script>
$(document).ready(function() {

		<%-- Block for providing bennu options --%>
		<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
		bennu_options = [
			<c:forEach items="${FinantialDocumentType_bennu_options}" var="element"> 
				{
					text : "<c:out value='${element}'/>",  
					id : "<c:out value='${element.externalId}'/>"
				},
			</c:forEach>
		];
		
		$("#finantialDocumentType_bennu").select2(
			{
				data : bennu_options,
			}	  
	    );
	    
	    
	    
	    $("#finantialDocumentType_bennu").select2().select2('val', '<c:out value='${param.bennu}'/>');
	
		<%-- End block for providing bennu options --%>
	
	
	});
</script>
