jQuery(document).ready(function() {
	$('.anchorlink').click(function(e){
		e.stopPropagation();
	});
	$( ".list-row" ).each( function () {
		$(this).on({
		mouseenter: function () {
			$( this ).find(".innerdiv-sharebtn" ).toggle('slide', {direction: "right"}, 500);
		},
		mouseleave: function () {
			$( this ).find(".innerdiv-sharebtn" ).hide();
		}
		});
	});
});

jQuery(document).ready(function($){

	$('#modal-background-subcategory-innerdiv .close-btn').on('click', function(){
		$('#modal-background-subcategory').fadeOut(1);
		$('.main_content_page').fadeTo( "slow", 1 );
	});

	$('#md-bg-services-plus-sys-level-innerdiv .close-btn').on('click', function(){
		$('#md-bg-services-plus-sys-level').fadeOut(1);
		$('.main_content_page').fadeTo( "slow", 1 );
	});

	$(document).keyup(function(event){
		//check if user has pressed 'Esc'
		if(event.which=='27'){
		$('#modal-background-subcategory').fadeOut(1);	
		$('#md-bg-services-plus-sys-level').fadeOut(1);
		$('.main_content_page').fadeTo( "slow", 1 );
		}
		if(event.which=='8'){
		$('#modal-background-subcategory').fadeOut(1);	
		$('#md-bg-services-plus-sys-level').fadeOut(1);
		$('.main_content_page').fadeTo( "slow", 1 );
		}
	});
});	

window.onload = function() {
	$('.advanced-filter-subcategory').on('click', function(){
		$('#modal-background-subcategory').fadeIn(1);
		$('.main_content_page').fadeTo( "slow", 0.33 );
		$("html, body").animate({ scrollTop: 0 }, "slow");
	});
	$('.advanced-filter-citzn-serv').on('click', function(){
		$('#md-bg-services-plus-sys-level').fadeIn(1);
		$('.main_content_page').fadeTo( "slow", 0.33 );
		$("html, body").animate({ scrollTop: 0 }, "slow");
	});
	$("#modal-background-subcategory").click(function(e) {
		if($(e.target).is('#modal-background-subcategory-innerdiv')){
			e.preventDefault();
			return;
		}
		$('.main_content_page').fadeTo( "slow", 1 );
		$('#modal-background-subcategory').fadeOut(1);	
	}); 
	$("#md-bg-services-plus-sys-level").click(function(e) {
		if($(e.target).is('#md-bg-services-plus-sys-level-innerdiv')){
			e.preventDefault();
			return;
		}
		$('.main_content_page').fadeTo( "slow", 1 );
		$('#md-bg-services-plus-sys-level').fadeOut(1);	
	}); 

	$( "#modal-background-subcategory-innerdiv a" ).each( function () {
		$(this).on('click', function() {
			$(this).toggleClass("modal_ahrefclick");
			});
	});
}										

	var citynames = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  prefetch: {
    url: 'http://www.json-generator.com/api/json/get/ceeSBtfmNu?indent=4',
    filter: function(list) {
      return $.map(list, function(cityname) {
        return { name: cityname }; });
    }
  }
});
citynames.initialize();

var cities = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('text'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  prefetch: 'http://www.json-generator.com/api/json/get/cmSkWToAHS?indent=4'
});
cities.initialize();

/**
 * Typeahead
 */
var elt = $('.example_typeahead > > input');
elt.tagsinput({
  typeaheadjs: {
    name: 'citynames',
    displayKey: 'name',
    valueKey: 'name',
    source: citynames.ttAdapter()
  }
});

/**
 * Objects as tags
 */
elt = $('.example_objects_as_tags > > input');
elt.tagsinput({
  itemValue: 'value',
  itemText: 'text',
  typeaheadjs: {
    name: 'cities',
    displayKey: 'text',
    source: cities.ttAdapter()
  }
});

elt.tagsinput('add', { "value": 1 , "text": "Water"   , "continent": "India"    });
elt.tagsinput('add', { "value": 5 , "text": "Roads"  , "continent": "India"   });



	var citynames_subcat = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  prefetch: {
    url: 'http://www.json-generator.com/api/json/get/bSGNSuCPNe?indent=4',
    filter: function(list) {
      return $.map(list, function(cityname_subcat) {
        return { name: cityname_subcat }; });
    }
  }
});
citynames_subcat.initialize();

var cities_subcat = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('text'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  prefetch: 'http://www.json-generator.com/api/json/get/cfcORgRTeG?indent=4'
});
cities_subcat.initialize();

/**
 * Typeahead
 */
var elt_subcat = $('.example_typeahead_subcat > > input');
elt_subcat.tagsinput({
  typeaheadjs: {
    name: 'citynames_subcat',
    displayKey: 'name',
    valueKey: 'name',
    source: citynames_subcat.ttAdapter()
  }
});

/**
 * Objects as tags
 */
elt_subcat = $('.example_objects_as_tags_subcat > > input');
elt_subcat.tagsinput({
  itemValue: 'value',
  itemText: 'text',
  typeaheadjs: {
    name: 'cities_subcat',
    displayKey: 'text',
    source: cities_subcat.ttAdapter()
  }
});

elt_subcat.tagsinput('add', { "value": 1 , "text": "No Munciplaity Water at homes"   , "id": "78261"    });
elt_subcat.tagsinput('add', { "value": 5 , "text": "Poor calliberation of water meters"  , "id": "78267"   });
