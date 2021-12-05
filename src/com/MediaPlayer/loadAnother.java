package com.MediaPlayer;

import com.MediaPlayer.Controller.about_Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class loadAnother {

    public void about(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader =new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("./FXML/aboutPage.fxml"));
        Parent root = fxmlLoader.load();
        about_Main controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 720, 480);
        Stage primaryStage = new Stage();
        controller.loading();
        primaryStage.setScene(scene);   // 设置初始 stage 的 scene
        primaryStage.setMinHeight(480);primaryStage.setMinWidth(720);  // 设置 stage 的最小高度以及最小宽度
        primaryStage.setTitle("Lumaca | About");  // 设置标题
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("com/MediaPlayer/RESOURCES/ICON/icon.png"));  // 设置 icon
        primaryStage.show();    // 显示场景
    }
}