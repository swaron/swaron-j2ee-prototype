<%@page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="u" uri="http://app.org/tags/utils"%>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<layout:basic title="" pageClass="">
<jsp:attribute name="scripts">
<script type="text/javascript">
Ext.require('App.view.db.internal.AllTableView');

Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	launch : function() {
		App.log('application launch');
		Ext.widget('alltableview', {
			width : '100%',
			height : 500,
			renderTo : Ext.getBody()
		});
		
	}
});

</script>
</jsp:attribute>
<jsp:body>
	<br />
</jsp:body>
</layout:basic>
