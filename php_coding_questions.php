<?php

/*
 * Given  a string of words (sentence), reverse every word in the string
 */

function reverse_words($sentence) {
    $start_word = 0;
    $end_word = 0;
    $len = strlen($sentence);
    if ($len < 1) return $sentence;
    
    for($i = 0; $i < len; $i++) {
        if ($sentence[$i] == " ") {
            $end_word = $i-1;
        }
        else if ($i > 1 && $sentence[$i] != " " && $sentence[$i-1] == " ") {
            $start_word = $i;
            $end_word = $start_word;
        }
        
        if ($end_word - $start_word > 0) {
            for(;$start_word < $end_word; $start_word++, $end_word--) {
                $temp = $sentence[$start_word];
                $sentence[$start_word] = $sentence[$end_word];
                $sentence[$end_word] = $temp;
            }
            $start_word = $end_word = $i;
        }
    }
    return $sentence;
}
//echo(reverse_words("I am a short boy"));


/*
 * Given  a number in an array form, move all the zeros to the end
 */

function move_zero_end($arr) {
    $len = count($arr);
    if($len < 2) return $arr;
    for($i = 0, $j = 0; $i < $len; $i++) {
        if($arr[$i] == 0 && $arr[$j] != 0) {
            $j = $i;
        }
        if($arr[$i] != 0 && $arr[$j] == 0) {
            $temp = $arr[$i];
            $arr[$j] = $temp;
            $arr[$i] = 0;
            $j++;
        }
    }
    return $arr;
}

//print_r(move_zero_end(array(0,1))) + "<br/>";

?>