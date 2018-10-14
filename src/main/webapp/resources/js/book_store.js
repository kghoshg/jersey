var app = angular.module("bookStoreApp", []);

app.service('bookStoreService', function($http) {
	this.getAllCategories = function() {
		return $http({
			method : 'GET',
			url : "/bookstore/rest/product_catalogue/categories"
		});
	};
});

app.controller('bookStoreController', function($scope, bookStoreService) {
	$scope.categories = [];
	bookStoreService.getAllCategories().then(function(result) {
		if (result) {
			$scope.categories = result.data;
		}
	});
});
