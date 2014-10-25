var chart_data = new Object(); //Can maintain data in hash if dont want to fetch again but then wont get latest data. Implement data expiry

$(document).ready(function(){
	//Data will come from server side. No need for client side fetching now
	//getData(0);
	create_all_graph(analyticsData);
});

function getData(aid) {
	if(chart_data.aid == null) {
		$.ajax({
			type: "GET",
			url:"http://demo5303989.mockable.io/ajax/amenity_data/"+aid,
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			async: false,
			success: function(data){
				//Enable chart_data after figuring out data expiry mechanism
				//chart_data.aid = data;
				create_all_graph(data);
			}
		});
	}
	else {
		create_all_graph(chart_data.aid);
	}
}

function create_all_graph(data) {
	create_graph_ts(data.ts,"chart_ts");
//	create_graph_stacked(data.ts,"chart_stacked");
	create_graph_pie(data.cat,"chart_pie_c");
	create_graph_pie(data.sys,"chart_pie_s");
	create_graph_bar(data.cat,"chart_bar_c");
	create_graph_bar(data.sys,"chart_bar_s");
}

function create_graph_ts(data,divid) {
    data.forEach(function (obj, index, array) {
        var list = [];
        obj.values.forEach(function (o, i, a) {
            var key = Object.keys(o)[0];
            var val = o[key];
            list.push([key, val]);
        });
        obj.values = list;
    });
	nv.addGraph(function() {
		chart = nv.models.cumulativeLineChart()
		.x(function(d) { return d[0] })
		.y(function(d) { return d[1]/100 }) //adjusting, 100% is 1.00, not 100 as it is in the data
		.color(d3.scale.category10().range())
		.useInteractiveGuideline(true)
		;

	chart.xAxis
		.tickValues([1078030800000,1122782400000,1167541200000,1251691200000])
		.tickFormat(function(d) {
			return d3.time.format('%x')(new Date(d))
		});

	chart.yAxis
		.tickFormat(d3.format(',.1%'));

	//chart.legend.dispatch.on('legendClick', function() { 
	//	return; //do nothing
	//});

	//for (var property in chart.legend.dispatch) {
	//	chart.legend.dispatch[property] = function() { };
	//}

	d3.select('#'+divid+' svg')
		.datum(data)
		.call(chart);

	//TODO: Figure out a good way to do this automatically
	nv.utils.windowResize(chart.update);

	return chart;
	});
}

function create_graph_stacked(data,divid) {
	nv.addGraph(function() {
		var chart = nv.models.stackedAreaChart()
		.margin({right: 100})
		.x(function(d) { return d[0] })   //We can modify the data accessor functions...
		.y(function(d) { return d[1] })   //...in case your data is formatted differently.
		.useInteractiveGuideline(true)    //Tooltips which show all data points. Very nice!
		.rightAlignYAxis(true)      //Let's move the y-axis to the right side.
		.transitionDuration(500)
		.showControls(true)       //Allow user to choose 'Stacked', 'Stream', 'Expanded' mode.
		.clipEdge(true);

	//Format x-axis labels with custom function.
	chart.xAxis
		.tickFormat(function(d) { 
		return d3.time.format('%x')(new Date(d)) 
		});

	chart.yAxis
		.tickFormat(d3.format(',.2f'));

	d3.select('#'+divid+' svg')
		.datum(data)
		.call(chart);

	nv.utils.windowResize(chart.update);

	return chart;
	});
}

function create_graph_pie(data,divid) {
	nv.addGraph(function() {
		var chart = nv.models.pieChart()
		.x(function(d) { return d.name })
		.y(function(d) { return d.count })
		.color(function(d) { return d.data.color })
		.showLabels(true)     //Display pie labels
		.labelThreshold(.05)  //Configure the minimum slice size for labels to show up
		.labelType("percent") //Configure what type of data to show in the label. Can be "key", "value" or "percent"
		.donut(true)          //Turn on Donut mode. Makes pie chart look tasty!
		.donutRatio(0.35)     //Configure how big you want the donut hole size to be.
		;

	d3.select('#'+divid+' svg')
		.datum(data)
		.transition().duration(350)
		.call(chart);

	nv.utils.windowResize(chart.update);

	return chart;
	});
}

function create_graph_bar(data,divid) {
	nv.addGraph(function() {
		data = [{key:"ComplaintData",values:data}];
		var chart = nv.models.discreteBarChart()
		.x(function(d) { return d.name })
		.y(function(d) { return d.count })
		.color(function(d) { return d.data.color })
		.staggerLabels(true)    //Too many bars and not enough room? Try staggering labels.
		.tooltips(false)        //Don't show tooltips
		.showValues(true)       //...instead, show the bar value right on top of each bar.
		.transitionDuration(350)
		;

	d3.select('#'+divid+' svg')
		.datum(data)
		.call(chart);

	nv.utils.windowResize(chart.update);

	return chart;
	});
}
