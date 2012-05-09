Ext.define('App.lazy.GridConfigFactory',{
	requires:['Ext.data.Store','Ext.data.Model'],
	constructor : function(config){
		this.newInstance(config);
		return this;
	},
	statics: {
		storeId : 'lazy.DbTable'
    },
	newInstance: function(config){
		var tableName = config.table;
		var typeMapping = {
			'Timestamp' : 'date',
			'Boolean' : 'boolean',
			'Integer' : 'int',
			'Long' : 'int',
			'Double' : 'float',
			'Float' : 'float',
			'String' : 'string'
		}
		var gridXTypeMapping = {
			'Timestamp' : 'datecolumn',
			'Boolean' : 'checkcolumn'
		}
		var storeId = this.statics().storeId;
		var store = Ext.data.StoreManager.lookup(storeId);
		if (!store) {
			Ext.Ajax.request({
				url : App.cfg.restUrl + '/rest/table_metadata/' + tableName,
				success : function(response, opts) {
					var obj = Ext.decode(response.responseText);
					var fields = [];
					if (Ext.isArray(obj.columns)) {
						for (var index = 0; index < obj.columns.length; index++) {
							var column = obj.columns[index];
							var field = {};
							field.name = column.name;
							field.dbName = column.mapping;
							field.hide = column.hide;
							if (typeMapping.hasOwnProperty(column.type)) {
								field.type = typeMapping[column.type];
							} else {
								filed.type = 'auto';
							}
							fields.push(field);
						}
					}
					var gridColumns = [];
					if (Ext.isArray(obj.columns)) {
						for (var index = 0; index < obj.columns.length; index++) {
							var column = obj.columns[index];
							var field = {};
							field.text = column.mapping;
							field.dataIndex = column.name;
							field.sortable = true;
							if (gridXTypeMapping.hasOwnProperty(column.type)) {
								field.xtype = gridXTypeMapping[column.type];
							}
							gridColumns.push(field);
						}
					}
					var modelName = 'App.lazy.model.DbTable';
					var storeName = 'App.lazy.store.DbTable';
					Ext.define(modelName, {
						extend : 'Ext.data.Model',
						idProperty : obj.idProperty ? obj.idProperty : undefined,
						fields : fields
					});
					Ext.define(storeName, {
						storeId : storeId,
						extend : 'Ext.data.Store',
						remoteSort : true,
						remoteFilter : true,
						autoLoad : true,
						autoSync : true,
						pageSize : 16,
						model : modelName,
						proxy : {
							type : 'rest',
							format : 'json',
							url : App.cfg.restUrl + '/rest/table/' + tableName,
							reader : {
								type : 'json',
								root : 'records'
							},
							writer : {
								type : 'json'
							}
						}
					});
					Ext.define('App.lazy.GridConfig',{
						model: modelName,
						store: storeId,
						gridColumns: gridColumns
					},function(){
						Ext.Loader.notify(['App.lazy.GridConfigFactory'])
					});
				},
				scope : this
			});
		} else {
			App.log('lazy resource already prepared.');
		}
	}
}, function(){
	console.log('App.lazy.GridConfigFactory has been defined.');
});
