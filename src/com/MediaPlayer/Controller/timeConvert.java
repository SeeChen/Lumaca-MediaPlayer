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
        String time, house, minute, second;

        if(toCovert / 60 > 60){
            house = lessThanTen(toCovert / 3600) + ":";
            minute = lessThanTen((toCovert / 60) - (toCovert / 3600 * 60));
        }else {
            house = "00:";
            minute = lessThanTen(toCovert / 60);
        }

        second = lessThanTen(toCovert % 60);

        time = house + minute + ":" + second;
        return time;
    }
}
