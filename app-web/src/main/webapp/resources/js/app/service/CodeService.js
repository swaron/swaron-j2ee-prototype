/**
 * ## Example Code
 * 
 * App.Code.getName('table','column', '1'); //return table->column->name <br>
 * App.Code.getCode('table','column', 'name'); // return table->column->code <br>
 * App.Code.converter('table','column'); //return a function which will convert code to name <br>
 * App.Code.store('table','column'); // return a store which contains all data of table.column
*/


Ext.define('App.service.CodeService', {
	alternateClassName: 'App.Code',
	singleton:true,
    requires: ['Ext.data.Store','App.model.DbCode','Ext.data.proxy.LocalStorage','Ext.data.proxy.Rest'],
	url : App.url('/rest/codes.json'),
	localStore : null,
	remoteStore : null,
	nameCache : {},
	mixins : {
		observable : 'Ext.util.Observable'
	},
	initStore : function() {
		var localStore = null;
		try {
			localStore = Ext.create('Ext.data.Store', {
				model : 'App.model.DbCode',
				autoLoad : false,
				proxy : {
					type : 'localstorage',
					id : 'app-code'
				}
			});
			//proxy will be instantiated in constructor of Ext.data.AbstractStore, will test the availability of localstorage.
		} catch (e) {
			localStore = null;
			App.log('failed to create localstorage, fallback to remote store.', e);
		}
		var remoteStore = Ext.create('Ext.data.Store', {
			model : 'App.model.DbCode',
			proxy : {
				type : 'ajax',
				url : this.url,
				reader : {
					type : 'json'
				}
			},
			autoLoad : false
		});
		if (localStore) {
			var reload;
			try {
				localStore.load();
				var version = localStore.findRecord('column', '_store_version');
				reload = (version == null) || (version.get('code') != App.cfg.version);
			} catch (e) {
				// fix if data corrupted
				reload = true;
				App.log('unable to read from local store, try to clear local store', e);
			}
			if (reload) {
				App.log('not latest version, reload codes.', e);
				var oldAsync = Ext.Ajax.async;
				Ext.Ajax.async = false;
				remoteStore.load({
					scope : this,
					callback : function(records, operation, success) {
						if(!success){
							return;
						}
						localStore.getProxy().clear();
						localStore.load();
						App.log('load code records into local storage.');
						Ext.each(records, function(record, index) {
							delete record.data[record.idProperty];
							localStore.add(record.data);
						});
						localStore.add({
							column : '_store_version',
							code : App.cfg.version,
							name : App.cfg.version
						});
						localStore.sync();
					}
				});
				Ext.Ajax.async = oldAsync;
			}
		} else {
			remoteStore.load();
		}
		this.localStore = localStore;
		this.remoteStore = remoteStore;
		this.codeStore = localStore || remoteStore;
	},
	findName:function(table, column, code){
		if (code == null || code == '') {
			return '';
		}
		var store = this.codeStore;
		var index = store.findBy(function(model) {
			return model.get('table') == table && model.get('column') == column && model.get('code') == code;
		});
		if (index == -1) {
			var msg = Ext.String.format('unable to get magiccode definition, table:{0},column:{1},code:{2}', table, column,code);
			App.log(msg);
			App.log('records count in store:' + store.getCount() + '. store info:', store);
		} else {
			return store.getAt(index).get('name');
		}
	},
	getName : function(table, column, code) {
		var key = table + '.' + column + '.' + code;
		if (this.nameCache[key] === undefined) {
			var name = this.findName(table, column, code);
            this.nameCache[key] = name;
        }
        return this.nameCache[key];
	},
	getCode : function(table, column, name) {
		if (name == null || name == '') {
			return null;
		}
		var store = this.codeStore;
		var index = store.findBy(function(model) {
			return model.get('table') == table && model.get('column') == column && model.get('name') == name;
		});
		if (index == -1) {
			var msg = Ext.String.format('unable to get magiccode definition, table:{0},column:{1},name:{2}', table, column,name);
			App.log(msg);
			App.log('records count in store:' + store.getCount() + '. store info:', store);
		} else {
			return store.getAt(index).get('code');
		}
	},
	store : function(table, column) {
		var columnStore = Ext.create('Ext.data.Store', {
			model : 'App.model.DbCode',
			sorters : [{
				property : 'order',
				direction : 'ASC'
			}]
		});
		var items = this.codeStore.queryBy(function(m) {
			return m.get('table') == table && m.get('column') == column;
		});
		columnStore.add(items.getRange());
		columnStore.sort();
		return columnStore;
	},
	converter : function(table, column) {
		var service = this;
		var fn = function(code) {
			return service.getName(table, column, code);
		}
		return fn;
	},
	test : function() {
		App.log('test getname with code 1 in syscode: ', this.getName('SYSCODE', 1));
		App.log('test getcode with 个人  in account_type: ', this.getCode('ACCOUNT_TYPE', '个人'));
		App.log('test store in result_code,count: ', this.store('RESULT_CODE').getCount());
		App.log('test converter in SYSCODE with 1 ', this.converter('SYSCODE')('1'));
	},
	constructor : function() {
		this.initStore();
	}
});
