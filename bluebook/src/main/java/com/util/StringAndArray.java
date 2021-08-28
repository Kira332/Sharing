package com.util;

import org.springframework.stereotype.Component;

@Component
public class StringAndArray {

    public static String[] stringToArray(String str){
        String[] array = str.split(",");
        for(int i=0;i<array.length;i++){
            array[i] = array[i].trim();
        }
        return array;
    }

    public static String arrayToString(String[] articleTags){
        StringBuilder sb = new StringBuilder();
        for(String s : articleTags){
            if(sb.length() == 0){
                sb.append(s.trim());
            } else {
                sb.append(",").append(s.trim());
            }
        }
        return sb.toString();
    }

}

