Ext.onReady(function() {
	if (Ext.grid.RowEditor) {
		Ext.apply(Ext.grid.RowEditor.prototype, {
			saveBtnText : '确认',
			cancelBtnText : '取消',
			errorsText: '错误'
		});
	}

	// fix messageBox i18n bug
	if (Ext.MessageBox) {
		var msgBox = Ext.MessageBox;
		Ext.each(msgBox.msgButtons, function(btn, index) {
			btn.setText(msgBox.buttonText[msgBox.buttonIds[index]]);
		});
	}
})