package com.MediaPlayer;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class mainStage {

    @FXML
    MediaView playView;

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

    Stage stage;

    String url = null;

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

    // 打开本地媒体文件
    public void openFile(ActionEvent actionEvent){

        FileChooser fileChooser = new FileChooser();
        // 添加文件选择，只允许选择后缀名为 .mp3 以及 .mp4 的文件
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("MAKE YOUR CHOICE" , "*.mp3" , "*.mp4");
        fileChooser.getExtensionFilters().add(filter);

        fileChooser.setTitle("Please select a Media File to Open");    // Setting dialog title

        File file = fileChooser.showOpenDialog(stage);

        url = file.toURI().toString();  // 将文件转换为字符串路径

        // 获取文件的后缀名
        String fileExtension = "";
        int i = url.lastIndexOf('.');
        if (i >= 0) {
            fileExtension = url.substring(i+1);
        }

        // 判断文件是音频文件还是视频文件
        if(fileExtension.equals("mp3")){

            System.out.println(fileExtension);
        }else if(fileExtension.equals("mp4")){

            System.out.println(fileExtension);
        }

        // 设置 url
        Media media = new Media(url);
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        // 打开文件后自动播放
        mediaPlayer.setAutoPlay(true);

        playView.setMediaPlayer(mediaPlayer);
    }

    // 关闭程序
    public void closeApp(){

        Platform.exit();
    }

    // 播放器绑定调整宽高
    public void mediaViewBind(Scene mediaViewScene){

        // 绑定播放器高度
        playView.fitHeightProperty().bind(mediaViewScene.heightProperty().subtract(46 + 75));

        // 当定播放器宽度
        playView.fitWidthProperty().bind(mediaViewScene.widthProperty());

    }
}
