Ext.require('App.view.db.external.dbinfo.DbInfoMaster');

Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	launch : function() {
		Ext.QuickTips.init();
		Ext.widget('dbinfomaster', {
			width : 700,
			height : 500,
			renderTo : 'table-gird'
		});
	}
});