Ext.define('App.service.XTemplateRender', {
	alternateClassName: 'App.XTemplateRender',
	requires : ['Ext.XTemplate','Ext.Ajax'],
	statics: {
		render: function(loader, response, active){
			var tpl = new Ext.XTemplate(response.responseText);
			var target = loader.getTarget();
			target.update(tpl.apply(target.data),active.scripts === true);
			return true;
		},
		
		//note! load template from url is asynchronous. the template will be apply to target's tpl
		//load template form current document will read template from dom immediately.
		tpl: function(target,url){
			if(Ext.isString(target)){
				var dom = document.getElementById(target);
				if(dom == null){
					Ext.Error.raise({
						sourceClass: "App.service.XTemplateRender",
						sourceMethod: "tpl",
						msg : "Target dom with id '" + target + "' no exist. did you put js before target dom in html?"
					});
					return null;
				}else{
					return new Ext.XTemplate(document.getElementById(target).innerHTML);
				}
			}
			
			if(!target || !url){
				Ext.Error.raise({
                    sourceClass: "App.service.XTemplateRender",
                    sourceMethod: "tpl",
                    msg: "Target component and url must both be specified."
                });
				return false;
			};
			Ext.Ajax.request({
				url: url,
				success: function(xhr){
					var template = new Ext.XTemplate(xhr.responseText);
					target.tpl = template;
					if(target.isVisible){
						target.update(target.tpl.apply(target.data));
					}
				}
			});
			return '<div>&nbsp;</div>';
		}
    }
});
