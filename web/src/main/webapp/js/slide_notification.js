
$( window ).load(function() {
var ward = $.trim($("#ward_details").html());
if ( ward == '') {
slideNotice('Mark your location on the Map to select AC/PC and ward');
} else if ( ward == "My Ward") {
slideNotice('Welcome ' + ward );
}

});

function slideNotice (text) {
$('#top_slide_notification').html('<h4>' + text + '</h4><button type="button" id="notification_close_button" style="position: absolute; top: 15%; right: 2%;" class="close"><span>&times;</span></button>').delay(1500).slideDown(1500);
}

$(document).ready(function(){

$( "#notification_close_button" ).on( 'click', function () {
$('#top_slide_notification').hide();
});

});