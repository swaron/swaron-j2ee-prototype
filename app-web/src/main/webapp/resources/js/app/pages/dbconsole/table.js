var Page = {
	defineStore : function(tableName) {
		var typeMapping = {
			'Timestamp' : 'date',
			'Boolean' : 'boolean',
			'Integer' : 'int',
			'Long' : 'int',
			'Double' : 'float',
			'Float' : 'float',
			'String' : 'string'
		}
		var name = 'table.' + tableName;
		var store = Ext.StoreManager.get(name);
		if (!store && !this.requestingGridConfig) {
			this.requestingGridConfig = true;
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

					var modelName = 'App.model.' + name;
					var storeName = 'App.store.' + name;
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
					Ext.Loader.notify('App.wait.Page.defineStore');
				},
				scope : this
			});
		}
	}
}
Page.defineStore(App.getParam('table'));
Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',
	controllers : ['dbconsole.TableController'],
	launch : function() {
		Ext.widget('tablegrid', {
			tableName:App.getParam('table'),
			width : 700,
			height : 500,
			renderTo : 'table-gird'
		});
	}
});
