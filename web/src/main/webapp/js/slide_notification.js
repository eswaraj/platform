
$( window ).load(function() {
var ward = $.trim($("#ward_details").html());
if ( ward == '') {
slideNotice('Mark your location on the Map to select AC/PC and ward');
} else if ( ward == "My Ward") {
slideNotice('Welcome ' + ward );
}

$( "#notification_close_button" ).on( 'click', function () {
$('#top_slide_notification').hide();
});

});

function slideNotice (text) {
$('#top_slide_notification').html('<h4>' + text + '<button type="button" id="notification_close_button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button></h4>').delay(1500).slideUp(1500);
}