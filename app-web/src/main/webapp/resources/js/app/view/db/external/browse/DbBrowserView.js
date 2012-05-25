Ext.define('App.view.db.external.browse.DbBrowserView', {
	requires : ['App.view.db.external.browse.TableTree', 'App.view.db.TableGrid', 'Ext.layout.container.Border',
			'Ext.tab.Panel','App.service.GridConfig'],
	extend : 'Ext.panel.Panel',
	alias : 'widget.dbbrowserview',
	title : 'Tables and Table Content CRUD Function',
	frame : true,
	layout:'border',
	config:{
		maxTabs:10,
		database: App.getParam('database')
	},
	initComponent : function() {
		var items = [{
			region : 'west',
			xtype : 'tabletree',
			itemId : 'tableTree',
			name : 'tableTree',
			collapsible: true,
			margins : '0 5 5 5',
			store: Ext.create('App.store.TableTree',{
				database: this.getDatabase(),
				autoLoad:true
			}),
			width : 200 
		}, {
			xtype : 'tabpanel',
			itemId : 'centerBox',
			name : 'centerBox',
			region : 'center',
			height : 500,
			margins : '0 5 5 0'
		}];
		this.items = items;
		this.callParent(arguments);
	},
	createChildTab:function(tabs, dbInfoId, tableName){
//		var tab = centerBox.getActiveTab();
		var name = dbInfoId + '.'+ tableName;
		Ext.create('App.service.GridConfig', {
			dbInfoId : dbInfoId,
			tableName : tableName,
			listeners : {
				load : function(gConfig) {
					var tableGrid = Ext.create('App.view.db.TableGrid', {
						gridConfig : gConfig,
						itemId: name,
						closable : true,
						title : name
					});
					tabs.add(tableGrid);
					tabs.setActiveTab(tableGrid);
				}
			}
		});
	},
	onSelectTable:function(view, record, el, index){
		var dbInfoId,tableName;
		var tabs = this.down('#centerBox');
		if(record.isLeaf()){
			var parent = record.parentNode;
			dbInfoId = parent.get('id'); 
			tableName = record.get('text');
		}else{
			App.log('clicking on folder node fired a itemclick event.')
			return ;
		}
		var name = dbInfoId + '.'+ tableName;
		if(tabs.child('#' + name)){
			tabs.setActiveTab(name);
		}else{
			if(tabs.items.length >= this.getMaxTabs()){
				var firstItem = tabs.child('box');
				tabs.remove(firstItem);
			}
			this.createChildTab(tabs,dbInfoId, tableName);
		}
	},
	listeners : {
		afterrender : function(cmp) {
			var tableTree = cmp.down('#tableTree');
			var centerBox = cmp.down('#centerBox');
			tableTree.on('itemclick', cmp.onSelectTable , cmp);
		}
	}
});
