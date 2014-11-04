
$( window ).load(function() {
var ward = $.trim($("#ward_details").html());
if ( ward == '') {
slideNotice('Mark your location on the Map to select AC/PC and ward');
} else if ( ward == "My Ward") {
slideNotice('Welcome ' + ward );
}

});

function slideNotice (text) {
$('#top_slide_notification').html('<h4>' + text + '<button type="button" id="notification_close_button" class="close"><span>&times;</span></button></h4>').delay(1500).slideDown(1500);
}

$(document).ready(function(){

$( "#notification_close_button" ).on( 'click', function () {
$('#top_slide_notification').hide();
});

});