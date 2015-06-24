
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
	
	// TODO get saved search data from server
	// TODO fill form fields with saved search criteria
	// TODO execute search
	
	// TODO call correct search function based on type
	//adverseEventSearch();
	//routeSavedSearch();
	//drugSavedSearch()
	
}

/**
 * Click handler for creating a new search from a saved search
 */
function newSavedSearch() {
	var ssId = $(this).parent('li').attr('ssId');

	// TODO get saved search data from server
	// TODO fill form fields with saved search criteria
}

/**
 * Click handler for deleting a saved search
 */
function deleteSavedSearch() {
	var $li = $(this).parent('li');
	var ssId = $li.attr('ssId');
	
	$('.confirmDeleteTrue').click(function() {

		// TODO make delete call to server
		
		// remove saved search from list
		$li.remove();
		
		$('#confirmDelete').modal('hide');
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
	
	// add saved search name and date
	$('<div>').html(name + ' <small>[' + date + ']</small>').appendTo($li);
	
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