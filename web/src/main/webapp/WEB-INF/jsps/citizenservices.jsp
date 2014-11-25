<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="include.jsp" />
<link rel="stylesheet" href="${staticHost}/css/dashboard.css">
<link rel="stylesheet" href="${staticHost}/css/citizenservices.css">
<script type="text/javascript" src="js/jquery.tocify.js"></script>
</head>
<body>
	<div class="outerwrapper">
		<header>
			<jsp:include page="header.jsp" />
		</header>
		<div class="row">
			<div id="citizenservices">
					
					<c:forEach items="${rootCategories}" var="oneCategory">
						<c:if test="${oneCategory.name == 'LawOrder'}">
						<c:set var="oneCategory.name" value="LawOrder"/>
						</c:if>
						<c:if test="${oneCategory.name == 'SewerageSanitation'}">
						<c:set var="oneCategory.name" value="SewerageSanitation"/>
						</c:if>
						<div id="${oneCategory.name}_col" class="box_style">
						<h3 class="frame_header">${oneCategory.name}<span class="glyphicon glyphicon-circle-arrow-up right_float"></span></h3>
						<div id="${oneCategory.name}_col_innnerdiv" class="box_style_innderdiv">

						<a href="#!" id="${oneCategory.name}_infographic_div">
						<div>
						<span class="frame_info_first_span">
						<span class="glyphicon glyphicon-new-window glyphicon_margin"></span>
						<span class="ahref_text_margin">Infographic View</span>
						</span>
						</div>
						</a>

						<div class="img_video_box">
						<div style="position: relative;">
						<img class="video_img" src=""></img>
						<a href="#!" class="${oneCategory.name}_video_link"><span class="glyphicon glyphicon-expand play_video_image"></span></a>
						</div>
						</div>

						<a href="#!" id="${oneCategory.name}_content_div">
						<div>						
						<span class="frame_info_second_span">
						<span class="glyphicon glyphicon-align-left glyphicon_margin"></span>
						<span class="ahref_text_margin">Related Content</span>
						</span>
						</div>
						</a>
						
						<p class="frame_content">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Delectus veritatis repudiandae velit qui necessitatibus, ipsa, dicta quasi saepe illum facilis, ut quas. Quidem excepturi, nobis blanditiis ipsum libero!</p>

						</div>
						
						</div>
					</c:forEach>

			</div>
		</div>
	</div>

    <div id="modal-background" style="display: none;">
	<div class="addthis_toolbox addthis_32x32_style addthis_floating_style" addthis:title="" addthis:description="" addthis:url="" style="border-radius: 0px; border-top-right-radius: 10px; border-bottom-right-radius: 10px; padding-bottom: 0px; width: 45px;">
		<a class="addthis_button_facebook"></a>
		<a class="addthis_button_twitter"></a>
		<a class="addthis_button_google_plusone_share" g:plusone:count="false"></a>
		<a class="addthis_button_gmail"></a>
		<a class="addthis_button_compact"></a>
	</div>	
	<iframe class="video_play" src="" frameborder="0" allowfullscreen></iframe>
	<a href="#0" class="close-btn">Close</a>
    </div>
	
    <div id="modal-background-infographic" style="display: none;">
	<div class="addthis_toolbox addthis_32x32_style addthis_floating_style" addthis:title="" addthis:description="" addthis:url="" style="border-radius: 0px; border-top-right-radius: 10px; border-bottom-right-radius: 10px; padding-bottom: 0px; width: 45px;">
		<a class="addthis_button_facebook"></a>
		<a class="addthis_button_twitter"></a>
		<a class="addthis_button_google_plusone_share" g:plusone:count="false"></a>
		<a class="addthis_button_gmail"></a>
		<a class="addthis_button_compact"></a>
	</div>	
	<img class="infographic_image" src=""></img>
	<a href="#0" class="close-btn">Close</a>
    </div>

    <div id="modal-background-content" style="display: none; padding-right: 30px; padding-left: 30px;">
<div id="modal_content"></div>
	<div class="addthis_toolbox addthis_32x32_style addthis_floating_style" addthis:title="" addthis:description="" addthis:url="" style="border-radius: 0px; border-top-right-radius: 10px; border-bottom-right-radius: 10px; padding-bottom: 0px; width: 45px; right: 0px;">
		<a class="addthis_button_facebook"></a>
		<a class="addthis_button_twitter"></a>
		<a class="addthis_button_google_plusone_share" g:plusone:count="false"></a>
		<a class="addthis_button_gmail"></a>
		<a class="addthis_button_compact"></a>
	</div>	
