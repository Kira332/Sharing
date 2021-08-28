package com.util.component;

import org.springframework.stereotype.Component;

@Component
public class PhoneRandomBuilder {

    public static String randomBuilder(){
        String result = "";
        for(int i=0;i<4;i++){
            result += Math.round(Math.random() * 9);
        }

        return result;

    }

}
