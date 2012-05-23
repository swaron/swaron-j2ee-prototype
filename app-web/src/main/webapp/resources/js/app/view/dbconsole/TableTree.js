Ext.define('App.view.dbconsole.TableTree', {
	requires : [
		'Ext.data.Store',
		'App.store.EntityName'
	],
	extend : 'Ext.tree.Panel',
	alias : 'widget.tableTree',
	title : 'Database - Table Tree',
	frame : false,
	rootVisible:false,
	singleExpand:true,
	initComponent : function() {
		if(!this.store){
			this.store = Ext.create('App.store.TableName');
		}
		this.callParent(arguments);
	}
});
