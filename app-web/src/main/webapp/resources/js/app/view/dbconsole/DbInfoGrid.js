Ext.define('App.view.dbconsole.DbInfoGrid', {
	requires : [
	    'App.service.CodeService',
	    'Ext.data.Store',
	    'Ext.toolbar.Paging'
	],

	extend : 'Ext.grid.Panel',
	alias : 'widget.dbinfogrid',

	store : 'DbInfo',
	title : 'DataSource Info Grid',
	frame : false,
	selModel : {
		selType : 'rowmodel'
	},
	initComponent : function() {
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
			store : this.store, // same store GridPanel is using
			dock : 'bottom',
			displayInfo : true
		}];
		this.callParent(arguments);
	}
});
