Ext.define('App.controller.dbconsole.DbInfoController', {
	extend : 'Ext.app.Controller',
	stores: [
        'DbInfo'
    ],
    models: ['DbInfo'],
	views : ['dbconsole.DbInfoMaster'],
	init : function() {
		//setup event handles
		this.control({
			'secusergrid' : {
				itemdblclick : this.editUser
			}
		});
		
	},
	editUser : function(grid, record) {
		App.log('edit user function is included in row editor of grid.');
	}
});
