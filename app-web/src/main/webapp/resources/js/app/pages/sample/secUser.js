Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	controllers : ['sample.SecUserController'],
	launch : function() {
		var grid = Ext.widget('secusergrid', {
			width : 700,
			height : 500,
			renderTo : 'table-gird'
		});
		this.initDomEvents();
	},
	initDomEvents : function() {
		App.log('there are no dom events now.');
	}
});