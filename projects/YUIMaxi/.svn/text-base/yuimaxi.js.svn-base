var yuiobject, 
		wikiTitle, 
		wikiButton, 
		console, 
		wikiDiv, 
		loadingOverlay,
		globalListener,
		newsReloadListener,
		tweetsReloadListener,
		chartReloadListener,
		newsTable,
		chart,
		title,
		tweetTable;

YUI({useBrowserConsole: false}).use(
				'autocomplete', 
			 	'datatable', 
			 	'datatable-scroll', 
			 	'json', 
			 	'charts', 
			 	'datasource',
			 	'console',
			 	'event-key',
			 	'node-event-simulate',
			 	'overlay',
			 	yuiloaded
			 );

function keyPressed(event){
	if(event.keyCode == 13){
		event.halt();
		wikiButton.simulate("click");
	}		
}

function trim( s ){
    var i, startPos = 0, endPos = 0;
    for( i = 0; i < s.length; i++ ){
        if( s.charAt(i) != ' ' ){
            startPos = i;
            break;
        }
    }
    for( i = s.length - 1; i >= 0; i-- ){
        if( s.charAt(i) != ' ' ){
            endPos = i + 1;
            break;
        }
    }
	s = s.substring( startPos, endPos );
   return s;
}

function buttonClicked(event){
	document.getElementById("titleError").style.display = "none";
	var timeZone = getUserTimeZone();
	var entryPoint = './proxy.cgi';
	yuiobject.log(wikiTitle.get('value'), "debug", "yuimaxi");
	if(trim(wikiTitle.get('value')) == ''){
		document.getElementById("titleError").style.display = "block";
		return;
	} 
	document.getElementById("overlay").style.display = "block";
	loadingOverlay.show();
	title = trim(wikiTitle.get('value'));
	var queryString = encodeURI('?title=' + title + '&timezone=' + timeZone);
	var sUrl = entryPoint + queryString;
	yuiobject.io(sUrl, globalListener);
}

//Chart

function loadChart(graph_data){
	//alert(yuiobject.JSON.stringify(graph_data));
	/*graph_data = [
   	{category:"5/1/2010", values:2000},
    	{category:"5/2/2010", values:50},
    	{category:"5/3/2010", values:400},
    	{category:"5/4/2010", values:200},
    	{category:"5/5/2010", values:5000}
	];*/
	
	// Instantiate and render the chart
	if(graph_data.length > 0){
		yuiobject.one("#charts").removeClass("chartsWrapper");
		yuiobject.one("#charts").empty();
		chart = new yuiobject.Chart({
   		dataProvider: graph_data,
   		render: "#charts"
   	});
   	var category = chart.getCategoryAxis();
		category.set("styles", {label:{rotation:90}});
	}else{
		yuiobject.one("#charts").addClass("chartsWrapper");	
	}	
}

//Auto Complete 

function autoCompleteWikiTitle(){
	var autoCompleteDataSource = new yuiobject.DataSource.Get({source:'http://en.wikipedia.org/w/api.php?action=opensearch&format=json'});
	autoCompleteDataSource.plug({fn: yuiobject.Plugin.DataSourceJSONSchema, cfg: {schema : {resultListLocator : "[1]"}}});	
   wikiTitle.plug(yuiobject.Plugin.AutoComplete,{
                                resultHighlighter:'phraseMatch',
                                source: autoCompleteDataSource,
                                requestTemplate: '&search={query}'
                            });	
}

//Twitter

var tweetFormatter = function (o) {
    var user  = o.record.getValue("user");
    var timestamp = o.record.getValue("timestamp");
	 var text = o.record.getValue("text"); 
    var tweetRow = "<a class='font5' target='_blank' href='http://www.twitter.com/" + user + "'>" + user + "</a><br/>" + text +
    						"<br/><span class='timestamp'>"  + timestamp + "</span>";
    return tweetRow; 
};

function refreshTweetsTable(event){
	document.getElementById("loadingTweets").style.visibility = "visible";
	document.getElementById("tweetsTableError").style.display = "none";
	var timeZone = getUserTimeZone();
	var entryPoint = './proxy.cgi';
	var queryString = encodeURI('?refresh=tweets&timezone=' + timeZone + "&title=" + title);
	var sUrl = entryPoint + queryString;
	yuiobject.io(sUrl, tweetsReloadListener);	
}

