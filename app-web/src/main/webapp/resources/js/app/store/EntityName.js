Ext.define('App.store.EntityName', {
	requires:['Ext.data.proxy.Rest'],
	extend : 'Ext.data.Store',
	fields : [{
		name : 'name',
		type : 'string'
	}],
	model : 'App.model.EntityName',
	proxy:{
		type : 'rest',
		format : 'json',
		url : App.cfg.restUrl + '/rest/table-list',
		reader : {
			type : 'json',
			root : 'records'
		},
		writer : {
			type : 'json'
		}
	}
});