Ext.require('App.view.db.external.browse.DbBrowserView');


Ext.application({
	database: App.getParam('database'),
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	launch : function() {
		Ext.widget('dbbrowserview', {
			database: App.getParam('database'),
			width : '100%',
			height : 500,
			renderTo : 'table-gird'
		});
	}
});
