
/**
 * Performs an Adverse Event search
 */
function adverseEventSearch() {

	// validate the form
	var validator = $('#adverseEventsSearch').validate({
		rules: {
			minAge: { number: true },
			maxAge : { number: true },
			minWeight : { number: true},
			maxWeight : { number: true}
		}
	});
	var valid = validator.form();
	
	if (valid) {
		loading();
		
		// TODO Deal with different deployment paths in a better way
		var context = location.pathname;
		if (context.indexOf('ads-webapp') > -1) {
			context = 'ads-webapp';
		} else {
			context = 'ads';
		}
		
		// extract user-values from form
		var minDate = $('#minDate').val();
		var maxDate = $('#maxDate').val();
		var minAge = $('#minAge').val();
		var maxAge = $('#maxAge').val();
		var gender = $('input:radio[name=gender]:checked').val();
		var minWeight = $('#minWeight').val();
		var maxWeight = $('#maxWeight').val();
		var indication = $('#indication').val();
		var brandName = $('#brandName').val();
		var genericName = $('#genericName').val();
		var manufacturerName = $('#manufacturerName').val();
		var substanceName = $('#substanceName').val();
		
		// clear results table
		var $resultsTable = $('#adverseEventsResults tbody');
		$resultsTable.empty();
	
		// make search request to server
		$.ajax('/' + context + '/rest/events', {
			type: 'get',
			data: {
				dateStart: minDate,
				dateEnd: maxDate,
				ageStart: minAge,
				ageEnd: maxAge,
				gender: gender,
				weightStart: minWeight,
				weightEnd: maxWeight,
				indication: indication,
				brandName: brandName,
				genericName: genericName,
				manufacturerName: manufacturerName,
				substanceName: substanceName
			},
			success: function(data, textStatus, jqXHR) {
				if (data.error) {
					// TODO handle error
					loading(true);
					window.alert(data.error.message);
				} else if (data.results) {
					for (var i = 0; i < data.results.length; i++) {
						var result = data.results[i];
						
						var serious = result.serious == 1;
						var death = result.seriousnessdeath == 1;
						var reactions = result.patient.reaction;
						var drugs = result.patient.drug;
						var date = result.receiptdate;
						
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
						for (j = 0; j < drugs.length; j++) {
							var drug = drugs[j];
							$('<li>').text(drug.medicinalproduct).appendTo($drugsList);
						}
						$tr.append($drugs);
						
						// serious cell
						var $serious = $('<td>');
						$serious.text(serious ? 'Yes' : 'No');
						$tr.append($serious);
						
						// death cell
						var $death = $('<td>');
						$death.text(death ? 'Yes' : 'No');
						$tr.append($death);
						
						$resultsTable.append($tr); 
	
						// store values in hidden field to support saved search creation
						$('#ssMinDate').val(minDate);
						$('#ssMaxDate').val(maxDate);
						$('#ssMinAge').val(minAge);
						$('#ssMaxAge').val(maxAge);
						$('#ssGender').val(gender);
						$('#ssMinWeight').val(minWeight);
						$('#ssMaxWeight').val(maxWeight);
						$('#ssIndication').val(indication);
						$('#ssBrandName').val(brandName);
						$('#ssGenericName').val(genericName);
						$('#ssManufacturerName').val(manufacturerName);
						$('#ssSubstanceName').val(substanceName);
						
						loading(true);
						
						$('#adverseEventsResultsPanel').show();
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
 * Saves an Adverse Event search
 */
function adverseEventSavedSearch() {
	
	// get name
	var ssName = $('#ssName').val();
	
	if (ssName) {
		
		// extract search criteria values to save
		var minDate = $('#ssMinDate').val();
		var maxDate = $('#ssMaxDate').val();
		var minAge = $('#ssMinAge').val();
		var maxAge = $('#ssMaxAge').val();
		var gender = $('#ssGender').val();
		var minWeight = $('#ssMinWeight').val();
		var maxWeight = $('#ssMaxWeight').val();
		var indication = $('#ssIndication').val();
		var brandName = $('#ssBrandName').val();
		var genericName = $('#ssGenericName').val();
		var manufacturerName = $('#ssManufacturerName').val();
		var substanceName = $('#ssSubstanceName').val();
		
		// TODO make call to server
		var ssId = 1111;
		var dateTime = '6/23/2015 2:30pm';
		
		// add item to list
		$ssList = $('#ssList');
		addSavedSearch($ssList, ssId, ssName, dateTime);
		navigate("aeSavedSearches");
	} else {
		$('#ssName').parent('span').addClass('has-error');
	}
}