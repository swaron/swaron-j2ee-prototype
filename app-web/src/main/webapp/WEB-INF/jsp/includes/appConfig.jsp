<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
	var App_Config = {
		restUrl:'<c:out value="${contextPath}" />',

		defaultLang: 'en',
		lang: '${lang}',
		contextPath: '<c:out value="${contextPath}" />',
		jsPath: '<c:out value="${jsPath}" />',
		cssPath: '<c:out value="${cssPath}" />',
		extJs: '<c:out value="${extJs}" />',
		debug: '<c:out value="${debug}" />',
		version: '<c:out value="${appVersion}" default="snapshot"/>'
	};
</script>