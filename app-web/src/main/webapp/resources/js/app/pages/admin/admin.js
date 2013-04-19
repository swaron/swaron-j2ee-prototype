Ext.define('App.model.Session', {
	extend : 'Ext.data.Model',
	fields : [{
		name : 'sessionId'
	}, {
		name : 'remoteAddr'
	}, {
		name : 'requestUri'
	}, {
		name : 'creationTime',
		type: 'date',
		dateFormat:'time'
	}, {
		name : 'lastRequestTimeString'
	}, {
		name : 'maxIdleString'
	}]

});

Ext.application({
	name : 'App',
	appFolder : App.cfg.jsPath + '/app',

	launch : function() {
		var store = Ext.create('Ext.data.Store', {
			model : 'App.model.Session',
			pageSize : 16,
			autoLoad:true,
			proxy : {
				type : 'ajax',
				url : App.cfg.contextPath + '/rest/admin/online-user.json',
				reader : {
					type : 'json',
					root : 'records'
				}
			}
		});

		var grid = Ext.widget('grid', {
			store : store,
			selType : 'rowmodel',//rowmodel,cellmodel, 
			title:'在线用户信息',
			frame : true,
			columns : {
				items : [{
	                xtype: 'rownumberer'
	            },{
					header : 'Session ID',
					dataIndex : 'sessionId'
				}, {
					header : 'IP地址',
					dataIndex : 'remoteAddr'
				}, {
					header : '创建时间',
					xtype : 'datecolumn',
					format: 'Y-m-d H:i:s',
					dataIndex : 'creationTime'
				}, {
					header : '上次访问地址',
					dataIndex : 'requestUri'
				}, {
					header : '上次请求时间',
					dataIndex : 'lastRequestTimeString',
					renderer:function(v){
						return v + "前";
					}
				}, {
					header : '最大允许空闲时间',
					dataIndex : 'maxIdleString'
				}]
			},
			bbar : Ext.create('Ext.PagingToolbar', {
				store : store,
				displayInfo : true
			}),
			renderTo : 'online-user'
		});
		this.initDomEvents();
	},
	initDomEvents : function() {
	}
});
