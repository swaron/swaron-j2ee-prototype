Ext.loader.requireSingleton('App.lazy.GridConfig',{
	table:'SecUser'
});
Ext.define('App.view.dbconsole.TableGrid', {
	requires : ['App.service.CodeService', 'Ext.data.Store', 'Ext.toolbar.Paging'],
	wait: ['App.lazy.GridConfig',{
		table:'SecUser'
	}],
	extend : 'Ext.grid.Panel',
	alias : 'widget.tablegrid',
	baseUrl : App.cfg.restUrl + '/rest/table/{table}',
	tableName : undefined,
	requestingGridConfig:false,
	config : {
		table : undefined
	},
	// store : Ext.create('Ext.data.ArrayStore', {
	// autoDestroy: true,
	// storeId: 'dummyStore',
	// fields: [
	// 'name'
	// ],
	// data: []
	// }),
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
		var name = 'table.' + this.tableName;
		var store = Ext.StoreManager.get(name);
		this.store = store;
	},
	initComponent : function() {
		this.buildStore();
		this.columns = [{
			text : 'Name',
			width : 120,
			sortable : true,
			dataIndex : 'name'
		}, {
			text : 'Url',
			flex : 1,
			sortable : true,
			dataIndex : 'email'
		}];
		this.dockedItems = [{
			xtype : 'pagingtoolbar',
			store : this.getStore(), // same store GridPanel is using
			dock : 'bottom',
			displayInfo : true
		}];
		this.callParent(arguments);
	}
});
