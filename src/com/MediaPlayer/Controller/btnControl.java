package com.MediaPlayer.Controller;

import com.jfoenix.controls.JFXSlider;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class btnControl {

    changeButtonPicture changeBtnPicture = new changeButtonPicture();

    public boolean fullScreenMode(boolean isFullMode, Stage mainStage, MenuItem menu_fullScreen, ImageView img_full){

        mainStage.setFullScreen(!isFullMode);   // 设置全屏或退出全屏

        if(!isFullMode){

            menu_fullScreen.setText("Esc _Full Screen Mode");
            changeBtnPicture.changeBtnPicture("fullEsc", img_full);
        }else{

            menu_fullScreen.setText("_Full Screen Mode");
            changeBtnPicture.changeBtnPicture("full", img_full);
        }

        return !isFullMode;
    }

    public boolean playOrPause(MediaPlayer mediaPlayer, boolean isPlaying, ImageView img_play){

        // 判断是否正在播放
        if(isPlaying){

            mediaPlayer.pause();
            changeBtnPicture.changeBtnPicture("play", img_play);

        } else {

            mediaPlayer.play();
            changeBtnPicture.changeBtnPicture("pause", img_play);
        }

        return !isPlaying;
    }

    public boolean setMute(JFXSlider volumnControl, ImageView img_volu, boolean isMuteMode, double currentVol){

        // 音量按钮点击时触发是否静音
        if(isMuteMode){
            volumnControl.adjustValue(currentVol);
            if(currentVol > 50)
                changeBtnPicture.changeBtnPicture("volu", img_volu);
            else
                changeBtnPicture.changeBtnPicture("midVol", img_volu);
        } else {
            volumnControl.adjustValue(0.0);
            changeBtnPicture.changeBtnPicture("mute", img_volu);
        }
        return !isMuteMode;
    }
}
