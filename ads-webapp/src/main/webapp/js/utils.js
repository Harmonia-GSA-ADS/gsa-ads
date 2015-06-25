
/**
 * Clears error styles on an element
 */
function clearError() {
	$(this).parent('span').removeClass('has-error');
}

/**
 * Click handler for saved search execution button
 */
function runSavedSearch() {
	var ssId = $(this).parent('li').attr('ssId');
	
	// get saved search data from server
	$.ajax('/' + getContext() + '/rest/search', {
		type: 'get',
		data: {
			id: ssId,
		},
		success: function(data, textStatus, jqXHR) {
			if (data) {
				var ssId = data.id;
				var type = data.type;
				
				if (type == 'ADVERSE_EVENTS') {
					populateAdverseEventsSearchForm(data);
					adverseEventSearch();
				} else if (type == 'ROUTES') {
					populateRoutesSearchForm(data);
					routeSearch();
				} else if (type == 'DRUGS') {
					populateDrugsSearchForm(data);
					drugSearch();
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
 * Click handler for creating a new search from a saved search
 */
function newSavedSearch() {
	var ssId = $(this).parent('li').attr('ssId');
	
	// get saved search data from server
	$.ajax('/' + getContext() + '/rest/search', {
		type: 'get',
		data: {
			id: ssId,
		},
		success: function(data, textStatus, jqXHR) {
			if (data) {
				var ssId = data.id;
				var type = data.type;
				
				if (type == 'ADVERSE_EVENTS') {
					populateAdverseEventsSearchForm(data);
				} else if (type == 'ROUTES') {
					populateRoutesSearchForm(data);
				} else if (type == 'DRUGS') {
					populateDrugsSearchForm(data);
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
 * Click handler for deleting a saved search
 */
function deleteSavedSearch() {
	var $li = $(this).parent('li');
	var ssId = $li.attr('ssId');
	
	$('.confirmDeleteTrue').click(function() {

		// TODO make delete call to server
		// get saved search data from server
		$.ajax('/' + getContext() + '/rest/search/' + ssId, {
			type: 'delete',
			success: function(data, textStatus, jqXHR) {
				$li.remove();
				$('#confirmDelete').modal('hide');
			},
			error: function(jqXHR, textStatus, errorThrown) {
				// TODO add error handling
				console.log(errorThrown);
				$('#confirmDelete').modal('hide');
			}
		});
	});
	$('#confirmDelete').modal();
}

/**
 * Scrolls with animation to the top of the element with the given id
 * @param id Id of the element to scroll to
 */
function navigate(id) {
	$('html, body').animate({ scrollTop: $("#" + id).offset().top }, 150);
}

/**
 * Adds a saved search to the given list of saved searches
 * @param list List element to add saved search to
 * @param ssId Id of the saved search
 * @param name Name of the saved search
 * @param date Date and time the search was saved
 */
function addSavedSearch(list, ssId, name, date) {
	
	// create list item
	$li = $('<li>').attr('ssId', ssId).addClass('list-group-item');
	$li.appendTo(list);
	
	// format date
	var formattedDate = date.getMonth() + 1;
	formattedDate += '/';
	formattedDate += date.getDate();
	formattedDate += '/';
	formattedDate += date.getFullYear();
	
	// format time
	var period = 'am';
	var hours = date.getHours();
	if (hours === 0) {
		hours = 12;
	} else if (hours > 12) {
		period = 'pm';
		hours = hours - 12;
	}
	var minutes = date.getMinutes();
	if (minutes < 10) {
		minutes = '0' + minutes;
	}
	var formattedTime = hours + ':' + minutes + period;
	
	// add saved search name and date
	//6/23/2015 2:30pm
	
	$('<div>').html(name + ' <small>[' + formattedDate + ' ' + formattedTime + ']</small>').appendTo($li);
	
	// add buttons
	$('<button>').attr('type', 'button').addClass('btn btn-xs btn-success ssSearch').css('margin-right','4px').text('Search').appendTo($li);
	$('<button>').attr('type', 'button').addClass('btn btn-xs btn-warning ssNew').css('margin-right','4px').text('New').appendTo($li);
	$('<button>').attr('type', 'button').addClass('btn btn-xs btn-danger ssDelete').text('Delete').appendTo($li);
	
	$('.ssSearch').click(runSavedSearch);
	$('.ssNew').click(newSavedSearch);
	$('.ssDelete').click(deleteSavedSearch);
}

/**
 * Manages the loading mask
 * 
 * @param hide If true, hides the mask
 */
function loading(hide) {
	if (hide) {
		$('.searchLoading').modal('hide');
	} else {
		$('.searchLoading').modal();
	}
}

/**
 * Get the context given the different deployment paths.
 */
function getContext() {
	var context = location.pathname;
	if (context.indexOf('ads-webapp') > -1) {
		context = 'ads-webapp';
	} else {
		context = 'ads';
	}
	return context;
}