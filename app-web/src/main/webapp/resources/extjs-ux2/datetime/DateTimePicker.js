Ext.define('Ext.ux2.datetime.DateTimePicker', {
	extend : 'Ext.picker.Date',
	alias : 'widget.datetimepicker',
	timeFormat : 'H:i:s',
	timeLabel : 'Time',
	todayText : 'Now',
	requires : ['Ext.ux2.datetime.TimePickerField'],

	initComponent : function() {
		// keep time part for value
		var value = this.value || new Date();
		this.callParent();
		this.value = value;
	},
	initEvents : function() {
		this.callParent();
		if(this.timefield){
			this.timefield.on('change', this.timeChange, this);
		}
	},
	onRender : function(container, position) {
		if (!this.timefield) {
			this.timefield = Ext.create('Ext.ux.form.TimePickerField', {
				fieldLabel : this.timeLabel,
				labelWidth : 40,
				value : Ext.Date.format(this.value, this.timeFormat)
			});
		}
		this.timefield.ownerCt = this;
		this.callParent(arguments);

		var table = Ext.get(Ext.DomQuery.selectNode('table', this.el.dom));
		var tfEl = Ext.core.DomHelper.insertAfter(table, {
			tag : 'div',
			style : 'border:0px;',
			children : [{
				tag : 'div',
				cls : 'x-datepicker-footer ux-timefield'
			}]
		}, true);
		this.timefield.render(this.el.child('div div.ux-timefield'));

		var p = this.getEl().parent('div.x-layer');
		if (p) {
			p.setStyle("height", p.getHeight() + 31);
		}
		
		var me = this;
        me.todayBtn = Ext.create('Ext.button.Button', {
            renderTo: me.footerEl,
            text: "ClearTime",
            handler: me.clearTime,
            scope: me
        });
	},
	timeChange : function(comp, time, raw) {
		var me=this, d = me.value,handler = me.handler;
		d.setHours(raw.h);
		d.setMinutes(raw.m);
		d.setSeconds(raw.s);
		me.setValue(d);
		me.fireEvent('timechange', me, d);
		if (handler) {
			handler.call(me.scope || me, me, me.value);
		}
		return me;
	},
	clearTime: function(){
		this.timefield.setValue("0:0:0");
	},
	/**
	 * Sets the current value to today.
	 * 
	 * @return {Ext.picker.Date} this
	 */
	selectToday : function() {
		var me = this, btn = me.todayBtn, handler = me.handler;
		
		this.value = new Date();
		this.update(this.value);
		
		me.fireEvent('select', me, me.value);
		if (handler) {
			handler.call(me.scope || me, me, me.value);
		}
		me.onSelect();
		return me;
	},
	 getDateTime : function() {
	 	var value = this.value;
		if (this.timefield) {
			var rawtime = this.timefield.getRawValue();
			value.setHours(rawtime.h);
			value.setMinutes(rawtime.m);
			value.setSeconds(rawtime.s);
		}
		return value;
	},
	getValue: function(){
		return this.getDateTime();
	},
	setValue : function(value) {
		this.value = this.getDateTime();
		return this.update(this.value);
	}
});