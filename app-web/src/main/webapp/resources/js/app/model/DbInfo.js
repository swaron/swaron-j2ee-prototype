Ext.define('App.model.DbInfo', {
	extend : 'Ext.data.Model',
	idProperty : 'dbInfoId',

	fields : [{
		name : 'dbInfoId', // id property
		mapping : 'dbInfoId'
	}, {
		name : 'name'
	}, {
		name : 'vendor'
	}, {
		name : 'url'
	}, {
		name : 'driverClass'
	}, {
		name : 'dbUser',
		mapping : 'dbUser'
	}, {
		name : 'dbPasswd',
		mapping : 'dbPasswd'
	}, {
		name : 'updateTime',
		mapping : 'updateTime'
	}],
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