<h1><span>Lorem Ipsum</span></h1>
<h4>"Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit..."</h4>
<h5>"There is no one who loves pain itself, who seeks after it and wants to have it, simply because it is pain..."</h5>
<div style="float:right;margin-left:6px;margin-bottom:6px;">
<p>
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin porttitor sapien eget elit interdum viverra. In et leo eu magna egestas tempus. Sed accumsan accumsan lacus, quis vehicula velit rutrum vel. Quisque lacinia convallis magna, ac aliquam ipsum pellentesque sit amet. Suspendisse finibus nibh non urna congue sagittis. Nunc scelerisque sapien at eros efficitur, at dapibus neque ultricies. Donec suscipit posuere felis eu feugiat. Integer commodo risus erat, vel maximus dolor tempor nec. Sed sodales lectus suscipit metus auctor, quis aliquam nisl tristique.
</p>
<p>
Vestibulum quis tortor sem. Aliquam interdum ante ac iaculis fringilla. Duis varius vitae orci iaculis sodales. Mauris elementum ullamcorper sapien sed feugiat. Curabitur dapibus non risus ut laoreet. Donec molestie mattis ullamcorper. Vestibulum eu ante eget lorem elementum tempus. Aenean vitae ante nec odio lobortis egestas at nec lorem. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas sit amet felis ac diam aliquet rhoncus. Morbi viverra mi at maximus gravida.
</p>
<p>
Donec quis quam volutpat, varius urna quis, facilisis ligula. Curabitur vel dictum arcu, sed rutrum odio. Praesent tempor pellentesque elit, quis pretium nibh aliquet quis. Proin rutrum urna vel pulvinar iaculis. Suspendisse sed sem sed velit imperdiet sollicitudin at vitae libero. In sodales, urna in sodales porta, justo dolor ullamcorper urna, hendrerit sollicitudin nisl leo in turpis. Fusce ac urna euismod est cursus consectetur. Mauris cursus ultrices convallis. Mauris nunc dui, congue id fermentum quis, malesuada ut tellus. Phasellus elementum dapibus venenatis. Proin sodales neque turpis, accumsan sagittis felis finibus ac. Vestibulum at urna tempor, convallis velit vitae, egestas risus.
</p>
<p>
Donec convallis est eu lorem vestibulum, a suscipit libero pharetra. Mauris iaculis mi turpis, non efficitur magna dictum sit amet. Mauris maximus libero eu ornare interdum. Ut volutpat, lectus vitae viverra congue, ligula diam posuere velit, nec malesuada turpis turpis eget lectus. Nulla ultricies luctus magna at auctor. Phasellus libero dolor, interdum eget tincidunt vel, euismod a orci. Nam sit amet ipsum elit. Curabitur vehicula at urna ac placerat. Mauris mattis dolor ut lacus cursus suscipit id quis augue. Proin vitae lorem felis. Nunc vitae est lacus.
</p>
<p>
Phasellus leo urna, gravida vitae justo eleifend, vulputate porttitor erat. Sed euismod ligula at tortor pellentesque rhoncus. Phasellus sed lacus blandit, aliquet tortor eu, accumsan sapien. Sed accumsan nulla augue, nec pulvinar neque sodales sit amet. Nullam a tincidunt nibh. Duis in tincidunt diam. Cras interdum imperdiet nulla, vitae rhoncus massa rhoncus ac. Aliquam ut lorem euismod magna dapibus laoreet id in sapien. Cras in orci ac nulla accumsan faucibus. Quisque et euismod lorem, convallis viverra dui. Mauris enim tellus, interdum a ex vel, porttitor aliquam metus. In hac habitasse platea dictumst. Nullam eu mollis erat. In et tortor metus.
</p>
</div>
	<a href="#0" class="close-btn">Close</a>
    </div>

    <div id="toc">
          </div><!--/.well -->

<script type="text/javascript">

