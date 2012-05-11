Ext.define('App.view.dbconsole.TableList', {
	requires : [
		'Ext.data.Store',
		'App.store.EntityName'
	],
	extend : 'Ext.grid.Panel',
	alias : 'widget.tablelist',
	title : 'Table List',
	frame : false,
	hideHeaders:true,
	columns:[{
			header : 'Name',
			flex : 1,
			sortable : false,
			dataIndex : 'tableName'
	}],
	initComponent : function() {
		this.store = Ext.create('App.store.EntityName');
		this.callParent(arguments);
	}
});
