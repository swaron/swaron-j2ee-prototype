Ext.define('App.model.DbCode', {
	extend : 'Ext.data.Model',
	fields : [{
		name : 'id', // DONT TOUCH, missing id will cause it unable to be removed from localStorage
		mapping : 'codeDictionaryId'
	},  {
		name : 'table',
		mapping : 'tableName'
	},  {
		name : 'column',
		mapping : 'columnName'
	}, {
		name : 'code',
		mapping : 'code'
	}, {
		name : 'name_zh',
		mapping : 'nameZh'
	}, {
		name : 'name_en',
		mapping : 'nameEn'
	}, {
		name : 'order',
		mapping : 'sortOrder'
	}, {
		name : 'description',
		mapping : 'description'
	}, {
		name : 'nameI18nKey',
		mapping : 'nameI18nKey'
	}, {
		name: 'name',
		mapping : 'nameI18nKey',
		convert: function(value, record) {
			var key = record.get('nameI18nKey');
			var fn = App.util.findValue(window.messages,key);
			if(fn){
				return fn();
			}else{
				return value;
			}
//			if(App.cfg.lang == 'zh'){
//				return record.get('name_zh');
//			}else{
//				return record.get('name_en');
//			}
		}
	}],
	
	getMessage:function(){
		var key = this.get('nameI18nKey');
		return App.util.findValue(window.messages,key);
	}

});
