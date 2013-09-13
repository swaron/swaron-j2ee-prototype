Ext.define('App.service.GridConfig', {
	requires : ['Ext.data.Store', 'App.service.CodeService','Ext.data.Model','Ext.data.reader.Json'],
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
				col.renderer = App.DictCode.converter(col.tableName, col.columnName);
				col.field = {
					xtype : 'combobox',
					store : App.DictCode.store(col.tableName, col.columnName),
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
						field.name = column.entityName;
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
						var col = {};
						col.tableName = obj.tableName;
						col.columnName = column.columnName;
						col.header = column.columnName;
						col.dataIndex = column.entityName;
						col.sortable = true;
						if (self.gridXTypeMapping.hasOwnProperty(column.type)) {
							col.xtype = self.gridXTypeMapping[column.type];
						}

						col.filter = {
							xtype : 'textfield'
						};
						col.field = {
							xtype : 'textfield'
						}
						if (col.xtype == 'datecolumn') {
							col.format = 'Y-m-d';
							col.field = {
								xtype : 'datefield',
								format : 'Y-m-d'
							}
						}
						if (column.hasAlias == true) {
							col.hasAlias = true;
						}
						if(column.columnName == obj.idProperty){
							delete col.field;
						}
						gridColumns.push(col);
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
