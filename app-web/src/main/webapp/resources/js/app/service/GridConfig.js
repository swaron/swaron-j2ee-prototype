Ext.define('App.service.GridConfig', {
	requires : ['Ext.data.Store', 'Ext.data.Model','Ext.data.reader.Json'],
    mixins: {
        observable: 'Ext.util.Observable'
    },
    config:{
		dbInfoId : 'sys',
		tableName : null
    },
    url : App.cfg.restUrl + '/rest/grid-config.json',
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
		var dbInfoId = this.getDbInfoId();
		var tableName = this.getTableName();
		this.modelName = Ext.getClassName(this)+".model."+dbInfoId + '-' +tableName;
		this.storeName = Ext.getClassName(this)+".store."+dbInfoId + '-' + tableName;
		this.storeId = 'store-' + tableName;
		Ext.Ajax.request({
			method : 'GET',
			params:{
				dbInfoId : dbInfoId,
				tableName : tableName
			},
			url : this.url,
			success : function(response, opts) {
				var self = this;
				var obj = Ext.decode(response.responseText);
				var fields = [];
				if (Ext.isArray(obj.columns)) {
					for (var index = 0; index < obj.columns.length; index++) {
						var column = obj.columns[index];
						var field = {};
						field.name = column.name;
						field.dbName = column.dbName;
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
						field.columnName = column.dbName;
						field.header = column.dbName;
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
				var db = gridConfig.getDbInfoId();
				var tableContentUrl;
				if(db == 'sys'){
					tableContentUrl = App.cfg.restUrl + '/rest/internal-table-content/' + obj.tableName;
				}else{
					tableContentUrl = App.cfg.restUrl + '/rest/external-table-content/' + db + '/' + obj.tableName;
					
				}
				if(!Ext.ClassManager.isCreated(storeName)){
					Ext.define(storeName, {
						extend : 'Ext.data.Store',
						storeId : 'store-' + db + '-' + obj.tableName,
						remoteSort : true,
						remoteFilter : true,
						autoLoad : false,
						autoSync : true,
						pageSize : 16,
						model : modelName,
						proxy : {
							type : 'rest',
							format : 'json',
							url : tableContentUrl,
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
