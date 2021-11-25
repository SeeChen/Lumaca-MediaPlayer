package com.MediaPlayer;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class mainStage {

    @FXML
    private JFXButton btn_play;

    @FXML
    private JFXButton btn_stop;

    @FXML
    private JFXButton btn_full;

    @FXML
    private JFXButton btn_mute;

    // 控制图标
    @FXML
    private ImageView img_play;
    @FXML
    private ImageView img_stop;
    @FXML
    private ImageView img_full;
    @FXML
    private ImageView img_volu;

    // 应用加载事件
    public void loading(){
        // 获取各个图标的路径
        File filePlay = new File("src/com/MediaPlayer/RESOURCES/ICON/play.png");
        File fileStop = new File("src/com/MediaPlayer/RESOURCES/ICON/stop.png");
        File fileFull = new File("src/com/MediaPlayer/RESOURCES/ICON/full.png");
        File fileVolu = new File("src/com/MediaPlayer/RESOURCES/ICON/volu.png");

        // 将路径转换为文字
        String stringPlay = filePlay.toURI().toString();
        String stringStop = fileStop.toURI().toString();
        String stringFull = fileFull.toURI().toString();
        String stringVolu = fileVolu.toURI().toString();

        // new 一个 image
        Image imagePlay = new Image(stringPlay);
        Image imageStop = new Image(stringStop);
        Image imageFull = new Image(stringFull);
        Image imageVolu = new Image(stringVolu);

        // 设置各个按钮的图片
        img_play.setImage(imagePlay);
        img_stop.setImage(imageStop);
        img_full.setImage(imageFull);
        img_volu.setImage(imageVolu);
    }
}
