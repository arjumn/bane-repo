function output() {
    var args = Array.prototype.slice.call(arguments),
        q = args[0], len = args.length, i,
        o = args[len-1],
        div = $("<div class='section'></div>"), html;
        
    i = formatInput(args.slice(1, len-1));
    if ($.isArray(o)) o = "[" + o.toString() + "]";
    
    var ans = $("<p></p>").html("input: " + i ).append("<br/>").append("output: " + o);
    if (q == "") 
        $("div.section:last p.output").append(ans);
    else {
        var ques = $("<p class='question'></p>").html(q),
            out = $("<p class='output'></p>").append(ans);
        div.append(ques).append(out);
        $("#container").append(div);
    }
}

function formatInput(args) {
    var i = 0, len = args.length, result = "";
    for(; i < len; i++) {
        if ($.isArray(args[i]))
            result += "[" + args[i].toString() + "]" + ", ";
        else
            result += args[i] + ", ";
    }
    return result;
}

$("body").on("click", "p.question", function(e){
    $(this).next("p.output").slideToggle("slow");
});

/*
 * Given two sorted arrays in one array, find the duplicate numbers
 */
var question = "Given two sorted arrays in one array, find the duplicate numbers";
function remove_duplicate(arr) {
    var i=0, len=arr.length, pos=len, j, result_arr = [];
    
    if (len < 1) {
        return [];
    }
    
    for(;i<len-1;i++) {
        if (arr[i] >= arr[i+1]) {
            pos = i+1;
            break;
        }
    }
    if (pos==len) {
        return [];
    }
    for(i=0,j=pos;i<pos;) {
        if (arr[i] == arr[j]) result_arr.push(arr[i]);
        if (arr[i] > arr[j]) j++;
        else i++;
    }
    return result_arr;
}

output(question, [], remove_duplicate([]));
output("", [1,2,3,4,5], remove_duplicate([1,2,3,4,5]));
output("", [1,1], remove_duplicate([1,1]));
output("", [1,2,1,2], remove_duplicate([1,2,1,2]));
output("", [10,20,30,5,15,25], remove_duplicate([10,20,30,5,15,25]));
output("", [1,3,14,15,21,33,6,14,17,21,40,54,66], remove_duplicate([1,3,14,15,21,33,6,14,17,21,40,54,66]));

/*
 * Remove all duplicate characters in an array in-place
 */
question = "Remove all duplicate characters in an array in-place";
function remove_char(arr, ch) {
    var i = 0, len = arr.length, j = 0;
    if (len < 1) return arr;
    while(i < len) {
        if (arr[i] == ch) {
            if(arr[j] != '/x') j = i;
            arr[i] = '/x';            
        }
        else {
            if (arr[j] == '/x') {
                arr[j] = arr[i];
                arr[i] = '/x';
                j++;
            }
        }
        i++;
    }
    return arr;
}

output(question, ['h', 'e', 'e', 'l', 'o'], 'l', remove_char(['h', 'e', 'e', 'l', 'o'], 'l'));

/*
 * Find the duplicate numbers in an array of numbers between 1-n.
 */
question = "Find the duplicate numbers in an array of numbers between 1-n";
function duplicates(arr) {
    var i = 0, size = arr.length, result_arr = [];
    for (; i < size; i++)
    {
        if (arr[Math.abs(arr[i])] >= 0)
            arr[Math.abs(arr[i])] = -arr[Math.abs(arr[i])];
        else
            result_arr.push(Math.abs(arr[i]));
    }
    return result_arr;
}
output(question, [1,2,3,1,3,6,6], duplicates([1,2,3,1,3,6,6]));


/*
 * Longest palindrome in a string
 */
question = "Longest palindrome in a string";
function longest_palindrome(str) {
    var i = 0, j = 0, k = 0, len = str.length, index = 0, longest = 0;
    var table = [];
    
    if (len < 1) {
        return "";
    }
    
    if (len == 1) {
        return str;
    }
    
    for(;i<len;i++) {
        table[i] = new Array();
        table[i][i] = true;
    }
    
    for(i=0;i<len-1;i++) {
        if (str[i] == str[i+1]) {
            table[i][i+1] = true;
            longest = 2;
            index = i;
        }
    }
    
    for(i=3;i<=len;i++) {
        for(j=0;j<=len-i+1;j++) {
            k = j+i-1;
            if (str[j] == str[k] && table[j+1][k-1]) {
                table[j][k] = true;
                longest = i;
                index = j;
            }
        }
    }
    
    return str.substring(index, longest+index);
}

output(question, "abbcbbbc", longest_palindrome("abbcbbbc"));

/*
 * All possible permutations of an array
 */
