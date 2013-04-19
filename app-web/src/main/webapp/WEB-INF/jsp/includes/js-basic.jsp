<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="jwr" uri="http://jawr.net/tags-el" %>

<jsp:include page="/WEB-INF/jsp/includes/appConfig.jsp" flush="false" />
<jwr:script src="/bundles/js/messages.js"/>
<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="${contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>

<!-- add css3 support to ie-->
<!--[if lt IE 9]>
  <script type="text/javascript" src="${resourcePath}/css/ie/PIE_IE678.js"></script>
<![endif]-->
<!--[if IE 9]>
  <script type="text/javascript" src="${resourcePath}/css/ie/PIE_IE9.js"></script>
<![endif]-->
<script type="text/javascript">
(function(){
	if (window.PIE) {
	    $('input texteara pre').each(function() {
	        PIE.attach(this);
	    });
	}
})();
</script>