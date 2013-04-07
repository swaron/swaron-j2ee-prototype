<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="jwr" uri="http://jawr.net/tags-el" %>


<jsp:include page="/WEB-INF/jsp/includes/appConfig.jsp" flush="false" />
<jwr:script src="/bundles/js/messages.js"/>
<script src="http://code.jquery.com/jquery.js"></script>
<script src="${contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>


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
<script src="${contextPath}/resources/extjs/ext-all.js"></script>
<script src="${contextPath}/resources/extjs/ext-theme-neptune.js"></script>
<jwr:script src="/bundles/js/base.js" useRandomParam="false"/>
<jwr:script src="/bundles/js/extux.js" useRandomParam="false"/>
<jwr:script src="/bundles/js/widget.js" useRandomParam="false"/>
<script type="text/javascript" src="${resourcePath}/libjs/lib.js"></script>
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
	//Ext change box mode to border, which affect other frameworks. refer to Ext.isBorderBox for detail
	//Ext.fly(Ext.getBody().dom.parentNode).removeCls('x-border-box');
});
</script>