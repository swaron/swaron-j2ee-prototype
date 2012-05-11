Ext.define('App.controller.dbconsole.TableController', {
	extend : 'Ext.app.Controller',
	views : ['dbconsole.AllTableView'],
	stores:['EntityName'],
	init : function(application) {
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
