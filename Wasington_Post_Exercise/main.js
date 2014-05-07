/*
 * Author: Arjune
 */

$(document).ready(function() {
    $("#_searchbtn").click(function(){
        var query = $("#_search").val();
        if (query == "" || query.length < 1) {
            return;
        }
        else {
            makeRequest(query, 1);
        }
    });
    
	$("#_search").keyup(function(event){
		if(event.keyCode == 13){
			$("#_searchbtn").click();
		}
	});

    $("#prev").click(function(){
        var query = $("#_search").val(),
            page = parseInt($("#hidden").val());
        makeRequest(query, page-1);
    });

    $("#next").click(function(){
        var query = $("#_search").val(),
            page = parseInt($("#hidden").val());
        makeRequest(query, page+1);
    });
    
    $("#results").on("click", "a", function(event){
        var self = $(event.target);
        var question_id = self.data('id'),
            url = "http://api.stackexchange.com/2.2/questions/" + question_id + "/answers?order=desc&sort=activity&site=stackoverflow&filter=withbody",
            div = $("#results"),
            article = self.closest("article");
            
        $.getJSON(url, function(result){
            if (result['error_id'] && result['error_id']=="400") {
                return;
            }
            
            processResponseViewItem(result['items'], question_id);
        })
        .fail(function(){
            div.empty();
            div.append("There was an error retrieving the results at this time");
        });   
    });
    
    
    function makeRequest(query, page) {
        if (page < 1) {
            return;
        }

        var site = "stackoverflow",
            pageSize = 1,
            div = $("#results");
        
        // div show processing msg
        var url = "http://api.stackexchange.com/2.2/search/excerpts?page=" + page + "&pagesize=" + pageSize + "&order=desc&sort=activity&q=" + query + "&site=" + site + "&callback=?";
        
        $.getJSON(url, function(result){
            if (result['error_id'] && result['error_id']=="400") {
                div.empty();
                div.append("There was an error retrieving the results at this time");
                return;
            }
            processResponse(result, div);
        })
        .fail(function(){
            div.empty();
            div.append("There was an error retrieving the results at this time");
        });
        page = parseInt($("input[type='hidden']").val());
        $("input[type='hidden']").val(page);        
    }
    
    
    function processResponse(results, div) {
        div.empty();
                
        $.each(results['items'], function(i, item){
            var template = "<h1 class='title'><a href='#' rel='#more_info' data-id={{question_id}}>{{title}}</a></h1><p class='subtitle'>" + 
                    "<span class='first'>Creation Date: </span>{{creation_date}}" + 
                    "<span>Number of Answers: </span>{{answer_count}}" +
                    "<span>Accepted Answer: </span>{{has_accepted_answer}}" +
                    "<span>Score: </span>{{score}}</p>";
                article = $("<article class='answer'></article"),
                header = $("<header class='qaheader'></header>"),
                body = $("<div class='desc'></div>").attr("data-info", item),

            header.html(Mustache.to_html(template, item));
            body.html(jQuery.trim(item['body']).substring(0, 400).trim(this) + "...");
            article.append(header).append(body);
            div.append(article).show();
			$("div.pag").show();
			storeStorage('q_'+item['question_id'], item);
        });
    }
    
    function processResponseViewItem(items, id) {
		var question_template = "<header class='qaheader'><h1 class='qtitle'>{{title}}</h1><p class='subtitle'>" + 
				"<span class='first'>Creation Date: </span>{{creation_date}}" + 
				"<span>Number of Answers: </span>{{answer_count}}" +
				"<span>Accepted Answer: </span>{{has_accepted_answer}}" +
				"<span>Score: </span>{{score}}</p></header><div class='qbody'>{{body}}";
			divq = $("<div id='question'></div>"),
			diva = $("<div id='answers'><h1>Answers</h1>No Answers</div>"),
			div = $("#viewitem");div.empty();
			
		var question = getStorage('q_'+id);	
		divq.html(Mustache.to_html(question_template, question));		
		
        $.each(items, function(i, item){
			var answer_template = "<div class='abody>{{}}</div>" + 
					"<footer><p><span class='first'>User: </span>{{owner.display_name}}" + 
					"<span>Reputation: </span>{{owner.reputation}}"
					"<span>Creation Date: </span>{{creation_date}}" +
					"<span>Score: </span>{{score}}</p>",
				article = $("<article></article>");
			article.html(Mustache.to_html(answer_template, item));	
			diva.append(article);
		});	
		div.append(divq).append("<hr/>").append(diva).show();
    }
    
    function storeStorage(key, value) {
        if ('sessionStorage' in window && window['sessionStorage'] != undefined) {
			if(sessionStorage.getItem(key) === null)
				sessionStorage[key] = JSON.stringify(value);
        }
    }
	
    function getStorage(key) {
        if ('sessionStorage' in window && window['sessionStorage'] != undefined) {
			if(sessionStorage.getItem(key) === null)
				return '';
			else
				return JSON.parse(sessionStorage[key]);
        }
    }
	
});

