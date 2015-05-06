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
	<h1><spring:message code="label.accounting.manageCustomer.readCustomer" />
	</h1>
		<small></small>
</div>
<div class="modal fade" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">
    <form id ="deleteForm" action="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/delete/${customer.externalId}"   method="POST">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><spring:message code="label.confirmation"/></h4>
      </div>
      <div class="modal-body">
        <p><spring:message code = "label.accounting.manageCustomer.readCustomer.confirmDelete"/></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "label.close"/></button>
        <button id="deleteButton" class ="btn btn-danger" type="submit"> <spring:message code = "label.delete"/></button>
      </div>
      </form>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/"  ><spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/adhoccustomer/update/${customer.externalId}"  ><spring:message code="label.event.update" /></a>
|&nbsp;&nbsp;	<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/read/${customer.externalId}/createpayment"  ><spring:message code="label.event.accounting.manageCustomer.createPayment" /></a>	&nbsp;|&nbsp;
	<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/read/${customer.externalId}/createdebtentry"  ><spring:message code="label.event.accounting.manageCustomer.createDebtEntry" /></a>	&nbsp;|&nbsp;
	<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/read/${customer.externalId}/createexemption"  ><spring:message code="label.event.accounting.manageCustomer.createExemption" /></a>	
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

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title"><spring:message code="label.details"/></h3>
	</div>
	<div class="panel-body">
<form method="post" class="form-horizontal">
<table class="table">
		<tbody>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.Customer.code"/></th> 
	<td>
		<c:out value='${customer.code}'/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.Customer.name"/></th> 
	<td>
		<c:out value='${customer.name}'/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.Customer.fiscalNumber"/></th> 
	<td>
		<c:out value='${customer.fiscalNumber}'/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.Customer.identificationNumber"/></th> 
	<td>
		<c:out value='${customer.identificationNumber}'/>
	</td> 
</tr>
</tbody>
</table>

</form>
</div>
</div>
</br>
<h2> Conta Corrente </h2>
 <div id="content">
    <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">

        <li class="active"><a href="#pending" data-toggle="tab">Docs. Pendentes</a></li>
        <li><a href="#details" data-toggle="tab">Extracto</a></li>
        <li><a href="#payments" data-toggle="tab">Pagamentos</a></li>
        <li><a href="#exemptions" data-toggle="tab">Isencoes</a></li>
    </ul>
    <div id="my-tab-content" class="tab-content">
        <div class="tab-pane active" id="pending">
<!--             <h3>Docs. Pendentes</h3> -->
<c:choose>
	<c:when test="${not empty pendingDocumentsDataSet}">
		<table id="pendingDocumentsTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
				
					<%--!!!  Field names here --%>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.debtItems"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.dueDate"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.debitAmount"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.creditAmount"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.pendingAmount"/></th>
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
        </div>
        <div class="tab-pane" id="details">
<!--             <h3>Extracto</h3> -->
<c:choose>
	<c:when test="${not empty allDocumentsDataSet}">
		<table id="allDocumentsTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.date"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.documentNumber"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.documentDescription"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.debitAmount"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.creditAmount"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.balance"/></th>
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
        </div>
        <div class="tab-pane" id="payments">
<!--             <h3>Pagamentos</h3> -->
<c:choose>
	<c:when test="${not empty paymentsDataSet}">
		<table id="paymentsTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.debtItems"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.dueDate"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.debitAmount"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.creditAmount"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.pendingAmount"/></th>
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
        </div>
        <div class="tab-pane" id="exemptions">
<!--             <h3>Isencoes</h3> -->
<c:choose>
	<c:when test="${not empty exemptionsDataSet}">
		<table id="exemptionsTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.debtItems"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.dueDate"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.debitAmount"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.creditAmount"/></th>
<th><spring:message code="label.accounting.manageCustomer.readCustomer.pendingAmount"/></th>
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
        </div>
    </div>
</div>

<script>

