Ext.define('App.store.TableTree', {
	requires:['Ext.data.proxy.Rest'],
	extend : 'Ext.data.TreeStore',
	proxy:{
		type : 'rest',
		format : 'json',
//		extraParams:{
//			database:''
//		},
		url : App.cfg.restUrl + '/rest/dbinfo/customdb/table-tree'	
	},
	root: {
        text: 'Root',
        expanded: true
    },
//	config:{
//		database : null
//	},
	listeners : {
		beforeload : function(store ,operation,eOpts) {
			App.log('beforeload tree store: ',operation)
		}
	},
	constructor:function(){
		this.callParent(arguments);
//		this.getProxy().setExtraParam("database",this.getDatabase());
	}
});