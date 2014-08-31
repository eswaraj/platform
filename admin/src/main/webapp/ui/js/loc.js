/*******************Load Root Node ***********************************/
$(function(){
      $("#menu").load("../ui/sidebar_menu.html"); 
});

//var locationTypeSet;
function setValue(data){
    data = {root:data};
	window.locationTypeSet = data;
	//alert("Values Set one Time");
	
}

function getValue(){
    //alert(window.locationTypeSet);
	return window.locationTypeSet; 
		
}

function searchLocationTypeName(id){
	var list_data = getValue();
	if(list_data){
	var valList =list_data,res = JSON.search(valList, '//root[id="'+id+'"]');
    if (!res || res == '') {
                res = JSON.search(valList, '//children[id="'+id+'"]');
    }
	
	return res[0].name;
	}
}

$(document).ready(function(){
var root_node,new_node,sel;

	$.ajax({
		  type: "GET",
		  url:"/ajax/locationtype/get",
		  contentType: "application/json; charset=utf-8",
		  dataType: "JSON",
		  success: function(data){
		      setValue(data);
			  
		  }
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
				'li_attr':{'title':data.name,'loc_typeid':data.locationTypeId,'p_id':data.parentLocationId,'center_lat':data.latitude,'center_long':data.longitude,'boundaryFile':data.boundaryFile}  
	   };

	$('#js_tree').jstree({ 'core' : {
		"check_callback" : true,
		'data' :root_node ,
		"plugins" : [ "types","contextmenu"]}}).bind("select_node.jstree", function (e, data) {  
		
		var parent = $('#js_tree').jstree('get_selected');
			
		$('#node_title').val($('#'+parent).attr('title'));
		$('#node_lat').val($('#'+parent).attr('center_lat'));
		$('#node_long').val($('#'+parent).attr('center_long'));
		if($('#'+parent).attr('boundaryFile') != 'null'){
			$('#kml_status').html("<p>KML File exists</p>");	
		} else { $('#kml_status').html("<p>KML File does not exist</p>");	}
		/* var mapOptions = {
			zoom: 11
		};
		 */
		  
/****************************Load KML Layer on Google Map*****************************/
/* var chicago = new google.maps.LatLng(41.875696,-87.624207);
  var mapOptions = {
    zoom: 11,
    center: chicago
  }

var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
alert($('#'+parent).attr('boundaryFile'));
  var ctaLayer = new google.maps.KmlLayer({
    url:$('#'+parent).attr('boundaryFile')
	//$('#'+parent).attr('boundaryFile')'http://gmaps-samples.googlecode.com/svn/trunk/ggeoxml/cta.kml'
	
  });
ctaLayer.setMap(map); */
/* var layer = new google.maps.KmlLayer({
  driveFileId: "0B1vuuiO3sog7bExvSFl3Y1Q1VEU"
}) */
var kml_path = $('#'+parent).attr('boundaryFile');
//alert(kml_path+"Encoded"+encodeURIComponent(kml_path));

var map_html = '<iframe width="625" height="550" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="http://maps.google.com/maps?q='+encodeURIComponent(kml_path)+'&output=embed"></iframe><br /><small><a href="http://maps.google.com/maps?q='+encodeURIComponent(kml_path)+'" style="color:#0000FF;text-align:left">View Larger Map</a></small>';

$('#map-canvas').html(map_html);

/* var mapOptions = {
    zoom: 11
  }

var map = new google.maps.Map($('#map-canvas'), mapOptions);

var ctaLayer = new google.maps.KmlLayer({
    url:$('#'+parent).attr('boundaryFile')
  });
ctaLayer.setMap(map);  */
		  
/*************************End KML Load****************************************************/


						
		var selected_loc_typeid =  $('#'+parent).attr('loc_typeid');
		//alert(selected_loc_typeid);
	
		$.ajax({
			  type: "GET",
			  url:"/ajax/locationtype/getchild/"+selected_loc_typeid,
			  contentType: "application/json; charset=utf-8",
			  dataType: "JSON",
			  success: function(data){
				   
				   var btn_html = "";
				   
				   for(var i=0;i < data.length; i++){
				    btn_html += "<td><a id='node_add_btn"+i+"' onclick='return runMyFunction(event);' value='"+data[i].id+"' class='btn blue add_child' href='#add-node' data-toggle='modal'>Add "+data[i].name+"</a></td>";
					
				   }
			   
				   $('#add_child_btn').html(btn_html);   
				   
				
			  }
			});
		
		if(!($('#'+parent).hasClass('jstree-open')) || $('#'+parent).closest("li").children("ul").length ==0){
		//alert("Dummy Node created");	
		new_node = {'text':'fake','id':'fake_node'};
		$('#js_tree').jstree(true).create_node(parent, new_node);
			
}
}).bind("open_node.jstree",function(e,data){
   var parent = $('#js_tree').jstree('get_selected');
   //$('#'+parent).jstree("destroy").empty();
  $("#js_tree").jstree("delete_node", $('#fake_node'));
	
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
			   	  new_node = {'text':data[i].name+" Type :"+searchLocationTypeName(data[i].locationTypeId),'id':data[i].id,'li_attr':{'title':data[i].name,'loc_typeid':data[i].locationTypeId,'p_id':data[i].parentLocationId,'center_lat':data[i].latitude,'center_long':data[i].longitude,'boundaryFile':data[i].boundaryFile}};
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

/**********************File Upload***********************************/



function sendFile(file) {
var selected_node =  $('#js_tree').jstree('get_selected');
  $.ajax({
    type: 'post',
    url: '/ajax/location/'+selected_node[0]+'/upload',
    data: file,
	headers: {
	'Content-Type': 'multipart/mixed;'
	},
    success: function () {
     alert('File Uploaded');
    },
    xhrFields: {
      // add listener to XMLHTTPRequest object directly for progress (jquery doesn't have this yet)
      onprogress: function (progress) {
        // calculate upload progress
        var percentage = Math.floor((progress.total / progress.totalSize) * 100);
        // log upload progress to console
        console.log('progress', percentage);
        if (percentage === 100) {
          console.log('DONE!');
        }
      }
    },
    processData: false,
    contentType: file.type
  });

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
'li_attr':{'title':data.name,'loc_typeid':data.locationTypeId,'p_id':data.parentLocationId,'center_lat':data.latitude,'center_long':data.longitude,'boundaryFile':data.boundaryFile}};
//alert(JSON.stringify(new_node));
var sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
  }
});



}


$(document).ready(function(){
	
$("#edit_btn").click(function() {
		$("#save_btn").css('display','block');
		$("#edit_btn").css('display','none');
		$('#tab1 input').toggleDisabled();
		
});	
$("#save_btn").click(function() {
		$("#edit_btn").css('display','block');
		$("#save_btn").css('display','none');
		update_selected_node();
		$('#tab1 input').toggleDisabled();
		
});

var form = document.getElementById("form1");
            form.onsubmit = function(){
                var searchText = document.getElementById("file");
				var selected_node =  $('#js_tree').jstree('get_selected');
           var  kml_url= '/ajax/location/'+selected_node[0]+'/upload'
                 $('#form1').attr('action', kml_url);
$('#form1').submit();				 // window.location = kml_url;
                return false;
            };    

$('#node_add_btn0').on('click',function(){
alert($(even.target).val());;

})
	

});

(function($) {
    $.fn.toggleDisabled = function() {
        return this.each(function() {
            var $this = $(this);
            if ($this.attr('disabled')) $this.removeAttr('disabled');
            else $this.attr('disabled', 'disabled');
        });
    };
})(jQuery);

$(function() {
    $('input:button').click(function() {
        $('input:text').toggleDisabled();
    });
});


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
  $('#js_tree').jstree('set_text',data.id, data.name+data.locationTypeId);
  
  }
});
}

function runMyFunction(event){
   
	var target = event.target || event.srcElement;
	$('#new_node_loc_typeid').val($('#'+target.id).attr('value'));
	return true;
	console.log($('#new_node_loc_typeid').attr('value'));
}