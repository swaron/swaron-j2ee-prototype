Ext.define('App.view.db.external.dbinfo.DbInfoMaster', {
	extend : 'Ext.Panel',
	alias : 'widget.dbinfomaster',
	requires : ['Ext.layout.container.Border','App.view.db.external.dbinfo.DbInfoGrid','App.view.db.external.dbinfo.DbInfoDetailForm'],
	frame : true,
	title : 'Database List',
	width : 540,
	height : 400,
	layout : 'border',

	// override initComponent
	initComponent : function() {
		this.items = [{
			xtype : 'dbinfogrid',
			itemId : 'gridPanel',
			region : 'north',
			height : 210,
			split : true
		}, {
			disabled:true,
			xtype : 'dbinfodetailform',
			bodyPadding : '20 10 10 10',
			itemId : 'detailPanel',
			region : 'center'
		}];
		this.tbar = [{
			text : 'Copy',
			itemId : 'copy',
			iconCls : 'icon-tags',
			disabled : true,
			handler : function(button) {
				var master = button.up('dbinfomaster');
				var grid = master.down('dbinfogrid');
				var selected = grid.getSelectionModel().getLastSelected();
				var newRec = selected.copy();
				newRec.setId(null); // set id to null to make it as phantom
				grid.getStore().add(newRec);
				grid.getSelectionModel().select(newRec)
			}
		}, {
			text : 'Add',
			iconCls : 'icon-plus-sign',
			handler : function(button) {
				var master = button.up('dbinfomaster');
				var grid = master.down('dbinfogrid');
				// Create a model instance
				grid.getStore().insert(0, {});
				grid.getSelectionModel().select(0)
			}
		}, {
			itemId : 'delete',
			text : 'Delete',
			iconCls : 'icon-remove-sign',
			handler : function(button) {
				var master = button.up('dbinfomaster');
				var grid = master.down('dbinfogrid');
				grid.getStore().remove(grid.getSelectionModel().getSelection());
				grid.getStore().sync();
				if (grid.getStore().getCount() > 0) {
					grid.getSelectionModel().select(0);
				}
			},
			scope:this,
			disabled : true
		}];
		// call the superclass's initComponent implementation
		this.callParent();
	},
	// override initEvents
	initEvents : function() {
		// call the superclass's initEvents implementation
		this.callParent();

		// now add application specific events
		// notice we use the selectionmodel's rowselect event rather
		// than a click event from the grid to provide key navigation
		// as well as mouse navigation
		var bookGridSm = this.getComponent('gridPanel').getSelectionModel();
		bookGridSm.on('selectionchange', this.onRowSelect, this);
	},
	// add a method called onRowSelect
	// This matches the method signature as defined by the 'rowselect'
	// event defined in Ext.selection.RowModel
	onRowSelect : function(sm, rs) {
		// getComponent will retrieve itemId's or id's. Note that itemId's
		// are scoped locally to this instance of a component to avoid
		// conflicts with the ComponentManager
		App.log('selection change');
		this.down('#delete').setDisabled(rs.length == 0);
		this.down('#copy').setDisabled(rs.length == 0);
		var detailPanel = this.getComponent('detailPanel');
		if (rs.length) {
			App.log('selected records length: ' + rs.length);
			detailPanel.enable();
			detailPanel.updateDetail(rs[0]);
		}else{
			detailPanel.disable();
		}

	}
});
