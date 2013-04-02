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
		<h2 class="text-center">User Information</h2>

		<table border="1" class="table table-striped table-bordered">
			<tr>
				<th>Tag</th>
				<th>Value</th>
			</tr>
			<tr>
				<td>&lt;sec:authentication property='name' /&gt;</td>
				<td><sec:authentication property="name" /></td>
			</tr>
				<sec:authorize ifAllGranted="ROLE_USER">
				<tr>
					<td>&lt;sec:authentication property='principal.username' /&gt;</td>
					<td><sec:authentication property="principal.username" /></td>
				</tr>
				<tr>
					<td>&lt;sec:authentication property='principal.enabled' /&gt;</td>
					<td><sec:authentication property="principal.enabled" /></td>
				</tr>
				<tr>
					<td>&lt;sec:authentication property='principal.accountNonLocked' /&gt;</td>
					<td><sec:authentication property="principal.accountNonLocked" /></td>
				</tr>
				</sec:authorize>
		</table>

		<div class="round-box">
			You are logged in as remote user <b><%=request.getRemoteUser()%></b> in session <b><%=session.getId()%></b>. <br>
			<br>
			<%
				if (request.getUserPrincipal() != null) {
			%>
			Your user principal name is <b><%=request.getUserPrincipal().getName()%></b>. <br>
			<br>
			<%
				} else {
			%>
			No user principal could be identified. <br>
			<br>
			<%
				}
			%>
			<%
				String role = request.getParameter("role");
				if (role == null)
					role = "";
				if (role.length() > 0) {
					if (request.isUserInRole(role)) {
			%>
			You have been granted role <b><%=role%></b>. <br>
			<br>
			<%
				} else {
			%>
			You have <i>not</i> been granted role <b><%=role%></b>. <br>
			<br>
			<%
				}
				}
			%>
			To check whether your username has been granted a particular role, enter it here:
			<form method="GET" action='<%=response.encodeURL("index.jsp")%>'>
				<input type="text" name="role" value="<%=role%>">
			</form>
			<br>
			<br> You can logoff by clicking <a href='<%=response.encodeURL("index.jsp?logoff=true")%>'>here</a>. This
			should cause automatic re-logon with Waffle and a new session ID.
		</div>


		<p>
			<a href="../">Home</a> <a href="../j_spring_security_logout">Logout</a>
		</p>

	</div>
	<jsp:include page="/WEB-INF/jsp/includes/fragment/footer.jspx" />
	<jsp:include page="/WEB-INF/jsp/includes/js-basic.jsp" />
</body>
</html>
