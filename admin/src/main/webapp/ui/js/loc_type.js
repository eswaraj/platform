$(function(){
      $("#menu_new").load("../ui/menu.html"); 
});

$(document).ready(function(){

var location_type;
var root_node;

$.ajax({
  type: "GET",
  url:"/ajax/locationtype/get",
  dataType: "JSON",
  success: function(data){
  var root_node = 
       {		'text'    	:data.name,
				'id'  	 	:data.id,								   
				'li_attr':{'title':data.name,'p_id':data.parentLocationTypeId}  
	   };

	$('#js_tree').jstree({ 'core' : {
		"check_callback" : true,
		'data' :root_node ,
		"plugins" : [ "types","contextmenu"]}}).bind("select_node.jstree", function (e, data) {  
		var parent = $('#js_tree').jstree('get_selected');		
		$('#location_type_name').val($('#'+parent).attr('title'));
		if(!$('#'+parent).hasClass('jstree-open')){
		var new_node = {'text':'fake','id':'fake_node'};
		$('#js_tree').jstree(true).create_node(parent, new_node);
		}
    }).bind("open_node.jstree",function(e,data){
	
	//alert(JSON.stringify(data.node.id));	
	$("#js_tree").jstree("delete_node", $('#fake_node'));
	var selected_node =  data.node.id;
	var new_node;
	var sel;
	$.ajax({
		  type: "GET",
		  url:"/ajax/locationtype/getchild/"+data.node.id,
		  contentType: "application/json; charset=utf-8",
		  dataType: "JSON",
		  success: function(data){
		       
			   if(data.length == 0){
			   alert("No Children found");
			   } else{
			   if($('#'+selected_node).closest("li").children("ul").length ==0){
			   
			   for(var i=0; i< data.length;i++){
			   
				  new_node = {'text':data[i].name,'id':data[i].id,'li_attr':{'title':data[i].name,'p_id':data[i].parentLocationTypeId}};
                  sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
				  
				}
			  }else{ alert('No New Nodes');};
			}
		  }
		});

	
	//$("#js_tree").jstree("open_node", data.node.id);
	});    
	
   $('#js_tree').jstree("select_node",root_node.id);	
	
   }
});

$("#edit_btn").click(function() {
		$("#save_btn").css('display','block');
		$("#edit_btn").css('display','none');
		$('#loc_type input').toggleDisabled();
		
});	
$("#save_btn").click(function() {
		$("#edit_btn").css('display','block');
		$("#save_btn").css('display','none');
		update_selected_node();
		$('#loc_type input').toggleDisabled();
		
});




$("#delete_btn").click(function() {
  var x = confirm("Are you sure you want to delete this node?");
  if (x)
      return true;
  else
    return false;
});
});


function add_child_node(){

var selected_node =  $('#js_tree').jstree('get_selected');
var new_node_id;
var post_data = {
   "name":$("#child_location_type_name").val(),
   "parentLocationTypeId":selected_node[0]
  };
$.ajax({
  type: "POST",
  url:"/ajax/locationtype/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  var new_node = {'text':$("#child_location_type_name").val(),'id':data.id,'li_attr':{'title':$("#child_location_type_name").val(),'p_id':data.parentLocationTypeId}};
  var sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
  $('#js_tree').jstree("select_node",data.id);
  }
});
	
}

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


function update_selected_node(){

var selected_node =  $('#js_tree').jstree('get_selected');
var new_node_id;

var post_data = {
   "id":selected_node[0],
   "name":$("#location_type_name").val()
     
  };
  
  if($('#'+selected_node[0]).attr('p_id') != 'null'){
	post_data.parentLocationTypeId = $('#'+selected_node[0]).attr('p_id');
  }
$.ajax({
  type: "POST",
  url:"/ajax/locationtype/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  //alert(JSON.stringify(data, null, 4));
  $('#js_tree').jstree('set_text',data.id, data.name);
  
  }
});
}
