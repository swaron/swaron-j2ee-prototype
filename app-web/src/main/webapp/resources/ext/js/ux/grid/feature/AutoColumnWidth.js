/**
 * The rowbody feature enhances the grid's markup to have an additional tr -> td -> div which spans the entire width of
 * the original row.
 * 
 * This is useful to to associate additional information with a particular record in a grid.
 * 
 * Rowbodies are initially hidden unless you override getAdditionalData.
 * 
 * Will expose additional events on the gridview with the prefix of 'rowbody'. For example: 'rowbodyclick',
 * 'rowbodydblclick', 'rowbodycontextmenu'.
 *  # Example
 * 
 * @example
 * Ext.define('Animal', {
 *         extend: 'Ext.data.Model',
 *         fields: ['name', 'latin', 'desc']
 *     });
 * 
 * Ext.create('Ext.grid.Panel', {
 *         width: 400,
 *         height: 300,
 *         renderTo: Ext.getBody(),
 *         store: {
 *             model: 'Animal',
 *             data: [
 *                 {name: 'Tiger', latin: 'Panthera tigris',
 *                  desc: 'The largest cat species, weighing up to 306 kg (670 lb).'},
 *                 {name: 'Roman snail', latin: 'Helix pomatia',
 *                  desc: 'A species of large, edible, air-breathing land snail.'},
 *                 {name: 'Yellow-winged darter', latin: 'Sympetrum flaveolum',
 *                  desc: 'A dragonfly found in Europe and mid and Northern China.'},
 *                 {name: 'Superb Fairy-wren', latin: 'Malurus cyaneus',
 *                  desc: 'Common and familiar across south-eastern Australia.'}
 *             ]
 *         },
 *         columns: [{
 *             dataIndex: 'name',
 *             text: 'Common name',
 *             width: 125
 *         }, {
 *             dataIndex: 'latin',
 *             text: 'Scientific name',
 *             flex: 1
 *         }],
 *         features: [{
 *             ftype: 'rowbody',
 *             getAdditionalData: function(data, rowIndex, record, orig) {
 *                 var headerCt = this.view.headerCt,
 *                     colspan = headerCt.getColumnCount();
 *                 // Usually you would style the my-body-class in CSS file
 *                 return {
 *                     rowBody: '<div style="padding: 1em">'+record.get("desc")+'</div>',
 *                     rowBodyCls: "my-body-class",
 *                     rowBodyColspan: colspan
 *                 };
 *             }
 *         }]
 *     });
 */
Ext.define('Ext.ux.grid.feature.AutoColumnWidth', {
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
//			this.headerCt.on('afterrender', this.afterHeaderRender, this, {
//				single : true
//			});
			this.view.on('viewready', this.afterViewReady, this, {
				single : true
			});
			
			this.minColWidth = this.minColWidth || this.resizer.minColWidth;
			this.maxColWidth = this.maxColWidth || this.resizer.maxColWidth;
		}else{
			this.minColWidth = this.minColWidth || Ext.grid.plugin.HeaderResizer.prototype.minColWidth;
			this.maxColWidth = this.maxColWidth || Ext.grid.plugin.HeaderResizer.prototype.maxColWidth;
		}
		App.log('min width: ' + this.minColWidth + ' ,max width: '+ this.maxColWidth);
	},
	afterViewReady : function(view) {
		App.log('afterViewReady');
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
		App.log('afterHeaderRender');
		var me = this, headerCt = me.headerCt, el = headerCt.el;
		// we only listen to dblclick since mousemove is handled by HeaderResizer
		headerCt.mon(el, 'dblclick', me.onHeaderCtDblClick, me);

		// implement a fake tracker since we need the getOffset method in "doResize"
		me.tracker = {
			getOffset : function() {
				return [me.newWidth - me.origWidth];
			}
		};
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
		var table = Ext.getBody().down('.' + Ext.baseCSSPrefix + 'grid-table-resizer');
		for(var i=0,ln = cols.length;i<ln;i++){
			var col = cols[i];
			var firstTh = viewEl.down('.' + Ext.baseCSSPrefix + 'grid-col-resizer-' + col.id);
			var maxHeaderwidth = this.getNeededHeaderWidth(col);
			col.origWidth = col.getWidth();
			App.log('original width:' + col.origWidth);
		}
		table.setWidth('auto');
		for(var i=0,ln = cols.length;i<ln;i++){
			var col = cols[i];
			var firstTh = viewEl.down('.' + Ext.baseCSSPrefix + 'grid-col-resizer-' + col.id);
			var maxHeaderwidth = this.getNeededHeaderWidth(col);
			firstTh.setWidth('auto');
			App.log('column width after auto: ' + firstTh.getWidth());
			var maxWidth = Math.max(maxHeaderwidth, firstTh.getWidth());
			col.newWidth = maxWidth = Ext.Number.constrain(maxWidth, this.minColWidth, this.maxColWidth);
			App.log('new max width: ' + maxWidth);
		}
	},
	getNeededHeaderWidth:function(col){
//		var colHeader = Ext.get(col.id);
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
		App.log('onHeaderCtDblClick');
		// HeaderResizer has to be enabled to use the auto resizing functionality
		if (this.resizer && !this.resizer.disabled && !!this.resizer.activeHd) {
			// use the activeHeader from HeaderResizer (there the onLeftEdge/onRightEdge checks are already done)
			var hd = this.resizer.activeHd, view = this.headerCt.view,
			// get all headers of the active column, they may be nested if we use grouping etc.
			
			columns = Ext.select('.' + Ext.baseCSSPrefix + 'grid-col-resizer-' + hd.id, false, view.el.dom),
			// get all table elements, they may also be nested
			tables = Ext.select('.' + Ext.baseCSSPrefix + 'grid-table-resizer', false, view.el.dom), width = 0;
			App.log('active header is : ' + hd + ', id: ' + hd.id);

			// these vars are needed within "doResize"
			this.origWidth = hd.getWidth();
			App.log('orig width: ' + hd.getWidth());
			this.dragHd = hd;
			// set all column headers and tables to auto width -> fits to content
			columns.setWidth('auto');
			tables.setWidth('auto');
			App.log('auto width: ' + hd.getWidth());
			// get the maximum absolute width of all headers (max content width)
			columns.each(function(c) {
				App.log('get max width, old:'+width+', next: ' + c.getWidth());
				width = Math.max(width, c.getWidth());
			});
			App.log('max width:'+width);
			this.newWidth = width = Ext.Number.constrain(width, this.resizer.minColWidth, this.resizer.maxColWidth);

			// header.setWidth only works if the new width differs from the old width
			// see Ext.grid.column.Column#setSize
			// so we simply have to set the old width back
			if (width - this.origWidth === 0) {
				App.log('with not changed.');
				columns.setWidth(width);
				tables.setWidth(this.headerCt.getFullWidth());
				// otherwise resize the column to the new width
			} else {
				App.log('change width with do resize.');
				this.doResize();
			}
		}
	},
	adjustColumnWidth : function(header) {
	}
}, function() {
	// use the doResize method of HeaderResizer since it handles
	// all the special cases (flex, forceFit, ...)
	this.borrow(Ext.grid.plugin.HeaderResizer, 'doResize');
});