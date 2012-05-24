<%@page language="java" pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="u" uri="http://app.org/tags/utils"%>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<layout:basic title="" pageClass="">
<jsp:attribute name="scripts">
<script type="text/javascript">
Ext.require('Ext.tree.Panel');
Ext.require('App.store.TableTree');
Ext.onReady(function(){
	Ext.create('Ext.tree.Panel', {
	    renderTo: Ext.getBody(),
	    title: 'Simple Tree',
	    width: 150,
	    height: 150,
	    
	    store:Ext.create('App.store.TableTree',{
	    	rootVisible:true,
	    	autoLoad : true,
	    	database : 'test'
	    })
	});
});
</script>
</jsp:attribute>
<jsp:body>
	<br />
</jsp:body>
</layout:basic>
