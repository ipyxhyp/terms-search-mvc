Ext.define('termsSearch.view.MainContainer', {
    extend: 'Ext.panel.Panel',
    alias:  'widget.mainContainer',
    title:  'Vega Search',
    layout: {"type" : 'hbox', "align":'center'},
    width : 800,
    height : 350,

    items:[
        {
            xtype : 'form',
            id : 'searchForm',
            title: 'Search Form',
            bodyPadding: 5,
            width: 350,
            height : 100,
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Search Term',
                id:'searchText1',
                name: 'searchTerm',
                allowBlank: false
            } ],
            buttons: [
                {
                    text: 'Submit',
                    formBind: true,
                    action: 'submit',
                    handler : function(){
                        Ext.getCmp('searchForm').getForm().submit({
                            url: 'get/foundTerms.json',
                            submitEmptyText: false,
                            waitMsg: 'Searching Data...',

                            success: function(form, action) {
                                var termsStore = Ext.getStore('TermStorage');
                                termsStore.load(action.result);
                            },
                            failure: function(form, action) {
                                var termsStore = Ext.getStore('TermStorage'),
                                    pagingToolbar =  Ext.getCmp('gridPanel1').down('pagingtoolbar'),
                                    rowCount = action.result ? action.result["totalTermRowCount"]: 0;
                                if (rowCount > 0) {
                                    termsStore.loadData(action.result["termRowList"]);
                                    termsStore.totalCount = (rowCount <= pagingToolbar.store.pageSize ? 1 : rowCount );
                                    pagingToolbar.getPageData().currentPage = 1;
                                    pagingToolbar.enable();
                                }
                            }
                        });
                    }
                }
            ]
        },{
            xtype: 'combo',
            classMember: 'letterFormat',
            name: 'letterFormat',
            fieldLabel: 'Quote Letter Format',
            valueField: 'value',
            displayField: 'name',
            forceSelection: true,
            triggerAction: 'all',
            mode: 'local',
            labelWidth : 220,
            store: new Ext.data.JsonStore({
                root: 'rows',
                fields: ['value', 'name'],
                data: {
                    rows: [
                        {value: 'pdf', name: 'Detailed Quote - PDF'},
                        {value: 'word', name: 'Detailed Quote - MS Word'}
                    ]
                }
            })
        },
        {
            xtype: 'splitter'
        },
        {
            xtype: 'gridpanel',
            id: 'gridPanel1',
            title: 'Search Vega',
            store: 'TermStorage',
            name: 'itemsGrid',
            columns: [
                {text: 'column 1', dataIndex: 'column1', flex: 1},
                {text: 'column 2', dataIndex: 'column2', flex: 2},
                {text: 'column 3', dataIndex: 'column3', flex : 2}
            ],
            columnLines: true,
            selModel: 'rowmodel',
            dockedItems: [


                {
                    xtype  : 'pagingtoolbar',
                    store: 'TermStorage',
                    displayInfo: true,
                    disabled : true,
                    dock : 'bottom',
                    displayMsg: '{0} - {1} of {2}',

                    doRefresh : function (){
                        var me = this,
                            current = me.store.currentPage,
                            submitBtn  = Ext.getCmp('searchForm').down('button[action=submit]');
                            submitBtn.handler(submitBtn); // fine!!!
                    },

                    moveNext : function(){
                        // call server ajax and get ranged results by page number
                        var me = this,
                            total = me.getPageData().total,
                            pageCount = me.getPageData().pageCount,
                            next = me.store.currentPage + 1;

                        if (next   <=  pageCount) {
                            if (me.fireEvent('beforechange', me, next) !== false) {
                                me.store.nextPage();
                                var toRange = 0;
                                if(next == pageCount){
                                    toRange = pageCount * me.store.pageSize - total;
                                    this.toggleToolbarButton(me,'next');
                                    this.toggleToolbarButton(me,'last');
                                }
                                this.callServer(next,toRange > 0 ? toRange : me.store.pageSize);
                            }
                        } else {
                            this.callServer(me.store.currentPage - 1, me.store.pageSize);
                        }

                    },
                    movePrevious : function(){
                        var me = this;
                        var previous = me.store.currentPage - 1;
                        // call server ajax and get ranged results by page number
                        if(previous > 0){
                            me.store.previousPage();
                            this.callServer((previous == 1)? 0 : previous ,me.store.pageSize);
                        }
                    },
                    changePage : function(page ){
                        var me = this;
                        this.callServer(page, me.store.pageSize);
                    },

                    moveLast : function(){
                        var me = this,
                            last = me.getPageData().pageCount;
                        if (me.fireEvent('beforechange', me, last) !== false) {
                            me.store.currentPage =  last;
                            me.store.loadPage(last);
                            last--;
                            this.callServer(last,me.store.pageSize);
                            this.toggleToolbarButton(me,'next');
                            this.toggleToolbarButton(me,'last');
                        }
                    },
                    moveFirst : function(){
                        var me = this,
                            first = 1 ;
                        me.store.currentPage =  first;
                        me.store.loadPage(first);
                        this.callServer(--first,me.store.pageSize);
                    },
                    callServer : function(pageNumber , range ){
                        var  result,totalItems,$termsStore;
                        var from =  pageNumber * range ,
                            to = from + range;
                        Ext.Ajax.request({
                            url:'get/termsInRange.json',
                            disableCaching: true,
                            method : "GET",
                            type : "application/json",
                            params: {
                                from : from,
                                to :  to
                            },
                            success: function(response,request) {

                                result = Ext.decode(response.responseText);
                                totalItems = result["totalTermRowCount"]
                                $termsStore = Ext.getStore('TermStorage');
                                $termsStore.totalCount = totalItems;
                                $termsStore.loadData(result["termRowList"]);

                            },
                            error: function(response,request) {
                                Ext.Msg.alert(' request failed');
                                var $termsStore = Ext.getStore('TermStorage');
                                $termsStore.loadData(response.responseText);
                            }
                        });
                    },

                    toggleToolbarButton : function(toolbar,buttonName){
                        if(toolbar && buttonName && buttonName != "") {
                            var $button =  toolbar.getComponent(buttonName);
                            if ($button) {
                                if (!$button.isDisabled()) {
                                    $button.disable();
                                }
                            }
                        }
                    }
                }
            ],
            width: 450,
            height : 290
        }
    ]
});

