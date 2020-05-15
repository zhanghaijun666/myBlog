(function (global) {
    define(["text!./show.html", "css!./show.css"], function (pageView) {
        function CanvasThreeModel(params, componentInfo) {
            const self = this;
            const arr = self.getParams();
            const distortion = self.distortions();
            const options = $.extend(
                self.getCommonParams(),
                arr[Math.floor(Math.random() * arr.length)],
                {distortion: distortion[Math.floor(Math.random() * distortion.length)]}
            );
            const myApp = new App(document.getElementsByClassName('canvas-three')[0], options);
            myApp.loadAssets().then(myApp.init)
        }

        CanvasThreeModel.prototype.distortions = function () {
            return [
                mountainDistortion, LongRaceDistortion, xyDistortion, turbulentDistortion,
                turbulentDistortionStill, deepDistortionStill, deepDistortion
            ];
        };
        CanvasThreeModel.prototype.getParams = function () {
            return [
                {}, {
                    distortion: xyDistortion,
                    speedUp: 3,
                    lightPairsPerRoadWay: 30,
                    lightStickWidth: [0.02, 0.05],
                    lightStickHeight: [0.3, 0.7],
                    movingAwaySpeed: [20, 50],
                    movingCloserSpeed: [-150, -230],
                    carLightsLength: [400 * 0.05, 400 * 0.2],
                    carLightsRadius: [0.03, 0.08],
                    carWidthPercentage: [0.1, 0.5],
                    carShiftX: [-0.5, 0.5],
                    carFloorSeparation: [0, 0.1],
                    colors: {
                        roadColor: 0x080808,
                        islandColor: 0x0a0a0a,
                        background: 0x000000,
                        shoulderLines: 0x131318,
                        brokenLines: 0x131318,
                        leftCars: [0xFF5F73, 0xE74D60, 0xff102a],
                        rightCars: [0xA4E3E6, 0x80D1D4, 0x53C2C6],
                        sticks: 0xA4E3E6
                    }
                }, {
                    distortion: turbulentDistortion,
                    roadWidth: 10,
                    totalSideLightSticks: 20,
                    lightPairsPerRoadWay: 40,
                    carLightsLength: [400 * 0.03, 400 * 0.2],
                    carShiftX: [-0.8, 0.8],
                    carFloorSeparation: [0, 5],
                    colors: {
                        roadColor: 0x080808,
                        islandColor: 0x0a0a0a,
                        background: 0x000000,
                        shoulderLines: 0x131318,
                        brokenLines: 0x131318,
                        leftCars: [0xD856BF, 0x6750A2, 0xC247AC],
                        rightCars: [0x03B3C3, 0x0E5EA5, 0x324555],
                        sticks: 0x03B3C3,
                    }
                }, {
                    distortion: turbulentDistortionStill,
                    colors: {
                        roadColor: 0x080808,
                        islandColor: 0x0a0a0a,
                        background: 0x000000,
                        shoulderLines: 0x131318,
                        brokenLines: 0x131318,
                        /***  Only these colors can be an array ***/
                        leftCars: [0xDC5B20, 0xDCA320, 0xDC2020],
                        rightCars: [0x334BF7, 0xE5E6ED, 0xBFC6F3],
                        sticks: 0xC5E8EB,
                    }
                }, {
                    distortion: deepDistortionStill,
                    roadWidth: 18,
                    colors: {
                        roadColor: 0x080808,
                        islandColor: 0x0a0a0a,
                        background: 0x000000,
                        shoulderLines: 0x131318,
                        brokenLines: 0x131318,
                        /***  Only these colors can be an array ***/
                        leftCars: [0xFF322F, 0xA33010, 0xA81508],
                        rightCars: [0xFDFDF0, 0xF3DEA0, 0xE2BB88],
                        sticks: 0xFDFDF0,
                    }
                }, {
                    distortion: deepDistortion,
                    colors: {
                        roadColor: 0x080808,
                        islandColor: 0x0a0a0a,
                        background: 0x000000,
                        shoulderLines: 0x131318,
                        brokenLines: 0x131318,
                        /***  Only these colors can be an array ***/
                        leftCars: [0xE2173C, 0x841010, 0xF23D3D],
                        rightCars: [0xffffff, 0x7686BF, 0x1338B5],
                        sticks: 0xDCE0EE,
                    }
                }
            ]
        };
        CanvasThreeModel.prototype.getCommonParams = function () {
            return {
                onSpeedUp: (ev) => {
                },
                onSlowDown: (ev) => {
                },
                // mountainDistortion || LongRaceDistortion || xyDistortion || turbulentDistortion || turbulentDistortionStill || deepDistortionStill || deepDistortion
                distortion: mountainDistortion,

                length: 400,
                roadWidth: 9,
                islandWidth: 2,
                lanesPerRoad: 3,

                fov: 90,
                fovSpeedUp: 150,
                speedUp: 2,
                carLightsFade: 0.4,

                totalSideLightSticks: 50,
                lightPairsPerRoadWay: 50,

                // Percentage of the lane's width
                shoulderLinesWidthPercentage: 0.05,
                brokenLinesWidthPercentage: 0.1,
                brokenLinesLengthPercentage: 0.5,

                /*** These ones have to be arrays of [min,max].  ***/
                lightStickWidth: [0.12, 0.5],
                lightStickHeight: [1.3, 1.7],

                movingAwaySpeed: [60, 80],
                movingCloserSpeed: [-120, -160],

                /****  Anything below can be either a number or an array of [min,max] ****/

                // Length of the lights. Best to be less than total length
                carLightsLength: [400 * 0.05, 400 * 0.15],
                // Radius of the tubes
                carLightsRadius: [0.05, 0.14],
                // Width is percentage of a lane. Numbers from 0 to 1
                carWidthPercentage: [0.3, 0.5],
                // How drunk the driver is.
                // carWidthPercentage's max + carShiftX's max -> Cannot go over 1.
                // Or cars start going into other lanes
                carShiftX: [-0.2, 0.2],
                // Self Explanatory
                carFloorSeparation: [0.05, 1],

                colors: {
                    roadColor: 0x080808,
                    islandColor: 0x0a0a0a,
                    background: 0x000000,
                    shoulderLines: 0x131318,
                    brokenLines: 0x131318,
                    /***  Only these colors can be an array ***/
                    leftCars: [0xff102a, 0xEB383E, 0xff102a],
                    rightCars: [0xdadafa, 0xBEBAE3, 0x8F97E4],
                    sticks: 0xdadafa,
                }
            };
        };
        return {
            viewModel: {
                createViewModel: function (params, componentInfo) {
                    return new CanvasThreeModel(params, componentInfo);
                }
            },
            template: pageView
        };
    });
})(this);
