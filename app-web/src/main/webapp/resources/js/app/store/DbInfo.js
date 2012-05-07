Ext.define('App.store.DbInfo', {
	extend: 'Ext.data.Store',
//	remoteSort : true,
//	remoteFilter : true,
//	autoLoad : true,
//	autoSync : true,
	pageSize : 16,
	model : 'App.model.DbInfo',
	proxy : {
		type : 'rest',
		format : 'json',
		url : App.cfg.restUrl + '/rest/dbinfo',
		reader : {
			type : 'json',
			successProperty : 'success',
			messageProperty : 'message',
			root : 'records'
		},
		writer : {
			type : 'json'
		}
	},
	listeners: {
			beforesync: function(options, eOpts){
		    	App.log('before sync', options);
		    },
            load: function(store, records,successful){
            	App.log('record loaded');
            },
            remove: function(stroe,record,index){
            	App.log('record removed');
            },
            update: function(stroe,record,operation){
            	if(operation == Ext.data.Model.EDIT){
	            	App.log('record edited');
            	}else if(operation == Ext.data.Model.COMMIT){
	            	App.log('record commited');
            	}
            },
            write: function(store, operation){
            	if(!operation.wasSuccessful()){
            		return;
            	}
            }
        }
});