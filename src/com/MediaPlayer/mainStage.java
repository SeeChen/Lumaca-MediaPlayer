package com.MediaPlayer;

import com.MediaPlayer.Controller.btnControl;
import com.MediaPlayer.Controller.changeButtonPicture;
import com.MediaPlayer.Controller.getMediaType;
import com.MediaPlayer.Controller.timeConvert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class mainStage {

    boolean isPlaying       = false;    // 用于判断是否正在播放, true 表示播放, false 表示未在播放
    boolean isFullMode      = false;    // 用于判断当前是否为全屏
    boolean isMuteMode      = false;    // 用于判断当前是否为静音
    boolean isTrackDuration = true;     // 用于跟踪播放进度，当值为假时，表示用户正在拖动进度

    double currentVolume;    // 用于保存静音前的音量状态

    Stage stage;
    MediaPlayer mediaPlayer;

    String mediaUrl = null;

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

    @FXML
    private JFXSlider mediaDuration;
    @FXML
    private JFXSlider volumnControl;

    changeButtonPicture changeBtnPicture = new changeButtonPicture();
    getMediaType getMediaType = new getMediaType();
    btnControl btnControl = new btnControl();
    timeConvert timeConvert = new timeConvert();

    // 应用加载事件
    public void loading(){

        // 设置各个按钮的初始图片
        changeBtnPicture.changeBtnPicture("play", img_play);
        changeBtnPicture.changeBtnPicture("stop", img_stop);
        changeBtnPicture.changeBtnPicture("full", img_full);
        changeBtnPicture.changeBtnPicture("volu", img_volu);

        // 设置按钮不可用
        btn_mute.setDisable(true);
        img_volu.setDisable(true);
        volumnControl.setDisable(true);

        // 设置播放进度的初始时间
        mediaDuration.adjustValue(0.0);

        // 设置音量
        currentVolume = 100;
        volumnControl.adjustValue(currentVolume);
    }

    // 媒体播放事件
    private void playMedia(){

        String fileExtension = getMediaType.getMediaType(mediaUrl); // 获取文件的后缀名

        // 判断文件是音频文件还是视频文件
        if(fileExtension.equals("mp3")){

            System.out.println(fileExtension);
        } else if(fileExtension.equals("mp4")) {

            System.out.println(fileExtension);
        }

        // 设置文件的播放
        Media media = new Media(mediaUrl);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setAutoPlay(true);  // 打开文件后自动播放
        mediaPlayer.setVolume(currentVolume);   // 设置播放音量

        mediaPlayerVol();

        // 更改播放的状态
        isPlaying = true;
        changeBtnPicture.changeBtnPicture("pause", img_play);

        volumeChange();   // 音量键调节事件
        volumeClicked();  // 音量键按下事件

        // 设置按钮可用
        btn_mute.setDisable(false);
        img_volu.setDisable(false);
        volumnControl.setDisable(false);

        // 设置 onready 事件
        mediaPlayer.setOnReady(() -> {
            mediaPlayerReady();
        });

        playView.setMediaPlayer(mediaPlayer);   // 播放文件
    }

    private void mediaPlayerReady(){

        // 设置事件条的最大值以及最小值
        mediaDuration.setMin(mediaPlayer.getStartTime().toSeconds());
        mediaDuration.setMax(mediaPlayer.getTotalDuration().toSeconds());

        mediaDuration.setOnMousePressed(event -> isTrackDuration = false);  // 当用户拖动进度时，取消自动跟踪

        // 当用户放手后，跳转到时间跳转到用户的区域
        mediaDuration.setOnMouseReleased(event -> {
            mediaPlayer.seek(Duration.seconds(mediaDuration.getValue()));
            isTrackDuration = true; // 继续跟踪播放
        });

        // 设置拖动时显示的时间
        mediaDuration.setValueFactory(slider ->
                Bindings.createStringBinding(() ->
                        (timeConvert.secondToTime((int) slider.getValue() / 60)) + ":" + (timeConvert.secondToTime((int) slider.getValue() % 60)),
                        slider.valueProperty()
                )
        );

        // 进度跟踪
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if(isTrackDuration)
                    mediaDuration.setValue(newValue.toSeconds());
            }
        });
    }

    // 音量条拖动
    private void volumeChange(){
        volumnControl.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double newVolume = (double) newValue;
                if (newVolume <= 50 && newVolume > 0) {
                    changeBtnPicture.changeBtnPicture("midVol", img_volu);
                    isMuteMode = false;
                } else if(newVolume > 50){
                    changeBtnPicture.changeBtnPicture("volu", img_volu);
                    isMuteMode = false;
                } else {
                    isMuteMode = btnControl.setMute(volumnControl, img_volu, false, currentVolume);
                }

            }
        });
    }

    // 音量条按下事件
    private void volumeClicked(){
        volumnControl.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // 若鼠标放开后的值不为零，则保存新的音量
                double newVolume = volumnControl.getValue();
                if(newVolume != 0){
                    currentVolume = newVolume;
                    if(newVolume <= 50)
                        changeBtnPicture.changeBtnPicture("midVol", img_volu);
                    else
                        isMuteMode = btnControl.setMute(volumnControl, img_volu, true, currentVolume);
                } else if(newVolume == 0){
                    isMuteMode = btnControl.setMute(volumnControl, img_volu, false, currentVolume);
                }
            }
        });
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

        // 如果还有媒体文件正在播放，先停止播放
        if(isPlaying)
            mediaStop();

        mediaUrl = file.toURI().toString();  // 将文件转换为字符串路径

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

            if(isPlaying)
                mediaStop();

            File fileDrop = dragboard.getFiles().get(0);
            mediaUrl = fileDrop.toURI().toString();
            playMedia();
        }
    }

    public void setMute(){
        isMuteMode = btnControl.setMute(volumnControl, img_volu, isMuteMode, currentVolume);
    }

    // 设置播放或暂停视频
    public void playOrPause(){

        // 判断是否有 url, 如果没有则调用打开文件函数，让用户选择媒体文件
        if(mediaUrl == null){
            openFile();
            return;
        }

        isPlaying = btnControl.playOrPause(mediaPlayer, isPlaying, img_play);
    }

    // 停止播放
    public void mediaStop(){

        // 若 mediaUrl 为空，则不执行任何语句
        if(mediaUrl == null)
            return;

        mediaPlayer.stop();
        isPlaying = false;
        changeBtnPicture.changeBtnPicture("play", img_play);
        mediaUrl = null;

        volumnControl.setDisable(true);
        btn_mute.setDisable(true);
        img_volu.setDisable(true);
    }

    // 设置全屏模式
    public void setFullScreen(){

        Stage mainStage = (Stage) parentPane.getScene().getWindow();    // 获取 Stage 容器
        isFullMode = btnControl.fullScreenMode(isFullMode, mainStage, menu_fullScreen, img_full);
    }

    // 关闭程序
    public void closeApp(){ Platform.exit();}

    // 播放器绑定调整宽高
    public void mediaViewBind(Scene mediaViewScene){

        playView.fitHeightProperty().bind(mediaViewScene.heightProperty().subtract(46 + 75));   // 绑定播放器高度
        playView.fitWidthProperty().bind(mediaViewScene.widthProperty());   // 绑定播放器宽度
    }

    public void mediaPlayerVol(){

        mediaPlayer.volumeProperty().bind(volumnControl.valueProperty().divide(100));
    }
}