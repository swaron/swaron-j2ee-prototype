/**
 * ## Example Code
 * 
 * App.TypeCode.getName('type_table','type_id_column','type_name_column', '1'); //return table->column->name <br>
 * App.TypeCode.getCode('type_table','type_id_column','type_name_column', 'name'); // return table->column->code <br>
 * App.TypeCode.converter('type_table','type_id_column','type_name_column'); //return a function which will convert code to name <br>
 * App.TypeCode.store('table','type_id_column','type_name_column'); // return a store which contains all data of table.column
*/


Ext.define('App.service.TypeCodeService', {
	alternateClassName: 'App.TypeCode',
	singleton:true,
    requires : ['Ext.data.reader.Json', 'Ext.data.Store', 'App.model.DbCode', 'Ext.data.proxy.Ajax', 'Ext.data.proxy.LocalStorage',
			'Ext.data.proxy.Rest',  'Ext.data.Request', 'Ext.data.Batch'],
	url : App.url('/rest/code/type.json'),
	storeId : 'app-tcode',
	//local image will be lazy filled from local storage when local storage are available.
	localImage : null,
	//remote image will be filled when local storage not available.
	remoteImage : null,
	nameCache : {},
	mixins : {
		observable : 'Ext.util.Observable'
	},
	initStore : function() {
		var service = this;
		var localStorageExist = 'localStorage' in window && window['localStorage'] !== null;
		if(localStorageExist){
			var storage = window.localStorage;
			var version = storage.getItem('app-tcode-_store_version');
			var reload = (version == null) || (version != App.cfg.version);
			if (reload) {
				App.log('not latest version, reload type codes. latest version: ' + App.cfg.version);
				Ext.Ajax.request({
					url:this.url,
					success: function(response){
						var result = Ext.decode(response.responseText);
						if(Ext.isObject(result)){
							storage.setItem('app-tcode-'+'_value', Ext.JSON.encode(result))
							storage.setItem('app-tcode-_store_version',App.cfg.version);
							service.localImage = result;
						}else{
							App.log('result from type code service is not an object.');
						}
					},
					failure: function(response, opts) {
						App.log('type code service not available.');
				    }
				});
			}
		}else{
			App.log('localstorage not exist, fallback to remote store.', e);
			Ext.Ajax.request({
				url:this.url,
				success: function(response){
					var result = Ext.decode(response.responseText);
					if(Ext.isObject(result)){
						service.remoteImage = result;
					}else{
						App.log('result from type code service is not an object.');
					}
				},
				failure: function(response, opts) {
					App.log('type code service not available.');
			    }
			});
		}
	},
	loadLocalImage:function(){
		var localStorageExist = 'localStorage' in window && window['localStorage'] !== null;
		if(localStorageExist){
			var storage = window.localStorage;
			var result = storage.getItem('app-tcode-'+'_value');
			return Ext.JSON.decode(result);
		}else{
			//local storage not exist
			return null;
		}
	},
	getLocalImage:function(){
		if(this.localImage == null){
			this.localImage = this.loadLocalImage();
		}
		return this.localImage;
	},
	getNameInternal:function(type_table, id_column,name_column, id){
		if (id == null || id == '') {
			return '';
		}
		var image = this.getLocalImage() || this.remoteImage;
		if(image == null){
			return '';
		}
		var tablePagingResult = image[type_table];
		var rows = tablePagingResult.records;
		var targetRow = null;
		Ext.Array.every(rows,function(item,index,array){
			if(item[id_column] == id){
				targetRow = item;
				return false;
			}
		});
		if(targetRow!=null){
			return targetRow[name_column];
		}else{
			var msg = Ext.String.format('unable to get magiccode definition, table:{0},column_id:{1},column_name:{2},id:{3}', type_table, id_column,name_column,id);
			App.log(msg);
		}
	},
	getName : function(type_table, id_column,name_column, id) {
		var key = table + '.' + id_column + '.' + name_column + '.' + id;
		if (this.nameCache[key] === undefined) {
			var name = this.getNameInternal(type_table, id_column,name_column, id);
            this.nameCache[key] = name;
        }
        return this.nameCache[key];
	},
	getCode : function(type_table, id_column,name_column, name) {
		if (name == null || name == '') {
			return '';
		}
		var image = this.remoteImage || this.localImage;
		if(image == null){
			this.loadLocalImage();
			image = this.localImage;
		}
		if(image == null){
			return '';
		}
		var tablePagingResult = image[type_table];
		var rows = tablePagingResult.records;
		var targetRow = null;
		Ext.Array.every(rows,function(item,index,array){
			if(item[name_column] == name){
				targetRow = item;
				return false;
			}
		});
		if(targetRow!=null){
			return targetRow[id_column];
		}else{
			var msg = Ext.String.format('unable to get magiccode definition, table:{0},column_id:{1},column_name:{2},name:{3}', type_table, id_column,name_column,name);
			App.log(msg);
		}
	},
	
	store : function(type_table, id_column,name_column) {
		var columnStore = Ext.create('Ext.data.Store', {
			model : 'App.model.DbCode'
		});
		var image = this.remoteImage || this.localImage;
		if(image == null){
			this.loadLocalImage();
			image = this.localImage;
		}
		if(image == null){
			return columnStore;
		}
		var tablePagingResult = image[type_table];
		var rows = tablePagingResult.records;
		var targetRow = null;
		Ext.Array.every(rows,function(item,index,array){
			var dbCode = {
				codeDictionaryId:item[id_column],
				code:item[id_column],
				name:item[name_column],
				nameEn:item[name_column],
				nameZh:item[name_column]
			}
			columnStore.add(dbCode);
		});
		return columnStore;
	},
	converter : function(type_table, id_column,name_column) {
		var service = this;
		var fn = function(code) {
			return service.getName(type_table, id_column,name_column, code);
		}
		return fn;
	},
	test : function() {
		App.log('test getcode with 个人  in account_type: ', this.getCode('ACCOUNT_TYPE','ACCOUNT_TYPE_ID','ACCOUNT_TYPE_NAME', '个人'));
	},
	constructor : function() {
		this.initStore();
	}
});