Ext.define('termsSearch.view.MainViewport', {
    extend : 'Ext.container.Viewport',
    alias : 'widget.mainViewport',
    requires: [
        'termsSearch.view.MainContainer'
    ],
    items : [{
        xtype : 'mainContainer'
    }]
});


Ext.define('termsSearch.model.TermModel', {
    extend: 'Ext.data.Model',
    fields: ['column1','column2','column3']
});

Ext.define('termsSearch.store.TermStorage', {
    extend: 'Ext.data.Store',
    model: 'termsSearch.model.TermModel',
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



var contextMenu = Ext.create('Ext.menu.Menu',{
    items: [{
        text: 'Execute Action',
        iconCls: 'execute',
        handler: function(data){
            var gridPanel1 = Ext.getCmp('gridPanel1'),
                searchTerm = gridPanel1.getSelectionModel().getSelection()[0].data["column2"],
                result;
            if(searchTerm !== null){
                searchTerm = encodeURIComponent(searchTerm);
                Ext.Ajax.request({
                    url:'search/g',
                    disableCaching: true,
                    method : "GET",
                    type : "text/html",
                    params: {
                        searchTerm: searchTerm
                    },
                    success: function(response,request) {
                        result = response.responseText;
                        //result = Ext.decode(response.responseText);
                        var iFrameCmp= $('#responseIframe');
                        var searchResultsDiv =  iFrameCmp.contents().find('body > #searchResultsDiv');
                        searchResultsDiv.html(result);
                    },
                    error: function(response,request) {
                        result = Ext.decode(response.responseText);
                    }
                });
            } else {
                Ext.Msg.alert(" The search term is empty ");
            }

        }
    }]
})

Ext.define('termsSearch.controller.TermsController', {
    extend: 'Ext.app.Controller',
    stores: ['TermStorage'],
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
                itemcontextmenu: function(view, rec, node, index, event) {
                    event.stopEvent();
                    contextMenu.showAt(event.getXY());
                    return false;
                }
            }
        });
    }
});


Ext.application({
    name: 'termsSearch',
    appFolder: 'resources/js/app',
    controllers: ['TermsController'],
    launch: function() {
        Ext.create('termsSearch.view.MainViewport');
    }
});
