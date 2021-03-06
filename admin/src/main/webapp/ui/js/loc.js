/*******************Load Root Node ***********************************/
//Google Maps variables
var map;
var layer;
var kmlPath;
var myMarker;
var set = true;
var urlSuffix;
var c;
var mylocation = {
	'latitude': 28.61,
	'longitude': 77.23
};


//
$(function(){
	$("#menu_new").load("../ui/menu.html"); 
});

function addLocation(node)
{
    loc_hash[node.id] = node.name;
    if(node.children)
    {   
        for(var i=0; i<node.children.length; i++)
        {   
            addLocation(node.children[i]);
        }   
    }   
};

$ = $.noConflict();
$(document).ready(function(){
	//Create map
	var myLatlng = new google.maps.LatLng( mylocation.latitude, mylocation.longitude );
	var mapOptions = {
		zoom: 5,
	center: myLatlng,
	mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	c = map.getCenter();
	myMarker = new google.maps.Marker({
		position: c,
		draggable: true
	});
	myMarker.setMap(map);
	urlSuffix = (new Date).getTime().toString(); //Will be used for KML layer

	google.maps.event.addListener(myMarker, 'dragend', function(evt){
		$('#node_lat').val(evt.latLng.lat());
		$('#node_long').val(evt.latLng.lng());
	});
	google.maps.event.addListener(map, "center_changed", function() {
		c = map.getCenter();
		myMarker.setPosition(c);
		$('#node_lat').val(c.lat());
		$('#node_long').val(c.lng());
	});

	//map end

	var root_node,new_node,sel;
	window.hash = {};
	window.loc_hash = {};

	$.ajax({
		type: "GET",
		url:"/ajax/locationtype/get",
		contentType: "application/json; charset=utf-8",
		dataType: "JSON",
		success: function(data){
			addLocation(data);
		}
	});

	$("#save_btn").click(function() {		
		update_selected_node();		
	});

	$('#form1').submit(function(event){
		event.preventDefault();
		//grab all form data  
		//var formData = new FormData($('#form1'));
		var formData = new FormData(this);
		var selected_node =  $('#js_tree').jstree('get_selected');
		var  kml_url= '/ajax/location/'+selected_node[0]+'/upload';

		$.ajax({
			url: kml_url,
			type: 'POST',
			data: formData,
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function (returndata) {
				$('#'+selected_node).attr('boundaryFile',returndata);
				$('#kml_status').html("<p>KML File exists</p>");
				update_map(returndata);
			}
		});

		return false;
	});

	$.ajax({
		type: "GET",
		url:"/ajax/location/getroot",
		dataType: "JSON",
		success: function(data){
			//alert(JSON.stringify(data));
			var root_node = 
	{		'text'    	:data.name+" Type: Country",
		'id'  	 	:data.id,								   
		'li_attr':{'title':data.name,'loc_typeid':data.locationTypeId,'p_id':data.parentLocationId,'center_lat':data.latitude,'center_long':data.longitude,'boundaryFile':data.boundaryFile, 'UrlIden':data.urlIdentifier}  
	};

	$('#js_tree').jstree({ 'core' : {
		"check_callback" : true,
		'data' :root_node ,
		"plugins" : [ "types","contextmenu"]}}).bind("select_node.jstree", function (e, data) {  

		var parent = $('#js_tree').jstree('get_selected');

		$('#node_title').val($('#'+parent).attr('title'));
		$('#node_lat').val($('#'+parent).attr('center_lat'));
		$('#node_long').val($('#'+parent).attr('center_long'));
		$('#node_urliden').val($('#'+parent).attr('UrlIden'));
		if($('#'+parent).attr('boundaryFile') != 'null'){
			$('#kml_status').html("<p>KML File exists</p>");	
		} else { $('#kml_status').html("<p>KML File does not exist</p>");	}

		/****************************Load KML Layer on Google Map*****************************/

		var kml_path = $('#'+parent).attr('boundaryFile');
		update_map(kml_path);

		/*************************End KML Load****************************************************/



		var selected_loc_typeid =  $('#'+parent).attr('loc_typeid');

		$.ajax({
			type: "GET",
			url:"/ajax/locationtype/getchild/"+selected_loc_typeid,
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				var btn_html = "";
				for(var i=0;i < data.length; i++){
					btn_html += "<a id='node_add_btn"+i+"' onclick='return runMyFunction(event);' value='"+data[i].id+"' class='btn btn-primary blue btn_round' href='#add-node' data-toggle='modal'>Add "+data[i].name+"</a><br>";
				}
				$('#add_child_btn').html(btn_html);   
			}
		});

		$.ajax({
			type: "GET",
			url:"/ajax/location/"+$('#'+parent).attr('id')+"/kmlfiles",
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				$("#files tbody").html("");
				for(var i=0;i < data.length; i++){
					$("#files tbody").append("<tr><td>"+data[i].originalFileName+"</td><td><a class='btn btn-primary blue kml_inp_btn' href='#' onclick='update_map(\""+data[i].fileNameAndPath+"\");'>OnMap</a></td><td><a class='btn btn-primary blue kml_inp_btn' href='#' onclick='set_current(\""+data[i].fileNameAndPath+"\");'>SetCurrent</a></td></tr>");
				}
			}
		});

		if(!($('#'+parent).hasClass('jstree-open')) && !window.hash.hasOwnProperty('fake_node'+$('#'+parent).attr('id'))){
			new_node = {'text':'fake','id':'fake_node'+$('#'+parent).attr('id')};
			$('#js_tree').jstree(true).create_node(parent, new_node);
			window.hash['fake_node'+$('#'+parent).attr('id')] = 1;

		}
		}).bind("open_node.jstree",function(e,data){
			var parent = data.node.id;
			$("#js_tree").jstree("delete_node", $('#fake_node'+$('#'+parent).attr('id')));
			if($('#'+parent).closest("li").children("ul").length ==0){
				var selected_node = data.node.id;
				var new_node;
				var sel;
				$.ajax({
					type: "GET",
					url:"/ajax/location/getchild/"+selected_node,
					contentType: "application/json; charset=utf-8",
					dataType: "JSON",
					success: function(data){
						if(data.length ==0){alert("No Children found");}		   			   
						for(var i=0; i< data.length;i++){
							new_node = {'text':data[i].name+" Type :"+loc_hash[data[i].locationTypeId],'id':data[i].id,'li_attr':{'title':data[i].name,'loc_typeid':data[i].locationTypeId,'p_id':data[i].parentLocationId,'center_lat':data[i].latitude,'center_long':data[i].longitude,'boundaryFile':data[i].boundaryFile, 'UrlIden':data[i].urlIdentifier}};
							sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);

						}
						;

					}
				});


				$("#js_tree").jstree("open_node", selected_node);
			}

		});    


		$('#js_tree').jstree("select_node",root_node.id);	

		}
		});

	});

	/*******************Update the map***********************************/

	function update_map(kml_path){

		//var map_html = '<iframe width="625" height="550" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="http://maps.google.com/maps?q='+encodeURIComponent(kml_path)+'&output=embed"></iframe><br /><small><a href="http://maps.google.com/maps?q='+encodeURIComponent(kml_path)+'" style="color:#0000FF;text-align:left">View Larger Map</a></small>';

		//$('#map-canvas').html(map_html);
		if (typeof layer !== 'undefined')
		{
			layer.setMap(null);
		}
		layer = new google.maps.KmlLayer(kml_path + '?' + urlSuffix );
		layer.setMap(map);
		//c = map.getCenter();
		//myMarker.setPosition(c);

	}

	/*******************Add a new Child Node***********************************/


	function add_child_node(){

		var selected_node =  $('#js_tree').jstree('get_selected');
		var new_node_id;

		var post_data = {
			"name":$("#new_node_title").val(),
			"parentLocationId":selected_node[0],
			"locationTypeId":$("#new_node_loc_typeid").val(), 
			"latitude":$("#new_node_lat").val(),
			"longitude":$("#new_node_long").val(),
		};
		$.ajax({
			type: "POST",
			url:"/ajax/location/save",
			data: JSON.stringify(post_data),
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				//alert(JSON.stringify(data, null, 4));

				var new_node = {'text':data.name+" Type :"+data.locationTypeId,
					'id':data.id,
			'li_attr':{'title':data.name,'loc_typeid':data.locationTypeId,'p_id':data.parentLocationId,'center_lat':data.latitude,'center_long':data.longitude,'boundaryFile':data.boundaryFile, 'UrlIden':data.urlIdentifier}};
		var error = data.message.length > 0;
		if(error) {
			alert("Error in node creation: " + data.message);
		}
		else {
			//alert(JSON.stringify(new_node));
			var sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
		}
			}
		});



	}

	/*******************Update Selected node***********************************/

	function update_selected_node(){

		var selected_node =  $('#js_tree').jstree('get_selected');

		var post_data = {
			"id":selected_node[0],
			"name":$("#node_title").val(),
			"locationTypeId":$('#'+selected_node[0]).attr('loc_typeid'),  
			"latitude":$('#node_lat').val(),
			"longitude":$('#node_long').val()
		};

		if($('#'+selected_node[0]).attr('p_id') != 'null'){
			post_data.parentLocationId = $('#'+selected_node[0]).attr('p_id');
		}
		$.ajax({
			type: "POST",
			url:"/ajax/location/save",
			data: JSON.stringify(post_data),
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				//alert(JSON.stringify(data, null, 4));
				//$('#js_tree').jstree('set_text',data.id, data.name+data.locationTypeId);
				$('#js_tree').jstree('set_text',data.id, data.name);
				var error = data.message.length > 0;
				if(error) {
					alert("Error in node creation: " + data.message);
				}
			}
		});
	}

	function runMyFunction(event){
		var target = event.target || event.srcElement;
		$('#new_node_loc_typeid').val($('#'+target.id).attr('value'));
		return true;
		console.log($('#new_node_loc_typeid').attr('value'));
	}

	function set_current(filepath){
		console.log("Set file "+filepath+" as current by API call");
	}
