<!doctype html>
<%@page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8" trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="jwr" uri="http://jawr.net/tags-el" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<jsp:include page="/WEB-INF/jsp/includes/pathVars.jsp" flush="false" />
<html>
<head>
<title><tiles:insertAttribute name="title" defaultValue="Web Application" ignore="true"/></title>
<c:if test="${debug}">
<link rel="stylesheet" type="text/css" href="${extCss}/ext-all-custom-debug.css" />
</c:if>
<c:if test="${not debug}">
<link rel="stylesheet" type="text/css" href="${extCss}/ext-all-custom.css" />
</c:if>
<link rel="stylesheet" type="text/css" href="${cssPath}/theme/${lang}/${theme}/locale.css?v=${appVersion}" />
<jwr:style src="/bundles/css/extux.css" useRandomParam="false"/>
<jwr:style src="/bundles/css/app.css" useRandomParam="false"/>
<tiles:insertAttribute name="styles" ignore="true"/> 
<jsp:include page="/WEB-INF/jsp/includes/appConfig.jsp" flush="false" />

<jwr:script src="/bundles/js/messages.js"/>
<s:eval expression="@appProps['app.env']" var="app_env"/>

<c:if test="${debug}">
<!-- dynamic loading version start -->
<script type="text/javascript" src="${extJs}/ext-dev.js"></script>
<script type="text/javascript">
	Ext.Loader.setConfig({
	    enabled: true,
	    disableCaching : false
	});
	Ext.Loader.setPath('Ext', '${extJs}/src');
	Ext.Loader.setPath('Ext.ux', '${resourcePath}/extjs-ux');
	Ext.Loader.setPath('App', '${jsPath}/app');
	Ext.Loader.setPath('Lib', '${resourcePath}/libjs/src');
</script>
<jwr:script src="/bundles/js/base.js" useRandomParam="false"/>
<!-- dynamic loading version end -->
</c:if>
<c:if test="${not debug}">
<!-- static loading version start, use cdn -->
<!-- <script type="text/javascript" src="${extJs}/ext-all.js"></script> -->
<script type="text/javascript" src="http://cdn.sencha.io/ext-4.1.1-gpl/ext-all.js"></script>
<jwr:script src="/bundles/js/base.js" useRandomParam="false"/>
<jwr:script src="/bundles/js/extux.js" useRandomParam="false"/>
<jwr:script src="/bundles/js/widget.js" useRandomParam="false"/>
<!-- static loading version end -->
</c:if>
<script type="text/javascript">
(function(){
	if(App.cfg.lang != 'en'){
		var url = Ext.util.Format.format("{0}/locale/ext-lang-{1}.js?v=${appVersion}", App.cfg.extJs, App.cfg.lang);
		Ext.Loader.loadScript(url);
	}
})();
Ext.onReady(function(){
	Ext.getBody().unmask();
});
</script>
</head>
<body id="app">
	<script type="text/javascript">
	Ext.getBody().mask("Loading Page ...");
	</script>
	<div id="wrapper" class="basic-page">
		<tiles:insertAttribute name="header" ignore="true" />
		<tiles:insertAttribute name="side" ignore="true" />   
		<div id="main">
			<tiles:insertAttribute name="body"/>
		</div>
		<tiles:insertAttribute name="side2" ignore="true"/>
		<tiles:insertAttribute name="footer" ignore="true"/>
	</div>
	<tiles:insertAttribute name="scripts" ignore="true"/> 
</body>
</html>