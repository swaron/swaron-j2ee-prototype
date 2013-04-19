/**
 * Override default configurations of some ext component. <br>
 * 这个文件根据应用程序的需要，重新定义Ext的组件的一些默认值和默认行为.<br>
 * 对应extjs版本为4.2，如果升级Ext时，需检查新版本的Ext在这里的默认值是否已经改变.
 */

/**
 * x-border-box 是个很奇怪的css， 称之为边框盒子模型。ext所有的布局大小都以这个为基础。<br>
 * 边框盒子模型表示： 一个元素的宽度（width属性），代表了这个元素的border，padding和内容区域。<br>
 * 内容盒子模型表示： 一个元素的宽度(width属性)，只代表了这个元素的内容区域。<br>
 * 一个元素所需要占据的空间，包括元素的 <i>margin</i>，border，padding和内容区域。<br>
 * w3c定的html标准是内容盒子模型。 可能很多人会觉得 边框盒子模型更符合直觉，可w3c那些家伙就是把内容盒子设为默认标准的。<br>
 * 
 * Ext 在body标签上加上x-border-box的css属性，告诉浏览器把盒子模型改为边框盒子模型，因为设置在body上，所有整个页面都会生效。<br>
 * 相关的代码参考Ext源文件里面的 Ext.isBorderBox。<br>
 * 边框盒子模型与标准不同，所以会影响其他框架或者用户写的CSS。<br>
 * 这边的解决办法是去掉放在body标签上的x-border-box, 在ext组件的最外层div上加上这个css属性。<br>
 * 这样保持ext组件外面的div不受这个x-border-box影响，ext组件里面又继承这个属性而可以正确地布局。<br>
 * 
 * 注意，如果新的项目里面只用了extjs，没有用其他ui框架，那么不需要这样做，保持边框盒子模型，在写css的时候明白width包括 border和padding即可。<br>
 */
Ext.onReady(function() {
	// 在页面完成加载之后，去掉ext在body标签上加的x-border-box css属性。
	Ext.fly(Ext.getBody().dom.parentNode).removeCls('x-border-box');
});

/**
 * 在ext组件创建的时候（onRender）： 1. 如果当前组件是顶层元素，那么加上x-border-box的css。<br>
 * 2. 如果当前组件是顶层容器，那么加上自动适应窗口大小的功能。 ---这个功能是为应用程序添加的，不是对Ext默认行为的修改。
 */
Ext.require(['Ext.container.Container'], function() {
	Ext.override(Ext.container.Container, {
		beforeRender : function() {
			var me = this;
			if (!me.ownerCt) {
				me.addCls('x-border-box');
				if (me.isXType('container')) {
					Ext.EventManager.onWindowResize(me.doLayout, me);
				}
			}
			this.callParent();
		}
	});
});

/**
 * for test purpose,set top level panel's frame.
 */
Ext.require(['Ext.container.Container'], function() {
	Ext.override(Ext.container.Container, {
		constructor : function(config) {
			var me = this;
			if (!me.ownerCt) {
				if (me.isXType('panel')) {
					//for test, switch between frame and no frame.
					if(Ext.isObject(config) && config.hasOwnProperty('frame') && config.frame == true){
						config.frame = true;
					}
				}
			}
			this.callParent(arguments);
		}
	});
});

/**
 * Ext.MessageBox是在执行这个文件之前就实例化了的单例。 这个单例当时实例化的时候不会添加'x-border-box'.这里给它补上去。
 * 
 */
Ext.require(['Ext.window.MessageBox'], function() {
	Ext.MessageBox.addCls('x-border-box');
});

/**
 * 设置json请求的默认header
 */
Ext.require(['Ext.data.proxy.Ajax', 'Ext.Ajax'], function() {
	// Spring MVC 可以根据请求的文件类型来判断是返回 json还是xml数据, 设置默认值为json来请求json数据。
	Ext.override(Ext.data.proxy.Ajax, {
		// 这里设置请求头
		headers : {
			'Accept' : 'application/json'
		}
	});
	// Ext.Ajax是在执行到这里之前创建的的单例，不会使用上面指定的值，也需要设置。
	Ext.Ajax.defaultHeaders = {
		'Accept' : 'application/json'
	};
});

