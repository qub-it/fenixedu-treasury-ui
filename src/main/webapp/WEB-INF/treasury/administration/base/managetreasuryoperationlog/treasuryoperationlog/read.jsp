<%@page
    import="org.fenixedu.treasury.ui.administration.base.managelog.TreasuryOperationLogController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
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
<%-- ${portal.angularToolkit()} --%>
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
        <spring:message code="label.managetreasuryoperationlog.readlog"
            arguments="${ (not empty treasuryOperationLogSet) ? treasuryOperationLogSet[0].type : ' ' }" />
        <small></small>
    </h1>
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

<div class="panel panel-default">
    <form method="get" class="form-horizontal">
        <div class="panel-body">
            <div class="form-group row">
                <div class="col-sm-4 control-label">
                    <spring:message
                        code="label.TreasuryOperationLog.logDate" />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.TreasuryOperationLog.logDateFrom" />
                </div>

                <div class="col-sm-4">
                    <input id="settlementNote_documentDate"
                        class="form-control" type="text"
                        name="logdatefrom" bennu-date
                        value='<c:out value='${param.logdatefrom }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label">
                    <spring:message
                        code="label.TreasuryOperationLog.logDateTo" />
                </div>

                <div class="col-sm-4">
                    <input id="settlement_documentDate"
                        class="form-control" type="text"
                        name="logdateto" bennu-date
                        value='<c:out value='${param.logdateto }'/>' />
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
    <c:when test="${not empty treasuryOperationLogSet }">
        <div class="panel-group" id="accordion">
            <c:forEach items="${ treasuryOperationLogSet }" var="log" varStatus="loopStatus">
                <div class="panel panel-default">
                    <div class="panel-heading" style="display : block">
                        <strong>  
                            [
                            <c:out value="${ log.versioningCreator }"/>
                            ]
                            <joda:format value='${log.versioningCreationDate}' style='SM' />
                        </strong>
                        <p class="text-primary" style="float : right;display : inline">
                            <a data-toggle="collapse"
                                data-target="#collapseLog${loopStatus.index}"
<%--                                     href="#collapseLog${loopStatus.index}" --%>
                                >
                                <strong><em><spring:message
                                            code="label.manageacademicdebtgenerationrulelog.expandcolapse" /></em></strong>
                            </a>
                        </p>
                    </div>
                    <div style="clear: both"></div>
                    <div id="collapseLog${loopStatus.index}" class="panel-collapse collapse" style="padding: 5px">
                        <div class="panel panel-body">
                        <pre>
                            <c:out value='${log.log}' />
                        </pre>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-warning" role="alert">
            <p>
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
                <spring:message code="label.noResultsFound" />
            </p>

        </div>
    </c:otherwise>
</c:choose>
<script>
	$(document).ready(function() {

	});
</script>