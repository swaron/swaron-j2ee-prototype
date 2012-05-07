<%@page contentType="text/html; charset=utf-8" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="resourcePath" value="${contextPath}/resources" scope="request" />
<c:set var="jsPath" value="${resourcePath}/js" scope="request" />
<c:set var="cssPath" value="${resourcePath}/css" scope="request" />
<c:set var="extJs" value="${resourcePath}/ext/js" scope="request"/>
<c:set var="extCss" value="${resourcePath}/ext/resources/css" scope="request"/>

<c:set var="locale" value="${pageContext.response.locale}" scope="request" />
<c:set var="lang" value="${pageContext.response.locale.language}" scope="request" />
<c:set var="theme" value="default" scope="request" />
<c:set var="themePath" value="${resourcePath}/css/theme/${lang}/${theme}" scope="request" />


<c:set var="appVersion" value="${applicationScope.appVersion}" scope="request"/>

<c:if test="${cookie.debug.value == 'true' or param['_debug'] == 'true'}">
<c:set var="debug" value="true" scope="request"/>
<c:set var="debugSuffix" value="-dev" scope="request" />
</c:if>