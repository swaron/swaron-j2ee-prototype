Ext.require('App.view.dbconsole.DbInfoMaster');

Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	launch : function() {
		Ext.widget('dbinfomaster', {
			width : 700,
			height : 500,
			renderTo : 'table-gird'
		});
	}
});