Ext.define('App.controller.sample.SecUserController', {
	extend : 'Ext.app.Controller',
	stores: [
        'SecUser'
    ],
    models: ['SecUser'],
	views : ['sample.secuser.SecUserGrid'],
	init : function() {
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
