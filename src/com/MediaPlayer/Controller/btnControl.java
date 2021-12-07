package com.MediaPlayer.Controller;

import com.jfoenix.controls.JFXSlider;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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

    public boolean playOrPause(MediaPlayer mediaPlayer, boolean isPlaying, ImageView img_play, BorderPane border_pane_volumeShow, ImageView on_screen_center_play, Label mediaName){

        // 判断是否正在播放
        if(isPlaying){

            mediaPlayer.pause();
            changeBtnPicture.changeBtnPicture("play", img_play);
            changeBtnPicture.changeBtnPicture("play", on_screen_center_play);
            mediaName.setText(mediaName.getText().replace("Now Playing", "Pause"));
            on_screen_center_play.setVisible(true);
            border_pane_volumeShow.setVisible(true);

        } else {

            mediaPlayer.play();
            changeBtnPicture.changeBtnPicture("pause", img_play);
            changeBtnPicture.changeBtnPicture("pause", on_screen_center_play);
            mediaName.setText(mediaName.getText().replace("Pause", "Now Playing"));
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    on_screen_center_play.setVisible(false);
                    border_pane_volumeShow.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return !isPlaying;
    }

    public boolean replayAction(MediaPlayer mediaPlayer, ImageView imgId, boolean isPlaying, JFXSlider mediaDuration){
        mediaPlayer.seek(mediaPlayer.getStartTime());
        changeBtnPicture.changeBtnPicture("pause", imgId);
        mediaDuration.setDisable(false);

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