/* For Users Pics Modal View */
jQuery(document).ready(function($){

$("#Water_col").fadeIn(1).animate({height:"auto"}, 1000, "linear");
$("#LawOrder").delay(500).fadeIn(1).animate({height:"auto"}, 1000, "linear");
$("#Electricity_col").delay(1000).fadeIn(1).animate({height:"auto"}, 1000, "linear");
$("#Roads_col").delay(1500).fadeIn(1).animate({height:"auto"}, 1000, "linear");
$("#SewerageSanitation").delay(2000).fadeIn(1).animate({height:"auto"}, 1000, "linear");
$("#Transportation_col").delay(2500).fadeIn(1).animate({height:"auto"}, 1000, "linear");
$('.play_video_image').delay(2500).fadeIn(500);

	//close the video modal page
	$('#modal-background .close-btn').on('click', function(){
		$('#modal-background').fadeOut(1);
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
		$('#modal-background .video_play').attr("src", "");

	});
	$(document).keyup(function(event){
		//check if user has pressed 'Esc'
    	if(event.which=='27'){
   		$('#modal-background').fadeOut(1);	
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
		$('#modal-background .video_play').attr("src", "");
	    }
    	if(event.which=='8'){
   		$('#modal-background').fadeOut(1);	
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
		$('#modal-background .video_play').attr("src", "");
	    }
    });    

	//close the infographic page
	$('#modal-background-infographic .close-btn').on('click', function(){
		$('#modal-background-infographic').fadeOut(1);
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
		$('#modal-background-infographic .infographic_image').attr("src", "");
	});
	$(document).keyup(function(event){
		//check if user has pressed 'Esc'
    	if(event.which=='27'){
   		$('#modal-background-infographic').fadeOut(1);	
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
		$('#modal-background-infographic .infographic_image').attr("src", "");
	    }
    	if(event.which=='8'){
   		$('#modal-background-infographic').fadeOut(1);	
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
		$('#modal-background-infographic .infographic_image').attr("src", "");
	    }
    });    

	//close the modal content page
	$('#modal-background-content .close-btn').on('click', function(){
		$('#modal-background-content').fadeOut(1);
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
	});
	$(document).keyup(function(event){
		//check if user has pressed 'Esc'
    	if(event.which=='27'){
   		$('#modal-background-content').fadeOut(1);	
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
	    }
    	if(event.which=='8'){
   		$('#modal-background-content').fadeOut(1);	
		$('.outerwrapper').fadeIn(1);
		$("#toc").fadeIn(1);
	    }
    });    
	
});	

