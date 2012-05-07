Ext.define('App.view.sample.secuser.SecUserGrid', {
	requires : [
	    'App.service.CodeService',
	    'Ext.grid.plugin.RowEditing',
	    'Ext.ux.grid.HeaderFilters',
	    'Ext.grid.column.Date',
	    'Ext.data.Store',
	    'App.model.SecUser',
	    'Ext.ux.CheckColumn',
	    'Ext.form.field.ComboBox',
	    'Ext.form.field.Date',
	    'Ext.form.field.Checkbox',
	    'Ext.toolbar.Paging'
	],

	extend : 'Ext.grid.Panel',
	alias : 'widget.secusergrid',

	store : 'SecUser',
	title : 'Table sec_user',
	frame : true,
	iconCls : 'icon-user',
	selModel : {
		selType : 'rowmodel'
	},
	listeners : {
		selectionchange : function(selModel, selections) {
			this.down('#delete').setDisabled(selections.length == 0);
			App.log('select change listener in grid itself.');
		}
	},
	// initEvents : function() {
	// this.on('selectionchange', function(selModel, selections) {
	// this.down('#delete').setDisabled(selections.length == 0);
	// }, this);
	// this.callParent(arguments);
	// },
	initComponent : function() {
		var headerFilter = Ext.create('Ext.ux.grid.HeaderFilters');
		var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
			errorSummary : false
		});
		this.headerFilter = headerFilter;
		this.rowEditing = rowEditing;
		this.plugins = [rowEditing, headerFilter];
		this.columns = [{
			text : 'Username',
			width : 80,
			sortable : true,
			dataIndex : 'username',
			field : {
				xtype : 'textfield',
				allowBlank : false
			},
			filter : {
				xtype : 'textfield'
			}
		}, {
			text : 'Email',
			flex : 1,
			sortable : true,
			dataIndex : 'email',
			field : {
				xtype : 'textfield',
				allowBlank : false
			},
			filter : {
				xtype : 'textfield'
			}
		}, {
			text : 'Login State',
			width : 100,
			sortable : true,
			dataIndex : 'loginState',
			renderer : App.Code.converter('sec_user', 'login_state'),
			field : {
				xtype : 'combobox',
				store : App.Code.store('sec_user', 'login_state'),
				queryMode : 'local',
				displayField : 'name',
				valueField : 'code'
			},
			filter : {
				xtype : 'textfield'
			}
		}, {
			text : 'Lock Time',
			width : 150,
			sortable : true,
			dataIndex : 'lockedTime',
			xtype : 'datecolumn',
			format : 'Y-m-d',
			field : {
				xtype : 'datefield',
				format : 'Y-m-d'
			},
			filter : {
				xtype : 'textfield'
			}
		}, {
			text : 'Last Login Date',
			width : 150,
			sortable : true,
			dataIndex : 'lastLoginTime',
			xtype : 'datecolumn',
			format : 'Y-m-d',
			field : {
				xtype : 'datefield',
				format : 'Y-m-d'
			},
			filter : {
				xtype : 'textfield'
			}
		}, {
			header : 'LoginFailedCount',
			width : 100,
			sortable : true,
			dataIndex : 'loginFailedCount',
			field : {
				xtype : 'textfield',
				allowBlank : false
			},
			filter : {
				xtype : 'textfield'
			}
		}, {
			xtype : 'checkcolumn',
			header : 'Enabled',
			dataIndex : 'enabled',
			width : 60,
			editor : {
				xtype : 'checkbox',
				cls : 'x-grid-checkheader-editor',
				allowBlank : false
			},
			filter : {
				xtype : 'textfield'
			}
		}];
		this.tbar = [{
			text : 'Add',
			iconCls : 'icon-add',
			handler : function(button) {
				var grid = button.up('secusergrid');
				// Create a model instance
				grid.getStore().insert(0, {});
				rowEditing.startEdit(grid.getStore().first(), 0);
			}
		}, {
			itemId : 'delete',
			text : 'Delete',
			iconCls : 'icon-delete',
			handler : function(button) {
				rowEditing.cancelEdit();
				var grid = this;
				grid.getStore().remove(grid.getSelectionModel().getSelection());
				if (grid.getStore().getCount() > 0) {
					grid.getSelectionModel().select(0);
				}
			},
			scope:this,
			disabled : true
		}];
		this.dockedItems = [{
			xtype : 'pagingtoolbar',
			store : 'SecUser', // same store GridPanel is using
			dock : 'bottom',
			displayInfo : true
		}];
		this.callParent(arguments);
	}
});
