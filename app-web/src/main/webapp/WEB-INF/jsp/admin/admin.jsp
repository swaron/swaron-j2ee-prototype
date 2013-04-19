<!doctype html>
<%@page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:include page="/WEB-INF/jsp/includes/pathVars.jsp" flush="false" />
<html>
<head>
<title>系统信息</title>
<jsp:include page="/WEB-INF/jsp/includes/css-all.jsp" />
<jsp:include page="/WEB-INF/jsp/includes/js-basic.jsp" />
</head>
<body class="home">
	<jsp:include page="/WEB-INF/jsp/includes/fragment/header.jspx" />
	<jsp:include page="/WEB-INF/jsp/includes/fragment/side.jspx" />
	<div id="main" class="container">
		<div id="online-user"></div>
	</div>
	<jsp:include page="/WEB-INF/jsp/includes/fragment/footer.jspx" />
	<jsp:include page="/WEB-INF/jsp/includes/js-ext.jsp" />
	<script type="text/javascript" src="${jsPath}/app/pages/admin/admin.js?v=${appVersion}"></script>
</body>
</html>
