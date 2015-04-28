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
	<h1><spring:message code="label.document.manageInvoice.searchDebitNote" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/create"   ><spring:message code="label.event.create" /></a>
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

<div class="panel panel-default">
<form method="get" class="form-horizontal">
<div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.payorDebtAccount"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="debitNote_payorDebtAccount" class="js-example-basic-single" name="payordebtaccount">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.finantialDocumentType"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="debitNote_finantialDocumentType" class="js-example-basic-single" name="finantialdocumenttype">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.debtAccount"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="debitNote_debtAccount" class="js-example-basic-single" name="debtaccount">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.documentNumberSeries"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="debitNote_documentNumberSeries" class="js-example-basic-single" name="documentnumberseries">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.currency"/></div> 

<div class="col-sm-4">
	<%-- Relation to side 1 drop down rendered in input --%>
		 <select id="debitNote_currency" class="js-example-basic-single" name="currency">
		 <option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%> 
		</select>
				</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.documentNumber"/></div> 

<div class="col-sm-10">
	<input id="debitNote_documentNumber" class="form-control" type="text" name="documentnumber"  value='<c:out value='${not empty param.documentnumber ? param.documentnumber : debitNote.documentNumber }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.documentDate"/></div> 

<div class="col-sm-4">
	<input id="debitNote_documentDate" class="form-control" type="text" name="documentdate"  bennu-datetime 
	value = '<c:out value='${not empty param.documentdate ? param.documentdate : debitNote.documentDate }'/>' />
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.documentDueDate"/></div> 

<div class="col-sm-4">
	<input id="debitNote_documentDueDate" class="form-control" type="text" name="documentduedate"  bennu-datetime 
	value = '<c:out value='${not empty param.documentduedate ? param.documentduedate : debitNote.documentDueDate }'/>' />
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.originDocumentNumber"/></div> 

<div class="col-sm-10">
	<input id="debitNote_originDocumentNumber" class="form-control" type="text" name="origindocumentnumber"  value='<c:out value='${not empty param.origindocumentnumber ? param.origindocumentnumber : debitNote.originDocumentNumber }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.DebitNote.state"/></div> 

<div class="col-sm-4">
	<select id="debitNote_state" class="form-control" name="state">
		<option value=""></option> <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME--%>
		<c:forEach items="${stateValues}" var="field">
			<option value='<c:out value='${field}'/>'><c:out value='${field}'/></option>
		</c:forEach>
	</select>
	<script>
		$("#debitNote_state").val('<c:out value='${not empty param.state ? param.state : debitNote.state }'/>');
	</script>	
</div>
</div>		
</div>
<div class="panel-footer">
	<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.search" />"/>
</div>
</form>
</div>


<c:choose>
	<c:when test="${not empty searchdebitnoteResultsDataSet}">
		<table id="searchdebitnoteTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.DebitNote.payorDebtAccount"/></th>
<th><spring:message code="label.DebitNote.finantialDocumentType"/></th>
<th><spring:message code="label.DebitNote.debtAccount"/></th>
<th><spring:message code="label.DebitNote.documentNumberSeries"/></th>
<th><spring:message code="label.DebitNote.currency"/></th>
<th><spring:message code="label.DebitNote.documentNumber"/></th>
<th><spring:message code="label.DebitNote.documentDate"/></th>
<th><spring:message code="label.DebitNote.documentDueDate"/></th>
<th><spring:message code="label.DebitNote.originDocumentNumber"/></th>
<th><spring:message code="label.DebitNote.state"/></th>
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
					
					<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>			<spring:message code="label.noResultsFound" /></p>
					
				</div>	
		
	</c:otherwise>
</c:choose>

