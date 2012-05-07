Ext.define('App.controller.dbconsole.TableController', {
	extend : 'Ext.app.Controller',
//	stores: [
//        'DbInfo'
//    ],
//    models: ['DbInfo'],
	views : ['dbconsole.TableGrid'],
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
