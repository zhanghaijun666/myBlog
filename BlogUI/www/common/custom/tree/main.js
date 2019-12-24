(function (global) {
    define(['knockout', "text!./show.html","css!./show.css"], function (ko,pageView) {
        function TreeNode(name, item, options) {
            var defaultValue = {
                icon: ko.observable("fa fa-user"),
                active: ko.observable(false),
                isReadonly: false,
                reason: ""
            };
            var self = $.extend(this, defaultValue, options);
            self.name = ko.observable(name);
            self.data = ko.observable(item);
            self.nodes = ko.observableArray([]);
            self.isShowChild = ko.observable(false);
        }
        function CustomTreeModel(params, componentInfo) {
            var self = this;
            BaseComponent.call(self, params, componentInfo);
            self.fullRowTemplate = params.fullRow || "tree-item-template";
            self.noDataHint = params.noDataHint || l10n("operate_hint.empty");
            self.context = params.context || {};
            self.nodeData = ko.observableArray([]);
            self.getChildItem = params.getChildItem;  //function
            self.context.refreshRootNode = self.getChildNodes.bind(this);  // 抛出更新根节点的方法

            if (params.dataList && params.dataList instanceof Array && params.dataList.length > 0) {
                for (var i = 0; i < params.dataList.length; i++) {
                    self.nodeData.push(new TreeNode("ccc", params.dataList[i]));
                }
                self.nodeData.isShowChild(true);
            } else {
                self.getChildNodes({});
            }
        }
        CustomTreeModel.prototype.getNodeRowParams = function (node, element) {
            var pNode = ko.dataFor($(element).parent("ul")[0]);
            return {
                context: this.context
                , calPaddingLeft: this.calPaddingLeft
                , toggleVisibility: this.toggleVisibility.bind(this, node)
                , originItem: node.data()
                , isShowChild: node.isShowChild
                , refreshNode: this.getChildNodes.bind(this, node)
                , refreshParentNode: this.getChildNodes.bind(this, pNode instanceof TreeNode ? pNode : {})
            };
        };
        CustomTreeModel.prototype.getChildNodes = function (node) {
            var context = this;
            var isNode = node instanceof TreeNode;
            if (isFunction(context.getChildItem)) {
                if (isNode) {
                    node.nodes([new TreeNode("加载中...", null)]);
                    node.isShowChild(true);
                } else {
                    context.nodeData([new TreeNode("加载中...", null)]);
                }
                context.getChildItem(isNode ? node.data() : {}, function (items) {
                    var newChild = new Array();
                    for (var i = 0; i < items.length; i++) {
                        newChild.push(new TreeNode("", items[i]));
                    }
                    if (isNode) {
                        node.nodes(newChild);
                    } else {
                        context.nodeData(newChild);
                        if (newChild.length > 0) {
                            context.toggleVisibility(newChild[0]);
                        }
                    }
                });
            }
        };
        CustomTreeModel.prototype.calPaddingLeft = function (element) {
            var num = $(element).parents("ul").length - $(".custom-tree").parents("ul").length;
            return (num * 20) + "px";
        };
        CustomTreeModel.prototype.toggleVisibility = function (node) {
            if (!(node instanceof TreeNode && node.data())) {
                return;
            }
            if (node.isShowChild()) {
                node.isShowChild(false);
            } else if (node.nodes().length > 0) {
                node.isShowChild(true);
            } else {
                this.getChildNodes(node);
            }
        };
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new CustomTreeModel(params, componentInfo);
                }
            },
            template: pageView
        };

    });
})(this);
