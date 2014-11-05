
$( window ).load(function() {
var ward = $.trim($("#ward_details").html());
if ( ward == '') {
slideNotice('Mark your location on the Map to select AC/PC and ward');
} else if ( ward == "My Ward") {
slideNotice('Welcome ' + ward );
}

});

function slideNotice (text) {
$('#top_slide_notification').html('<h4>' + text + '</h4><button type="button" id="notification_close_button" style="position: absolute; top: 15%; right: 2%;" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>').delay(1500).slideDown(1500);
}