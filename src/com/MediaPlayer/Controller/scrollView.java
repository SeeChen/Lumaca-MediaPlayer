package com.MediaPlayer.Controller;

import com.jfoenix.controls.JFXSlider;
import javafx.scene.control.Label;

public class scrollView {
    public double volumeUp(JFXSlider volumeControl, Label volumeShow){
        volumeControl.setValue(volumeControl.getValue() + 2);
        volumeShow.setText((int) volumeControl.getValue() + "%");
        return volumeControl.getValue() + 2;
    }
    public double volumeDown(JFXSlider volumeControl, Label volumeShow, double currentVolume){
        volumeControl.setValue(volumeControl.getValue() - 2);
        if(volumeControl.getValue() == 0)
            volumeShow.setText("Mute");
        else {
            volumeShow.setText((int) volumeControl.getValue() + "%");
            return volumeControl.getValue() - 2;
        }
        return currentVolume;
    }
}
