<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>

<div style="text-align:center;border: #f0f0f0 solid 1px;">
	
	<p><spring:message code="label.ForwardPaymentController.forwardPayment.payline.warning" /></p>
	
	<p style="align:center">
		<img src="${pageContext.request.contextPath}/static/treasury/images/forwardpayments/payline/redunicre_aviso_pagamento_${localeCode}.png" />
	</p>
	
</div>
<p>&nbsp;</p>
