package com.MediaPlayer.Controller;

import com.jfoenix.controls.JFXSlider;
import javafx.scene.control.Label;

public class scrollView {
    public void volumeUp(JFXSlider volumeControl, Label volumeShow){
        volumeControl.setValue(volumeControl.getValue() + 2);
        volumeShow.setText((int) volumeControl.getValue() + "%");
    }
    public void volumeDown(JFXSlider volumeControl, Label volumeShow){
        volumeControl.setValue(volumeControl.getValue() - 2);
        volumeShow.setText((int) volumeControl.getValue() + "%");
    }
}
