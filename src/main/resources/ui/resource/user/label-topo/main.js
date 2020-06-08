(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function LabelTopoModel(params, componentInfo) {
            let self = this;
            BaseComponent.call(self, params, componentInfo);
            setTimeout(function () {
                // self.init($(pageView).find("canvas")[0]);
                self.init($(".label_topo>canvas")[0]);
            }, 500);
        }

        LabelTopoModel.prototype.init = function (canvas) {
            var stage = new JTopo.Stage(canvas);
            var scene = new JTopo.Scene(stage);
            scene.background = "http://www.jtopo.com/img/bg.jpg";

            var node = new JTopo.Node("Hello");
            // node.setSize(10, 10);
            scene.add(node);
        };

        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new LabelTopoModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
