/**
 * Scrolls with animation to the top of the element with the given id
 * @param id Id of the element to scroll to
 */
function navigate(id) {
	$('html, body').animate({ scrollTop: $("#" + id).offset().top }, 1500);
}