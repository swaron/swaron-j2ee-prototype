Ext.define('App.view.dbconsole.DbInfoMaster', {
	extend : 'Ext.Panel',
	alias : 'widget.dbinfomaster',

	frame : true,
	title : 'Book List',
	width : 540,
	height : 400,
	layout : 'border',

	// override initComponent
	initComponent : function() {
		this.items = [{
			xtype : 'bookgrid',
			itemId : 'gridPanel',
			region : 'north',
			height : 210,
			split : true
		}, {
			xtype : 'bookdetail',
			itemId : 'detailPanel',
			region : 'center'
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
		('selectionchange', function(sm, rs) {
			if (rs.length) {
				var detailPanel = Ext.getCmp('detailPanel');
				bookTpl.overwrite(detailPanel.body, rs[0].data);
			}
		})
		bookGridSm.on('selectionchange', this.onRowSelect, this);
	},
	// add a method called onRowSelect
	// This matches the method signature as defined by the 'rowselect'
	// event defined in Ext.selection.RowModel
	onRowSelect : function(sm, rs) {
		// getComponent will retrieve itemId's or id's. Note that itemId's
		// are scoped locally to this instance of a component to avoid
		// conflicts with the ComponentManager
		if (rs.length) {
			var detailPanel = this.getComponent('detailPanel');
			detailPanel.updateDetail(rs[0].data);
		}

	}
});
