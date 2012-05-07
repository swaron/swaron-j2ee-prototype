Ext.define('App.model.SecUser', {
	extend : 'Ext.data.Model',
	idProperty : 'secUserId',
	fields : [{
		name : 'secUserId', // id property
		mapping : 'secUserId'
	}, {
		name : 'email',
		mapping : 'email'
	}, {
		name : 'enabled',
		type : 'boolean'
	}, {
		name : 'username',
		mapping : 'username'
	}, {
		name : 'loginState',
		mapping : 'loginState'
	}, {
		name : 'loginFailedCount',
		mapping : 'loginFailedCount'
	}, {
		name : 'lockedTime',
		mapping : 'lockedTime',
		type: 'date',
		dateFormat:'time'
	}, {
		name : 'lastLoginTime',
		mapping : 'lastLoginTime',
		type: 'date',
		dateFormat:'time'
	},{
		name: 'updateTime',
		mapping: 'updateTime'
	}]
	,
	validations : [{
		field : 'email',
		type : 'presence'
	}, {
		field : 'username',
		type : 'presence'
	}, {
		field : 'loginFailedCount',
		type : 'presence'
	}]
});