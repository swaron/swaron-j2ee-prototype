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
				url : App.cfg.restUrl + '/rest/grid-config/' + tableName + '.json',
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
								field.type = 'auto';
							}
							if(field.type == 'date'){
								field.dateFormat = 'time'
							}
							fields.push(field);
						}
					}
					var gridColumns = [];
					console.log('grid columns ' + Ext.encode(obj.columns));
					if (Ext.isArray(obj.columns)) {
						for (var index = 0; index < obj.columns.length; index++) {
							var column = obj.columns[index];
							var field = {};
							field.header = column.mapping;
							field.dataIndex = column.name;
							field.sortable = true;
							if (gridXTypeMapping.hasOwnProperty(column.type)) {
								field.xtype = gridXTypeMapping[column.type];
							}
							
							field.filter = {
								xtype : 'textfield'
							};
							field.field = {
								xtype : 'textfield'
							}
							if(field.xtype == 'datecolumn'){
								field.format = 'Y-m-d',
								field.field = {
									xtype : 'datefield',
									format : 'Y-m-d'
								}
							}
							if(column.hasAlias == true){
								console.log('alias column ' + column.mapping);
								field.hasAlias = true;
								field.tablename = obj.tableName;
								field.columnName = column.mapping;
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
						statics: {
							modelName: modelName,
							storeName: storeName,
							gridColumns: gridColumns,
							getGridColumns:function(){
								var cols = this.gridColumns;
								for(var i=0,ln=cols.length;i<ln;i++){
									var col = cols[i]; 
									if(col.hasAlias == true){
										col.renderer = App.Code.converter(obj.tableName, col.columnName);
										col.field = {
											xtype : 'combobox',
											store : App.Code.store(obj.tableName, col.columnName),
											queryMode : 'local',
											displayField : 'name',
											valueField : 'code'
										}
									}
								}
								return cols;
							}
						}
					},function(){
						Ext.Loader.notify(['App.lazy.GridConfig'])
					});
				},
				scope : this
			});
		} else {
			App.log('lazy resource already prepared.');
		}
	}
}, function(){
});
