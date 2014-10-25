$(window).load(function(){

$(".show-more a").each(function() {
    var $link = $(this);
    var $content = $link.parent().prev(".text-content");

    console.log($link);

    var visibleHeight = $content[0].clientHeight;
    var actualHide = $content[0].scrollHeight - 1;

    console.log(actualHide);
    console.log(visibleHeight);

    if (actualHide > visibleHeight) {
        $link.show();
    } else {
        $link.hide();
    }
});

$(".show-more a").on("click", function() {
    var $link = $(this);
    var $content = $link.parent().prev(".text-content");
    var linkText = $link.text();

    $content.toggleClass("short-text, full-text", 500);

    $link.text(getShowLinkText(linkText));

    return false;
});

function getShowLinkText(currentText) {
    var newText = '';

    if (currentText.toUpperCase() === "SHOW MORE") {
        newText = "Show less";
    } else {
        newText = "Show more";
    }

    return newText;
}
});//]]>  

$(document).ready(function(){

  $("#comments_status").click(function(){
  $("#comments_box").fadeIn(500);
  var x = $("#comments_status").offset().left;
  var y = $("#comments_status").offset().top;
  window.scrollTo(x,y-15);
	});
	
comments_counter = parseInt($( ".issue-info .whom a").text().match(/\d+/)[0]);

sub_comments_counter = 3;
  $("#comments_box").fadeIn(500);


$("#user_sub_com_input_button").click(function(){

sub_comments_counter = sub_comments_counter + 1;

var d = new Date();

var month = d.getMonth()+1;
var day = d.getDate();
var seconds = d.getSeconds();

var output = ' ' + seconds + 'secs ago on ' + d.getFullYear() + '/' +
    (month<10 ? '0' : '') + month + '/' +
    (day<10 ? '0' : '') + day + '  ' ;

var user_sub_com_input = $("#user_sub_com_input").val();
if ( user_sub_com_input != '') {
	$("#add_sub_comment").before('<div id="old_sub_comments_block"><a href="#" class="profile-pic-comments"><img src="images/profile-pic.jpg" alt=""></a><p class="comments_whom"><strong class="issue-id">Comment #' + sub_comments_counter + ' by</strong><a href="#" class="username">Somnath Nabajja</a><img src = "images/time.png" class="posttimestatus" alt=""><a href="#" class="location">' + output + '</a></p><div class="comments-info" ><p id="user_comment_store" class="desc elipsis">' + user_sub_com_input + '</p></div></div>');
}
$("#user_sub_com_input").val('');
	});


$("#collapse_comments_box").click(function(){
  $("#comments_box").hide();
});

 
});


$(document).ready(function(){
	$( ".reporters_profile_pic" ).each(function() {
				img = $(this).attr('src');
				$(this).mouseover(function(e) {
				$(this).after('<div id="divhoverText"><button type="button" id="notification_close_button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><a href="#"><img class="reporters_profile_pic" style="float: left; margin: 15px 10px 10px 20px; width: 60px; height: 60px;" src="' + img + '" alt=""></a><p style = "font-size: 14px; color: #929292; margin: 25px 0px 10px 0px;" >Somnath Nabajja<br /><span class="glyphicon glyphicon-map-marker"></span><span class="description_author_area">Kadubeesanahalli</span></div>');
				$('#divhoverText').hide(); 
				}).mousemove( function(e) {
				var top = $(".issue_reporters_box").offset().top;
				top = top - 80;
				var left = $(".issue_reporters_box").offset().left;
				$('#divhoverText').css('top', top).css('left', left).show().fadeIn(5000);
				}).mouseout( function() {
					if($('#divhoverText').is(':visible')) {
					     $('#divhoverText').on('mouseenter', function() {
					         $('#divhoverText').show();
					     }).on('mouseleave', function(){
					         $('#divhoverText').hide();
					     });

						$(".issue_reporters_box").on('mouseleave', function() {
						$('#divhoverText').hide();
						});
					}
				});

		});
});

/* For Users Description Modal View */
jQuery(document).ready(function($){
	//open the testimonials modal page
	$('.cd-see-all').on('click', function(){
		$('.cd-testimonials-all').addClass('is-visible');
	});

	//close the testimonials modal page
	$('.cd-testimonials-all .close-btn').on('click', function(){
		$('.cd-testimonials-all').removeClass('is-visible');
	});
	$(document).keyup(function(event){
		//check if user has pressed 'Esc'
    	if(event.which=='27'){
    		$('.cd-testimonials-all').removeClass('is-visible');	
	    }
    	if(event.which=='8'){
    		$('.cd-testimonials-all').removeClass('is-visible');	
	    }
    });
    
	//build the grid for the testimonials modal page
	$('.cd-testimonials-all-wrapper').children('ul').masonry({
  		itemSelector: '.cd-testimonials-item'
	});
});

/* For Users Pics Modal View */
jQuery(document).ready(function($){
	//open the testimonials-pics modal page
	$('.text_reporters_anchor').on('click', function(){
		$('.cd-testimonials-pics-all').addClass('is-visible');
	});

	//close the testimonials-pics modal page
	$('.cd-testimonials-pics-all .close-btn').on('click', function(){
		$('.cd-testimonials-pics-all').removeClass('is-visible');
	});
	$(document).keyup(function(event){
		//check if user has pressed 'Esc'
    	if(event.which=='27'){
    		$('.cd-testimonials-pics-all').removeClass('is-visible');	
	    }
    	if(event.which=='8'){
    		$('.cd-testimonials-pics-all').removeClass('is-visible');	
	    }
    });
    
	//build the grid for the testimonials-pics modal page
	$('.cd-testimonials-pics-all-wrapper').children('ul').masonry({
  		itemSelector: '.cd-testimonials-pics-item'
	});
});