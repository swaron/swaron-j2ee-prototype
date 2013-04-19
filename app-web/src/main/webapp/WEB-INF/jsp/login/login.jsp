<!doctype html>
<%@page trimDirectiveWhitespaces="true" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:include page="/WEB-INF/jsp/includes/pathVars.jsp" flush="false" />
<html>
<head>
<title>User Information</title>
<jsp:include page="/WEB-INF/jsp/includes/css-basic.jsp" />
</head>
<body id="app" class="test-page">
	<jsp:include page="/WEB-INF/jsp/includes/fragment/header.jspx" />
	<jsp:include page="/WEB-INF/jsp/includes/fragment/side.jspx" />
	
	<div id="main" class="container">
		<h2 class="text-center">Login Page</h2>
		<form class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="inputEmail">Email</label>
				<div class="controls">
					<input type="text" id="inputEmail" placeholder="Email">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputPassword">Password</label>
				<div class="controls">
					<input type="password" id="inputPassword" placeholder="Password">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<label class="checkbox"> <input type="checkbox"> Remember me
					</label>
					<button type="submit" class="btn btn-primary">Sign in</button>
				</div>
			</div>
		</form>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/includes/fragment/footer.jspx" />
	<jsp:include page="/WEB-INF/jsp/includes/js-basic.jsp" />
</body>
</html>
