
Ext.ns('App.cfg');
if (typeof App_Config !== 'undefined') {
	Ext.apply(App.cfg, App_Config);
};
Ext.apply(App, {
	url : function(str) {
		if (String(str).indexOf('/')==0) {
			var contextPath = App.cfg.contextPath || '';
			return contextPath + str;
		} else {
			return str;
		}
	},
	mask : function(maskText) {
		Ext.getBody().mask(maskText, 'x-mask-loading').addClass('x-body-mask');
	},
	unmask : function() {
		Ext.getBody().unmask();
	},
	log : function(/* args... */) {
		if (App.cfg.debug && window.console && console.log) {
			var args = Ext.toArray(arguments);
			args.push(" : " + arguments.callee.caller.name);
			args.push(arguments.callee.caller);
			console.log.apply(console,args);
		}
	},
	fireEvent : function(element, event) {
		if(document.createEvent){
			// dispatch for firefox + others
			var evt = document.createEvent("HTMLEvents");
			evt.initEvent(event, true, true); // event type,bubbling,cancelable
			return !element.dispatchEvent(evt);
		}else if(document.createEventObject){
			// dispatch for IE 6,7,8
			var evt = document.createEventObject();
			return element.fireEvent('on' + event, evt);
		}else{
			var fn = element[event]; 
			if(Ext.isFunction(fn)){
				fn();
			}
		}
	},
	getParam: function(paramKey){
		var query = window.location.search;
		var params = Ext.Object.fromQueryString(query);
		return params[paramKey];
	},
	util : {
		//used for convert message.p.d.key() to message['p.d.key']()
		findValue : function(obj,keys){
			//<debug error>
            if (typeof keys !== 'string') {
                Ext.Error.raise({
                    sourceClass: "App.util",
                    sourceMethod: "findValue",
                    msg: "Invalid key, must be a string"
                });
            }
            //</debug>
            var finalValue = obj;
            var keyList = keys.split('.');
            while(keyList.length > 0){
            	var key = keyList.shift();
            	finalValue = finalValue[key];
            	if(finalValue == undefined || finalValue == null){
            		break;
            	}
            }
            return finalValue;
		}
	}
});

//override default configurations of some ext component
Ext.require(['Ext.data.proxy.Ajax', 'Ext.Ajax'], function() {
	Ext.override(Ext.data.proxy.Ajax, {
//		actionMethods : {
//			create : 'POST',
//			read : 'GET',
//			update : 'POST',
//			destroy : 'POST'
//		},
		//application/json type for spring mvc 
		headers : {
			'Accept' : 'application/json'
		}
	});
	Ext.Ajax.defaultHeaders = {
		'Accept' : 'application/json'
	};
});
