package com.MediaPlayer;

import com.MediaPlayer.Controller.about.about_Main;
import com.MediaPlayer.Controller.theme.theme_Main;
import com.MediaPlayer.Controller.tutorial.tutorial_main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

;

public class loadAnother {

    public void about(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXML/aboutPage.fxml"));
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


        primaryStage.setOnCloseRequest(e -> controller.clearMemory());
    }

    public void howToUse() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXML/howToUsePage.fxml"));
        Parent root = fxmlLoader.load();
        tutorial_main controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 720, 480);
        Stage primaryStage = new Stage();
        controller.loading();
        primaryStage.setScene(scene);   // 设置初始 stage 的 scene
        primaryStage.setMinHeight(480);primaryStage.setMinWidth(720);  // 设置 stage 的最小高度以及最小宽度
        primaryStage.setTitle("Lumaca | How To Use");  // 设置标题
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("com/MediaPlayer/RESOURCES/ICON/icon.png"));  // 设置 icon
        primaryStage.show();    // 显示场景


        primaryStage.setOnCloseRequest(e -> controller.clearMemory());
    }

    public void themePick() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXML/themePick.fxml"));
        Parent root = fxmlLoader.load();
        theme_Main controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 720, 480);
        Stage primaryStage = new Stage();
        controller.loading();
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Lumaca | Theme");
        primaryStage.getIcons().add(new Image("com/MediaPlayer/RESOURCES/ICON/icon.png"));
        primaryStage.show();
    }
}
