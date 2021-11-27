package com.MediaPlayer.Controller;

public class timeConvert {

    private String lessThanTen(int num){
        String time;
        if(num < 10)
            time = "0" + num;
        else
            time = String.valueOf(num);
        return time;
    }

    public String secondToTime(int toCovert){
        String time;
        time = lessThanTen(toCovert);
        return time;
    }
}
