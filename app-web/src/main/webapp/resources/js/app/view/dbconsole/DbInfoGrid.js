Ext.define('App.view.dbconsole.DbInfoGrid', {
	requires : [
	    'App.service.CodeService',
	    'Ext.data.Store',
	    'App.store.DbInfo',
	    'Ext.toolbar.Paging'
	],

	extend : 'Ext.grid.Panel',
	alias : 'widget.dbinfogrid',

	store : 'DbInfo',
	title : 'DataSource Info Grid',
	header:false,
	frame : false,
	selModel : {
		selType : 'rowmodel'
	},
	initComponent : function() {
		this.store = Ext.getStore('DbInfo');
		if(!this.store){
			this.store = Ext.create('App.store.DbInfo',{
				storeId : 'DbInfo'
			});
		}
		this.columns = [{
			text : 'Name',
			width : 120,
			sortable : true,
			dataIndex : 'name'
		}, {
			text : 'Url',
			flex : 1,
			sortable : true,
			dataIndex : 'url'
		}];
		this.dockedItems = [{
			xtype : 'pagingtoolbar',
			store : this.store, // same store GridPanel is using
			dock : 'bottom',
			displayInfo : true
		}];
		this.callParent(arguments);
	}
});
