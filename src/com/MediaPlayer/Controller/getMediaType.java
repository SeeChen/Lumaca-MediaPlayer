// 用于获取媒体文件的类型，以方便确认是音频文件还是视频文件
package com.MediaPlayer.Controller;

public class getMediaType {

    public String getMediaType(String mediaUrl){

        String type = "";

        int i = mediaUrl.lastIndexOf('.');

        if (i >= 0) {
            type = mediaUrl.substring(i+1);
        }

        return type;
    }
}
