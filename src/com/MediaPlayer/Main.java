package com.MediaPlayer;

// 导入应用所需的包

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        // 加载 FXML 文件
        FXMLLoader fxmlLoader =new FXMLLoader();
        // 设置 FXML 路径
        fxmlLoader.setLocation(getClass().getResource("./FXML/main.fxml"));

        // 定义 root 的 FXML 场景
        Parent root = fxmlLoader.load();

        // 获取 controller 的文件
        mainStage controller = fxmlLoader.getController();
        controller.loading();   // 运行 loading 函数，为应用加载

        // 设置一个 Scene ， 并设置宽为 720，高为 480
        Scene scene = new Scene(root, 720, 480);

        // 设置按下 esc 键退出全屏时，与正常退出全拼相同
        scene.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            switch (key){
                case ESCAPE:
                    controller.setFullScreen();
                    break;
            }
        });

        // 设置鼠标空闲时隐藏界面
        PauseTransition idle = new PauseTransition(Duration.seconds(2));
        idle.setOnFinished(e -> controller.mouseIdle(scene));
        scene.addEventHandler(Event.ANY, e -> {
            if(e.getEventType().toString().equals("MOUSE_MOVED")) {
                idle.playFromStart();
                controller.mouseNotIdle(scene);
            }
        });

        // 对播放器进行宽高绑定，实时调整宽高
        controller.mediaViewBind(scene);

        primaryStage.setScene(scene);   // 设置初始 stage 的 scene
        primaryStage.setMinHeight(480);primaryStage.setMinWidth(720);  // 设置 stage 的最小高度以及最小宽度
        primaryStage.setTitle("Lumaca");  // 设置标题
        primaryStage.getIcons().add(new Image("com/MediaPlayer/RESOURCES/ICON/icon.png"));  // 设置 icon
        primaryStage.show();    // 显示场景
    }

    // 程序的主入口
    public static void main(String[]args){
        // 启动 application
        Application.launch(args);
    }
}
