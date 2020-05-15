define(['knockout', "text!./show.html", "css!./show.css"], function (ko, pageView) {
    function PersonalModel(params, componentInfo) {
        var self = this;
        BaseComponent.call(self, params, componentInfo);

        // setTimeout(function () {
        //     $(".personal").scroll(function () {
        //         console.log($(".personal").offset());
        //         console.log($(".personal .navbar").offset());
        //         if ($(".personal .navbar").offset().top > 50) {
        //             $(".personal .navbar-fixed-top").addClass("top-nav-collapse");
        //         } else {
        //             $(".personal .navbar-fixed-top").removeClass("top-nav-collapse");
        //         }
        //     });
        // }, 500);
    }

    PersonalModel.prototype.gotoElement = function (element) {
        $(".personal").find(element)[0].scrollIntoView();
    };
    PersonalModel.prototype.sendEmail = function () {
        bootoast({
            message: '暂未开通，请联系管理员',
            type: 'warning',
            position: 'bottom-right',
            timeout: 1
        });
    };

    return {
        viewModel: {
            createViewModel: function (params, componentInfo) {
                return new PersonalModel(params, componentInfo);
            }
        },
        template: pageView
    };
});
