//Ext.loader.requireSingleton('App.lazy.GridConfig',{
//	table:'SecUser'
//});
Ext.require('App.view.dbconsole.AllTableView');

Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	controllers : ['dbconsole.TableController'],
	launch : function() {
		App.log('application launch');
		Ext.widget('alltableview', {
			width : '100%',
			height : 500,
			renderTo : 'table-gird'
		});
		
	}
});
