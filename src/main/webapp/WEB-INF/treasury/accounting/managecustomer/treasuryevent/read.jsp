<%@page import="org.fenixedu.treasury.ui.document.manageinvoice.CreditNoteController"%>
<%@page import="org.fenixedu.treasury.ui.document.manageinvoice.DebitNoteController"%>
<%@page import="org.fenixedu.treasury.domain.document.DebitNote"%>
<%@page import="org.fenixedu.treasury.ui.document.manageinvoice.DebitEntryController"%>
<%@page
    import="org.fenixedu.treasury.ui.accounting.managecustomer.TreasuryEventController"%>
<%@page
    import="org.fenixedu.treasury.ui.managetreasuryexemption.TreasuryExemptionController"%>
<%@page
    import="org.fenixedu.treasury.domain.exemption.TreasuryExemption"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="datatables"
    uri="http://github.com/dandelion/datatables"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

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

<style type="text/css">

.my-table-option .input-group-addon {
	border: 0px solid #ccc;
	border-radius: 0px;
}

.my-table-option .input-group .form-control {
	border: 0px solid #ccc;
	border-radius: 0px;
}

</style>


<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.viewCustomerTreasuryEvents.readTreasuryEvent" />
        <small></small>
    </h1>
</div>
<div class="modal fade" id="annulDebitEntriesModal"> 
   <div class="modal-dialog"> 
     <div class="modal-content">

       <div class="modal-header">
         <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> 
         <h4 class="modal-title"><spring:message code="label.TreasuryEvent.annul.all.debit.entries.title"/></h4>
       </div> 

     <form id ="annulDebitEntriesModalForm" action="${pageContext.request.contextPath}<%= TreasuryEventController.ANNULALLDEBITENTRIES_URL %>${debtAccount.externalId}/${treasuryEvent.externalId}"  method="POST">

	       <div class="modal-body"> 
       
		        <p><em><spring:message code = "label.TreasuryEvent.annul.debit.entry.reason.confirm"/></em></p>
	            <p>&nbsp;</p>
	            <div class="form-group row">
	                <div class="col-sm-2 control-label">
	                    <spring:message code="label.TreasuryEvent.annul.debit.entry.reason" />
	                </div>
	
	                <div class="col-sm-10">
	                    <input id="" class="form-control" type="text" name="treasuryEventAnullDebitEntriesReason" />
	                </div>
	            </div>
		       </div> 
		       
		       <div class="modal-footer"> 
		         <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "label.close"/></button>
		         <button id="anullButton" class ="btn btn-danger" type="submit"><spring:message code = "label.annul"/></button>
		       </div> 
       </form> 
       
     </div> 
   </div> 
 </div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
	&nbsp;
	<a class=""
		href="${pageContext.request.contextPath}/treasury/accounting/managecustomer/treasuryevent/?debtaccount=${debtAccount.externalId}">
		<spring:message code="label.event.back" />
	</a>
	&nbsp;|&nbsp;
    <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp; 
   	<a class="" href="#" data-toggle="modal" data-target="#annulDebitEntriesModal"> 
       	<spring:message code="label.event.anull.all.debit.entries" />
    </a>

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

<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">
            <spring:message code="label.details" />
        </h3>
    </div>
    <div class="panel-body">
        <form method="post" class="form-horizontal">
            <table class="table">
                <tbody>
                <c:if test='${ debtAccount.getClosed() }'>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.DebtAccount.closed" /></th>
                        <td><span class="label label-warning"><spring:message code="warning.DebtAccount.is.closed" /></span></td>
                    </tr>
                </c:if>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.Customer.fiscalNumber" /></th>
                        <td><c:out value='${debtAccount.customer.uiFiscalNumber}' /></td>
                    </tr>

                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.DebtAccount.customer" /></th>
                        <td><c:out value='${debtAccount.customer.businessIdentification}' /> - <c:out value='${debtAccount.customer.name}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message code="label.DebtAccount.finantialInstitution" /></th>
                        <td><c:out value='${debtAccount.finantialInstitution.name}' /></td>
                    </tr>
                
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.TreasuryEvent.description" /></th>
                        <td><c:out
                                value='${treasuryEvent.description.content}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.TreasuryEvent.amountToPay" />
                        </th>
                        <td><c:out
                                value='${debtAccount.finantialInstitution.currency.getValueFor(treasuryEvent.amountWithVatToPay)}' /></td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.TreasuryEvent.treasuryEventDate" />
                        </th>
                        <td>
                        	<joda:format value="${treasuryEvent.treasuryEventDate}" style="S-" />
                        </td>
                    </tr>
               </tbody>
            </table>
        </form>
    </div>
