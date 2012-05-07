

Ext.namespace('App.lazy');
Ext.apply(Page.lazy, {
	'App.lazy.store.DbTable' : function(tableName) {
		var typeMapping = {
			'Timestamp' : 'date',
			'Boolean' : 'boolean',
			'Integer' : 'int',
			'Long' : 'int',
			'Double' : 'float',
			'Float' : 'float',
			'String' : 'string'
		}
		var name = 'lazy.DbTable';
		var store = Ext.StoreManager.get(name);
		if (!store) {
			Ext.Ajax.request({
				url : App.cfg.restUrl + '/rest/table_metadata/' + tableName,
				success : function(response, opts) {
					var obj = Ext.decode(response.responseText);
					var fields = [];
					if (Ext.isArray(obj.columns)) {
						for (var index = 0; index < obj.columns.length; index++) {
							var config = obj.columns[index];
							var field = {};
							field.name = config.name;
							field.mapping = config.mapping;
							field.hide = config.hide;
							if (typeMapping.hasOwnProperty(config.type)) {
								field.type = typeMapping[config.type];
							} else {
								filed.type = 'auto';
							}
							fields.push(field);
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
						storeId : name,
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
					Ext.Loader.notify(['App.lazy.model.DbTable', 'App.lazy.store.DbTable']);
				},
				scope : this
			});
		} else {
			App.log('lazy resource already prepared.');
		}
	}
});