function tweetsReloadSuccess(id, o, args){
	document.getElementById("loadingTweets").style.visibility = "hidden";
	try{
		var response = yuiobject.JSON.parse(o.responseText);
		setTwitterData(response.t_data);
		loadChart(response.graph_data);
	}catch(e){
		document.getElementById("tweetsTableError").style.display = "block";
	}
}

function tweetsReloadFailed(id, o, args){
	document.getElementById("loadingTweets").style.visibility = "hidden";
	document.getElementById("tweetsTableError").style.display = "block";
}


function loadTweetTable(data){
	if(data.length > 0){
		yuiobject.one("#tweetTable").removeClass("tableWrapper");
		yuiobject.one("#tweetTable").empty();
		var col = [{key: "tweets", formatter: tweetFormatter} ];
		tweetTable = new yuiobject.DataTable.Base({columnset: col, recordset: data});
		tweetTable.plug(yuiobject.Plugin.DataTableScroll, {height: "350px"});
   	tweetTable.render("#tweetTable");
   }else{
   	yuiobject.one("#tweetTable").addClass("tableWrapper");
   }
}

function setTwitterData(twitterData){
	//format : {"text":"Deputies: 2 wanted for Pizza Hut robbery in northwest Harris County: by khou.com staff HARRIS COUNTY, Texas – Au... http://bit.ly/dFOXaa","user":"TheProgramGirl","timestamp":"Sat, 05 Feb 2011 20:42:56 +0000"}
	/*
	twitterData = [
   				{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
   				{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
   				{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
   				{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
   				{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
   				{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
					{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
					{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
					{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"},
   				{text : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", user:"test", timestamp:"test"}
   				];
   */
   loadTweetTable(twitterData);	
}

//Google News

var newsFormatter = function (o) {
    var title  = o.record.getValue("title");
    var content = o.record.getValue("content");
	 var publisher = o.record.getValue("publisher"); 
	 var url = o.record.getValue("url");
	 var date = o.record.getValue("date");
    var newsRow = "<span class='publisher font5'>" + publisher + "</span>:&nbsp";
    newsRow += "<a class='font5' target='_blank' href='" + url + "'>" + title + "</a><br/>";
    newsRow += content;
    newsRow += "<br/><span class='timestamp'>" + date + "<span>";
    return newsRow; 
};

function refreshNewsTable(event){
	document.getElementById("loadingNews").style.visibility = "visible";
	document.getElementById("newsTableError").style.display = "none";
	var timeZone = getUserTimeZone();
	var entryPoint = './proxy.cgi';
	var queryString = encodeURI('?refresh=news&timezone=' + timeZone + "&title=" + title);
	var sUrl = entryPoint + queryString;
	yuiobject.io(sUrl, newsReloadListener);	
}

function newsReloadSuccess(id, o, args){
	document.getElementById("loadingNews").style.visibility = "hidden";
	try{
		var response = yuiobject.JSON.parse(o.responseText);
		setNewsData(response);
	}catch(e){
		document.getElementById("newsTableError").style.display = "block";
	}
}

function newsReloadFailed(id, o, args){
	document.getElementById("loadingNews").style.visibility = "hidden";
	document.getElementById("newsTableError").style.display = "block";
}

function loadNewsTable(data){
	if(data.length > 0){
		yuiobject.one("#newsTable").removeClass("tableWrapper");
		yuiobject.one("#newsTable").empty();
		var col = [{key: "news", formatter: newsFormatter} ];
		newsTable = new yuiobject.DataTable.Base({columnset: col, recordset: data});
		newsTable.plug(yuiobject.Plugin.DataTableScroll, {height: "350px"});
	   newsTable.render("#newsTable");
	}else{
		yuiobject.one("#newsTable").addClass("tableWrapper");
	}
}

