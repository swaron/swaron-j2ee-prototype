Ext.define('Lib.boots.NavItem', {
    /* Begin Definitions */
    alias: 'widget.boots.navitem',
    extend: 'Lib.Button',
	baseCls : Ext.baseCSSPrefix + 'boots-navitem',
    
    //default style
    btnCls:'btn-primary',
    enableToggle:true,
    //toggle group must be specified
    toggleGroup:'default-nav-group',
    pressed:false
});