/*
 * Author: Arjune
 * Simple js file with raw js
 */

var ac_div = document.getElementById("ac"),
    input = document.getElementById("_search"),
    button = document.getElementById("_searchbtn"),
    prev_kw, ajaxRequest;
    
    if (window.XMLHttpRequest) ajaxRequest = new XMLHttpRequest();
    else ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");

function Cache(size) {
    var _size = size,
        _obj = {};
        
    this.getSize = function(){ return _size; }
    this.addEntry = function(key, value){
            if (key in _obj && _obj.hasOwnProperty(key)) {
                return;
            }
            else
                _obj[key] = value;
            _size++;    
    }
    
    this.getEntry = function(key) {
            if (key in _obj && _obj.hasOwnProperty(key)) {
                return _obj[key];
            }
            else
                return false;
    }
    
    this.print = function(){
        for(var item in _obj)
            if (_obj.hasOwnProperty(item)) 
                console.log("Key: " + item + ", value: " + _obj[item]);
    }
}

var cache = new Cache(10);
    
function autocomplete_request(e) {
    var q = input.value;
    
    // return if an enter key is pressed
    e.which = e.which || e.keycode;
    if (e.which == 13) {
        return;
    }
    
    // return is the q is empty or if it is equal to the previous keyword
    if (q == "" || prev_kw == q) return;
    
    // check if entry is in cache
    if (cache.getEntry(q) != false) {
        ac_div.innerHTML = cache.getEntry(q);
        ac_div.style.display = "block";
        return;
    }
    
    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == 4 && ajaxRequest.status==200) {
            var i = 0, arr = eval('(' + ajaxRequest.responseText + ')'), list = "";
            if (arr.length < 1) {
                return;
            }
            list = "<ul>"
            for(var i = 0; i < arr.length; i++) {
                list += "<li>" + arr[i] + "</li>";
            }
            list += "</ul";
            ac_div.innerHTML = list;
            ac_div.style.display = "block";
            cache.addEntry(q, list);
            prev_kw = q;
            cache.print();        
        }    
    }
    ajaxRequest.open("GET", "main.php?ac=1&q="+q, true);
    ajaxRequest.send();
}

function submit_request(e) {
    var q = input.value, result_div = document.getElementById("result");
    ajaxRequest.onreadystatechange = function(){
        if (ajaxRequest.readyState == 4 && ajaxRequest.status == 200) {
            result.div.innerHTML = eval('(' + ajaxresponse.responseText + ')');
        }       
    }
}

input.addEventListener('keyup', autocomplete_request, false);
button.addEventListener('click', submit_request, false);

