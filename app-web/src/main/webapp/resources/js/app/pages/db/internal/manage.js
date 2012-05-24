//Ext.loader.requireSingleton('App.lazy.GridConfig',{
//	table:'SecUser'
//});
Ext.require('App.view.db.internal.AllTableView');

Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	launch : function() {
		App.log('application launch');
		Ext.widget('alltableview', {
			width : '100%',
			height : 500,
			renderTo : 'table-gird'
		});
		
	}
});
