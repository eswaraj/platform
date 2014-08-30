var jstree_json;
$(document).ready(function() {
	
	/*** Responsive Menu  ***/
	$(".responsive-menu ul li").click( function(){
	$("ul",this).slideToggle();
	});		

	/*** Responsive Menu  ***/
	$(".responsive-menu-dropdown").click( function(){
	$(".responsive-menu > ul").slideToggle();
	});
	/*** Responsive Menu  ***/
	$(".right-bar-btn-mobile").click( function(){
	$(".right-bar").slideToggle('slow');
	});


});
