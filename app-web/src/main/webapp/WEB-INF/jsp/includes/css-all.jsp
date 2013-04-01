<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="jwr" uri="http://jawr.net/tags-el" %>
<c:if test="${debug}">
<link rel="stylesheet" type="text/css" href="${extCss}/ext-all-custom-debug.css" />
</c:if>
<c:if test="${not debug}">
<link rel="stylesheet" type="text/css" href="${extCss}/ext-all-custom.css" />
</c:if>
<link rel="stylesheet" type="text/css" href="${cssPath}/theme/${lang}/${theme}/locale.css?v=${appVersion}" />
<jwr:style src="/bundles/css/extux.css" useRandomParam="false"/>
<jwr:style src="/bundles/css/app.css" useRandomParam="false"/>