/**
 * 设置默认组件默认值
 * <ul>
 * <li>items xtype类型： textfield</li>
 * <li>textfield 宽度： 280</li>
 * <li>labelWidth ： 90</li>
 * <li>labelAlign ： right</li>
 * </ul>
 * 
 */

/**
 * 设置form的默认值
 */
Ext.require(['Ext.form.Panel' ], function() {
	Ext.override(Ext.form.Panel, {
		layout : 'column',
		defaultType :'textfield',
		fieldDefaults:{
			margin : 5,
			width : 280,
			xtype: 'textfield',
			labelWidth : 90,
			labelAlign : 'right'
		}
	});

});

/**
 * 设置默认日期格式,
 * 
 */
Ext.require(['Ext.Date' ], function() {
	Ext.override(Ext.Date, {
		defaultFormat : "Y-m-d"
	});
//	submitFormat
});
Ext.require(['Ext.form.field.Date' ], function() {
	Ext.override(Ext.form.field.Date, {
		//date fomat: "time      A javascript millisecond timestamp" and "c         ISO 8601 date" is acceptable, refer to format in Ext.Date
		submitFormat : "c"
	});
});

/**
 * 设置默认的按钮类型, 暂时没有办法为弹出windows的button自动添加btn-primary类，需要手动设置.
 */
Ext.require(['Ext.toolbar.Toolbar','Ext.toolbar.Paging','App.button.Button'], function() {
	Ext.override(Ext.toolbar.Toolbar, {
		defaultType : 'app.button'
	});
	//paging bar的默认button改不得
	Ext.override(Ext.toolbar.Paging, {
		defaultType : 'button'
	});
});

/**
 * 设置 表格宽度自匹配 功能， 使用AutoWidth <b>注意这个AutoWidth是个插件，使用AutoWidth之后无法手动设置Column宽度</b><br>
 * features 可能的构造方式如下。 <br>
 * features: [Ext.create('Ext.grid.feature.GroupingSummary', {groupHeaderTpl: 'Subject: {name}'})], <br>
 * features: [{ftype: 'groupingsummary', groupHeaderTpl: 'Subject: {name}'}], <br>
 * features: ['grouping', 'groupingsummary'], ps; this seems not working, refer to view.Table.js.698
 */
Ext.require(['Ext.grid.Panel', 'Ext.ux.grid.feature.AutoColumnWidth'], function() {
	Ext.override(Ext.grid.Panel, {
		//默认设置 forcefit 
		forceFit:true,
		//设置默认带checkbox，simple选择默认， 自定义的时候通常要修改selType或者disableSelection属性。
		selType : 'checkboxmodel',//rowmodel,cellmodel, 
		disableSelection:false,
		selModel : {
			showHeaderCheckbox : true,
			// options:"SINGLE"/"SIMPLE"/"MULTI"
			mode : "SIMPLE"
		},
		// 提供1个属性来控制是否添加 auto width feature功能，默认为true
		autoColumnWidthFetaure : true,
		findFeature : function(ftype) {
			if (this.features) {
				// 先统一为数组
				if (!Ext.isArray(this.features)) {
					this.features = [this.features];
				}
				// 判断是string还是object，对应查找
				return Ext.Array.findBy(this.features, function(feature) {
					if (!feature) {
						return null;
					}
					if (Ext.isString(feature)) {
						if (feature === ftype) {
							return true;
						}
					} else {
						if (feature.ftype === ftype) {
							return true;
						}
					}
				});
			} else {
				return null;
			}
		},
		initComponent : function() {
			var me = this;
			if (me.autoColumnWidthFetaure) {
				var ftype = 'autocolumnwidth';
				var feature = this.findFeature(ftype);
				// 如果已经存在，则不重复添加
				if (feature == null) {
					me.features = Ext.Array.push(me.features, {
						ftype : ftype
					});
				}
			}
			this.callParent(arguments);
		}
	});

});
// fix tool tip bug in chrome.
// Ext 4.2 的tooltip在chrome下有bug，这是修复补丁代码。
Ext.require(['Ext.tip.Tip'], function() {
	delete Ext.tip.Tip.prototype.minWidth;
});