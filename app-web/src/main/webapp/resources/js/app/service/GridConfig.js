Ext.define('App.service.GridConfig', {
	requires : ['Ext.data.Store', 'Ext.data.Model','Ext.data.reader.Json'],
    mixins: {
        observable: 'Ext.util.Observable'
    },
    config:{
    	name : null
    },
	typeMapping : {
		'Timestamp' : 'date',
		'Boolean' : 'boolean',
		'Integer' : 'int',
		'Long' : 'int',
		'Double' : 'float',
		'Float' : 'float',
		'String' : 'string'
	},
	gridXTypeMapping : {
		'Timestamp' : 'datecolumn',
		'Boolean' : 'checkcolumn'
	},
	modelName : null,
	storeName : null,
	storeId : null,
	gridColumns : [],
	constructor: function (config) {
        this.mixins.observable.constructor.call(this, config);
        this.addEvents(
            'load'
        );
        this.callParent(arguments);
        this.load();
    },
	getGridColumns : function() {
		var cols = this.gridColumns;
		for (var i = 0, ln = cols.length; i < ln; i++) {
			var col = cols[i];
			if (col.hasAlias == true) {
				col.renderer = App.Code.converter(col.tableName, col.columnName);
				col.field = {
					xtype : 'combobox',
					store : App.Code.store(col.tableName, col.columnName),
					queryMode : 'local',
					displayField : 'name',
					valueField : 'code'
				}
			}
		}
		return cols;
	},
	load : function() {
		var tableName = this.getName();
		this.modelName = Ext.getClassName(this)+".model."+tableName;
		this.storeName = Ext.getClassName(this)+".store."+ tableName;
		this.storeId = 'store-' + tableName;
		Ext.Ajax.request({
			url : App.cfg.restUrl + '/rest/grid/config/' + tableName + '.json',
			success : function(response, opts) {
				var self = this;
				var obj = Ext.decode(response.responseText);
				var fields = [];
				if (Ext.isArray(obj.columns)) {
					for (var index = 0; index < obj.columns.length; index++) {
						var column = obj.columns[index];
						var field = {};
						field.name = column.name;
						field.dbName = column.mapping;
						field.hide = column.hide;
						if (self.typeMapping.hasOwnProperty(column.type)) {
							field.type = self.typeMapping[column.type];
						} else {
							field.type = 'auto';
						}
						if (field.type == 'date') {
							field.dateFormat = 'time'
						}
						fields.push(field);
					}
				}
				var gridColumns = [];
				if (Ext.isArray(obj.columns)) {
					for (var index = 0; index < obj.columns.length; index++) {
						var column = obj.columns[index];
						var field = {};
						field.tableName = obj.tableName;
						field.columnName = column.mapping;
						field.header = column.mapping;
						field.dataIndex = column.name;
						field.sortable = true;
						if (self.gridXTypeMapping.hasOwnProperty(column.type)) {
							field.xtype = self.gridXTypeMapping[column.type];
						}

						field.filter = {
							xtype : 'textfield'
						};
						field.field = {
							xtype : 'textfield'
						}
						if (field.xtype == 'datecolumn') {
							field.format = 'Y-m-d';
							field.field = {
								xtype : 'datefield',
								format : 'Y-m-d'
							}
						}
						if (column.hasAlias == true) {
							field.hasAlias = true;
						}
						gridColumns.push(field);
					}
				}
				this.gridColumns = gridColumns;
				var modelName = this.modelName;
				var storeName = this.storeName;
				if(!Ext.ClassManager.isCreated(modelName)){
					Ext.define(modelName, {
						extend : 'Ext.data.Model',
						idProperty : obj.idProperty ? obj.idProperty : undefined,
						fields : fields
					});
				}
				var gridConfig = this;
				if(!Ext.ClassManager.isCreated(storeName)){
					Ext.define(storeName, {
						extend : 'Ext.data.Store',
						storeId : 'store-' + obj.tableName,
						remoteSort : true,
						remoteFilter : true,
						autoLoad : true,
						autoSync : true,
						pageSize : 16,
						model : modelName,
						proxy : {
							type : 'rest',
							format : 'json',
							url : App.cfg.restUrl + '/rest/table/' + obj.tableName,
							reader : {
								type : 'json',
								root : 'records'
							},
							writer : {
								type : 'json'
							}
						}
					},function(){
						gridConfig.fireEvent('load',gridConfig);
					});
				}else{
					gridConfig.fireEvent('load', gridConfig);
				}
			},
			scope : this
		});
	}
});