window.onload = function() {

var headTags = [
   '<meta name="title"        content="" />',
   '<meta itemprop="name"     content="" />',
   '<meta property="og:title" content="" />',
   '<meta itemprop="url"    content=""/>',
   '<meta property="og:url" content=""/>',
   '<link rel="canonical"   href=""/>',
   '<meta itemprop="image"    content="">',
   '<meta property="og:image" content="">',
   '<meta property="og:image:width" content="">',
   '<meta property="og:image:height" content="">',
];

$.each(headTags, function (index, tag) {
    $('head').append(tag);
});

if ( $( "#citizenservices" ).has( "Water_col" ) ) {
Water_img_src = "http://www.youtube.com/embed/R1MqsddGeUw"; //Waterurl
Water_infograph_img_src = "http://www.creativebloq.com/sites/creativebloq.com/files/images/2013/04/controllersfull2.jpg";
Water_content="http://www.lipsum.com/"

var arr = Water_img_src.split('/');
var url = "http://img.youtube.com/vi/" + arr[4] + "/hqdefault.jpg";
$( '#Water_col .video_img' ).attr("src", url);
$('.Water_video_link').on('click', function(){
	$('#modal-background .video_play').attr("src", Water_img_src);
	$('#modal-background .addthis_32x32_style').attr('addthis:url', Water_img_src);
    $("meta[itemprop='url']").attr("content", Water_img_src);
    $("meta[property='og:url']").attr("content", Water_img_src);
    $("link[rel='canonical']").attr("href", Water_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Water Category");
    $("meta[name='title']").attr("content", "Water Category");
    $("meta[itemprop='name']").attr("content", "Water Category");
    $("meta[property='og:title']").attr("content", "Water Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#Water_infographic_div').on('click', function(){
	$('#modal-background-infographic .infographic_image').attr("src", Water_infograph_img_src);
	$('#modal-background-infographic .addthis_32x32_style').attr('addthis:url', Water_infograph_img_src);
    $("meta[itemprop='url']").attr("content", Water_infograph_img_src);
    $("meta[property='og:url']").attr("content", Water_infograph_img_src);
    $("link[rel='canonical']").attr("href", Water_infograph_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Water Category");
    $("meta[name='title']").attr("content", "Water Category");
    $("meta[itemprop='name']").attr("content", "Water Category");
    $("meta[property='og:title']").attr("content", "Water Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-infographic').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#Water_content_div').on('click', function(){
	$('#modal-background-content .addthis_32x32_style').attr('addthis:url', Water_content);
    $("meta[itemprop='url']").attr("content", Water_content);
    $("meta[property='og:url']").attr("content", Water_content);
    $("link[rel='canonical']").attr("href", Water_content);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Water Category");
    $("meta[name='title']").attr("content", "Water Category");
    $("meta[itemprop='name']").attr("content", "Water Category");
    $("meta[property='og:title']").attr("content", "Water Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-content').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
}

if ( $( "#citizenservices" ).has( "LawOrder_col" ) ) {
law_ord_img_src = "http://www.youtube.com/embed/rapvMuB02e0"; //Waterurl
law_ord_infograph_img_src = "http://media.creativebloq.futurecdn.net/sites/creativebloq.com/files/images/2013/10/productive.jpg";
law_ord_content="http://www.lipsum.com/"
var arr = law_ord_img_src.split('/');
var url = "http://img.youtube.com/vi/" + arr[4] + "/hqdefault.jpg";
$( '#LawOrder_col .video_img' ).attr("src", url);
$('.LawOrder_video_link').on('click', function(){
	$('#modal-background .video_play').attr("src", law_ord_img_src);
	$('#modal-background .addthis_32x32_style').attr('addthis:url', law_ord_img_src);
    $("meta[itemprop='url']").attr("content", law_ord_img_src);
    $("meta[property='og:url']").attr("content", law_ord_img_src);
    $("link[rel='canonical']").attr("href", law_ord_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "LawOrder Category");
    $("meta[name='title']").attr("content", "LawOrder Category");
    $("meta[itemprop='name']").attr("content", "LawOrder Category");
    $("meta[property='og:title']").attr("content", "LawOrder Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#LawOrder_infographic_div').on('click', function(){
	$('#modal-background-infographic .infographic_image').attr("src", law_ord_infograph_img_src);
	$('#modal-background-infographic .addthis_32x32_style').attr('addthis:url', law_ord_infograph_img_src);
    $("meta[itemprop='url']").attr("content", law_ord_infograph_img_src);
    $("meta[property='og:url']").attr("content", law_ord_infograph_img_src);
    $("link[rel='canonical']").attr("href", law_ord_infograph_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "LawOrder Category");
    $("meta[name='title']").attr("content", "LawOrder Category");
    $("meta[itemprop='name']").attr("content", "LawOrder Category");
    $("meta[property='og:title']").attr("content", "LawOrder Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-infographic').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#LawOrder_content_div').on('click', function(){
	$('#modal-background-content .addthis_32x32_style').attr('addthis:url', law_ord_content);
    $("meta[itemprop='url']").attr("content", law_ord_content);
    $("meta[property='og:url']").attr("content", law_ord_content);
    $("link[rel='canonical']").attr("href", law_ord_content);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "LawOrder Category");
    $("meta[name='title']").attr("content", "LawOrder Category");
    $("meta[itemprop='name']").attr("content", "LawOrder Category");
    $("meta[property='og:title']").attr("content", "LawOrder Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-content').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
}

if ( $( "#citizenservices" ).has( "Electricity_col" ) ) {
Electricity_img_src = "http://www.youtube.com/embed/hjAP8Fy5WhE"; //Waterurl
Electricity_infograph_img_src = "http://th07.deviantart.net/fs50/PRE/i/2009/309/8/3/Panda_Infographic_by_Lish_55.jpg";
Electricity_content="http://www.lipsum.com/"
var arr = Electricity_img_src.split('/');
var url = "http://img.youtube.com/vi/" + arr[4] + "/hqdefault.jpg";
$( '#Electricity_col .video_img' ).attr("src", url);
$('.Electricity_video_link').on('click', function(){
	$('#modal-background .video_play').attr("src", Electricity_img_src);
	$('#modal-background .addthis_32x32_style').attr('addthis:url', Electricity_img_src);
    $("meta[itemprop='url']").attr("content", Electricity_img_src);
    $("meta[property='og:url']").attr("content", Electricity_img_src);
    $("link[rel='canonical']").attr("href", Electricity_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Electricity Category");
    $("meta[name='title']").attr("content", "Electricity Category");
    $("meta[itemprop='name']").attr("content", "Electricity Category");
    $("meta[property='og:title']").attr("content", "Electricity Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#Electricity_infographic_div').on('click', function(){
	$('#modal-background-infographic .infographic_image').attr("src", Electricity_infograph_img_src);
	$('#modal-background-infographic .addthis_32x32_style').attr('addthis:url', Electricity_infograph_img_src);
    $("meta[itemprop='url']").attr("content", Electricity_infograph_img_src);
    $("meta[property='og:url']").attr("content", Electricity_infograph_img_src);
    $("link[rel='canonical']").attr("href", Electricity_infograph_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Electricity Category");
    $("meta[name='title']").attr("content", "Electricity Category");
    $("meta[itemprop='name']").attr("content", "Electricity Category");
    $("meta[property='og:title']").attr("content", "Electricity Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-infographic').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#Electricity_content_div').on('click', function(){
	$('#modal-background-content .addthis_32x32_style').attr('addthis:url', Electricity_content);
    $("meta[itemprop='url']").attr("content", Electricity_content);
    $("meta[property='og:url']").attr("content", Electricity_content);
    $("link[rel='canonical']").attr("href", Electricity_content);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Electricity Category");
    $("meta[name='title']").attr("content", "Electricity Category");
    $("meta[itemprop='name']").attr("content", "Electricity Category");
    $("meta[property='og:title']").attr("content", "Electricity Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-content').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
}

if ( $( "#citizenservices" ).has( "Roads_col" ) ) {
Roads_img_src = "http://www.youtube.com/embed/R1MqsddGeUw"; //Roads_url
Roads_infograph_img_src = "http://th05.deviantart.net/fs70/PRE/i/2010/287/5/9/typographic_elephant_by_lish_55-d30qrbr.jpg";
Roads_content="http://www.lipsum.com/"
var arr = Roads_img_src.split('/');
var url = "http://img.youtube.com/vi/" + arr[4] + "/hqdefault.jpg";
$( '#Roads_col .video_img' ).attr("src", url);
$('.Roads_video_link').on('click', function(){
	$('#modal-background .video_play').attr("src", Roads_img_src);
	$('#modal-background .addthis_32x32_style').attr('addthis:url', Roads_img_src);
    $("meta[itemprop='url']").attr("content", Roads_img_src);
    $("meta[property='og:url']").attr("content", Roads_img_src);
    $("link[rel='canonical']").attr("href", Roads_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Roads Category");
    $("meta[name='title']").attr("content", "Roads Category");
    $("meta[itemprop='name']").attr("content", "Roads Category");
    $("meta[property='og:title']").attr("content", "Roads Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#Roads_infographic_div').on('click', function(){
	$('#modal-background-infographic .infographic_image').attr("src", Roads_infograph_img_src);
	$('#modal-background-infographic .addthis_32x32_style').attr('addthis:url', Roads_infograph_img_src);
    $("meta[itemprop='url']").attr("content", Roads_infograph_img_src);
    $("meta[property='og:url']").attr("content", Roads_infograph_img_src);
    $("link[rel='canonical']").attr("href", Roads_infograph_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Roads Category");
    $("meta[name='title']").attr("content", "Roads Category");
    $("meta[itemprop='name']").attr("content", "Roads Category");
    $("meta[property='og:title']").attr("content", "Roads Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-infographic').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#Roads_content_div').on('click', function(){
	$('#modal-background-content .addthis_32x32_style').attr('addthis:url', Roads_content);
    $("meta[itemprop='url']").attr("content", Roads_content);
    $("meta[property='og:url']").attr("content", Roads_content);
    $("link[rel='canonical']").attr("href", Roads_content);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Roads Category");
    $("meta[name='title']").attr("content", "Roads Category");
    $("meta[itemprop='name']").attr("content", "Roads Category");
    $("meta[property='og:title']").attr("content", "Roads Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-content').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
}

if ( $( "#citizenservices" ).has( "SewerageSanitation_col" ) ) {
sew_san_img_src = "http://www.youtube.com/embed/X63ZVyjd4dY"; //sew_san_url
sew_san_infograph_img_src = "https://farm3.staticflickr.com/2750/4314987544_ca47fb5b72_z.jpg";
sew_san_content="http://www.lipsum.com/"
var arr = sew_san_img_src.split('/');
var url = "http://img.youtube.com/vi/" + arr[4] + "/hqdefault.jpg";
$( '#SewerageSanitation_col .video_img' ).attr("src", url);
$('.SewerageSanitation_video_link').on('click', function(){
	$('#modal-background .video_play').attr("src", sew_san_img_src);
	$('#modal-background .addthis_32x32_style').attr('addthis:url', sew_san_img_src);
    $("meta[itemprop='url']").attr("content", sew_san_img_src);
    $("meta[property='og:url']").attr("content", sew_san_img_src);
    $("link[rel='canonical']").attr("href", sew_san_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Sewage & Sanitation Category");
    $("meta[name='title']").attr("content", "Sewage & Sanitation Category");
    $("meta[itemprop='name']").attr("content", "Sewage & Sanitation Category");
    $("meta[property='og:title']").attr("content", "Sewage & Sanitation Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#SewerageSanitation_infographic_div').on('click', function(){
	$('#modal-background-infographic .infographic_image').attr("src", sew_san_infograph_img_src);
	$('#modal-background-infographic .addthis_32x32_style').attr('addthis:url', sew_san_infograph_img_src);
    $("meta[itemprop='url']").attr("content", sew_san_infograph_img_src);
    $("meta[property='og:url']").attr("content", sew_san_infograph_img_src);
    $("link[rel='canonical']").attr("href", sew_san_infograph_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Sewage & Sanitation Category");
    $("meta[name='title']").attr("content", "Sewage & Sanitation Category");
    $("meta[itemprop='name']").attr("content", "Sewage & Sanitation Category");
    $("meta[property='og:title']").attr("content", "Sewage & Sanitation Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-infographic').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#SewerageSanitation_content_div').on('click', function(){
	$('#modal-background-content .addthis_32x32_style').attr('addthis:url', sew_san_content);
    $("meta[itemprop='url']").attr("content", sew_san_content);
    $("meta[property='og:url']").attr("content", sew_san_content);
    $("link[rel='canonical']").attr("href", sew_san_content);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Sewage & Sanitation Category");
    $("meta[name='title']").attr("content", "Sewage & Sanitation Category");
    $("meta[itemprop='name']").attr("content", "Sewage & Sanitation Category");
    $("meta[property='og:title']").attr("content", "Sewage & Sanitation Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-content').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
}

if ( $( "#citizenservices" ).has( "Transportation_col" ) ) {
Transportation_img_src = "http://www.youtube.com/embed/rapvMuB02e0"; //Transportation_url
Transportation_infograph_img_src = "http://media.creativebloq.futurecdn.net/sites/creativebloq.com/files/images/2014/04/font-infography.jpg";
Transportation_content="http://www.lipsum.com/"
var arr = Transportation_img_src.split('/');
var url = "http://img.youtube.com/vi/" + arr[4] + "/hqdefault.jpg";
$( '#Transportation_col .video_img' ).attr("src", url);
$('.Transportation_video_link').on('click', function(){
	$('#modal-background .video_play').attr("src", Transportation_img_src);
	$('#modal-background .addthis_32x32_style').attr('addthis:url', Transportation_img_src);
    $("meta[itemprop='url']").attr("content", Transportation_img_src);
    $("meta[property='og:url']").attr("content", Transportation_img_src);
    $("link[rel='canonical']").attr("href", Transportation_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Transportation Category");
    $("meta[name='title']").attr("content", "Transportation Category");
    $("meta[itemprop='name']").attr("content", "Transportation Category");
    $("meta[property='og:title']").attr("content", "Transportation Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#Transportation_infographic_div').on('click', function(){
	$('#modal-background-infographic .infographic_image').attr("src", Transportation_infograph_img_src);
	$('#modal-background-infographic .addthis_32x32_style').attr('addthis:url', Transportation_infograph_img_src);
    $("meta[itemprop='url']").attr("content", Transportation_infograph_img_src);
    $("meta[property='og:url']").attr("content", Transportation_infograph_img_src);
    $("link[rel='canonical']").attr("href", Transportation_infograph_img_src);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Transportation Category");
    $("meta[name='title']").attr("content", "Transportation Category");
    $("meta[itemprop='name']").attr("content", "Transportation Category");
    $("meta[property='og:title']").attr("content", "Transportation Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-infographic').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
$('#Transportation_content_div').on('click', function(){
	$('#modal-background-content .addthis_32x32_style').attr('addthis:url', Transportation_content);
    $("meta[itemprop='url']").attr("content", Transportation_content);
    $("meta[property='og:url']").attr("content", Transportation_content);
    $("link[rel='canonical']").attr("href", Transportation_content);
    $(".updated_sharing .addthis_toolbox").attr('addthis:title', "Transportation Category");
    $("meta[name='title']").attr("content", "Transportation Category");
    $("meta[itemprop='name']").attr("content", "Transportation Category");
    $("meta[property='og:title']").attr("content", "Transportation Category");
    addthis.toolbox(".addthis_toolbox");
	$('#modal-background-content').fadeIn(1);
	$('.outerwrapper').fadeOut(1);
	$("#toc").fadeOut(1);
});
} 
}
	
$( "#Water_col .frame_header" ).click( function () {
$( "#Water_col_innnerdiv" ).fadeToggle(500);
$( "#Water_col .frame_header span" ).toggleClass("glyphicon-fullscreen");
});

$( "#LawOrder_col .frame_header" ).click( function () {
$( "#LawOrder_col_innnerdiv" ).fadeToggle(500);
$( "#LawOrder_col .frame_header span" ).toggleClass("glyphicon-fullscreen");
});

$( "#Electricity_col .frame_header" ).click( function () {
$( "#Electricity_col_innnerdiv" ).fadeToggle(500);
$( "#Electricity_col .frame_header span" ).toggleClass("glyphicon-fullscreen");
});

$( "#Roads_col .frame_header" ).click( function () {
$( "#Roads_col_innnerdiv" ).fadeToggle(500);
$( "#Roads_col .frame_header span" ).toggleClass("glyphicon-fullscreen");
});

$( "#SewerageSanitation_col .frame_header" ).click( function () {
$( "#SewerageSanitation_col_innnerdiv" ).fadeToggle(500);
$( "#SewerageSanitation_col .frame_header span" ).toggleClass("glyphicon-fullscreen");
});

$( "#Transportation_col .frame_header" ).click( function () {
$( "#Transportation_col_innnerdiv" ).fadeToggle(500);
$( "#Transportation_col .frame_header span" ).toggleClass("glyphicon-fullscreen");
});

        $(function() {

            var toc = $("#toc").tocify({
              selectors: "h3"
            });

        });

$(window).scroll(function() {
   if($(window).scrollTop() + $(window).height() > $(document).height() - 10) {
	   $("#toc ul:nth-last-child(2) li").removeClass("active");
       $("#toc ul:nth-last-child(1) li").addClass("active");
   }
});

$(document).ready(function(){
$( "#toc ul:nth-child(1)" ).click( function () {
event.preventDefault();
$( "#Water_col_innnerdiv" ).show();
$( "#Water_col .frame_header span" ).removeClass("glyphicon-fullscreen");
});

$( "#toc ul:nth-child(2)" ).click( function () {
event.preventDefault();
$( "#LawOrder_col_innnerdiv" ).show();
$( "#LawOrder_col .frame_header span" ).removeClass("glyphicon-fullscreen");
});

$( "#toc ul:nth-child(3)" ).click( function () {
event.preventDefault();
$( "#Electricity_col_innnerdiv" ).show();
$( "#Electricity_col .frame_header span" ).removeClass("glyphicon-fullscreen");
});

$( "#toc ul:nth-child(4)" ).click( function () {
event.preventDefault();
$( "#Roads_col_innnerdiv" ).show();
$( "#Roads_col .frame_header span" ).removeClass("glyphicon-fullscreen");
});

$( "#toc ul:nth-child(5)" ).click( function () {
event.preventDefault();
$( "#SewerageSanitation_col_innnerdiv" ).show();
$( "#SewerageSanitation_col .frame_header span" ).removeClass("glyphicon-fullscreen");
});

$( "#toc ul:nth-child(6)" ).click( function () {
event.preventDefault();
$( "#Transportation_col_innnerdiv" ).show();
$( "#Transportation_col .frame_header span" ).removeClass("glyphicon-fullscreen");
});
});

</script>

<!-- Go to www.addthis.com/dashboard to customize your tools -->
<script type="text/javascript">var addthis_config = {"data_track_addressbar":false};</script>
<script type="text/javascript" src="//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-5461a4f33cabcdfe" async="async"></script>		  

<jsp:include page="footer.jsp" />
</body>
</html>
