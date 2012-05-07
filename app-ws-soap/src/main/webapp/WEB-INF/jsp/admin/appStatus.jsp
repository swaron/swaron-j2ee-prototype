<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="u" uri="http://app.org/tags/utils"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Application Status</title>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Pragma" content="no-cache" />
<script type="text/javascript">
	var Page = {};
	var resetPassword = function(){
		var id = document.getElementById("crmMemberId").value;
		if(id){
			window.location.href = "../user/resetPwd/"+id;
		}else{
			alert("require member id");
		}
	}
</script>
<style type="text/css">
h3 {
	font-size: large;
}
</style>
</head>
<body>
	<div id="app-status" class="round-box">
		<h3>Database Status</h3>
		<div id="db-status">
			<span>Database Connection: </span>
			<c:if test="${dbPass}">
				<span>Valid</span>
			</c:if>
			<c:if test="${not dbPass}">
				<span style="color: red;">Invalid</span>
				<div>
					Exception:<br />
					<u:out text="${dbError}" />
				</div>
			</c:if>
		</div>
	<hr />
	<form id="test-form" method="get" onsubmit="resetPassword();return false;">
		<div style="line-height: 30px;">Test Reset Password function:</div>
		<label style="line-height: 30px;">Crm Member ID:</label>
		<input size="40" type="text" id="crmMemberId" name="crmMemberId" value="${param.crmMemberId}"/>
		<button class="normal-button" type="submit">Reset Password</button>
	</form>
	</div>
</body>
</html>
