Ext.define('App.view.dbconsole.AllTableView', {
	requires : ['App.view.dbconsole.TableList', 'App.view.dbconsole.TableGrid', 'Ext.layout.container.Border',
			'Ext.tab.Panel','App.service.GridConfig'],
	extend : 'Ext.panel.Panel',
	alias : 'widget.alltableview',
	title : 'Tables and Table Content CRUD Function',
	frame : true,
	layout:'border',
	items : [{
		region : 'west',
		xtype : 'tablelist',
		itemId : 'tableList',
		name : 'tableList',
		title : 'Table List',
		collapsible: true,
		margins : '0 5 5 5',
		width : 200 
	}, {
		xtype : 'tabpanel',
		itemId : 'centerBox',
		name : 'centerBox',
		region : 'center',
		height : 500,
		margins : '0 5 5 0'
	}],
	createChildTab:function(tabs, tableName){
//		var tab = centerBox.getActiveTab();
		Ext.create('App.service.GridConfig', {
			name : tableName,
			listeners : {
				load : function(gConfig) {
					var tableGrid = Ext.create('App.view.dbconsole.TableGrid', {
						gridConfig : gConfig,
						itemId:tableName,
						closable : true,
						title : tableName
					});
					tabs.add(tableGrid);
					tabs.setActiveTab(tableGrid);
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
				var firstItem = tabs.child('box');
				tabs.remove(firstItem);
			}
			this.createChildTab(tabs,tableName);
		}
	},
	listeners : {
		afterrender : function(cmp) {
			App.log('AllTableView afterrender');
			var tableList = cmp.down('#tableList');
			tableList.getStore().load();
			var centerBox = cmp.down('#centerBox');
			tableList.on('itemclick', cmp.onSelectTable , cmp);
		}
	}
});