</div>

<h2>
    <spring:message code="label.TreasuryEvent.treasuryExemption" />
</h2>

<p>
	<a class="btn btn-default" href="${pageContext.request.contextPath}<%= TreasuryExemptionController.CREATE_URL %>${debtAccount.externalId}/${treasuryEvent.externalId}">
		<span class="glyphicon glyphicon-plus-sign" aria-hidden="true">&nbsp;</span>
		<spring:message code="label.event.create" />
	</a>
</p>
<c:if test="${not empty treasuryEvent.activeTreasuryExemptions}">

    <script type="text/javascript">
		function processDelete(externalId) {
			url = '${pageContext.request.contextPath}<%=TreasuryEventController.SEARCH_TO_DELETE_ACTION_URL %>${debtAccount.externalId}/' + externalId;
			$("#deleteForm").attr("action", url);
			$('#deleteModal').modal('toggle')
		}
	</script>

    <div class="modal fade" id="deleteModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="deleteForm" action="" method="POST">
                    <div class="modal-header">
                        <button type="button" class="close"
                            data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title">
                            <spring:message code="label.confirmation" />
                        </h4>
                    </div>
                    <div class="modal-body">
                        <p>
                            <spring:message
                                code="label.manageTreasuryExemption.searchTreasuryExemption.confirmDelete" />
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default"
                            data-dismiss="modal">
                            <spring:message code="label.close" />
                        </button>
                        <button id="deleteButton" class="btn btn-danger"
                            type="submit">
                            <spring:message code="label.delete" />
                        </button>
                    </div>
                </form>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->

    <datatables:table id="exemptionsTable" row="exemption" data="${treasuryEvent.activeTreasuryExemptions}"
        cssClass="table responsive table-bordered table-hover" cdn="false" cellspacing="2">
        <datatables:column cssStyle="width:15%">
            <datatables:columnHead>
                <spring:message code="label.TreasuryExemption.treasuryExemptionType" />
            </datatables:columnHead>
            <c:out value="${exemption.treasuryExemptionType.name.content}" />
        </datatables:column>
        <datatables:column cssStyle="width:10%">
            <datatables:columnHead>
                <spring:message code="label.TreasuryExemption.netAmountToExempt" />
            </datatables:columnHead>
            <c:out value="${exemption.debitEntry.debtAccount.finantialInstitution.currency.getValueFor(exemption.netAmountToExempt)}" />
        </datatables:column>
        <datatables:column cssStyle="width:40%">
            <datatables:columnHead>
                <spring:message code="label.TreasuryExemption.debitEntry" />
            </datatables:columnHead>
            <p align=left>
                <c:if test="${not empty exemption.debitEntry.finantialDocument}">
	                [<a target="_blank" href="${pageContext.request.contextPath}<%= DebitNoteController.READ_URL %>/${exemption.debitEntry.finantialDocument.externalId}">
						<c:out value='${exemption.debitEntry.finantialDocument.uiDocumentNumber}' />
                	</a>]
                	&nbsp;
                </c:if>
                <a target="_blank" href="${pageContext.request.contextPath}<%= DebitEntryController.READ_URL %>/${exemption.debitEntry.externalId}">
               		<c:out value='${exemption.debitEntry.description}' />
               	</a>
            </p>
        </datatables:column>
        <datatables:column cssStyle="width:25%">
            <datatables:columnHead>
                <spring:message code="label.TreasuryExemption.reason" />
            </datatables:columnHead>
            <p align=left>
                <c:out value='${exemption.reason}' />
            </p>
        </datatables:column>
        <datatables:column cssStyle="width:10%">
             <a class="btn btn-danger" href="#" onclick="processDelete('${exemption.externalId}')">
                <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp; 
				<spring:message code="label.event.delete" />
			</a>
        </datatables:column>
    </datatables:table>

