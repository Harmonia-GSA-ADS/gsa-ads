
/**
 * Performs a Route of Administration search
 */
function routeSearch() {
	
	loading();
	
	// TODO Deal with different deployment paths in a better way
	var context = location.pathname;
	if (context.indexOf('ads-webapp') > -1) {
		context = 'ads-webapp';
	} else {
		context = 'ads';
	}

	// extract user-values from form
	var indication = $('#rIndication').val();
	var brandName = $('#rBrandName').val();
	var genericName = $('#rGenericName').val();
	var manufacturerName = $('#rManufacturerName').val();
	var substanceName = $('#rSubstanceName').val();

	// TODO make call to server and display results
	
	// store values in hidden field to support saved search creation
	$('#ssRouteIndication').val(indication);
	$('#ssRouteBrandName').val(brandName);
	$('#ssRouteGenericName').val(genericName);
	$('#ssRouteManufacturerName').val(manufacturerName);
	$('#ssRouteSubstanceName').val(substanceName);
	
	loading(true);
	
	$('#routesResultsPanel').show();
	navigate('routesResultsPanel');
}

/**
 * Saves an Route of Administration search
 */
function routeSavedSearch() {
	
	// get name
	var ssName = $('#routeSSName').val();
	if (ssName) {
		
		// extract search criteria values to save
		var indication = $('#ssRouteIndication').val();
		var brandName = $('#ssRouteBrandName').val();
		var genericName = $('#ssRouteGenericName').val();
		var manufacturerName = $('#ssRouteManufacturerName').val();
		var substanceName = $('#ssRouteSubstanceName').val();

		
		// TODO make call to server
		var ssId = 1111;
		var dateTime = '6/23/2015 2:30pm';
		
		// add item to list
		$ssList = $('#routeSSList');
		addSavedSearch($ssList, ssId, ssName, dateTime);
		navigate("routesSavedSearches");
	} else {
		$('#routeSSName').parent('span').addClass('has-error');
	}
}


/**
 * Performs a Drugs search
 */
function drugSearch() {
	
	loading();
	
	// TODO Deal with different deployment paths in a better way
	var context = location.pathname;
	if (context.indexOf('ads-webapp') > -1) {
		context = 'ads-webapp';
	} else {
		context = 'ads';
	}
	
	// extract user-values from form
	var indication = $('#dIndication').val();
	var route = $('#dRoute').val();

	// TODO make call to server and display results
	
	// store values in hidden field to support saved search creation
	$('#ssDrugIndication').val(indication);
	$('#ssDrugRoute').val(route);
	
	loading(true);
	
	$('#drugsResultsPanel').show();
	navigate('drugsResultsPanel');
}

/**
 * Saves a Drugs search
 */
function drugSavedSearch() {
	
	// get name
	var ssName = $('#drugSSName').val();
	if (ssName) {
		
		// extract search criteria values to save
		var indication = $('#ssDrugIndication').val();
		var route = $('#ssDrugRoute').val();
		
		// TODO make call to server
		var ssId = 1111;
		var dateTime = '6/23/2015 2:30pm';

		// add item to list
		$ssList = $('#drugSSList');
		addSavedSearch($ssList, ssId, ssName, dateTime);
		navigate("drugsSavedSearches");
	} else {
		$('#drugSSName').parent('span').addClass('has-error');
	}

}