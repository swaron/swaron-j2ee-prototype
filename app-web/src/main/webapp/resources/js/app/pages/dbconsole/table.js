//Ext.loader.requireSingleton('App.lazy.GridConfig',{
//	table:'SecUser'
//});
//Ext.Loader.wait('App.lazy.GridConfigFactory',{
//	table:'SecUser'
//});

Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	controllers : ['dbconsole.TableController'],
	launch : function() {
		Ext.widget('tablegrid', {
			tableName:App.getParam('table'),
			width : 700,
			height : 500,
			renderTo : 'table-gird'
		});
	}
});
