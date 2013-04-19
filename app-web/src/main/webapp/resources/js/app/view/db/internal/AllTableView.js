Ext.define('App.view.db.internal.AllTableView', {
	requires : ['App.view.db.internal.TableList', 'App.view.db.TableGrid', 'Ext.layout.container.Border',
			'Ext.tab.Panel','App.service.GridConfig'],
	extend : 'Ext.panel.Panel',
	alias : 'widget.alltableview',
	title : 'Tables and Table Content CRUD Function',
	frame : true,
	layout:'border',
	items : [{
		region : 'west',
		border:false,
		xtype : 'tablelist',
		itemId : 'tableList',
		name : 'tableList',
		collapsible: true,
		margins : '0 5 5 5',
		width : 200 
	}, {
		xtype : 'tabpanel',
		itemId : 'centerBox',
		border:false,
		name : 'centerBox',
		region : 'center',
		height : 500,
		margins : '0 5 5 0'
	}],
	createChildTab:function(tabs, tableName){
//		var tab = centerBox.getActiveTab();
		Ext.create('App.service.GridConfig', {
			tableName : tableName,
			listeners : {
				load : function(gConfig) {
					var tableGrid = Ext.create('App.view.db.TableGrid', {
						gridConfig : gConfig,
						itemId:tableName,
						closable : true,
						title : tableName
					});
					tabs.add(tableGrid);
					tabs.setActiveTab(tableGrid);
					tableGrid.store.load();
				}
			}
		});
	},
	onSelectTable:function(view, record, el, index){
		var tabs = this.down('#centerBox');
		var tableName = record.get('tableName');

		if(tabs.child('#' + tableName)){
			tabs.setActiveTab(tableName);
		}else{
			if(tabs.items.length >= 10){
				var firstItem = tabs.child('.tablegrid');
				tabs.remove(firstItem);
			}
			this.createChildTab(tabs,tableName);
		}
	},
	listeners : {
		afterrender : function(cmp) {
			var tableList = cmp.down('#tableList');
			tableList.getStore().load();
			var centerBox = cmp.down('#centerBox');
			tableList.on('itemclick', cmp.onSelectTable , cmp);
		}
	}
});
