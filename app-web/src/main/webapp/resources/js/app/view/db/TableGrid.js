Ext.define('App.view.db.TableGrid', {
	requires : [
	    'App.service.GridConfig',
	    'App.service.CodeService',
	    'Ext.grid.plugin.RowEditing',
	    'Ext.ux2.grid.HeaderFilters',
	    'Ext.ux2.grid.feature.AutoColumnWidth',
	    'Ext.grid.column.Date',
	    'Ext.data.Store',
	    'Ext.data.reader.Json',
	    'Ext.ux.CheckColumn',
	    'Ext.form.field.ComboBox',
	    'Ext.form.field.Date',
	    'Ext.form.field.Checkbox',
	    'Ext.toolbar.Paging'
	],
	extend : 'Ext.grid.Panel',
	alias : 'widget.tablegrid',
	config : {
		gridConfig : null
	},
	title : 'DataSource Info Grid',
	frame : false,
	selModel : {
		selType : 'rowmodel'
	},
	buildStore:function(){
		var storeName = this.getGridConfig().storeName;
		this.store = Ext.create(storeName);
	},
	initComponent : function() {
		var autoWidthFeature = Ext.create('Ext.ux2.grid.feature.AutoColumnWidth',{
			autoWidth:true,
			maxColWidth:200,
			minColWidth:20
		});
		this.features =  [autoWidthFeature];
		var headerFilter = Ext.create('Ext.ux2.grid.HeaderFilters');
		var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
			errorSummary : false
		});
		this.plugins = [rowEditing, headerFilter];
		this.buildStore();
		this.columns = this.getGridConfig().getGridColumns();
		this.dockedItems = [{
			xtype : 'pagingtoolbar',
			store : this.store,
			dock : 'bottom',
			displayInfo : true
		}];
		this.callParent(arguments);
	}
});
