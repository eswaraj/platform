
$( window ).load(function() {
var ward = $.trim($("#ward_details").html());
if ( ward == '') {
slideNotice('Do you want to mark your location on the Map ?');
} else if ( ward == "My Ward") {
slideNotice('Welcome ' + ward );
}
});

function slideNotice (text) {
$('#top_slide_notification').html('<h4>' + text + '<button type="button" id="notification_close_button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button></h4>').delay(1500).slideDown(1500);
}