function setNewsData(newsData){
	//{"Content":"New Delhi, Feb 6 (PTI) Sachin Tendulkar feels that hecan&#39;&#39;t thank his countrymen enough for the amount of faith theyhave showed in his integrity during his ...","url":"http://news.oneindia.in/2011/02/06/thankmy-countrymen-for-showing-faith-in-my-integritysachi-aid0126.html","publisher":"Oneindia","date":"Sun, 06 Feb 2011 04:49:55 -0800","title":"Thank my countrymen for showing faith in my integrity: Sachin"}
	/*newsData = [
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"},
   					{title : "Lorem ipsum dolor sit amet", publisher:"test", date:"test", content:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin massa lacus, commodo quis suscipit at, eleifend at purus. Pellentesque dui orci, tincidunt at", url:"http://test.com"}
   				];*/
   loadNewsTable(newsData);
}

//Wikipedia

function setWikiData(wikiData){
	wikiDiv.empty();
	wikiDiv.set("innerHTML", wikiData);
}

//IO handlers

function dataRetrievedSucessfully(id, o, args){
	document.getElementById("errorHTML").innerHTML = o.responseText;
	document.getElementById("overlay").style.display = "none";
	document.getElementById("errorMessage").style.display = "none";
	document.getElementById("pageContents").style.display = "block";
	loadingOverlay.hide();
	yuiobject.log("Sucessfully retrived the contents", "debug", "yuimaxi");
	try{
		var response = yuiobject.JSON.parse(o.responseText);
		yuiobject.one("#title").set("innerHTML", wikiTitle.get('value'));
		setWikiData(response.w_data);
		setTwitterData(response.t_data);
		loadChart(response.graph_data);
		setNewsData(response.g_data);	
	}catch(e){
		showErrorMessage();
	}		
}

function dataRetrievedFailure(id, o, args){
	yuiobject.log("Failed in retriving the contents", "debug", "yuimaxi");
	showErrorMessage();
}

function showErrorMessage(){
	document.getElementById("overlay").style.display = "none";
	document.getElementById("pageContents").style.display = "none";
	document.getElementById("errorMessage").style.display = "block";
}

//YUI load call back

function yuiloaded(Y){
	yuiobject = Y;
	wikiTitle =  yuiobject.one('#wikiTitle');
	wikiButton = yuiobject.one('#wikiButton');
	wikiDiv = yuiobject.one('#wikiData');
	loadingOverlay = new yuiobject.Overlay({
			srcNode: "#overlay",
			visible: false,
			render : true,
			x: 555,
			y: 37
    });
   //new yuiobject.Console().render();
   autoCompleteWikiTitle();
	yuiobject.on('click', buttonClicked, wikiButton); 
	yuiobject.on('click', refreshNewsTable , "#newsRefresh");
	yuiobject.on('click', refreshTweetsTable, "#tweetsRefresh");
   yuiobject.on('key', keyPressed, wikiTitle);
   globalListener = {on: {complete:dataRetrievedSucessfully, failurew:dataRetrievedFailure}};
   newsReloadListener = {on: {complete:newsReloadSuccess, failure:newsReloadFailed}};
   tweetsReloadListener = {on: {complete:tweetsReloadSuccess, failure:tweetsReloadFailed}};   
} 

// Timezone
// script by Josh Fraser (http://www.onlineaspect.com)
function getUserTimeZone(){
	var rightNow = new Date();
	var jan1 = new Date(rightNow.getFullYear(), 0, 1, 0, 0, 0, 0);  // jan 1st
	var june1 = new Date(rightNow.getFullYear(), 6, 1, 0, 0, 0, 0); // june 1st
	var temp = jan1.toGMTString();
	var jan2 = new Date(temp.substring(0, temp.lastIndexOf(" ")-1));
	temp = june1.toGMTString();
	var june2 = new Date(temp.substring(0, temp.lastIndexOf(" ")-1));
	var std_time_offset = (jan1 - jan2) / (1000 * 60 * 60);
	var daylight_time_offset = (june1 - june2) / (1000 * 60 * 60);
	var dst;
	if (std_time_offset == daylight_time_offset) {
		dst = "0"; // daylight savings time is NOT observed
	} else {
		// positive is southern, negative is northern hemisphere
		var hemisphere = std_time_offset - daylight_time_offset;
		if (hemisphere >= 0)
			std_time_offset = daylight_time_offset;
		dst = "1"; // daylight savings time is observed
	}
	return std_time_offset;
}
    