function permutations(arr, index, len) {
    var j; 
    if (index == len)
        console.log(arr.join(""));
    else {
        for (j = index; j <= len; j++) {
            swap(arr, index, j);
            permutations(arr, index+1, len);
            swap(arr, index, j);
        }
    }
} 

function swap(arr, i, j) {
    var temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

//permutations([1,2,3], 0, 2);


/* This function will be given a list of words (such as might be tokenized from a paragraph of text), and takes two
 * words and returns the shortest distance (in words) between those two words in the provided text. 
 * Example:
 *   shortest_distance_words(["the", "quick", "brown" "fox", "quick"], "fox","the") == 3
 *   shortest_distance_words(["the", "quick", "brown" "fox", "quick"], "quick","fox") == 1
 */
question = "This function will be given a list of words (such as might be tokenized from a paragraph of text)," + 
           "and takes two words and returns the shortest distance (in words) between those two words in the provided text.<br/>" + 
           'Example: shortest_distance_words(["the", "quick", "brown" "fox", "quick"], "fox","the") == 3<br/>' + 
           'shortest_distance_words(["the", "quick", "brown" "fox", "quick"], "quick","fox") == 1';
function shortest_distance_words(arr, word1, word2) {
    var i = 0, len = arr.length, index1 = -1, index2 = -1, distance = -1;
    if (len < 1) return -1;   
    if (word1 == word2) return 0;
    
    while(i < len) {
        if (arr[i] == word1) {
            if(index1 == -1)
                index1 = i;
            else if (Math.abs(i - index2) < distance) {
                index1 = i;
            }
        }            
        if (arr[i] == word2) {
            if (index2 == -1) {
                index2 = i;
            }
            else if (Math.abs(i - index1) < distance) {
                index2 = i;
            }
        }
        if (index1 != 1 && index2 != -1) {
            distance = Math.abs(index1 - index2);
        }
        i++;
    }
    return distance;
}

output(question, ["the", "quick", "brown", "fox", "quick"], "fox", "the", shortest_distance_words(["the", "quick", "brown", "fox", "quick"], "fox","the"));
output("",  ["the", "quick", "brown", "fox", "quick"], "quick", "fox", shortest_distance_words(["the", "quick", "brown", "fox", "quick"], "quick","fox"));

/*
 * Given an array, find the largest subsequence
 */
question = "Given an array, find the largest subsequence";
function largest_subsequence(arr) {
    var i = 0, len = arr.length, curr_len = 3, j = 0, table = [], max = -9999999, temp;
    if (len < 1) return [];    
    if (len == 1) return arr;
    
    for(; i < len; i++) {
        table[i] = new Array();
        table[i][i] = arr[i];
        if (arr[i] > max) {
            max = arr[i];
        }
    }
    
    for(i = 0; i < len-1; i++) {
        table[i][i+1] = arr[i] + arr[i+1];
        if (table[i][i+1] > max) {
            max = table[i][i+1];
        }
    }
    
    for(; curr_len <= len; curr_len++) {
        for(j = 0; j <= len - curr_len; j++) {
            temp = j + curr_len - 1;
            if (arr[j] + table[j+1][temp] > max) {
                max = arr[j] + table[j+1][temp];
            }
            table[j][temp] = arr[j] + table[j+1][temp];
        }
    }
    
    for(i = 0; i < len; i++)
        for(j = 0; j < len; j++)
            if (table[i][j] == max)
                return arr.splice(i, j+1);
    
    return [];        
}
output(question, [-1,3,2,-4,6], largest_subsequence([-1,3,2,-4,6]));

/*
 * Given a string, figure out if its a number
 */

question = "Given a string, figure out if its a number";
function isNumber(str) {
    var i = 0, len = str.length;
    if (len < 1) return false;
    if (len == 1) {
        if(str[0] >= '0' && str[0] <= '9') return true;
        else return false;
    }
    if (str[0] != '-' && !(str[0] >= '0' && str[0] <= '9')) return false;
    
    for(i = 1; i < len; i++) {
        if (str[i] >= '0' && str[i] <= '9') continue;
        else return false;
    }
    return true;
}

output(question, '-', isNumber("-123"));

/**
 * Implement a method which takes an integer array and returns an integer array (of equal size) in
 * which each element is the product of every number in the input array with the exception of the
 * number at that index.
 *
 * Example:
 *   [3, 1, 4, 2] => [8, 24, 6, 12]
 */

function selfExcludingProduct(arr) {
    var i = 0, len = arr.length, product = 1, product_zero = 1;
    if (len < 2) return arr;
    
    for(; i < len; i++) {
        product *= arr[i];
        if (arr[i] != 0) {
            product_zero *= arr[i];
        }
    }
    
    for(i = 0; i < len; i++) {
        if (arr[i] != 0)
            arr[i] = product/arr[i];
        else
            arr[i] = product_zero;
    }
    return arr;
}

output("self excluding product", [3,-1,-4,2], selfExcludingProduct([3,-1,-4,2]));

/*
 * Sort an array of strings by the length of the string
 */

function sortString(arr) {
    var i = 0, len = arr.length, j = 0, temp, max_length = -1, max_index = -1;
    
    if (len < 2) {
        return arr;
    }
    if (len == 2) {
        if (arr[0].length > arr[1].length) {
            temp = arr[0];
            arr[0] = arr[1];
            arr[1] = temp;
        }
        return arr;
    }

    for(i = len-1; i >= 0; i--) {
        for(j = 1; j <= i; j++) {
            if (arr[j].length > max_length) {
                max_length = arr[j].length;
                max_index = j;
            }
        }
        
        temp = arr[i];
        arr[i] = arr[max_index];
        arr[max_index] = temp;
        max_length = -1;
        max_index = -1;
    }    
    return arr;
}
output("Sort an array of strings by the length of the string",
       "This is a fun problem to solve",
       sortString("This is a fun problem to solve".split(" ")));

output("", "This", sortString("This".split(" ")));
output("", "This is", sortString("This is".split(" ")));

/*
 * Replace the string The quick brown fox jumps over the lazy dog with the string The1 quick2 brown3 fox4 jumps5 over6 the7 lazy8 dog9.
 */

function stringSubstitution(str) {
    var i = 1, len = str.length, str1 = "", num = 1, j = 0;
    
    if (len < 1) {
        return str;
    }
    
    for(;i < len; i++) {
        if (str[i] == " " && str[i-1]) {
            str1 += str.substring(j, i) + num + " ";
            num++;
        }
        
        if (str[i] != " " && str[i-1] == " ") {
            j = i;
        }
    }
    
    str1 += str.substring(j, i) + num;
    
    return str1;
}

output("Replace the string The quick brown fox jumps over the lazy dog with the string The1 quick2 brown3 fox4 jumps5 over6 the7 lazy8 dog9",
       "The quick brown fox jumps over the lazy dog",
       stringSubstitution("The quick brown fox jumps over the lazy dog"));

output("", "The", stringSubstitution("The"));
output("", "A", stringSubstitution("A"));

/*
 * Write a function that takes an array of integers and returns that array rotated by N positions
 */

function rotateArray(arr, n) {
    var i = 0, len = arr.length, temp, j = n;
    
    if (len < 2 || n < 0 || n == len) {
        return arr;
    }
    
    reverseArray(arr, 0, len-1);
    reverseArray(arr, 0, n-1);
    reverseArray(arr, n, len-1);
    return arr;
}

function reverseArray(arr, start, end) {
    var i = start, j = end, temp;
    for (; i < j; i++,j--) {
        temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }    
}
output("Write a function that takes an array of integers and returns that array rotated by N positions",
       [1,2,3,4,5,6], rotateArray([1,2,3,4,5,6], 2));

output("", [1,2,3,4,5,6], rotateArray([1,2,3,4,5,6], 0));
output("", [1,2,3,4,5,6], rotateArray([1,2,3,4,5,6], 5));
output("", [1,2,3,4,5,6], rotateArray([1,2,3,4,5,6], 6));
output("", [1,2], rotateArray([1,2], 1));

/*
 * Given a number in a string format, implement an incrementor
 */

function incrementNumber(str) {
    var i = 0, len = str.length, num_arr;
    if (len < 1) return str;
    num_arr = str.split(".");
    if (num_arr[0] == "") {
        num_arr[0] = "1";
    }
    else
        num_arr[0] = parseInt(num_arr[0]) + 1 + "";
        
    return num_arr.join(".");
}

output("Given a number in a string format, implement an incrementor",
       "123", incrementNumber("123"));

output("", "123.345", incrementNumber("123.345"));
output("", ".345", incrementNumber(".345"));
output("", "-123.345", incrementNumber("-123.345"));

/*
 * Binary search
 */

function search(arr, n) {
    var len = arr.length;
    if (len == 0) {
        return -1;
    }
    
    else
        return binarySearch(arr, n, 0, len-1);
}

function binarySearch(arr, n, start, end) {
    if (start > end) return -1;
    
    var mid = parseInt((start + end)/2);
    if (n == arr[mid]) {
        return mid;
    }
    
    if (n > arr[mid]) {
        return binarySearch(arr, n, mid+1, end);
    }
    
    if (n < arr[mid]) {
        return binarySearch(arr, n, start, mid-1);
    }
    return -1;
}

output("Binary Search", [1,2,3,4,5,6,7,8], search([1,2,3,4,5,6,7,8], 7));
output("", [1,2,3,4,5,6,7,8], search([1,2,3,4,5,6,7,8], 3));
output("", [1,2,3,4,5,6,7,8], search([1,2,3,4,5,6,7,8], 9));


/*
 * 
 */