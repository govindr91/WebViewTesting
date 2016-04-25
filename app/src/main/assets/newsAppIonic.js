'use strict';

var feedApp = angular.module('newsApp', ['ionic','tabSlideBox','infinite-scroll','ngSanitize']);

feedApp.controller('newsController', ['$scope', '$log','$ionicSlideBoxDelegate','$timeout', 'feedService',  function($scope, $log,$ionicSlideBoxDelegate,$timeout, feedService)
{

    //var feedId = 1;

    $scope.tabs=[{ link :null, label : null,data:[],end:false,length:0 }];

    feedService.getNewsCategories().success(function(categories){
        for(var row in categories){
            if(row==0){
                $log.info(categories[row].name);
                $scope.tabs[0].label=categories[row].name;
                $scope.tabs[0].link=categories[row].id;
                feedService.getFeedData($scope.tabs[0].link,$scope.tabs[0].label).success(function(data)
                {
                    $log.info(data);
                    $scope.data = data;
                    $scope.tabs[0].data=data;
                    $scope.tabs[0].length=1;
                }).error(function()
                {
                    $log.error("There was error fetching news");
                });
            }
            else{
                var tab={link:categories[row].id,label:categories[row].name,data:[],end:false,length:0};
                $scope.tabs.push(tab);
            }
        }
});

    $scope.called=0;


    $scope.onSlideMove = function(data_sent){
        var tabs=$scope.tabs;
        if(tabs[data_sent.index].data.length==0){
            feedService.getFeedData(tabs[data_sent.index].link,tabs[data_sent.index].label).success(function(data)
            {
                $scope.data = data;
                $scope.tabs[data_sent.index].data=data;

            }).error(function()
            {
                $log.error("There was error fetching news");
            });
        }
        else{
            $scope.data=$scope.tabs[data_sent.index].data;
        }
    };

    $scope.loadMore=function(index){
        if($scope.tabs[index].data.length!=0){
            var categoryId=$scope.tabs[index].link;
            var articleId=$scope.tabs[index].data[$scope.tabs[index].data.length-1].id;
            //var articleId=30213;
            if($scope.called==0&&!$scope.tabs[index].end){
                $scope.called=1;
                feedService.getMoreFeedData(categoryId,articleId).success(function(data, status, headers)
                {
                    if( status==200){
                        $log.info(data);
                        $scope.data=$scope.data.concat(data);
                        $scope.tabs[index].data=$scope.tabs[index].data.concat(data);
                        $scope.called=0;
                        $scope.tabs[index].length+=1;
                        analytics.track('Feed Loaded', {
                            name: $scope.tabs[index].label,
                            number:$scope.tabs[index].length
                        });
                    }
                    if ( status == 204 ) {
                        $scope.tabs[index].end = true;
                        $scope.called=0;
                    }
                }).error(function()
                {
                    $log.error("There was error fetching news");
                });
            }
        }


    };

    $scope.getSelector=function(index){
        return "#inscroll"+index;
    }

}]);


feedApp.filter('video',function(){
    return function (link){
        if(link == null)
            return "";
        link=link.replace("watch?v=", "embed/");
        return link.concat("?autoplay=1&autohide=1");
    };
});


feedApp.service('feedService', function($http) {

    return{
        getFeedData: getFeedData,
        getNewsCategories:getNewsCategories,
        getMoreFeedData: getMoreFeedData
    };

    function getFeedData(categoryId,categoryName)
    {

        var req  = $http.get("/feed/mobileapp/v4/news/" + categoryId);
        analytics.track('Feed Loaded', {
            event: categoryName
        });
        return req;
    }
    function getNewsCategories()
    {

        var req  = $http.get("/feed/news/v2/categories");
        return req;
    }
    function getMoreFeedData(categoryId,articleId)
    {

        var req  = $http.get("/feed/mobileapp/v4/news/" + categoryId+"/"+articleId+"/0");
        return req;
    }

});

feedApp.config(['$httpProvider', function ($httpProvider) {

    $httpProvider.defaults.headers.common['X-AKOSHA-AUTH'] = "eyJ1c2VyX25hbWUiOm51bGwsImlkIjo0OTk5MTEsIm1vYmlsZSI6Ijk5MTY1NTQ2NDgiLCJleHBpcmVzIjoxNzczOTg5NTMxNjIzfQ==.pdk8OqzttHLotzQNJvEXM+rryij+Irulp4JFae23Ors=";

}]);

feedApp.config(function($sceDelegateProvider) {
    $sceDelegateProvider.resourceUrlWhitelist(['**']);

});

feedApp.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider.state('index', {
        url: '/',
        templateUrl: 'index.html',
        controller: 'newsController'
    });
    $urlRouterProvider.otherwise("/");
});

feedApp.config(function($sceProvider)
{
    $sceProvider.enabled(false);

});
feedApp.config(['$compileProvider', function($compileProvider) {
    $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|file|tel):/);
    $compileProvider.aHrefSanitizationWhitelist(/^\s*(whatsapp):/);
}]);

