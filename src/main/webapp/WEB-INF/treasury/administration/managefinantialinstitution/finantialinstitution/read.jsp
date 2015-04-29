<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="org.fenixedu.treasury.domain.FinantialInstitution"%>
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

<link
    href="//cdn.datatables.net/responsive/1.0.4/css/dataTables.responsive.css"
    rel="stylesheet" />
<script
    src="//cdn.datatables.net/responsive/1.0.4/js/dataTables.responsive.js"></script>
<link
    href="//cdn.datatables.net/tabletools/2.2.3/css/dataTables.tableTools.css"
    rel="stylesheet" />
<script
    src="//cdn.datatables.net/tabletools/2.2.3/js/dataTables.tableTools.min.js"></script>
<link
    href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/css/select2.min.css"
    rel="stylesheet" />
<script
    src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/js/select2.min.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}
<% FinantialInstitution finantialInstitution = (FinantialInstitution) request.getAttribute("finantialInstitution"); %>
<%-- TITLE --%>
<div class="page-header">
    <h1>
        <spring:message
            code="label.administration.manageFinantialInstitution.readFinantialInstitution" />
        <small></small>
    </h1>
</div>
<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">
                    <spring:message code="label.confirmation" />
                </h4>
            </div>
            <div class="modal-body">
                <p>
                    <spring:message
                        code="label.administration.manageFinantialInstitution.readFinantialInstitution.confirmDelete" />
                </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                    data-dismiss="modal">
                    <spring:message code="label.close" />
                </button>
                <a class="btn btn-danger"
                    href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/delete/${finantialInstitution.externalId}">
                    <spring:message code="label.delete" />
                </a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/"><spring:message
            code="label.event.back" /></a> |&nbsp;&nbsp; <span
        class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp;<a
        class="" href="#" data-toggle="modal" data-target="#deleteModal"><spring:message
            code="label.event.delete" /></a> |&nbsp;&nbsp; <span
        class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<a
        class=""
        href="${pageContext.request.contextPath}/treasury/administration/managefinantialinstitution/finantialinstitution/update/${finantialInstitution.externalId}"><spring:message
            code="label.event.update" /></a> |&nbsp;&nbsp;
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
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.fiscalCountryRegion" /></th>
                        <td><c:out                            
                                 value='${finantialInstitution.fiscalCountryRegion.name.content}' /> 
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.code" /></th>
                        <td><c:out
                                value='${finantialInstitution.code}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.fiscalNumber" /></th>
                        <td><c:out
                                value='${finantialInstitution.fiscalNumber}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.companyId" /></th>
                        <td><c:out
                                value='${finantialInstitution.companyId}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.name" /></th>
                        <td><c:out
                                value='${finantialInstitution.name}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.companyName" /></th>
                        <td><c:out
                                value='${finantialInstitution.companyName}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.address" /></th>
                        <td><c:out
                                value='${finantialInstitution.address}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.country" /></th>
                        <td>
                        <c:if test= "${not empty finantialInstitution.country}">
                        	<c:out
                                 value="<%=finantialInstitution.getCountry().getLocalizedName(I18N.getLocale())%>" />
                        </c:if> 
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.district" /></th>
                        <td>
                        <c:if test= "${not empty finantialInstitution.district}">
                        
                        <c:out
                                 value="<%=finantialInstitution.getDistrict().getLocalizedName(I18N.getLocale())%>" /> 
                                 </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.municipality" /></th>
                        <td>
                        <c:if test= "${not empty finantialInstitution.municipality}">
                        
                        <c:out
                                 value="<%=finantialInstitution.getMunicipality().getLocalizedName(I18N.getLocale())%>" />
                        </c:if> 
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.locality" /></th>
                        <td><c:out
                                value='${finantialInstitution.locality}' />
                        </td>
                    </tr>
                    <tr>
                        <th scope="row" class="col-xs-3"><spring:message
                                code="label.FinantialInstitution.zipCode" /></th>
                        <td><c:out
                                value='${finantialInstitution.zipCode}' />
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>

<script>
	$(document).ready(function() {

	});
</script>
