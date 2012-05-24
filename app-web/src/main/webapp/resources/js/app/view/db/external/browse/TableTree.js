Ext.define('App.view.db.external.browse.TableTree', {
	requires : [
		'Ext.data.Store',
		'App.store.TableTree'
	],
	extend : 'Ext.tree.Panel',
	alias : 'widget.tabletree',
	title : 'Database - Table Tree',
	frame : false,
	rootVisible:false,
	singleExpand:false,
	initComponent : function() {
		if(!this.store){
			this.store = Ext.create('App.store.TableTree');
		}
		this.callParent(arguments);
	}
});