<script>
	var searchdebitnoteDataSet = [
			<c:forEach items="${searchdebitnoteResultsDataSet}" var="searchResult">
				<%-- Field access / formatting  here CHANGE_ME --%>
				{
				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
"payordebtaccount" : "<c:out value='${searchResult.payorDebtAccount}'/>",
"finantialdocumenttype" : "<c:out value='${searchResult.finantialDocumentType}'/>",
"debtaccount" : "<c:out value='${searchResult.debtAccount}'/>",
"documentnumberseries" : "<c:out value='${searchResult.documentNumberSeries}'/>",
"currency" : "<c:out value='${searchResult.currency}'/>",
"documentnumber" : "<c:out value='${searchResult.documentNumber}'/>",
"documentdate" : "<c:out value='${searchResult.documentDate}'/>",
"documentduedate" : "<c:out value='${searchResult.documentDueDate}'/>",
"origindocumentnumber" : "<c:out value='${searchResult.originDocumentNumber}'/>",
"state" : "<c:out value='${searchResult.state}'/>",
"actions" :
" <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
                "" },
            </c:forEach>
    ];
	
	$(document).ready(function() {

	<%-- Block for providing payorDebtAccount options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	payorDebtAccount_options = [
		<c:forEach items="${DebitNote_payorDebtAccount_options}" var="element"> 
			{
				text :"<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#debitNote_payorDebtAccount").select2(
		{
			data : payorDebtAccount_options,
		}	  
		    );
		    
		    <%-- If it's not from parameter change param.payorDebtAccount to whatever you need (it's the externalId already) --%>
		    $("#debitNote_payorDebtAccount").select2().select2('val', '<c:out value='${param.payorDebtAccount}'/>');
	<%-- End block for providing payorDebtAccount options --%>
	<%-- Block for providing finantialDocumentType options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	finantialDocumentType_options = [
		<c:forEach items="${DebitNote_finantialDocumentType_options}" var="element"> 
			{
				text :"<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#debitNote_finantialDocumentType").select2(
		{
			data : finantialDocumentType_options,
		}	  
		    );
		    
		    <%-- If it's not from parameter change param.finantialDocumentType to whatever you need (it's the externalId already) --%>
		    $("#debitNote_finantialDocumentType").select2().select2('val', '<c:out value='${param.finantialDocumentType}'/>');
	<%-- End block for providing finantialDocumentType options --%>
	<%-- Block for providing debtAccount options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	debtAccount_options = [
		<c:forEach items="${DebitNote_debtAccount_options}" var="element"> 
			{
				text :"<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#debitNote_debtAccount").select2(
		{
			data : debtAccount_options,
		}	  
		    );
		    
		    <%-- If it's not from parameter change param.debtAccount to whatever you need (it's the externalId already) --%>
		    $("#debitNote_debtAccount").select2().select2('val', '<c:out value='${param.debtAccount}'/>');
	<%-- End block for providing debtAccount options --%>
	<%-- Block for providing documentNumberSeries options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	documentNumberSeries_options = [
		<c:forEach items="${DebitNote_documentNumberSeries_options}" var="element"> 
			{
				text :"<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#debitNote_documentNumberSeries").select2(
		{
			data : documentNumberSeries_options,
		}	  
		    );
		    
		    <%-- If it's not from parameter change param.documentNumberSeries to whatever you need (it's the externalId already) --%>
		    $("#debitNote_documentNumberSeries").select2().select2('val', '<c:out value='${param.documentNumberSeries}'/>');
	<%-- End block for providing documentNumberSeries options --%>
	<%-- Block for providing currency options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	currency_options = [
		<c:forEach items="${DebitNote_currency_options}" var="element"> 
			{
				text :"<c:out value='${element}'/>", 
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#debitNote_currency").select2(
		{
			data : currency_options,
		}	  
		    );
		    
		    <%-- If it's not from parameter change param.currency to whatever you need (it's the externalId already) --%>
		    $("#debitNote_currency").select2().select2('val', '<c:out value='${param.currency}'/>');
	<%-- End block for providing currency options --%>
	


		var table = $('#searchdebitnoteTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'payordebtaccount' },
			{ data: 'finantialdocumenttype' },
			{ data: 'debtaccount' },
			{ data: 'documentnumberseries' },
			{ data: 'currency' },
			{ data: 'documentnumber' },
			{ data: 'documentdate' },
			{ data: 'documentduedate' },
			{ data: 'origindocumentnumber' },
			{ data: 'state' },
			{ data: 'actions' }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 10 } 
		             ],
		"data" : searchdebitnoteDataSet,
		//Documentation: https://datatables.net/reference/option/dom
//"dom": '<"col-sm-6"l><"col-sm-3"f><"col-sm-3"T>rtip', //FilterBox = YES && ExportOptions = YES
"dom": 'T<"clear">lrtip', //FilterBox = NO && ExportOptions = YES
//"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
//"dom": '<"col-sm-6"l>rtip', // FilterBox = NO && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/static/treasury/swf/copy_csv_xls_pdf.swf"
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchdebitnoteTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

