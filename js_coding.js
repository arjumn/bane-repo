/*
 * Given two sorted arrays in one array, find the duplicate numbers
 */
function remove_duplicate(arr) {
    var i=0, len=arr.length, pos=len, j, result_arr = [];
    
    if (len < 1) {
        console.log("empty array");
        return;
    }
    
    for(;i<len-1;i++) {
        if (arr[i] >= arr[i+1]) {
            pos = i+1;
            break;
        }
    }
    if (pos==len) {
        console.log("Only one array here");
        return;
    }
    for(i=0,j=pos;i<pos;) {
        if (arr[i] == arr[j]) result_arr.push(arr[i]);
        if (arr[i] > arr[j]) j++;
        else i++;
    }
    console.log(result_arr);
}

/*
remove_duplicate([]);
remove_duplicate([1,2,3,4,5]);
remove_duplicate([1,1]);
remove_duplicate([1,2,1,2])
remove_duplicate([10,20,30,5,15,25]);
remove_duplicate([1,3,14,15,21,33,6,14,17,21,40,54,66]);
*/

/*
 * Remove all duplicate characters in an array in-place
 */

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

//console.log(remove_char(['h', 'e', 'e', 'l', 'o'], 'l'));

/*
 * Find the duplicate numbers in an array of numbers between 1-n.
 */
function duplicates(arr) {
    var i = 0, size = arr.length;
    for (; i < size; i++)
    {
        if (arr[Math.abs(arr[i])] >= 0)
            arr[Math.abs(arr[i])] = -arr[Math.abs(arr[i])];
        else
            console.log("Reapeating number: " + Math.abs(arr[i]));
        console.log(arr);
    }
}
//duplicates([1,2,3,1,3,6,6]);


/*
 * Longest palindrome in a string
 */

function longest_palindrome(str) {
    var i = 0, j = 0, k = 0, len = str.length, index = 0, longest = 0;
    var table = [];
    
    if (len < 1) {
        return;
    }
    
    if (len == 1) {
        console.log("The longest sbstring is " + str);
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
    
    console.log("LOngest palindrome is " + str.substring(index, longest+index));
}

longest_palindrome("abbcbbbc")

