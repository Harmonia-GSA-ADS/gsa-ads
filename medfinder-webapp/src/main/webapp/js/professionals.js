$(document).ready(function() {
    		
	// configure search info pop ups
	$('#routeInfo').popover({
		container: 'body',
		placement: 'right',
		content: 'All drug criteria values are joined with the OR operator when the search is performed.'
	});
	$('#drugInfo').popover({
		container: 'body',
		placement: 'right',
		content: 'The criteria values are joined with the AND operator when the search is performed.'
	});
	
	$('#routesResultsPanel').hide();
	$('#drugsResultsPanel').hide();
	$('.form-reset').click(resetForm);
	
	$('.ssName').keyup(clearError);
	$('.ssSearch').click(runSavedSearch);
	$('.ssNew').click(newSavedSearch);
	$('.ssDelete').click(deleteSavedSearch);
	
	loadSavedSearches('ROUTES', $('#routeSSList'));
	loadSavedSearches('DRUGS', $('#drugSSList'));
	
	// jump to tab
	if (window.location.hash && window.location.hash.indexOf('drugLookup') > -1) {
		$('.nav-tabs a[href="#drugLookup"]').tab('show');
	}
	
	// submit form on Enter
	$('.form-control').keyup(function(e) {
		if (e.keyCode === 13) {
			var formId = $(this).parents('form').attr('id');
			if (formId === 'routeSearch') {
				routeSearch();
			} else if (formId === 'drugSearch') {
				drugSearch();
			} 
		}
	});
});

/**
 * Performs a Route of Administration search
 */
