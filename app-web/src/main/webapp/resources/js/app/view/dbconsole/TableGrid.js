Ext.define('App.view.dbconsole.TableGrid', {
	requires : ['App.service.CodeService', 'Ext.data.Store', 'Ext.toolbar.Paging'],
//	wait: ['App.lazy.GridConfigFactory',{
//		table:'SecUser'
//	}],
	extend : 'Ext.grid.Panel',
	alias : 'widget.tablegrid',
	baseUrl : App.cfg.restUrl + '/rest/table/{table}',
	tableName : undefined,
	config : {
		table : undefined
	},
	title : 'DataSource Info Grid',
	frame : false,
	selModel : {
		selType : 'rowmodel'
	},
	constructor : function(config) {
		var table = config.table;
		this.tableName = table;
		this.baseUrl = this.baseUrl.replace(/{table}/g, table);
		this.callParent(arguments);
		config = config || {};
	},
	buildStore:function(){
		var name = App.lazy.GridConfig.storeId;
		var store = Ext.StoreManager.get(name);
		this.store = store;
	},
	initComponent : function() {
		this.buildStore();
		this.columns = App.lazy.GridConfig.gridColumns;
		this.callParent(arguments);
	}
});
