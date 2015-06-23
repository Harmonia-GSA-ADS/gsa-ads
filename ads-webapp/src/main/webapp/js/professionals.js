
/**
 * Performs a Route of Administration search
 */
function routeSearch() {
	// TODO Deal with different deployment paths in a better way
	var context = location.pathname;
	if (context.indexOf('ads-webapp') > -1) {
		context = 'ads-webapp';
	} else {
		context = 'ads';
	}

	$('#routesResultsPanel').show();
	navigate('routesResultsPanel');
}

/**
 * Saves an Route of Administration search
 */
function routeSavedSearch() {
	// TODO Save Search
	
	navigate("routesSavedSearches");
}


/**
 * Performs a Drugs search
 */
function drugSearch() {
	// TODO Deal with different deployment paths in a better way
	var context = location.pathname;
	if (context.indexOf('ads-webapp') > -1) {
		context = 'ads-webapp';
	} else {
		context = 'ads';
	}

	$('#drugsResultsPanel').show();
	navigate('drugsResultsPanel');
}

/**
 * Saves a Drugs search
 */
function drugSavedSearch() {
	// TODO Save Search
	
	navigate("drugsSavedSearches");
}