package com.MediaPlayer;

// 导入应用所需的包

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        // 设置一个 Scene ， 并设置宽为 1080，高为 720
        Scene scene = new Scene(root, 1080, 720);

        // 对播放器进行宽高绑定，实时调整宽高
        controller.mediaViewBind(scene);

        primaryStage.setScene(scene);   // 设置初始 stage 的 scene
        primaryStage.setMinHeight(720);primaryStage.setMinWidth(1080);  // 设置 stage 的最小高度以及最小宽度
        primaryStage.setTitle("哈哈哈哈哈哈哈哈");  // 设置标题
        primaryStage.show();    // 显示场景
    }

    // 程序的主入口
    public static void main(String[]args){
        // 启动 application
        Application.launch(args);
    }
}
