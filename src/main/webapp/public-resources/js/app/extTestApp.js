Ext.define('testApp.view.MainContainer', {
	extend: 'Ext.panel.Panel',
	alias:  'widget.mainContainer',
	title:  'Test App Template',
	layout: {"type" : 'hbox', "align":'center'},
	width : 800,
	height : 350,

	items:[

	]
});
var items = [];



getTableItems: function(items){
 for(var i = 0 ; i <= 8, ++i){
	 item =
	 items.push();
 	for (j = 0; j<= 8 ++j){


	 }

 };


},

Ext.define('testApp.view.MainViewport', {
	extend : 'Ext.container.Viewport',
	alias : 'widget.mainViewport',
	requires: [
		'testApp.view.MainContainer'
	],
	items : [{
		xtype : 'mainContainer'
	}]
});


Ext.define('testApp.model.TestModel', {
	extend: 'Ext.data.Model',
	fields: ['column1','column2','column3']
});

Ext.define('testApp.store.TestStorage', {
	extend: 'Ext.data.Store',
	model: 'testApp.model.TestModel',
	data:[] ,
	lastOptions: {params: {start: 0, limit: 10}},
	pageSize : 10,
	proxy: {
		type: 'memory',
		reader:{
			type: 'array',
			root: '',
			totalProperty:'totalCount'
		}
	}
});

Ext.define('termsSearch.controller.TermsController', {
	extend: 'Ext.app.Controller',
	stores: ['TestStorage'],
	views: ['MainViewport'],
	refs:[
		{
			ref: 'mainContainer',
			selector: 'mainContainer'
		},
		{
			ref: 'itemsGrid',
			selector: 'panel > gridpanel[name=itemsGrid]'
		}
	],

	init: function () {
		this.control({
			'mainContainer > gridpanel[name=itemsGrid]' : {
//				itemcontextmenu: function(view, rec, node, index, event) {
//					event.stopEvent();
//					contextMenu.showAt(event.getXY());
//					return false;
//				}
			}
		});
	}
})



Ext.application({
	name: 'testAPP',
	appFolder: 'resources/js/app',
	controllers: ['TestAppController'],
	launch: function() {
		Ext.create('testApp.view.MainViewport');
	}
});