function routeSearch() {
	
	loading();
	
	// extract user-values from form
	var indication = $('#rIndication').val();
	var brandName = $('#rBrandName').val();
	var genericName = $('#rGenericName').val();
	var substanceName = $('#rSubstanceName').val();
	
	// extra search options from form
	var limit = $('#routeResultLimit').val();
	
	// clear results table
	var $resultsTable = $('#routesResults tbody');
	$resultsTable.empty();
	
	// make call to server and display results
	$.ajax('/' + getContext() + '/rest/routes', {
		type: 'get',
		data: {
			indication: indication,
			brandName: brandName,
			genericName: genericName,
			substanceName: substanceName,
			limit: limit
		},
		success: function(data, textStatus, jqXHR) {
			if (data.error) {
				loading(true);
				$('#routesResultsPanel').show();
				$('#routesResultsPanel table').hide();
				$('#routesResultsPanel .alert').show();
				navigate('routesResultsPanel');
			} else if (data.results) {
				for (var i = 0; i < data.results.length; i++) {
					var result = data.results[i];

					var rBrandName = result.openfda.brand_name;
					var rGenericName = result.openfda.generic_name;
					var rManufacturerName = result.openfda.manufacturer_name;
					var rSubstanceName = result.openfda.substance_name;
					var rRoutes = result.openfda.route;
					var rIndication = result.indications_and_usage;
					
					var $tr = $('<tr>');

					// brand name cell
					var $brandName = $('<td>');
					$brandName.text(rBrandName);
					$tr.append($brandName);
				
					// generic name cell
					var $genericName = $('<td>');
					$genericName.text(rGenericName);
					$tr.append($genericName);
					
					// routes cell
					var $routes = $('<td>');
					if (rRoutes) {
						var $routesList = $('<ul>').appendTo($routes);
						for (var j = 0; j < rRoutes.length; j++) {
							var rRoute = rRoutes[j];
							$('<li>').text(rRoute).appendTo($routesList);
						}
					}
					$tr.append($routes);
					
					// manufacturer name cell
					var $manufacturerName = $('<td>');
					$manufacturerName.text(rManufacturerName);
					$tr.append($manufacturerName);
					
					// substance name cell
					var $substanceName = $('<td>');
					$substanceName.text(rSubstanceName);
					$tr.append($substanceName);
					
					// indication cell
					var $indication = $('<td>');
					$indication.text(rIndication);
					$tr.append($indication);
					
					$resultsTable.append($tr); 
				}

				// store values in hidden field to support saved search creation
				$('#ssRouteIndication').val(indication);
				$('#ssRouteBrandName').val(brandName);
				$('#ssRouteGenericName').val(genericName);
				$('#ssRouteSubstanceName').val(substanceName);
				
				loading(true);
				
				$('#routesResultsPanel').show();
				$('#routesResultsPanel table').show();
				$('#routesResultsPanel .alert').hide();
				navigate('routesResultsPanel');
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			displayError(jqXHR.responseText);
			loading(true);
		}
	});
}

/**
 * Fills in values of the routes search form with values from the given saved search.
 * 
 * @param savedSearch Saved search from which to get values
 */
function populateRoutesSearchForm(savedSearch) {
	$('#rIndication').val(savedSearch.indication);
	$('#rBrandName').val(savedSearch.brandName);
	$('#rGenericName').val(savedSearch.genericName);
	$('#rSubstanceName').val(savedSearch.substanceName);
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
		var substanceName = $('#ssRouteSubstanceName').val();
		
		// make request to server to create saved search
		$.ajax('/' + getContext() + '/rest/search', {
			type: 'post',
			data: {
				name: ssName,
				type: 'ROUTES',
				indication: indication,
				brandName: brandName,
				genericName: genericName,
				substanceName: substanceName
			},
			success: function(data, textStatus, jqXHR) {
				if (data) {
					
					// clear name field
					$('#routeSSName').val('');
					
					var ssId = data.id;
					var dateTime = new Date(data.datetime);
					
					// add item to list
					$ssList = $('#routeSSList');
					addSavedSearch($ssList, ssId, ssName, dateTime);
					navigate("routesSavedSearches");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				displayError(jqXHR.responseText);
				loading(true);
			}
		});
	} else {
		$('#routeSSName').parent('span').addClass('has-error');
	}
}

/**
 * Performs a Drugs search
 */
function drugSearch() {
	
	// validate the form
	var validator = $('#drugsSearch').validate({
		rules: {
			dIndication: { required: true },
			dRoute : { required: true }
		}
	});
	var valid = validator.form();
	
	if (valid) {
	
		loading();
		
		// extract user-values from form
		var indication = $('#dIndication').val();
		var route = $('#dRoute').val();
		
		// extra search options from form
		var limit = $('#drugResultLimit').val();
	
		// clear results table
		var $resultsTable = $('#drugsResults tbody');
		$resultsTable.empty();
		
		// make call to server and display results
		$.ajax('/' + getContext() + '/rest/drugs', {
			type: 'get',
			data: {
				indication: indication,
				route: route,
				limit: limit
			},
			success: function(data, textStatus, jqXHR) {
				if (data.error) {
					loading(true);
					$('#drugsResultsPanel').show();
					$('#drugsResultsPanel table').hide();
					$('#drugsResultsPanel .alert').show();
					navigate('drugsResultsPanel');
				} else if (data.results) {
					for (var i = 0; i < data.results.length; i++) {
						var result = data.results[i];
	
						var rBrandName = result.openfda.brand_name;
						var rGenericName = result.openfda.generic_name;
						var rManufacturerName = result.openfda.manufacturer_name;
						var rRoutes = result.openfda.route;
						var rPurposes = result.purpose;
						var rIndication = result.indications_and_usage;
						
						var $tr = $('<tr>');
	
						// brand name cell
						var $brandName = $('<td>');
						$brandName.text(rBrandName);
						$tr.append($brandName);
					
						// generic name cell
						var $genericName = $('<td>');
						$genericName.text(rGenericName);
						$tr.append($genericName);
						
						// routes cell
						var $routes = $('<td>');
						var $routesList = $('<ul>').appendTo($routes);
						for (var j = 0; j < rRoutes.length; j++) {
							var rRoute = rRoutes[j];
							$('<li>').text(rRoute).appendTo($routesList);
						}
						$tr.append($routes);
						
						// indication cell
						var $indication = $('<td>');
						$indication.text(rIndication);
						$tr.append($indication);
						
						// purposes cell
						var $purposes = $('<td>');
						var $purposesList = $('<ul>').appendTo($purposes);
						if (rPurposes) {
							for (j = 0; j < rPurposes.length; j++) {
								var rPurpose = rPurposes[j];
								$('<li>').text(rPurpose).appendTo($purposesList);
							}
						}
						$tr.append($purposes);
						
						$resultsTable.append($tr); 
					}
	
					// store values in hidden field to support saved search creation
					$('#ssDrugIndication').val(indication);
					$('#ssDrugRoute').val(route);
					
					loading(true);
					
					$('#drugsResultsPanel').show();
					$('#drugsResultsPanel table').show();
					$('#drugsResultsPanel .alert').hide();
					navigate('drugsResultsPanel');
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				displayError(jqXHR.responseText);
				loading(true);
			}
		});
	}
}

/**
 * Fills in values of the drugs search form with values from the given saved search.
 * 
 * @param savedSearch Saved search from which to get values
 */
function populateDrugsSearchForm(savedSearch) {
	$('#dIndication').val(savedSearch.indication);
	$('#dRoute').val(savedSearch.route);
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
		
		// make request to server to create saved search
		$.ajax('/' + getContext() + '/rest/search', {
			type: 'post',
			data: {
				name: ssName,
				type: 'DRUGS',
				indication: indication,
				route: route
			},
			success: function(data, textStatus, jqXHR) {
				if (data) {
					
					// clear name field
					$('#drugSSName').val('');
					
					var ssId = data.id;
					var dateTime = new Date(data.datetime);
					
					// add item to list
					$ssList = $('#drugSSList');
					addSavedSearch($ssList, ssId, ssName, dateTime);
					navigate("drugsSavedSearches");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				displayError(jqXHR.responseText);
			}
		});
	} else {
		$('#drugSSName').parent('span').addClass('has-error');
	}
}

