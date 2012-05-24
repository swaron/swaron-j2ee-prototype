Ext.define('App.store.DbInfo', {
	extend: 'Ext.data.Store',
	requires:['Ext.data.proxy.Rest','Ext.data.reader.Json'],
//	remoteSort : true,
//	remoteFilter : true,
	autoLoad : true,
//	autoSync : true,
	pageSize : 16,
	model : 'App.model.DbInfo',
	proxy : {
		type : 'rest',
		format : 'json',
		url : App.cfg.restUrl + '/rest/db-info',
		reader : {
			type : 'json',
			root : 'records'
		}
	}
});