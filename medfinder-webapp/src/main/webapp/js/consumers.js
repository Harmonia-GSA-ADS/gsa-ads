
/**
 * Performs an Adverse Event search
 */
function adverseEventSearch() {

	// validate the form
	var validator = $('#adverseEventsSearch').validate({
		rules: {
			minAge: { 
				number: true,
				min: 0,
				ageRangeMin: true
			},
			maxAge : { 
				number: true, 
				min: 0,
				ageRangeMax: true
			},
			minWeight : { 
				number: true,
				min: 0,
				weightRangeMin: true
			},
			maxWeight : { 
				number: true,
				min: 0,
				weightRangeMax: true
			}
		}
	});

	var valid = validator.form();
	
	if (valid) {
		loading();
		
		// extract user-values from form
		var minAge = $('#minAge').val();
		var maxAge = $('#maxAge').val();
		var gender = $('input:radio[name=gender]:checked').val();
		var minWeight = $('#minWeight').val();
		var maxWeight = $('#maxWeight').val();
		var indication = $('#indication').val();
		var brandName = $('#brandName').val();
		var genericName = $('#genericName').val();
		var substanceName = $('#substanceName').val();
		
		// extra search options from form
		var limit = $('#resultLimit').val();
		
		// clear results table
		var $resultsTable = $('#adverseEventsResults tbody');
		$resultsTable.empty();
	
		// make search request to server
		$.ajax('/' + getContext() + '/rest/events', {
			type: 'get',
			data: {
				ageStart: minAge,
				ageEnd: maxAge,
				gender: gender,
				weightStart: minWeight,
				weightEnd: maxWeight,
				indication: indication,
				brandName: brandName,
				genericName: genericName,
				substanceName: substanceName,
				limit: limit
			},
			success: function(data, textStatus, jqXHR) {
				if (data.error) {
					loading(true);
					$('#adverseEventsResultsPanel').show();
					$('#adverseEventsResultsPanel table').hide();
					$('#adverseEventsResultsPanel .alert').show();
					navigate('adverseEventsResultsPanel');
				} else if (data.results) {
					for (var i = 0; i < data.results.length; i++) {
						var result = data.results[i];
						
						var serious = result.serious == 1;
						var death = result.seriousnessdeath == 1;
						var reactions = result.patient.reaction;
						var drugs = result.patient.drug;
						var date = result.receiptdate;
						var age = result.patient.patientonsetage;
						var gender = result.patient.patientsex;
						var weight = result.patient.patientweight; //kg
						
						// sort and make drug list unique
						var d = [];
						for (j = 0; j < drugs.length; j++) {
							var dName = drugs[j].medicinalproduct;
							if (d.indexOf(dName) === -1) {
								d.push(dName);
							}
						}
						d.sort();
						
						// get gender display value
						var g = 'Unknown';
						if (gender == 1) {
							g = 'Male';
						} else if (gender == 2) {
							g = 'Female';
						}
						
						// convert weight to lbs
						if (weight) {
							weight = Math.round(weight * 2.20462 * 100) / 100;
						}
						
						var $tr = $('<tr>');
						
						// reactions cell
						var $reactions = $('<td>');
						var $reactionsList = $('<ul>').appendTo($reactions);
						for (var j = 0; j < reactions.length; j++) {
							var reaction = reactions[j];
							$('<li>').text(reaction.reactionmeddrapt).appendTo($reactionsList);
						}
						$tr.append($reactions);
						
						// drugs cell
						var $drugs = $('<td>');
						var $drugsList = $('<ul>').appendTo($drugs);
						for (j = 0; j < d.length; j++) {
							var drug = d[j];
							$('<li>').text(drug).appendTo($drugsList);
						}
						$tr.append($drugs);
						
						// age cell
						var $age = $('<td>');
						if (age) {
							$age.text(Math.round(age));
						}
						$tr.append($age);
						
						// gender cell
						var $gender = $('<td>');
						$gender.text(g);
						$tr.append($gender);
						
						// weight cell
						var $weight = $('<td>');
						$weight.text(weight);
						$tr.append($weight);
						
						// serious cell
						var $serious = $('<td>');
						$serious.text(serious ? 'Yes' : 'No');
						$tr.append($serious);
						if (serious) {
							$serious.addClass('danger');
						}
						
						// death cell
						var $death = $('<td>');
						$death.text(death ? 'Yes' : 'No');
						$tr.append($death);
						if (death) {
							$death.addClass('danger');
						}
						
						$resultsTable.append($tr); 
	
						// store values in hidden field to support saved search creation
						$('#ssMinAge').val(minAge);
						$('#ssMaxAge').val(maxAge);
						$('#ssGender').val(gender);
						$('#ssMinWeight').val(minWeight);
						$('#ssMaxWeight').val(maxWeight);
						$('#ssIndication').val(indication);
						$('#ssBrandName').val(brandName);
						$('#ssGenericName').val(genericName);
						$('#ssSubstanceName').val(substanceName);
						
						loading(true);
						
						$('#adverseEventsResultsPanel').show();
						$('#adverseEventsResultsPanel table').show();
						$('#adverseEventsResultsPanel .alert').hide();
						navigate('adverseEventsResultsPanel');
					}
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				// TODO add error handling
				console.log(errorThrown);
				loading(true);
			}
		});
	}
}

/**
 * Fills in values of the adverse events search form with values from the given saved search.
 * 
 * @param savedSearch Saved search from which to get values
 */
function populateAdverseEventsSearchForm(savedSearch) {
	$('#minAge').val(savedSearch.minAge > 0 ? savedSearch.minAge : '');
	$('#maxAge').val(savedSearch.maxAge > 0 ? savedSearch.maxAge : '');
	$('input:radio[name=gender]').val(savedSearch.gender);
	$('#minWeight').val(savedSearch.minWeight > 0 ? savedSearch.minWeight : '');
	$('#maxWeight').val(savedSearch.maxWeight > 0 ? savedSearch.maxWeight : '');
	$('#indication').val(savedSearch.indication);
	$('#brandName').val(savedSearch.brandName);
	$('#genericName').val(savedSearch.genericName);
	$('#substanceName').val(savedSearch.substanceName);
}

/**
 * Saves an Adverse Event search
 */
function adverseEventSavedSearch() {
	
	// get name
	var ssName = $('#ssName').val();
	
	if (ssName) {
		
		// extract search criteria values to save
		var minAge = $('#ssMinAge').val();
		var maxAge = $('#ssMaxAge').val();
		var gender = $('#ssGender').val();
		var minWeight = $('#ssMinWeight').val();
		var maxWeight = $('#ssMaxWeight').val();
		var indication = $('#ssIndication').val();
		var brandName = $('#ssBrandName').val();
		var genericName = $('#ssGenericName').val();
		var substanceName = $('#ssSubstanceName').val();
		
		// make request to server to create saved search
		$.ajax('/' + getContext() + '/rest/search', {
			type: 'post',
			data: {
				name: ssName,
				type: 'ADVERSE_EVENTS',
				minAge: minAge ? minAge : -1,
				maxAge: maxAge ? maxAge : -1,
				gender: gender,
				minWeight: minWeight ? minWeight : -1,
				maxWeight: maxWeight ? maxWeight: -1,
				indication: indication,
				brandName: brandName,
				genericName: genericName,
				substanceName: substanceName
			},
			success: function(data, textStatus, jqXHR) {
				if (data) {
					
					// clear name field
					$('#ssName').val('');
					
					var ssId = data.id;
					var dateTime = new Date(data.datetime);
					
					// add item to list
					$ssList = $('#ssList');
					addSavedSearch($ssList, ssId, ssName, dateTime);
					navigate("aeSavedSearches");
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				// TODO add error handling
				console.log(errorThrown);
			}
		});
	} else {
		$('#ssName').parent('span').addClass('has-error');
	}
}

/**
 * Custom validator for max age field. Min age value must be less 
 * than max age value, unless min age or max age value is undefined.
 */
$.validator.addMethod('ageRangeMin', function(value, element) {
	if (!value) { return true; }
	console.log($(element).attr('id'));
	if ($(element).attr('id') === 'minAge') {
		console.log(value);
		var maxVal = $('#maxAge').val();
		console.log(maxVal);
		return maxVal === '' || value <= Number(maxVal);
	} 
	return false;
}, 'Minimum age must be less than or equal to maximum age');

/**
 * Custom validator for max age field. Max age value must be greater 
 * than min age value, unless min age or max age value is undefined.
 */
$.validator.addMethod('ageRangeMax', function(value, element) {
	if (!value) { return true; }
	if ($(element).attr('id') === 'maxAge') {
		var minVal = $('#minAge').val();
		return minVal === '' || value >= Number(minVal);
	}
	return false;
}, 'Maximum age must be greater than or equal to minimum age');

/**
 * Custom validator for min weight field. Min weight value must be less 
 * than max weight value, unless min weight or max weight value is undefined.
 */
$.validator.addMethod('weightRangeMin', function(value, element) {
	if (!value) { return true; }
	if ($(element).attr('id') === 'minWeight') {
		var maxVal = $('#maxWeight').val();
		return maxVal === '' || value <= Number(maxVal);
	} 
	return false;
}, 'Minimum weight must be less than or equal to maximum weight');

/**
 * Custom validator for max weight field. Max weight value must be greater 
 * than min weight value, unless min weight or max weight value is undefined.
 */
$.validator.addMethod('weightRangeMax', function(value, element) {
	if (!value) { return true; }
	if ($(element).attr('id') === 'maxWeight') {
		var minVal = $('#minWeight').val();
		return minVal === '' ||  value >= Number(minVal);
	}
	return false;
}, 'Maximum weight must be greater than or equal to minimum weight');
