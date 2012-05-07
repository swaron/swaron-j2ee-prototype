Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	controllers : ['dbconsole.DbInfoController'],
	launch : function() {
		Ext.widget('dbinfomaster', {
			width : 700,
			height : 500,
			renderTo : 'table-gird'
		});
	}
});