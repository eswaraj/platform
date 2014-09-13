$(function(){
      $("#menu").load("../ui/sidebar_menu.html"); 
});

$(document).ready(function(){

var root_node;

$.ajax({
  type: "GET",
  url:"/ajax/categories/getroot",
  dataType: "JSON",
  success: function(data){
 
 var root_node_array = new Array();
 
 for(var i=0; i< data.length;i++){
			   
				  new_node = {'text':data[i].name+'-'+data[i].id,'id':data[i].id,'li_attr':{'title':data[i].name,'p_id':data[i].parentCategoryId,'desc':data[i].description,'img':data[i].imageUrl,'headerImg':data[i].headerImageUrl,'videoUrl':data[i].videoUrl,'root':data[i].root}};
                  root_node_array.push(new_node);
				  //sel = $('#js_tree').jstree(true).create_node("#", new_node);
				  
}
 
 
/* var root_node = 
       {		'text'    	:data[0].name,
				'id'  	 	:data[0].id,								   
				'li_attr':{'title':data[0].name,'p_id':data[0].parentCategoryId,'desc':data[0].description}  
	   };*/

	$('#js_tree').jstree({ 'core' : {
		"check_callback" :true,
		'data' :root_node_array,
		"plugins" : [ "types","contextmenu"]}}).bind("select_node.jstree", function (e, data) {  
		var parent = $('#js_tree').jstree('get_selected');	
		
		$('#cat_name').val($('#'+parent).attr('title'));
		$('#cat_desc').val($('#'+parent).attr('desc'));
		$('#cat_img').val($('#'+parent).attr('img'));
		
		$('#cat_headerImg').val($('#'+parent).attr('headerImg'));
		if($('#'+parent).attr('img')!= null){
		   $('#image').css("display","block");
		   $('#image').attr('src',$('#'+parent).attr('img'));
		}
		if($('#'+parent).attr('headerImg') != null){
		   $('#headerImg').css("display","block");
		   $('#headerImg').attr('src',$('#'+parent).attr('headerImg'));
		}
		if($('#'+parent).attr('videoUrl') != null){
		   $('#video').css("display","block");
		   $('#video').attr('src',$('#'+parent).attr('videoUrl'));
		}
		
		$('#cat_videoUrl').val($('#'+parent).attr('videoUrl'));
		
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
		  url:"/ajax/categories/getchild/"+data.node.id,
		  contentType: "application/json; charset=utf-8",
		  dataType: "JSON",
		  success: function(data){
		       
			   if(data.length == 0){
			   alert("No Children found");
			   } else{
			   if($('#'+selected_node).closest("li").children("ul").length ==0){
			   
			   for(var i=0; i< data.length;i++){
			   
				  new_node = {'text':data[i].name+'-'+data[i].id,'id':data[i].id,'li_attr':{'title':data[i].name,'p_id':data[i].parentCategoryId,'desc':data[i].description,'img':data[i].imageUrl,'headerImg':data[i].headerImg,'videoUrl':data[i].videoUrl,'root':data[i].root}};
                  sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
				  
				}
			  }else{ alert('No New Nodes');};
			}
		  }
		});

	
	//$("#js_tree").jstree("open_node", data.node.id);
	});    
	
   $('#js_tree').jstree("select_node",root_node_array[0].id);	
	
   }
});

$("#edit_btn").click(function() {
		$("#save_btn").css('display','block');
		$("#edit_btn").css('display','none');
		$('#category1 input').toggleDisabled();
		
});	
$("#save_btn").click(function() {
		
		update_selected_node();
		
		
});


});

/******************************Add Root Category*************************/

function add_root_node(){

var post_data = {
   "id":null,	
   "name":$("#root_cat_name").val(),
   "root":true,
   "description":$("#root_cat_desc").val(),
   "imageUrl":$("#root_cat_img").val(),
   "headerImageUrl":$("#root_cat_headerImg").val(),
   "videoUrl":$("#root_cat_videoUrl").val()
  };
$.ajax({
  type: "POST",
  url:"/ajax/categories/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  
  var new_node = {'text':data.name+'-'+data.id,'id':data.id,'li_attr':{'title':data.name,'p_id':data.parentCategoryId,'desc':data.description,'img':data.imageUrl,'headerImg':data.headerImageUrl,'videoUrl':data.videoUrl,'root':data.root}};
  var sel = $('#js_tree').jstree(true).create_node('#', new_node);
  $('#js_tree').jstree("select_node",data.id);
  }
});
	
}

/******************************Add Child Category*************************/

function add_child_node(){

var selected_node =  $('#js_tree').jstree('get_selected');
var new_node_id;

var post_data = {
   "id":null,	
   "name":$("#child_cat_name").val(),
   "parentCategoryId":selected_node[0],
   "root":false,
   "description":$("#child_desc").val(),
   "imageUrl":$("#child_cat_img").val(),
   "headerImageUrl":$("#child_cat_headerImg").val(),
   "videoUrl":$("#child_cat_videoUrl").val()
  };
$.ajax({
  type: "POST",
  url:"/ajax/categories/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  
  var new_node = {'text':data.name+'-'+data.id,'id':data.id,'li_attr':{'title':data.name,'p_id':data.parentCategoryId,'desc':data.description,'img':data.imageUrl,'headerImg':data.headerImageUrl,'videoUrl':data.videoUrl,'root':data.root}};
  var sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
  $('#js_tree').jstree("select_node",data.id);
  }
});
	
}

/*****************************Update Selected Category*************************/

function update_selected_node(){

var selected_node =  $('#js_tree').jstree('get_selected');
var new_node_id;

var post_data = {
   "id":selected_node[0],
   "name":$("#cat_name").val(),   
   "description":$("#cat_desc").val(),
   "imageUrl":$("#cat_img").val(),
   "headerImageUrl":$("#cat_headerImg").val(),
   "videoUrl":$("#cat_videoUrl").val()
  };
  
  if($('#'+selected_node[0]).attr('root') == 'true'){
	post_data.root = true;
  } else {
	post_data.root = false;
  } 
  
  if($('#'+selected_node[0]).attr('p_id') != 'null'){
	post_data.parentCategoryId = $('#'+selected_node[0]).attr('p_id');
  }else {
   post_data.parentCategoryId = null;
  }
  
$.ajax({
  type: "POST",
  url:"/ajax/categories/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  $('#js_tree').jstree('set_text',data.id, data.name);
  $('#'+data.id).attr('img',data.imageUrl);
  $('#'+data.id).attr('desc',data.description);  
  $('#'+data.id).attr('img',data.imageUrl);
  $('#'+data.id).attr('headerImg',data.headerImageUrl);
  $('#'+data.id).attr('videoUrl',data.videoUrl);
  }
});
}
