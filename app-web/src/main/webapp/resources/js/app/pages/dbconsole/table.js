//Ext.loader.requireSingleton('App.lazy.GridConfig',{
//	table:'SecUser'
//});
Ext.Loader.wait('App.lazy.GridConfigFactory',{
	table:'SecUser'
});

Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	controllers : ['dbconsole.TableController'],
	launch : function() {
		var grid = Ext.widget('tablegrid', {
			tableName:App.getParam('table'),
			width : '100%',
			height : 500,
			renderTo : 'table-gird'
		});
	}
});
