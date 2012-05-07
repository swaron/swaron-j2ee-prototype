/**
 * bug: Ext.grid.RowEditor.loadRecord does not take account of errorSummary parameter.
*/
Ext.onReady(function(){
	if(Ext.ClassManager.isCreated('Ext.grid.RowEditor')){
		Ext.override(Ext.grid.RowEditor, {
		    loadRecord: function(record) {
		        var me = this,
		            form = me.getForm(),
		            valid = form.isValid();
		        form.loadRecord(record);
		        if (me.errorSummary && me.isVisible()) {
		            me[valid ? 'hideToolTip' : 'showToolTip']();
		        }
		        // render display fields so they honor the column renderer/template
		        Ext.Array.forEach(me.query('>displayfield'), function(field) {
		            me.renderColumnData(field, record);
		        }, me);
		    }
		});
    }
}); 