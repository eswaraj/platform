$(function(){
      $("#menu").load("../ui/menu.html"); 
});

$(document).ready(function(){

$.ajax({
  type: "GET",
  url:"/ajax/categories/getroot",
  dataType: "JSON",
  success: function(data){
 
 var root_node_array = new Array();
 
 for(var i=0; i< data.length;i++){
			   
				  new_node = {'text':data[i].name,'id':data[i].id,'li_attr':{'title':data[i].name,'p_id':data[i].parentCategoryId,'desc':data[i].description,'root':data[i].root}};
                  root_node_array.push(new_node);
				  
				  
}
 

	$('#js_tree').jstree({ 'core' : {
		"check_callback" : true,
		'data' :root_node_array,
		"plugins" : [ "types","contextmenu"]}}).bind("select_node.jstree", function (e, data) {  
		var categoryId = $('#js_tree').jstree('get_selected');	
		
		
		$.ajax({
		  type: "GET",
		  url:"/ajax/department/getall/"+categoryId,
		  contentType: "application/json; charset=utf-8",
		  dataType: "JSON",
		  success: function(data){
		
		 if(data.length == 0){
		 $('#dept_data').html("<p>No Departments Found for the Selected Category</p>");
		 $('#cat_id').html(categoryId);
		 } else {
			var dept_data = "<tr><td>S.No</td><td>Name</td><td>Description</td><td>Edit</td></tr>";
			for(var i=0;i<data.length;i++){
			dept_data += "<tr ><td>"+(i+1)+"</td><td id='dept_name"+data[i].id+"'>"+data[i].name+"</td><td id='dept_desc"+data[i].id+"'>"+data[i].description+"<td><a class='btn pink' id='"+data[i].id+"' value='"+data[i].id+"' description='"+data[i].description+"' full_name='"+data[i].name+"' href='#add-dept' onclick='return runMyFunction(event);' data-toggle='modal'></a></td></tr> ";
			}
								
			$('#dept_data').html(dept_data);							
		  }}
		});
		
		
		
		
		
		$('#cat_name').val($('#'+parent).attr('title'));
		$('#cat_desc').val($('#'+parent).attr('desc'));
		
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
			   
				  new_node = {'text':data[i].name,'id':data[i].id,'li_attr':{'title':data[i].name,'p_id':data[i].parentCategoryId,'desc':data[i].description,'root':data[i].root}};
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
});

function add_update_dept(){

var post_data = {
   "id":$("#dept_id").val(),
   "description":$("#description").val(),
   "name":$("#full_name").val(),
   "categoryId":$("#category_id").val()
  };
$.ajax({
  type: "POST",
  url:"/ajax/department/save",
  data: JSON.stringify(post_data),
  contentType: "application/json; charset=utf-8",
  dataType: "JSON",
  success: function(data){
  var count = document.getElementById('dept_data').rows.length;

  if(post_data.id){
   
  $("#dept_name"+post_data.id).html($('#full_name').val());
  $("#dept_desc"+post_data.id).html($('#description').val());
  $("#"+post_data.id).attr('full_name',$('#full_name').val());
  $("#"+post_data.id).attr('description',$('#description').val());
  
  } else { 
  var departments_data ="<tr ><td>"+count+"</td><td id='dept_name"+data.id+"'>"+data.name+"</td><td id='dept_desc"+data.id+"'>"+data.description+"<td><a class='btn pink' id='"+data.id+"' value='"+data.id+"' description='"+data.description+"' full_name='"+data.name+"' href='#add-dept' onclick='return runMyFunction(event);' data-toggle='modal'></a></td></tr> ";
  
  var prev_dept_data = $('#dept_data').html();
  $('#dept_data').html(prev_dept_data+departments_data);	} 
  
  }
});
	
}

function runMyFunction(event){
    var categoryId = $('#js_tree').jstree('get_selected');	
		
	var target = event.target || event.srcElement;
    $('#full_name').val($('#'+target.id).attr('full_name'));
	$('#description').val($('#'+target.id).attr('description'));	
	$('#category_id').val(categoryId);
    $('#dept_id').val($('#'+target.id).attr('id'));	
	
	return true;
	
}
