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

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.document.manageInvoice.searchDebitNote" />
        <small></small>
    </h1>
</div>
<%-- NAVIGATION --%>
<!-- <div class="well well-sm" style="display:inline-block"> -->
<%-- 	<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/create"   ><spring:message code="label.event.create" /></a> --%>
<!-- &nbsp;|&nbsp;</div> -->

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

<div class="panel panel-default">
    <form method="get" class="form-horizontal">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.DebitNote.debtAccount" />
                </div>

                <div class="col-sm-10">
                    <%-- Relation to side 1 drop down rendered in input --%>
                    <select id="debitNote_debtAccount"
                        class="select2 col-sm-10" name="debtaccount">
                        <option value=""></option>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.DebitNote.documentNumber" />
                </div>

                <div class="col-sm-10">
                    <input id="debitNote_documentNumber"
                        class="form-control" type="text"
                        name="documentnumber"
                        value='<c:out value='${not empty param.documentnumber ? param.documentnumber : debitNote.documentNumber }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.DebitNote.documentDate" />
                </div>

                <div class="col-sm-4">
                    <input id="debitNote_documentDate"
                        class="form-control" type="text"
                        name="documentdate" bennu-datetime
                        value='<c:out value='${not empty param.documentdate ? param.documentdate : debitNote.documentDate }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.DebitNote.originDocumentNumber" />
                </div>

                <div class="col-sm-10">
                    <input id="debitNote_originDocumentNumber"
                        class="form-control" type="text"
                        name="origindocumentnumber"
                        value='<c:out value='${not empty param.origindocumentnumber ? param.origindocumentnumber : debitNote.originDocumentNumber }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.DebitNote.state" />
                </div>

                <div class="col-sm-4">
                    <select id="debitNote_state" class="form-control"
                        name="state">
                        <option value=""></option>
                        <%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME--%>
                        <c:forEach items="${stateValues}" var="field">
                            <option value='<c:out value='${field}'/>'><c:out
                                    value='${field}' /></option>
                        </c:forEach>
                    </select>
                    <script>
		$("#debitNote_state").val('<c:out value='${not empty param.state ? param.state : debitNote.state }'/>');
	</script>
                </div>
            </div>
        </div>
        <div class="panel-footer">
            <input type="submit" class="btn btn-default" role="button"
                value="<spring:message code="label.search" />" />
        </div>
    </form>
</div>


<c:choose>
    <c:when test="${not empty searchdebitnoteResultsDataSet}">

        <datatables:table id="searchdebitnoteTable" row="debitNote"
            data="${searchdebitnoteResultsDataSet}"
            cssClass="table responsive table-bordered table-hover"
            cdn="false" cellspacing="2">
            <datatables:column>
                <datatables:columnHead>
                    <spring:message code="label.DebitNote.documentDate" />
                </datatables:columnHead>
                				<c:out value='${debitNote.documentDate.toString("YYYY-MM-dd")}' />
<%--                 <joda:format value="${debitNote.documentDate}" --%>
<%--                     style="S-" /> --%>
            </datatables:column>
            <datatables:column>
                <datatables:columnHead>
                    <spring:message
                        code="label.DebitNote.documentNumber" />
                </datatables:columnHead>
                <c:out value="${debitNote.uiDocumentNumber}" />
            </datatables:column>
            <datatables:column>
                <datatables:columnHead>
                    <spring:message code="label.DebitNote.debtAccount" />
                </datatables:columnHead>
                <c:out value="${debitNote.debtAccount.customer.name}" />
            </datatables:column>
            <datatables:column>
                <datatables:columnHead>
                    <spring:message code="label.DebitNote.state" />
                </datatables:columnHead>

                <c:if test="${debitNote.isAnnulled()}">
                    <span class="label label-danger">
                </c:if>
                <c:if test="${debitNote.isPreparing() }">
                    <span class="label label-warning">
                </c:if>
                <c:if test="${debitNote.isClosed()}">
                    <span class="label label-primary">
                </c:if>

                <c:out
                    value='${debitNote.state.descriptionI18N.content}' />
                </span>
            </datatables:column>
            <datatables:column>
                <!--  ACTIONS COLUMN -->
                <a class="btn btn-default btn-xs"
                    href="${pageContext.request.contextPath}/treasury/document/manageinvoice/debitnote/search/view/${debitNote.externalId}">
                    <spring:message code='label.view' />
                </a>
            </datatables:column>
        </datatables:table>
        <script>
			createDataTables("searchdebitnoteTable", true, false,true, "${pageContext.request.contextPath}","${datatablesI18NUrl}");
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

<script>
	$(document).ready(function() {


		    
	$("#debitNote_debtAccount").select2({
		  ajax: {
		    url: "${pageContext.request.contextPath}/treasury/accounting/managecustomer/debtaccount/autocompletehelper",
		    dataType: 'json',
		    delay: 250,
		    data: function (params) {
		      return {
		        q: params.term, // search term
		        page: params.page
		      };
		    },
		    processResults: function (data, page) {
		      return {
		        results: data
		      };
		    },
		    cache: true
		  },
		  escapeMarkup: function (markup) { return markup; }, 
		  minimumInputLength: 3,
		});

	}); 
</script>

