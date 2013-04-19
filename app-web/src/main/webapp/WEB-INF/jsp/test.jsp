<!doctype html>
<%@page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<jsp:include page="/WEB-INF/jsp/includes/pathVars.jsp" flush="false" />
<html>
<head>
<title><s:message code="${page.title.test}" /></title>
<jsp:include page="/WEB-INF/jsp/includes/css-all.jsp" />
</head>
<body id="app" class="test-page">
	<jsp:include page="/WEB-INF/jsp/includes/fragment/header.jspx" />
	<jsp:include page="/WEB-INF/jsp/includes/fragment/side.jspx" />
	<div id="main">
		<div id="wrapper" class=""></div>
		<div id="side2"></div>
	</div>
	<jsp:include page="/WEB-INF/jsp/includes/fragment/footer.jspx" />
	<jsp:include page="/WEB-INF/jsp/includes/js-ext.jsp" />
	<script type="text/javascript">
		Ext.getBody().mask("Loading Page ...");
		Ext.onReady(function() {
			Ext.getBody().unmask();
		});
	</script>
</body>
</html>