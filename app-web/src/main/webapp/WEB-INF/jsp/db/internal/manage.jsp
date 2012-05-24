<%@page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="u" uri="http://app.org/tags/utils"%>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<layout:basic title="" pageClass="">
<jsp:attribute name="scripts">
<script type="text/javascript" src="${jsPath}/app/pages/db/internal/manage.js"></script>
</jsp:attribute>
<jsp:body>
	<br />
	<div id="table-gird">
	</div>
</jsp:body>
</layout:basic>