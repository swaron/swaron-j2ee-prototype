Ext.define('App.store.TableTree', {
	requires:['Ext.data.proxy.Rest'],
	extend : 'Ext.data.TreeStore',
	model : 'App.model.EntityName',
	proxy:{
		type : 'rest',
		format : 'json',
		extraParams:{
			database:''
		},
		url : App.cfg.restUrl + '/rest/dbinfo/customdb/table-tree',
		reader : {
			type : 'json',
			root : 'records'
		},
		writer : {
			type : 'json'
		}
	},
	config:{
		database : null
	},
	constructor:function(){
		this.callParent(arguments);
		this.getProxy().setExtraParam("database",this.getDatabase());
	}
});