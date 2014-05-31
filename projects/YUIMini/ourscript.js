YUI({ filter: 'raw' }).use('node', 'io-base', 'json',	'event-key', 'node-event-simulate', function(Y) {
	var revisions;
	var searchText = Y.one('#wikiname');
	var respDiv = Y.one('#responseDiv');

	var buttonCallback = function(e) {
			var entryPoint = './proxy.cgi';
	   	var queryString = encodeURI('?action=query&format=json&prop=revisions&titles=' + searchText.get('value') +
           					 '&rvlimit=100&rvprop=timestamp|user|comment');
   										 
	   	var sUrl = entryPoint + queryString;
			
	   	var request = Y.io(sUrl);	   
   	}	   
 	
  var successHandler = function(id, o, args) {
			try {
		   	var response = Y.JSON.parse(o.responseText);
				var pageid;
				var pages = Y.JSON.stringify(response.query.pages);
				var pageValue = Y.JSON.parse(pages, function(key, value){
						if(key == 'pageid')
							pageid = value;
				});
			}
			catch (e) {
		    respDiv.append('Invalid returned data');
			}
		
			if( !pageid )	{
		   	respDiv.append('No page of that name in Wikipedia');
		   	return;
		   }
				
			revisions = response.query.pages[pageid].revisions;
			var user_array = new Array();		

	      respDiv.empty();
			for( i in revisions )	{
					var item = revisions[i];
					// check if the user is anonymous
					var split_name = item.user.split('.');
					if( split_name.length == 4 )	{
						var temp = parseInt(split_name[0]);
						if( !(isNaN(parseInt(split_name[0])) && 
							 	isNaN(parseInt(split_name[1])) && 
							 	isNaN(parseInt(split_name[2])) && 
							 	isNaN(parseInt(split_name[3]))) )	{
							 
							if( item.user in user_array )
								user_array[item.user]++;
							else
								user_array[item.user] = 1;
						}
					}
					var user = Y.Node.create('<div id = "' + i + '" class="contracted" name="userDiv"><strong>' + item.user + '</strong></div>');
		 			respDiv.appendChild(user);
			}
			respDiv.appendChild(Y.Node.create('<h4>Anonymous Edits</h4>'));	
			for( key in user_array )	{
				var anonymous_user = Y.Node.create('<div>' + key + ': ' + user_array[key] + '</div>');
				respDiv.appendChild(anonymous_user);
			}
		}
	
	var failureHandler = function(id, o ,args) {
			respDiv.append('Failure Response:' + o.statusText);
		}

	var keyPressed = function(e){
		if(e.keyCode == 13){
			e.halt();
			Y.one("#wikisub").simulate("click");
		}		
	}

	var moreInformation = function(e){
		if(this.get('className') == 'contracted'){
			this.set('className', 'expanded');
			
			var divid = this.get('id');
			var item = revisions[parseInt(divid)];
			
			var item_node = Y.Node.create('<div id = "extrainfo' + divid + '" class="extrainfo"></div>');
			if( item.timestamp )
				item_node.appendChild('Timestamp: ' + item.timestamp + '<br/>');
			if( item.comment )
				item_node.appendChild('Comment: ' + item.comment);
				
			this.appendChild(item_node);			
		}
		else	{
			this.set('className', 'contracted');
			var child = Y.one('#extrainfo' + this.get('id'));
			child.remove();
		}
	}
	
	// We attach the callback by ID, not using the variable above.
	Y.on('io:complete', successHandler, Y);
 	Y.on('io:failure',  failureHandler, Y);
	Y.on('click', buttonCallback, '#wikisub'); 
   Y.on('key', keyPressed, '#wikiname');
   //var userList = Node.getElementsByTagName('userDiv');
   Y.on('click', moreInformation, ".contracted"	);
   Y.on('click', moreInformation, ".expanded");

});	
	

