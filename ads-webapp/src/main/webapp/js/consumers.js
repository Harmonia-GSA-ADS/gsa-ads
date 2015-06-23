
/**
 * Performs an Adverse Event search
 */
function adverseEventSearch() {
	// TODO Deal with different deployment paths in a better way
	var context = location.pathname;
	if (context.indexOf('ads-webapp') > -1) {
		context = 'ads-webapp';
	} else {
		context = 'ads';
	}

	// make search request to server
	$.ajax('/' + context + '/rest/event', {
		success: function(data, textStatus, jqXHR) {
			
			var $resultsTable = $('#adverseEventsResults tbody');
			
			if (data.results) {
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
					
					$('#adverseEventsResultsPanel').show();
					navigate('adverseEventsResultsPanel');
				}
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			// TODO add error handling
			console.log(errorThrown);
		}
	});
}

/**
 * Saves an Adverse Event search
 */
function adverseEventSavedSearch() {
	// TODO Save Search
	
	navigate("aeSavedSearches");
}