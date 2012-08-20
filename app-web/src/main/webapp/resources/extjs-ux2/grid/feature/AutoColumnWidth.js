Ext.define('Ext.ux2.grid.feature.AutoColumnWidth', {
	extend : 'Ext.grid.feature.Feature',
	alias : 'feature.autocolumnwidth',
	requires : ['Ext.grid.plugin.HeaderResizer'],
	eventPrefix : 'autowidth',
	config:{
		autoWidth : false,
		maxColWidth : null,
		minColWidth : null
	},
	init : function() {
		this.grid;
		this.view;
		this.headerCt = this.view.headerCt;
		this.resizer = this.headerCt.resizer;
		if (this.resizer) {
			this.headerCt.on('afterrender', this.afterHeaderRender, this, {
				single : true
			});
//			this.view.on('viewready', this.afterViewReady, this, {
//				single : true
//			});
			this.grid.mon(this.grid.store,'load',this.afterViewReady, this, {
				single : true
			});
			this.minColWidth = this.minColWidth || this.resizer.minColWidth;
			this.maxColWidth = this.maxColWidth || this.resizer.maxColWidth;
		}else{
			this.minColWidth = this.minColWidth || Ext.grid.plugin.HeaderResizer.prototype.minColWidth;
			this.maxColWidth = this.maxColWidth || Ext.grid.plugin.HeaderResizer.prototype.maxColWidth;
		}
	},
	afterViewReady : function() {
		if(!this.autoWidth || this.forceFit){
			//ajust column width only when auto width is true. and force fit is false.
			return;
		}
		var headerCt = this.headerCt;
		var cols = headerCt.getVisibleGridColumns();
		this.updateWidthPorperties(cols);
		this.layoutWithNewWidth(cols);
	},
	afterHeaderRender : function(headerCt) {
		var me = this, headerCt = me.headerCt, el = headerCt.el;
		// we only listen to dblclick since mousemove is handled by HeaderResizer
		headerCt.mon(el, 'dblclick', me.onHeaderCtDblClick, me);
	},
	layoutWithNewWidth:function(cols){
        Ext.suspendLayouts();
		for(var i=0,ln = cols.length;i<ln;i++){
			var col = cols[i];
			if (col.flex) {
	        	delete col.flex;
	        }
	        col.setWidth(col.newWidth);
		}
		var viewEl = this.view.el;
		var table = viewEl.down('.' + Ext.baseCSSPrefix + 'grid-table-resizer');
		table.setWidth(this.headerCt.getFullWidth());
        Ext.resumeLayouts(true);
	},
	updateWidthPorperties:function(cols){
		var viewEl = this.view.el;
		var table = viewEl.down('.' + Ext.baseCSSPrefix + 'grid-table-resizer');
		for(var i=0,ln = cols.length;i<ln;i++){
			var col = cols[i];
			var firstTh = viewEl.down('.' + Ext.baseCSSPrefix + 'grid-col-resizer-' + col.id);
			var maxHeaderWidth = this.getComputedHeaderWidth(col);
			var maxTdWidth = this.getComputedTdWidth(col);
			col.origWidth = col.getWidth();
			var maxWidth = Math.max(maxHeaderWidth, maxTdWidth);
			col.newWidth = maxWidth = Ext.Number.constrain(maxWidth, this.minColWidth, this.maxColWidth);
		}
	},
	getComputedTdWidth:function(col){
		var viewEl = this.view.el;
		var tds = Ext.select('.' + Ext.baseCSSPrefix + 'grid-cell-' + col.id,false, viewEl.dom);
		
		var maxWidth = 0;
		tds.each(function(el){
			maxWidth = Math.max(maxWidth,el.getFrameWidth('lr') + el.getTextWidth());
		});
		return maxWidth;
	},
	getComputedHeaderWidth:function(col){
		var colHeader = col.getEl();
		var headerInner = colHeader.down('.' + Ext.baseCSSPrefix + 'column-header-inner');
		var headerText = colHeader.down('.' + Ext.baseCSSPrefix + 'column-header-text');
		var headerTrigger = colHeader.down('.' + Ext.baseCSSPrefix + 'column-header-trigger');
		var hiddenWidth = headerTrigger.getStyle('width');
		var triggerWidth = 14;
		var match = /&(\d)+px$/g.exec(hiddenWidth);
		if(match){
			triggerWidth = match[1];
		}
		return headerInner.getBorderWidth('lr')+headerInner.getFrameWidth('lr')+headerText.getWidth()+triggerWidth;
	},
	onHeaderCtDblClick : function() {
		// HeaderResizer has to be enabled to use the auto resizing functionality
		if (this.resizer && !this.resizer.disabled && !!this.resizer.activeHd) {
			// activeHd is the target column
			var col = this.resizer.activeHd, viewEl = this.view.el;
			var maxHeaderWidth = this.getComputedHeaderWidth(col);
			var maxTdWidth = this.getComputedTdWidth(col);
			col.origWidth = col.getWidth();
			var maxWidth = Math.max(maxHeaderWidth, maxTdWidth);
			col.newWidth = maxWidth = Ext.Number.constrain(maxWidth, this.minColWidth, this.maxColWidth);
			Ext.suspendLayouts();
			col.setWidth(col.newWidth);
			var table = viewEl.down('.' + Ext.baseCSSPrefix + 'grid-table-resizer');
			table.setWidth(this.headerCt.getFullWidth());
			Ext.resumeLayouts(true);
		}
	}
});