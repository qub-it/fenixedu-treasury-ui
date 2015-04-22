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

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link href="//cdn.datatables.net/responsive/1.0.4/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="//cdn.datatables.net/responsive/1.0.4/js/dataTables.responsive.js"></script>
<link href="//cdn.datatables.net/tabletools/2.2.3/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="//cdn.datatables.net/tabletools/2.2.3/js/dataTables.tableTools.min.js"></script>
<link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/css/select2.min.css" rel="stylesheet" />
<script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/js/select2.min.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/bootbox.min.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.document.manageInvoice.updateCreditNote" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/creditnote/read/${creditNote.externalId}" ><spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;</div>
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
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.debitNote"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="creditNote_debitNote" class="js-example-basic-single" name="debitnote">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.payorDebtAccount"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="creditNote_payorDebtAccount" class="js-example-basic-single" name="payordebtaccount">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.finantialDocumentType"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="creditNote_finantialDocumentType" class="js-example-basic-single" name="finantialdocumenttype">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.debtAccount"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="creditNote_debtAccount" class="js-example-basic-single" name="debtaccount">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.documentNumberSeries"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="creditNote_documentNumberSeries" class="js-example-basic-single" name="documentnumberseries">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.currency"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="creditNote_currency" class="js-example-basic-single" name="currency">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.documentNumber"/></div> 

<div class="col-sm-10">
	<input id="creditNote_documentNumber" class="form-control" type="text" name="documentnumber"  value='<c:out value='${not empty param.documentnumber ? param.documentnumber : creditNote.documentNumber }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.documentDate"/></div> 

<div class="col-sm-4">
	<input id="creditNote_documentDate" class="form-control" type="text" name="documentdate"  bennu-datetime 
	value = '<c:out value='${not empty param.documentdate ? param.documentdate : creditNote.documentDate }'/>' />
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.documentDueDate"/></div> 

<div class="col-sm-4">
	<input id="creditNote_documentDueDate" class="form-control" type="text" name="documentduedate"  bennu-datetime 
	value = '<c:out value='${not empty param.documentduedate ? param.documentduedate : creditNote.documentDueDate }'/>' />
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.originDocumentNumber"/></div> 

<div class="col-sm-10">
	<input id="creditNote_originDocumentNumber" class="form-control" type="text" name="origindocumentnumber"  value='<c:out value='${not empty param.origindocumentnumber ? param.origindocumentnumber : creditNote.originDocumentNumber }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.CreditNote.state"/></div> 

<div class="col-sm-4">
	<select id="creditNote_state" class="form-control" name="state">
		<option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME--%>
		<c:forEach items="${stateValues}" var="field">
			<option value='<c:out value='${field}'/>'><c:out value='${field}'/></option>
		</c:forEach>
	</select>
	<script>
		$("#creditNote_state").val('<c:out value='${not empty param.state ? param.state : creditNote.state }'/>');
	</script>	
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

	<%-- Block for providing debitNote options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	debitNote_options = [
		<c:forEach items="${CreditNote_debitNote_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#creditNote_debitNote").select2(
		{
			data : debitNote_options,
		}	  
		    );
		    
		    
		    $("#creditNote_debitNote").select2().select2('val', '<c:out value='${not empty param.debitnote ? param.debitnote : creditNote.debitNote.externalId }'/>');
		    <%-- End block for providing debitNote options --%>
	<%-- Block for providing payorDebtAccount options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	payorDebtAccount_options = [
		<c:forEach items="${CreditNote_payorDebtAccount_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#creditNote_payorDebtAccount").select2(
		{
			data : payorDebtAccount_options,
		}	  
		    );
		    
		    
		    $("#creditNote_payorDebtAccount").select2().select2('val', '<c:out value='${not empty param.payordebtaccount ? param.payordebtaccount : creditNote.payorDebtAccount.externalId }'/>');
		    <%-- End block for providing payorDebtAccount options --%>
	<%-- Block for providing finantialDocumentType options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	finantialDocumentType_options = [
		<c:forEach items="${CreditNote_finantialDocumentType_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#creditNote_finantialDocumentType").select2(
		{
			data : finantialDocumentType_options,
		}	  
		    );
		    
		    
		    $("#creditNote_finantialDocumentType").select2().select2('val', '<c:out value='${not empty param.finantialdocumenttype ? param.finantialdocumenttype : creditNote.finantialDocumentType.externalId }'/>');
		    <%-- End block for providing finantialDocumentType options --%>
	<%-- Block for providing debtAccount options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	debtAccount_options = [
		<c:forEach items="${CreditNote_debtAccount_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#creditNote_debtAccount").select2(
		{
			data : debtAccount_options,
		}	  
		    );
		    
		    
		    $("#creditNote_debtAccount").select2().select2('val', '<c:out value='${not empty param.debtaccount ? param.debtaccount : creditNote.debtAccount.externalId }'/>');
		    <%-- End block for providing debtAccount options --%>
	<%-- Block for providing documentNumberSeries options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	documentNumberSeries_options = [
		<c:forEach items="${CreditNote_documentNumberSeries_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#creditNote_documentNumberSeries").select2(
		{
			data : documentNumberSeries_options,
		}	  
		    );
		    
		    
		    $("#creditNote_documentNumberSeries").select2().select2('val', '<c:out value='${not empty param.documentnumberseries ? param.documentnumberseries : creditNote.documentNumberSeries.externalId }'/>');
		    <%-- End block for providing documentNumberSeries options --%>
	<%-- Block for providing currency options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	currency_options = [
		<c:forEach items="${CreditNote_currency_options}" var="element"> 
			{
				text : "<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#creditNote_currency").select2(
		{
			data : currency_options,
		}	  
		    );
		    
		    
		    $("#creditNote_currency").select2().select2('val', '<c:out value='${not empty param.currency ? param.currency : creditNote.currency.externalId }'/>');
		    <%-- End block for providing currency options --%>
	
	
	});
</script>
