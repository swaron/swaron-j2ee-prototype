Ext.define('App.view.dbconsole.DbInfoDetailForm', {
	extend : 'Ext.form.Panel',
	alias : 'widget.dbinfodetailform',
	trackResetOnLoad:true,
	layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
	defaultType: 'textfield',
	items : [{
		fieldLabel : 'Name',
		name : 'name',
		allowBlank : false
	}, {
		fieldLabel : 'URL',
		name : 'url',
		allowBlank : false
	}, {
		fieldLabel : 'vendor',
		name : 'vendor',
		allowBlank : false
	}, {
		fieldLabel : 'driverClass',
		name : 'driverClass',
		allowBlank : false
	}, {
		fieldLabel : 'username',
		name : 'dbUser'
	}, {
		fieldLabel : 'password',
		name : 'dbPasswd'
	}],
	initComponent : function() {
		this.callParent();
	},
	// Reset and Submit buttons
	buttons : [{
		text : 'Reset',
		handler : function() {
			this.up('form').getForm().reset();
		}
	}, {
		text : 'Submit',
		formBind : true, // only enabled once the form is valid
		disabled : true,
		handler : function() {
			var form = this.up('form').getForm();
			if (form.isValid()) {
				App.log('summit modify/add db info.');
				form.updateRecord();
				var store = form.getRecord().store;
				store.sync({
					success:function(batch,options){
						var current = store.currentPage;
			            store.loadPage(current);
					}
				});
//				rec.setProxy(rec.store.getProxy());
//				rec.save({
//					callback:function(){
//				        var current = rec.store.currentPage;
//			            rec.store.loadPage(current);
//						App.log('record saved.');
//					}
//				});
			}
		}
	}],
	updateDetail : function(rec) {
		App.log('loading rec .' ,rec);
		this.loadRecord(rec);
		App.log('loaded rec .' ,rec);
		
	}
});
