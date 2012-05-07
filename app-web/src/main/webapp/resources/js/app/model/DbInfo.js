Ext.define('App.model.DbInfo', {
	extend : 'Ext.data.Model',
	idProperty : 'id',

	fields : [{
		name : 'id', // id property
		mapping : 'id'
	}, {
		name : 'name'
	}, {
		name : 'vendor'
	}, {
		name : 'url'
	}, {
		name : 'driverClass'
	}, {
		name : 'username'
	}, {
		name : 'password'
	}]
	,
	validations : [{
		field : 'name',
		type : 'presence'
	}, {
		field : 'vendor',
		type : 'presence'
	}, {
		field : 'url',
		type : 'presence'
	}]
});