package com.MediaPlayer;

import com.MediaPlayer.Controller.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
    boolean isToReplay      = false;    // 用于判断点击播放按钮是否为 replay
    boolean isMusicPlay     = false;    // 用于判断当前是否为音频文件

    double currentVolume;           // 用于保存静音前的音量状态
    double currentPlayRate = 1.0;   // 用于记录当前的播放速度

    String fileName;    // 用于保存文件的名字

    Stage stage;
    MediaPlayer mediaPlayer;
    MediaPlayer musicPlayer;

    String mediaUrl = null;

    @FXML
    MediaView playView;

    @FXML
    MenuItem menu_fullScreen;

    @FXML
    AnchorPane parentPane;

    @FXML
    private Label current_time;
    @FXML
    private Label total_time;

    @FXML
    private JFXButton btn_play;
    @FXML
    private JFXButton btn_stop;
    @FXML
    private JFXButton btn_full;
    @FXML
    private JFXButton btn_mute;
    @FXML
    private JFXButton btn_rate;

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
    private ImageView img_rate;

    // 播放速率列表
    @FXML
    private VBox speed_list;
    @FXML
    private Label speedX50;
    @FXML
    private Label speedX100;
    @FXML
    private Label speedX125;
    @FXML
    private Label speedX150;
    @FXML
    private Label speedX175;
    @FXML
    private Label speedX200;

    @FXML
    private JFXSlider mediaDuration;
    @FXML
    private JFXSlider volumnControl;

    @FXML
    private BorderPane border_pane_media_player;
    @FXML
    private BorderPane border_pane_volumeShow;

    @FXML
    private Label volumeShow;

    @FXML
    private ImageView on_screen_center_play;

    @FXML
    private Label mediaName;

    changeButtonPicture changeBtnPicture = new changeButtonPicture();
    getMediaType getMediaType = new getMediaType();
    btnControl btnControl = new btnControl();
    timeConvert timeConvert = new timeConvert();
    scrollView scrollView = new scrollView();

    // 应用加载事件
    public void loading(){

        // 设置各个按钮的初始图片
        changeBtnPicture.changeBtnPicture("play", img_play);
        changeBtnPicture.changeBtnPicture("stop", img_stop);
        changeBtnPicture.changeBtnPicture("full", img_full);
        changeBtnPicture.changeBtnPicture("volu", img_volu);
        changeBtnPicture.changeBtnPicture("rate", img_rate);

        changeBtnPicture.changeBtnPicture("play", on_screen_center_play);

        // 设置按钮不可用
        btn_mute.setDisable(true);
        img_volu.setDisable(true);
        volumnControl.setDisable(true);
        btn_rate.setDisable(true);
        mediaDuration.setDisable(true);

        // 设置播放进度的初始时间
        mediaDuration.adjustValue(0.0);

        // 设置音量
        currentVolume = 50;
        volumnControl.adjustValue(currentVolume);

        // 设置播放速率按钮的点击事件
        speedX50.setOnMouseClicked(e -> setPlayRate(speedX50));
        speedX100.setOnMouseClicked(e -> setPlayRate(speedX100));
        speedX125.setOnMouseClicked(e -> setPlayRate(speedX125));
        speedX150.setOnMouseClicked(e -> setPlayRate(speedX150));
        speedX175.setOnMouseClicked(e -> setPlayRate(speedX175));
        speedX200.setOnMouseClicked(e -> setPlayRate(speedX200));


        //  设置点击 mediaView 事件
        playView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    //  鼠标双击事件
                    //  因为单击两次会先计算一次，在计算一次，所以会先暂停，需要在再播放解决双击放大屏幕时媒体文件会暂停
                    if(event.getClickCount() == 2){
                        playOrPause();
                        setFullScreen();
                    }

                    // 鼠标单击事件
                    if(event.getClickCount() == 1){
                        playOrPause();
                    }
                }
            }
        });

        loadNormalVideo();
    }

    public void loadNormalVideo(){
        String startUrl = getClass().getResource("./RESOURCES/VIDEO/normal.mp4").toString();
        Media media = new Media(startUrl);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        playView.setMediaPlayer(mediaPlayer);
        border_pane_media_player.setStyle("-fx-background-color: white;");
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void scrollView(){

        playView.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                volumeShow.setVisible(true);
                border_pane_volumeShow.setVisible(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                        volumeShow.setVisible(false);
                        border_pane_volumeShow.setVisible(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                if(event.getDeltaY() > 0)
                    volumeUp();
                else if (event.getDeltaY() < 0)
                    volumeDown();
            }
        });
    }

    public void volumeUp(){
        volumeShow.setVisible(true);
        scrollView.volumeUp(volumnControl, volumeShow);
    }
    public void volumeDown(){
        scrollView.volumeDown(volumnControl, volumeShow);
    }

    // 媒体播放事件
    private void playMedia(){
        mediaPlayer.stop();

        if(isMusicPlay) {
            musicPlayer.stop();
            isMusicPlay = false;
        }

        String fileExtension = getMediaType.getType(mediaUrl); // 获取文件的后缀名

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
        btn_rate.setDisable(false);
        mediaDuration.setDisable(false);

        // 设置播放速率的显示情况
        playRateHover();
        volumeHover();
        border_pane_media_player.setStyle("-fx-background-color: black;");

        mediaPlayer.setOnReady(() -> mediaPlayerReady() );  // 设置 onready 事件
        mediaPlayer.setOnEndOfMedia(() -> {
            changeBtnPicture.changeBtnPicture("replay", img_play);
            changeBtnPicture.changeBtnPicture("replay", on_screen_center_play);

            border_pane_volumeShow.setVisible(true);
            on_screen_center_play.setVisible(true);

            isToReplay = !isToReplay;
            isPlaying = !isPlaying;
            mediaDuration.setDisable(true);

            if(isMusicPlay)
                musicPlayer.stop();
        });

        mediaPlayer.setRate(currentPlayRate);
        playView.setMediaPlayer(mediaPlayer);   // 播放文件

        mediaName.setText(fileName);    // 显示文件名称

        // 判断文件是音频文件还是视频文件
        if(fileExtension.equals("mp3")){
            String musicUrl = getClass().getResource("./RESOURCES/VIDEO/musicBack.mp4").toString();
            Media musicPlayback = new Media(musicUrl);
            musicPlayer = new MediaPlayer(musicPlayback);
            musicPlayer.setAutoPlay(true);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            playView.setMediaPlayer(musicPlayer);
            isMusicPlay = true;
        } else if(fileExtension.equals("mp4")) {

            System.out.println(fileExtension);
        }
    }

    private void mediaPlayerReady(){

        scrollView();

        // 设置事件条的最大值以及最小值
        mediaDuration.setMin(mediaPlayer.getStartTime().toSeconds());
        mediaDuration.setMax(mediaPlayer.getTotalDuration().toSeconds());

        // 设置 total_time 的时间为视频的总时长
        total_time.setText(timeConvert.secondToTime((int) mediaPlayer.getTotalDuration().toSeconds()));

        mediaDuration.setOnMousePressed(event -> isTrackDuration = false);  // 当用户拖动进度时，取消自动跟踪

        // 当用户放手后，跳转到时间跳转到用户的区域
        mediaDuration.setOnMouseReleased(event -> {
            mediaPlayer.seek(Duration.seconds(mediaDuration.getValue()));
            isTrackDuration = true; // 继续跟踪播放
        });

        // 设置拖动时显示的时间
        mediaDuration.setValueFactory(slider ->
                Bindings.createStringBinding(() ->
                        timeConvert.secondToTime((int) slider.getValue()),
                        slider.valueProperty()
                )
        );

        // 进度跟踪
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if(isTrackDuration)
                    mediaDuration.setValue(newValue.toSeconds());   // 跟踪时间条的进度

                //  当拖动时间栏的时候，可以得到实时的反馈得知当前的时间
                current_time.setText(timeConvert.secondToTime((int) mediaDuration.getValue()));
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
        fileName = file.getName();  // 获取媒体文件的名称

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
            fileName = fileDrop.getName();
            mediaUrl = fileDrop.toURI().toString();
            playMedia();
        }
    }

    public void setMute(){
        btn_play.requestFocus();
        isMuteMode = btnControl.setMute(volumnControl, img_volu, isMuteMode, currentVolume);
    }

    // 设置播放或暂停视频
    public void playOrPause(){

        // 判断是否有 url, 如果没有则调用打开文件函数，让用户选择媒体文件
        if(mediaUrl == null){
            openFile();
            return;
        }

        if(isToReplay) {
            if(isMusicPlay)
                musicPlayer.play();
            changeBtnPicture.changeBtnPicture("pause", on_screen_center_play);
            isPlaying = btnControl.replayAction(mediaPlayer, img_play, isPlaying, mediaDuration);
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    on_screen_center_play.setVisible(true);
                    border_pane_volumeShow.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            isToReplay = false;
        }else {
            if(isMusicPlay)
                if(isPlaying)
                    musicPlayer.pause();
                else
                    musicPlayer.play();
            isPlaying = btnControl.playOrPause(mediaPlayer, isPlaying, img_play, border_pane_volumeShow, on_screen_center_play);
        }
    }

    // 停止播放
    public void mediaStop(){
        btn_play.requestFocus();

        // 若 mediaUrl 为空，则不执行任何语句
        if(mediaUrl == null)
            return;

        if(isMusicPlay) {
            musicPlayer.stop();
            isMusicPlay = false;
        }

        mediaPlayer.stop();
        loadNormalVideo();
        isPlaying = false;
        changeBtnPicture.changeBtnPicture("play", img_play);
        mediaUrl = null;

        total_time.setText("00:00:00");

        btn_mute.setDisable(true);
        img_volu.setDisable(true);
        volumnControl.setDisable(true);
        btn_rate.setDisable(true);
        mediaDuration.setDisable(true);
    }

    // 设置全屏模式
    public void setFullScreen(){
        btn_play.requestFocus();

        Stage mainStage = (Stage) parentPane.getScene().getWindow();    // 获取 Stage 容器
        isFullMode = btnControl.fullScreenMode(isFullMode, mainStage, menu_fullScreen, img_full);
    }

    public void playRateHover(){
        btn_rate.setOnMouseEntered(e -> {
            speed_list.setVisible(true);
        });
        btn_rate.setOnMouseExited(e -> {
            speed_list.setVisible(false);
        });

        speed_list.setOnMouseEntered(e -> {
            speed_list.setVisible(true);
        });
        speed_list.setOnMouseExited(e -> {
            speed_list.setVisible(false);
        });
    }
    public void volumeHover(){
        btn_mute.setOnMouseEntered(e -> {
            volumnControl.setVisible(true);
        });
        btn_mute.setOnMouseExited(e -> {
            volumnControl.setVisible(false);
        });

        volumnControl.setOnMouseEntered(e -> {
            volumnControl.setVisible(true);
        });
        volumnControl.setOnMouseExited(e -> {
            volumnControl.setVisible(false);
        });
    }

    // 设置视频的播放速度
    public void setPlayRate(Label labelId){
        Double speed = Double.valueOf(labelId.getText());   // 获取 label 的速率
        mediaPlayer.setRate(speed); // 设置播放速度
        currentPlayRate = speed;

        speed_list.setVisible(false);   // 将列表隐藏
        btn_rate.setText(labelId.getText());    // 将速率显示在按钮上
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