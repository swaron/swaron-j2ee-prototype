Ext.define('App.store.SecUser', {
	extend: 'Ext.data.Store',
	remoteSort : true,
	remoteFilter : true,
	autoLoad : true,
	autoSync : true,
	pageSize : 16,
	model : 'App.model.SecUser',
	proxy : {
		type : 'rest',
		format : 'json',
		url : App.cfg.restUrl + '/rest/users',
		reader : {
			type : 'json',
			root : 'records'
		},
		writer : {
			type : 'json'
		}
	}
});