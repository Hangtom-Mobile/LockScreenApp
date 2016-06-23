package com.askhmer.mobileapp.utils;

import java.util.Date;
import java.util.Random;

/**
 * Created by soklundy on 6/22/2016.
 */
public class TokenGenerator {

    public String resultTokenId(){
        Date date= new Date();
        Random rand = new Random();

        //getTime() returns current time in milliseconds
        long time = date.getTime();

        //50 is the maximum and the 1 is our minimum
        int  randomNum = rand.nextInt(999) + 1;

        long result = time + randomNum;
        return String.valueOf(result);
    }

}
