package com.MediaPlayer;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class mainStage {


    boolean isPlaying = false;      // 定义一个布尔值判断是播放还是暂停, true 表示 play; false 表示 pause
    boolean isFullMode = false;     // 用于判断当前是否为全屏

    MediaPlayer mediaPlayer;
    @FXML
    MediaView playView;

    @FXML
    MenuItem menu_fullScreen;

    @FXML
    AnchorPane parentPane;

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

    String mediaUrl = null;

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

    // 媒体播放事件
    private void playMedia(){
        // 获取文件的后缀名
        String fileExtension = "";
        int i = mediaUrl.lastIndexOf('.');
        if (i >= 0) {
            fileExtension = mediaUrl.substring(i+1);
        }

        // 判断文件是音频文件还是视频文件
        if(fileExtension.equals("mp3")){

            System.out.println(fileExtension);
        }else if(fileExtension.equals("mp4")){

            System.out.println(fileExtension);
        }

        // 设置 url
        Media media = new Media(mediaUrl);
        mediaPlayer = new MediaPlayer(media);

        // 打开文件后自动播放
        mediaPlayer.setAutoPlay(true);

        isPlaying = true;
        changeBtnPicture("pause", img_play);

        playView.setMediaPlayer(mediaPlayer);
    }

    // 打开本地媒体文件
    public void openFile(){

        FileChooser fileChooser = new FileChooser();
        // 添加文件选择，只允许选择后缀名为 .mp3 以及 .mp4 的文件
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("MAKE YOUR CHOICE" , "*.mp3" , "*.mp4");
        fileChooser.getExtensionFilters().add(filter);

        fileChooser.setTitle("Please select a Media File to Open");    // Setting dialog title

        File file = fileChooser.showOpenDialog(stage);

        // 如果用户未选择文件
        if(file == null)
            return;

        mediaUrl = file.toURI().toString();  // 将文件转换为字符串路径

        // 如果还有媒体文件正在播放，先停止播放
        if(isPlaying) {
            mediaPlayer.stop();
            changeBtnPicture("play", img_play);

            mediaUrl = null;

            isPlaying = false;     // 将播放的状态改为 false
        }

        playMedia();    //播放媒体
    }

    // 设置拖拽打开文件
    // 拖拽事件
    public void fileDragOver(DragEvent event){
        event.acceptTransferModes(TransferMode.ANY);
    }
    // 拖拽放手后事件
    public void fileDragDrop(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        if(dragboard.hasFiles()){

            if(isPlaying){
                mediaPlayer.stop();;
                mediaUrl = null;
                isPlaying = false;
                changeBtnPicture("play", img_play);
            }
            File fileDrop = dragboard.getFiles().get(0);
            mediaUrl = fileDrop.toURI().toString();
            playMedia();
        }
    }

    // 更改 play 按钮的图片
    private void changeBtnPicture(String imageName, ImageView btnId){

        String toChangeUrl = "src/com/MediaPlayer/RESOURCES/ICON/" + imageName + ".png";
        File fileChange = new File(toChangeUrl);
        String stringChage = fileChange.toURI().toString();
        Image imageChange = new Image(stringChage);
        btnId.setImage(imageChange);

    }

    // 设置播放或暂停视频
    public void playOrPause(){

        // 判断是否有 url, 如果没有则调用打开文件函数，让用户选择媒体文件
        if(mediaUrl == null){
            openFile();
            return;
        }

        isPlaying = !isPlaying;

        if(isPlaying){
            mediaPlayer.play();

            // 更改按钮的图片
            changeBtnPicture("pause", img_play);

        } else {
            mediaPlayer.pause();

            // 更改按钮的图片
            changeBtnPicture("play", img_play);
        }
    }

    // 结束播放
    public void mediaStop(){

        if(mediaUrl == null)
            return;

        mediaPlayer.stop();
        isPlaying = false;
        changeBtnPicture("play", img_play);
        mediaUrl = null;
    }

    public void setFullScreen(){
        Stage mainStage = (Stage) parentPane.getScene().getWindow();
        
        mainStage.setFullScreen(!isFullMode);   // 设置全屏或退出全屏
        if(!isFullMode){
            menu_fullScreen.setText("Esc _Full Screen Mode");
            changeBtnPicture("fullEsc", img_full);

        }else{
            menu_fullScreen.setText("_Full Screen Mode");
            changeBtnPicture("full", img_full);
        }

        isFullMode = !isFullMode;
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
