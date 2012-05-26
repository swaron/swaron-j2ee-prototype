/**
 * bugfixs for 4.1.0
 * 
 */
Ext.onReady(function() {
	Ext.override(Ext.view.AbstractView, {
		onRender : function() {
			var me = this;
			this.callOverridden();
			if (me.loadMask && Ext.isObject(me.store)) {
				me.setMaskBind(me.store);
			}

		}
	});
});