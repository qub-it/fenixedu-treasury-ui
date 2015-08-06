<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css" />
<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<link href="${pageContext.request.contextPath}/static/treasury/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/treasury/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/treasury/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message code="label.administration.base.manageProduct.updateProduct" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class=""
		href="${pageContext.request.contextPath}/treasury/administration/base/manageproduct/product/read/${product.externalId}"><spring:message code="label.event.back" /></a>
	&nbsp;
</div>
<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>
	</div>
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">

		<c:forEach items="${warningMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>
	</div>
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span> ${message}
			</p>
		</c:forEach>
	</div>
</c:if>

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Product.productGroup" />
				</div>

				<div class="col-sm-10">
					<select id="product_productGroup" class="js-example-basic-single" name="productGroup" required>
					</select>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Product.code" />
				</div>

				<div class="col-sm-10">
					<input id="product_code" class="form-control" type="text" name="code" value='<c:out value='${not empty param.code ? param.code : product.code }'/>' readonly />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Product.name" />
				</div>

				<div class="col-sm-10">
					<input id="product_name" class="form-control" type="text" name="name" bennu-localized-string value='${not empty param.name ? param.name : product.name.json() } ' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Product.unitOfMeasure" />
				</div>

				<div class="col-sm-10">
					<input id="product_unitOfMeasure" class="form-control" type="text" name="unitofmeasure" bennu-localized-string
						value='${not empty param.unitofmeasure ? param.unitofmeasure : product.unitOfMeasure.json() } ' />
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Product.active" />
				</div>

				<div class="col-sm-2">
					<select id="product_active" name="active" class="form-control">
						<option value="false"><spring:message code="label.no" /></option>
						<option value="true"><spring:message code="label.yes" /></option>
					</select>
					<script>
		$("#product_active").select2().select2('val', '<c:out value='${not empty param.active ? param.active : product.active }'/>');
	</script>
				</div>
			</div>

			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Product.vatType" />
				</div>

				<div class="col-sm-4">
					<%-- Relation to side 1 drop down rendered in input --%>
					<select id="product_vatType" class="js-example-basic-single" name="vatType">
						<option value="">&nbsp;</option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
					</select>
				</div>
			</div>
                        <div class="form-group row" id="vatExemptionReasonId">
                <div class="col-sm-2 control-label">
                    <spring:message code="label.Vat.vatExemptionReason" />
                </div>

                <div class="col-sm-10">
                    <select id="vat_vatExemptionReason"
                        class="js-example-basic-single"
                        name="vatExemptionReason">
                        <option value="">&nbsp;</option>
                    </select>
                </div>
            </div>            


			<div class="form-group row">
				<div class="col-sm-2 control-label">
					<spring:message code="label.Product.finantialInstitution" />
				</div>

				<div class="col-sm-2">
					<select id="finantial_institutions" class="js-example-basic-single" name="finantialInstitution" multiple="multiple">
						<option value="">&nbsp;</option>
						<%-- empty option remove it if you don't want to have it or give it a label CHANGE_ME --%>
					</select>
					<script>
					<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
						var finantial_institutions_options = [
						<c:forEach items="${finantial_institutions_options}" var="element">   // THIS _FIELD_NAME__options must be added in the Controller.java 
						{
							text :"<c:out value='${element.name}'/>",  //Format the Output for the HTML Option
							id : "<c:out value='${element.externalId}'/>" //Define the ID for the HTML Option
						},
						</c:forEach>
						];

						var selectedOptions = [
						<c:forEach items="${product.finantialInstitutionsSet}" var="element" varStatus="loop"><c:out value='${element.externalId}'/>    ${!loop.last ? ',' : ''}	</c:forEach>
						];
						//Init Select2Options
						initSelect2Multiple("#finantial_institutions",finantial_institutions_options, selectedOptions); //
					</script>
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />" />
		</div>
	</div>
</form>

<script>
$(document).ready(function() {
	
    var sortFunction = function(a,b) { return a.text.localeCompare(b.text) };
	<%-- Block for providing vatType options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	vattype_options = [
		<c:forEach items="${vattype_options}" var="element"> 
			{
				text : "<c:out value='${element.name.content}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#product_vatType").select2(
		{
			data : vattype_options,
		}	  
    );
    
    
    
    $("#product_vatType").select2().select2('val', '<c:out value="${product.vatType.externalId}"/>');

	<%-- End block for providing series options --%>
	
	<%-- Block for providing administrativeOffice options --%>
	<%-- CHANGE_ME --%> <%-- INSERT YOUR FORMAT FOR element --%>
	administrativeOffice_options = [
		<c:forEach items="${productGroupList}" var="element"> 
			{
				text : "<c:out value='${element.name.content}'/>",  
				id : "<c:out value='${element.externalId}'/>"
			},
		</c:forEach>
	];
	
	$("#product_productGroup").select2(
		{
			data : administrativeOffice_options,
		}	  
    );
	
	$("#product_productGroup").select2().select2('val','${param.productGroup != null ? param.productGroup : product.productGroup.externalId}');		

    vatExemptionReason_options = [
                                  <c:forEach items="${vatExemptionReasonList}" var="element"> 
                                      {
                                          text : "<c:out value='${element.name.content}'/>",  
                                          id : "<c:out value='${element.externalId}'/>"
                                      },
                                  </c:forEach>
                              ];

                              $("#vat_vatExemptionReason").select2(
                                  {
                                      data : vatExemptionReason_options.sort(sortFunction),
                                  }     
                              );  
                              $("#vat_vatExemptionReason").select2().select2('val','${param.vatExemptionReason != null ? param.vatExemptionReason : product.vatExemptionReason.externalId}');
});
function checkValue(elem) {
    if(elem.value != 0){
        $('#vatExemptionReasonId').hide();
        $("#vat_vatExemptionReason").select2().select2('val','');
    } else {
        $('#vatExemptionReasonId').show();                      
        $("#vat_vatExemptionReason").select2({ width: 'resolve' });
    }
};
</script>