var pendingDocumentsDataSet = [
	                 			<c:forEach items="${pendingDocumentsDataSet}" var="searchResult">
	                 				<%-- Field access / formatting  here CHANGE_ME --%>
	                 				{
	                 				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
	                 "document" : "<c:out value='${searchResult.code}'/>",
	                 "debtItems" : "<c:out value='${searchResult.name}'/>",
	                 "dueDate" : "<c:out value='${searchResult.fiscalNumber}'/>",
	                 "debitAmount" : "<c:out value='${searchResult.identificationNumber}'/>",
	                 "creditAmount" : "<c:out value='${searchResult.identificationNumber}'/>",
	                 "pendingAmount" : "<c:out value='${searchResult.identificationNumber}'/>",
// 	                 "" :
// 	                 " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
// 	                                 "" 
// 	                 			},
	                             </c:forEach>
	                     ];

	var allDocumentsDataSet = [
		                 			<c:forEach items="${allDocumentsDataSet}" var="searchResult">
		                 				<%-- Field access / formatting  here CHANGE_ME --%>
		                 				{
		                 				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
		                 "date" : "<c:out value='${searchResult.date}'/>",
		                 "documentNumber" : "<c:out value='${searchResult.document.date}'/>",
		                 "description" : "<c:out value='${searchResult.description}'/>",
		                 "debitAmount" : "<c:out value='${searchResult.debitAmount}'/>",
		                 "creditAmount" : "<c:out value='${searchResult.creditAmount}'/>",
		                 "balance" : "<c:out value='${searchResult.balance}'/>",
// 		                 "actions" :
// 		                 " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
// 		                                 "" 
// 		                 			},
		                             </c:forEach>
		                     ];
	var paymentsDataSet = [
		                 			<c:forEach items="${paymentsDataSet}" var="searchResult">
		                 				<%-- Field access / formatting  here CHANGE_ME --%>
		                 				{
		                 				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
		                 "date" : "<c:out value='${searchResult.code}'/>",
		                 "document" : "<c:out value='${searchResult.name}'/>",
		                 "description" : "<c:out value='${searchResult.name}'/>",
		                 "settlementAmount" : "<c:out value='${searchResult.fiscalNumber}'/>",
		                 "actions" :
		                 " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
		                                 "" 
		                 			},
		                             </c:forEach>
		                     ];

	var exemptionsDataSet = [
		                 			<c:forEach items="${exemptionsDataSet}" var="searchResult">
		                 				<%-- Field access / formatting  here CHANGE_ME --%>
		                 				{
		                 				"DT_RowId" : '<c:out value='${searchResult.externalId}'/>',
		                 "description" : "<c:out value='${searchResult.code}'/>",
		                 "exemption" : "<c:out value='${searchResult.name}'/>",
		                 "reason" : "<c:out value='${searchResult.fiscalNumber}'/>",
		                 "totalAmount" : "<c:out value='${searchResult.identificationNumber}'/>",
		                 "exemptionAmount" : "<c:out value='${searchResult.identificationNumber}'/>",
		                 "finalAmount" : "<c:out value='${searchResult.identificationNumber}'/>",
		                 "actions" :
		                 " <a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/treasury/accounting/managecustomer/customer/search/view/${searchResult.externalId}\"><spring:message code='label.view'/></a>" +
		                                 "" 
		                 			},
		                             </c:forEach>
		                     ];



$(document).ready(function() {

	//Enable Bootstrap Tabs
	 $('#tabs').tab();

	 var table = $('#pendingDocumentsTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'document' },
			{ data: 'debtItems' },
			{ data: 'dueDate' },
			{ data: 'debitAmount' },
			{ data: 'creditAmount' },
			{ data: 'pendingAmount' },
			{ data: 'actions' }			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 4 } 
		             ],
		"data" : pendingDocumentsDataSet,
"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
        "tableTools": {
            "sSwfPath": "${pageContext.request.contextPath}/static/treasury/swf/copy_csv_xls_pdf.swf"
        }
		});
	table.columns.adjust().draw();

	 var tableAllDocuments = $('#allDocumentsTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'code' },
			{ data: 'name' },
			{ data: 'fiscalnumber' },
			{ data: 'identificationnumber' },
			{ data: 'actions' }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 4 } 
		             ],
		"data" : allDocumentsDataSet,
"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
     "tableTools": {
         "sSwfPath": "${pageContext.request.contextPath}/static/treasury/swf/copy_csv_xls_pdf.swf"
     }
		});
		
	 tableAllDocuments.columns.adjust().draw();

	 var tablePayments = $('#paymentsTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'code' },
			{ data: 'name' },
			{ data: 'fiscalnumber' },
			{ data: 'identificationnumber' },
			{ data: 'actions' }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 4 } 
		             ],
		"data" : paymentsDataSet,
"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
     "tableTools": {
         "sSwfPath": "${pageContext.request.contextPath}/static/treasury/swf/copy_csv_xls_pdf.swf"
     }
		});
		
	 tablePayments.columns.adjust().draw();

	 var tableExemptions = $('#exemptionsTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"columns": [
			{ data: 'code' },
			{ data: 'name' },
			{ data: 'fiscalnumber' },
			{ data: 'identificationnumber' },
			{ data: 'actions' }
			
		],
		//CHANGE_ME adjust the actions column width if needed
		"columnDefs": [
		//54
		               { "width": "54px", "targets": 4 } 
		             ],
		"data" : exemptionsDataSet,
		//Documentation: https://datatables.net/reference/option/dom
"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
     "tableTools": {
         "sSwfPath": "${pageContext.request.contextPath}/static/treasury/swf/copy_csv_xls_pdf.swf"
     }
		});
		
	 table.columns.adjust().draw();
	 
	
	 //Set the Selection option on tables
	 $('#pendingDocumentsTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  


	});
</script>