</c:if>

<c:if test="${not empty treasuryEvent.propertiesMap}">
    <h2>
        <spring:message code="label.TreasuryEvent.propertiesJsonMap" />
    </h2>

    <table id="treasuryEventTableMap"
        class="table responsive table-bordered table-hover" width="100%">

        <c:forEach var="property" items="${treasuryEvent.propertiesMap}">
            <tr>
                <th><c:out value="${property.key}" /></th>
                <td><c:out value="${property.value}" /></td>
            </tr>
        </c:forEach>
    </table>

</c:if>

<h2>
    <spring:message code="label.TreasuryEvent.allDebitEntries" />
</h2>
<div class="tab-pane" id="allDebitEntries">
    <p></p>
    <c:choose>
        <c:when test="${not empty allActiveDebitEntriesDataSet}">
        
        <script type="text/javascript">
        function processExclude(externalId) {
            url = '${pageContext.request.contextPath}<%=TreasuryEventController.ANNULDEBITENTRY_URL%>${debtAccount.externalId}/' + externalId;
                            $("#excludeForm").attr("action", url);
                            $('#excludeModal').modal('toggle')
                        }
        
        function processInclude(externalId) {
            url = '${pageContext.request.contextPath}<%=TreasuryEventController.REVERTANNULDEBITENTRY_URL%>${debtAccount.externalId}/' + externalId;
                            $("#includeForm").attr("action", url);
                            $('#includeModal').modal('toggle')
                        }
        
        </script>
        
    <div class="modal fade" id="excludeModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="excludeForm" action="" method="POST">
                    <div class="modal-header">
                        <button type="button" class="close"
                            data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title">
                            <spring:message code="label.confirmation" />
                        </h4>
                    </div>
                    <div class="modal-body">
                        <p><spring:message code="label.TreasuryEvent.removeDebitEntry.confirm" /></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default"
                            data-dismiss="modal">
                            <spring:message code="label.close" />
                        </button>
                        <button id="deleteButton" class="btn btn-danger"
                            type="submit">
                            <spring:message code="label.TreasuryEvent.excludeFromEvent" />
                        </button>
                    </div>
                </form>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <div class="modal fade" id="includeModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="includeForm" action="" method="POST">
                    <div class="modal-header">
                        <button type="button" class="close"
                            data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title">
                            <spring:message code="label.confirmation" />
                        </h4>
                    </div>
                    <div class="modal-body">
                        <p><spring:message code="label.TreasuryEvent.revertDebitEntry.confirm" /></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default"
                            data-dismiss="modal">
                            <spring:message code="label.close" />
                        </button>
                        <button id="deleteButton" class="btn btn-danger"
                            type="submit">
                            <spring:message code="label.TreasuryEvent.includeInEvent" />
                        </button>
                    </div>
                </form>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
        
			<div class="my-table-option row">
			  <div class="col-xs-12">
			    <div class="input-group">
			      <span class="input-group-addon">
			        <input id="includeAnnuledCheckbox" type="checkbox" aria-label="...">
			      </span>
			      <input type="text" class="form-control" aria-label="..." value="<spring:message code="label.TreasuryEvent.show.annuled" />" disabled />
			    </div>
			  </div>
			</div>
            <datatables:table id="allDebitEntriesTable" row="debitEntry" data="${allActiveDebitEntriesDataSet}"
                cssClass="table responsive table-bordered table-hover" cdn="false" cellspacing="2">
                <datatables:column cssClass="never">
                	<c:out value="${not empty debitEntry.finantialDocument && debitEntry.finantialDocument.isAnnulled()}" />
                </datatables:column>
                <datatables:column cssStyle="width:10%">
                    <datatables:columnHead>
                        <spring:message code="label.TreasuryEvent.allDebitEntries.documentNumber" />
                    </datatables:columnHead>
                    <c:if test="${not empty debitEntry.finantialDocument}">
	                    <a target="_blank" href="${pageContext.request.contextPath}<%= DebitNoteController.READ_URL %>/${debitEntry.finantialDocument.externalId}">
	                    	<c:out value="${debitEntry.finantialDocument.uiDocumentNumber}" />
	                    </a>
						<c:if test="${debitEntry.finantialDocument.isAnnulled()}">
							<span class="label label-danger"><c:out value='${debitEntry.finantialDocument.state.descriptionI18N.content}' /></span>
						</c:if>
					</c:if>
                </datatables:column>
                <datatables:column cssStyle="width:10%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.TreasuryEvent.allDebitEntries.dueDate" />
                    </datatables:columnHead>
                    <p align=center>
                    <c:out value="${debitEntry.dueDate}" />
                    	<%-- 
                        <joda:format value="${debitEntry.dueDate}"
                            style="S-" />
                            --%>
                    </p>
                </datatables:column>
                <datatables:column cssStyle="width:50%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.TreasuryEvent.allDebitEntries.description" />
                    </datatables:columnHead>
                    <p>
                    	<a target="_blank" href="${pageContext.request.contextPath}<%= DebitEntryController.READ_URL %>/${debitEntry.externalId}">
                        	<c:out value="${debitEntry.description}" />
                        </a>
                    </p>
                    <p>
                    	<em><c:out value="${debitEntry.debtAccount.customer.uiFiscalNumber}" /></em>
                    </p>
					<p>
	                    <c:if test="${debitEntry.eventAnnuled}">
	                        <span class="label label-danger"><spring:message code="label.TreasuryExemption.excludedFromEvent" /></span>
	                    </c:if>
                    </p>
                </datatables:column>
                <datatables:column cssStyle="width:10%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.TreasuryEvent.allDebitEntries.amount" />
                    </datatables:columnHead>
                    <p align=right>
                        <c:out
                            value="${debitEntry.debtAccount.finantialInstitution.currency.getValueFor(debitEntry.amountWithVat)}" />
                    </p>
                </datatables:column>
                <datatables:column cssStyle="width:10%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.TreasuryEvent.allDebitEntries.exemptedAmount" />
                    </datatables:columnHead>
                    <p align=right>
                        <c:out
                            value="${debitEntry.debtAccount.finantialInstitution.currency.getValueFor(debitEntry.netExemptedAmount)}" />
                    </p>
                </datatables:column>
                <datatables:column cssStyle="width:10%">
                        <c:if test="${!debitEntry.eventAnnuled}">
                            <a class="btn btn-default" 
                            	onclick="processExclude('${treasuryEvent.externalId}/${debitEntry.externalId}')" href="#">
                                <span aria-hidden="true" class="glyphicon glyphicon-remove-circle" ></span>&nbsp;
                                <spring:message code="label.TreasuryEvent.excludeFromEvent" />
                            </a>
                        </c:if>
                        <c:if test="${debitEntry.eventAnnuled && (empty debitEntry.finantialDocument || !debitEntry.finantialDocument.isAnnulled())}">

							<a class="btn btn-default"
	                    		onclick="processInclude('${treasuryEvent.externalId}/${debitEntry.externalId}')" href="#">
			                        <span aria-hidden="true" class="glyphicon glyphicon-retweet" ></span>&nbsp;
	                                <spring:message code="label.TreasuryEvent.includeInEvent" />
	                        </a>
                        </c:if>
                </datatables:column>
            </datatables:table>
            <script>
	        	$(document).ready(function() {
	        		var table = $('#allDebitEntriesTable').DataTable({
	        			language : { url : "${datatablesI18NUrl}" },
	            		"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', //FilterBox = YES && ExportOptions = NO
	        			"bDeferRender" : true,
	        			"bPaginate" : false,
	                    "tableTools": {
	                        "sSwfPath": "${pageContext.request.contextPath}/static/treasury/swf/copy_csv_xls_pdf.swf"
	                    }
	        		});

	        		table.columns.adjust().draw();
					$('#allDebitEntriesTable tbody').on( 'click', 'tr', function () {
					      $(this).toggleClass('selected');
					});
					
					$.fn.dataTable.ext.search.push(
					    function( settings, data, dataIndex ) {
					        var includeAnnuledChecked = $('#includeAnnuledCheckbox').is(':checked');
					        var annuled = (data[0].trim() === "true");
					        return (!annuled || includeAnnuledChecked);
					    }
					);
					
					$('#includeAnnuledCheckbox').click(function() {
						table.columns.adjust().draw();
					});
	        	});
			</script>
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
</div>
<c:choose>
    <c:when test="${not empty allActiveCreditEntriesDataSet}">
        <h2>
            <spring:message
                code="label.TreasuryEvent.allActiveCreditEntries" />
        </h2>
        <div class="tab-pane" id="allActiveCreditEntriesDataSet">
            <p></p>
            <datatables:table id="allActiveCreditEntriesDataSetTable"
                row="creditEntry"
                data="${allActiveCreditEntriesDataSet}"
                cssClass="table responsive table-bordered table-hover"
                cdn="false" cellspacing="2">
                <datatables:column cssStyle="width:10%">
                    <datatables:columnHead>
                        <spring:message code="label.TreasuryEvent.allDebitEntries.documentNumber" />
                    </datatables:columnHead>
                    <p>
	                    <a target="_blank"  href="${pageContext.request.contextPath}<%= CreditNoteController.READ_URL %>/${creditEntry.finantialDocument.externalId}">
                    		<c:out value="${creditEntry.finantialDocument.uiDocumentNumber}" />
                    	</a>
                    </p>
                    <c:if test="${not empty creditEntry.debitEntry && not empty creditEntry.debitEntry.finantialDocument}">
	                    <p>[<a target="_blank" href="${pageContext.request.contextPath}<%= DebitNoteController.READ_URL %>/${creditEntry.debitEntry.finantialDocument.externalId}">
		                    	<c:out value="${creditEntry.debitEntry.finantialDocument.uiDocumentNumber}" />
		                    </a>]
		                </p>
                    </c:if>
                </datatables:column>
                <datatables:column cssStyle="width:15%">
                    <datatables:columnHead>
                        <spring:message code="label.TreasuryEvent.allDebitEntries.entryDateTime" />
                    </datatables:columnHead>
                    <p align=center>
                        <joda:format value="${creditEntry.entryDateTime}" style="S-" />
                    </p>
                </datatables:column>
                <datatables:column cssStyle="width:60%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.TreasuryEvent.allDebitEntries.description" />
                    </datatables:columnHead>
                    <p><c:out value="${creditEntry.description}" /></p>
                    <p><em><c:out value="${creditEntry.debtAccount.customer.uiFiscalNumber}" /></em></p>
                </datatables:column>
                <datatables:column cssStyle="width:10%">
                    <datatables:columnHead>
                        <spring:message
                            code="label.TreasuryEvent.allDebitEntries.amount" />
                    </datatables:columnHead>
                    <p align=right>
                        -
                        <c:out
                            value="${creditEntry.debtAccount.finantialInstitution.currency.getValueFor(creditEntry.amountWithVat)}" />
                    </p>
                </datatables:column>
            </datatables:table>
            <script>
				createDataTables(
						'allActiveCreditEntriesDataSetTable',
						true,
						false,
						false,
						"${pageContext.request.contextPath}",
						"${datatablesI18NUrl}");
			</script>
        </div>
    </c:when>
</c:choose>

<script>
</script>
