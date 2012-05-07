//Ext.require(['Ext.container.Viewport']);
Ext.require([
    'Ext.grid.plugin.RowEditing',
    'Ext.grid.plugin.CellEditing',
    'Ext.grid.Panel',
    'Ext.data.Store',
    'Ext.ux.grid.HeaderFilters',
    'Ext.grid.column.Date',
    'App.model.SecUser',
    'App.service.CodeService',
    'Ext.ux.CheckColumn',
    'Ext.toolbar.Paging'
]);

Ext.onReady(function(){
    var store = Ext.create('Ext.data.Store', {
    	remoteSort: true,
    	remoteFilter: true,
        autoLoad: true,
        autoSync: true,
        pageSize: 16,
        model: 'App.model.SecUser',
        proxy: {
            type: 'rest',
            format : 'json',
            url: App.cfg.restUrl + '/rest/users',
            reader: {
                type: 'json',
                successProperty: 'success',
                messageProperty: 'message',
                root: 'records'
            },
            writer: {
                type: 'json'
            },
            listeners: {
                exception: function(proxy, response, operation){
                    Ext.MessageBox.show({
                        title: 'REMOTE EXCEPTION',
                        msg: operation.getError().statusText,
                        icon: Ext.MessageBox.ERROR,
                        buttons: Ext.Msg.OK
                    });
                }
            }
        },
        listeners: {
			beforesync: function(options, eOpts){
		    	App.log('before sync', options);
		    },
            load: function(store, records,successful){
            	App.log('record loaded');
            },
            remove: function(stroe,record,index){
            	App.log('record removed');
            },
            update: function(stroe,record,operation){
            	App.log('record updated');
            },
            write: function(store, operation){
            	if(!operation.wasSuccessful()){
            		return;
            	}
//                var record = operation.records[0];
//				var name = operation.action;
//                if(name == 'create'){
//                	var result = operation.getRecords()[0];
//	                record.setId(result.getId());
//                }else if(name == 'update'){
//                }else if(name == 'destroy'){
//                }else{
//                	//will never go here
//                }
            }
        }
    });
    
    var rowEditing = Ext.create('Ext.grid.plugin.RowEditing',{
    	errorSummary : false
    });
    var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
        clicksToEdit: 1
    });
    var headerFilter = Ext.create('Ext.ux.grid.HeaderFilters');
    
    var grid = Ext.create('Ext.grid.Panel', {
    	selModel: {
            selType: 'rowmodel'
        },
        plugins: [rowEditing,headerFilter],
        width: 700,
        height: 500,
        frame: true,
        title: 'Users',
        store: store,
        iconCls: 'icon-user',
        columns: [{
            text: 'Username',
            width: 80,
            sortable: true,
            dataIndex: 'username',
            field: {
                xtype: 'textfield',
                allowBlank: false
            },
            filter: {
                xtype: 'textfield'
            }
        }, {
            text: 'Email',
            flex: 1,
            sortable: true,
            dataIndex: 'email',
            field: {
                xtype: 'textfield',
                allowBlank: false
            },
            filter: {
                xtype: 'textfield'
            }
        }, {
        	text: 'Login State',
        	width: 100,
        	sortable: true,
        	dataIndex: 'loginState',
        	renderer: App.Code.converter('sec_user','login_state'),
        	field: {
				xtype : 'combobox',
				store : App.Code.store('sec_user','login_state'),
				queryMode : 'local',
				displayField : 'name',
				valueField : 'code'
        	},
        	filter: {
        		xtype: 'textfield'
        	}
        }, {
            text: 'Lock Time',
            width: 150,
            sortable: true,
            dataIndex: 'lockedTime',
            xtype:'datecolumn', 
            format:'Y-m-d',
            field: {
                xtype: 'datefield',
                format: 'Y-m-d'
            },
            filter: {
                xtype: 'textfield'
            }
        }, {
            text: 'Last Login Date',
            width: 150,
            sortable: true,
            dataIndex: 'lastLoginTime',
            xtype:'datecolumn', 
            format:'Y-m-d',
            field: {
                xtype: 'datefield',
                format: 'Y-m-d'
            },
            filter: {
                xtype: 'textfield'
            }
        }, {
            header: 'LoginFailedCount',
            width: 100,
            sortable: true,
            dataIndex: 'loginFailedCount',
            field: {
                xtype: 'textfield',
                allowBlank: false
            },
            filter: {
                xtype: 'textfield'
            }
        }, {
        	xtype:'checkcolumn',
            header: 'Enabled',
            dataIndex: 'enabled',
            width: 60,
            editor: {
                xtype: 'checkbox',
                cls: 'x-grid-checkheader-editor',
                allowBlank: false
            },
            filter: {
                xtype: 'textfield'
            }
        }],
        tbar: [{
            text: 'Add',
            iconCls: 'icon-add',
            handler : function() {
                rowEditing.cancelEdit();
                // Create a model instance
                var r = Ext.create('App.model.SecUser');
                store.insert(0, r);
                rowEditing.startEdit(r,0);
            }
        }, {
            itemId: 'delete',
            text: 'Delete',
            iconCls: 'icon-delete',
            handler: function() {
                var sm = grid.getSelectionModel();
                rowEditing.cancelEdit();
                store.remove(sm.getSelection());
                if (store.getCount() > 0) {
                    sm.select(0);
                }
            },
            disabled: true
        }],
        dockedItems : [ {
			xtype : 'pagingtoolbar',
			store : store, // same store GridPanel is using
			dock : 'bottom',
			displayInfo : true
		} ],
        listeners: {
            'selectionchange': function(selModel, selections) {
                grid.down('#delete').setDisabled(selections.length == 0);
            }
        }
    });
    grid.render('crud-gird');
});
