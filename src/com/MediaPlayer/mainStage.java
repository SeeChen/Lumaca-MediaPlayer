package com.MediaPlayer;

import com.MediaPlayer.Controller.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class mainStage {

    // 布尔值类型定义 true 表示为 是  false 表示为否
    boolean isPlaying       = false;    // 判断当前是否有媒体文件正在播放
    boolean isMusicPlay     = false;    // 判断播放的文件是否为音频文件

    boolean isToReplay      = false;    // 判断按钮是否为 replay 按钮
    boolean isMuteMode      = false;    // 判断当前是否静音
    boolean isFullMode      = false;    // 判断当前是否为全屏

    boolean isTrackDuration = true;     // 判断是否需要继续追踪播放进度 若为 否 表示当前用户正在拖动进度条

    boolean isContextMenu   = false;    // 判断右键菜单是否为显示状态

    boolean isPopUp         = false;    // 判断当前是否为 Pop up 模式

    // 浮点型定义
    double currentVolume;               // 用于保存静音前的音量状态
    double currentPlayRate = 1.0;       // 用于记录当前的播放速度

    // 字符型定义
    String fileName;                    // 用于保存文件的名字
    String mediaUrl = null ;            // 用于存储媒体文件的路径

    Stage stage;

    MediaPlayer mediaPlayer;            // 用于播放所有的媒体文件
    MediaPlayer musicPlayer;            // 当媒体文件为音频文件时播放特定视频

    ContextMenu contextMenu;            // 右键菜单

    // FXML 定义
    @FXML
    AnchorPane parentPane;

        // 顶部菜单栏元素
        @FXML
        MenuBar menu_bar;
        //  View 子菜单
            @FXML
            MenuItem menu_fullScreen;
            @FXML
            MenuItem menu_popUp;
            @FXML
            private Label mediaName;

        // 播放器元素
        @FXML
        private BorderPane border_pane_media_player;
            @FXML
            MediaView playView;

        @FXML
        private BorderPane border_pane_volumeShow;
            @FXML
            private Label volumeShow;
            @FXML
            private ImageView on_screen_center_play;

        // 控制栏元素
        @FXML
        AnchorPane control_bar;
            @FXML
            private JFXButton btn_play;
            @FXML
            private JFXButton btn_stop;
            @FXML
            private JFXButton btn_full;

            @FXML
            private JFXSlider mediaDuration;
                @FXML
                private Label current_time;
                @FXML
                private Label total_time;

            @FXML
            private JFXButton btn_mute;
                @FXML
                private JFXSlider volumnControl;

            @FXML
            private JFXButton btn_rate;
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

        // 按钮的图片
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

    changeButtonPicture changeBtnPicture = new changeButtonPicture();
    getMediaType getMediaType = new getMediaType();
    btnControl btnControl = new btnControl();
    timeConvert timeConvert = new timeConvert();
    scrollView scrollView = new scrollView();
    loadAnother loadAnother = new loadAnother();

    // ** Application Loading Event Start ** //
    // 创建应用的右键菜单
    private void createContextMenu(){
        contextMenu = new ContextMenu();    // 创建右键菜单

        // 设置菜单的文字
        MenuItem menuItem_Open       = new MenuItem("Open");
        MenuItem menuItem_FullScreen = new MenuItem("Full Screen Mode");
        MenuItem menuItem_PopUp      = new MenuItem("Pop Up");
        Menu menu_Help               = new Menu("Help");
            MenuItem menuItem2_About    = new MenuItem("About");
            MenuItem menuItem2_Use      = new MenuItem("How to Use");
        Menu menu_Settings           = new Menu("Settings");
            MenuItem menuItem2_Theme    = new MenuItem("Theme");
        MenuItem menuItem_Exit       = new MenuItem("Exit");

        // 设置菜单的快捷键
        menuItem_Open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        menuItem_FullScreen.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        menuItem_PopUp.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN));
        menuItem_Exit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));

        menuItem2_About.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
        menuItem2_Use.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        menuItem2_Theme.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));

        menu_Help.getItems().addAll(menuItem2_About, menuItem2_Use);
        menu_Settings.getItems().addAll(menuItem2_Theme);

        contextMenu.getItems().addAll(menuItem_Open, menuItem_FullScreen, menuItem_PopUp, menu_Settings, menu_Help, menuItem_Exit);

        // 添加分割线
        SeparatorMenuItem br1 = new SeparatorMenuItem();
        SeparatorMenuItem br2 = new SeparatorMenuItem();
        contextMenu.getItems().add(3, br1);
        contextMenu.getItems().add(6, br2);

        // 设置右键菜单的点击事件
        menuItem_Open.setOnAction(e -> openFile());
        menuItem_FullScreen.setOnAction(e -> setFullScreen());
        menuItem_PopUp.setOnAction(e -> setPopUp());

        menuItem2_About.setOnAction(e -> {
            try {
                openAbout(new ActionEvent());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        menuItem2_Use.setOnAction(e -> openHowToUse());
        menuItem2_Theme.setOnAction(e -> openTheme());

        menuItem_Exit.setOnAction(e -> closeApp());
    }
    // 设置播放器点击事件
    public void playViewClickEvent(){
        playView.setOnMouseClicked(e -> {
            // 左键点击事件
            if(e.getButton().equals(MouseButton.PRIMARY)) {
                // 如果右键菜单任然在显示
                if (isContextMenu) {
                    contextMenu.hide();
                    isContextMenu = false;
                    return;
                }

                // 单击事件
                if(e.getClickCount() == 1){
                    if(!Objects.equals(mediaUrl, null))
                        playOrPause();
                }else{
                    // 双击事件
                    if(!Objects.equals(mediaUrl, null))
                        playOrPause();
                    setFullScreen();
                }
            }

            // 右键点击事件
            if(e.getButton().equals(MouseButton.SECONDARY)){
                contextMenu.show(parentPane, e.getScreenX(), e.getScreenY());
                isContextMenu = true;
            }
        });
    }
    // 加载初始界面
    public void loadNormalVideo(){
        String startUrl = Objects.requireNonNull(getClass().getResource("./RESOURCES/VIDEO/normal.mp4")).toString();    // 设置路径

        Media media = new Media(startUrl);
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setAutoPlay(true);  // 设置自动播放
        playView.setMediaPlayer(mediaPlayer);
        border_pane_media_player.setStyle("-fx-background-color: black;");
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);  // 设置无限循环
    }

    private void setConfiguration(){
        try{
            Class.forName("org.sqlite.JDBC");

            Connection connection = DriverManager.getConnection("jdbc:sqlite:./Configuration.db");
            Statement statement = connection.createStatement();

            ResultSet color = statement.executeQuery("select * from color");

            while(color.next()){
                String backgroundStyle = "-fx-background-color: rgba(" + color.getInt("red") + "," + color.getInt("green") + "," + color.getInt("blue") + ", 0.5);";
                menu_bar.setStyle(backgroundStyle);
                control_bar.setStyle(backgroundStyle);
                contextMenu.setStyle(backgroundStyle);
                speed_list.setStyle(backgroundStyle);
                volumnControl.setStyle(backgroundStyle);
            }

            ResultSet volume = statement.executeQuery("select * from volume");
            while(volume.next()){
                currentVolume = volume.getInt("value");
            }

            connection.close();
            statement.close();
            color.close();
            volume.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            // 查不到数据库存在 表示用户是第一次使用
            // 对于第一次使用的用户 打开使用教程
            openHowToUse();
            try{
                // 配置初始化的文件
                Connection connection = DriverManager.getConnection("jdbc:sqlite:./Configuration.db");

                Statement statement = connection.createStatement();

                statement.execute("create table color(color text, red integer, green integer, blue integer)");
                statement.executeUpdate("insert into color(color, red, green, blue) values ('color', 59, 200, 255)");

                // 由于没有原先的数据 所以设置背景
                volumnControl.setStyle("-fx-background-color: rgba(59, 200, 255, 0.5);");
                menu_bar.setStyle("-fx-background-color: rgba(59, 200, 255, 0.5);");
                control_bar.setStyle("-fx-background-color: rgba(59, 200, 255, 0.5);");
                contextMenu.setStyle("-fx-background-color: rgba(59, 200, 255, 0.5);");
                speed_list.setStyle("-fx-background-color: rgba(59, 200, 255, 0.5);");

                // 配置音量
                statement.execute("create table volume(volume text, value integer)");
                statement.executeUpdate("insert into volume(volume, value) values ('volume', 50)");
                currentVolume = 50;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 应用加载事件
    public void loading(){


        createContextMenu();    // 创建右键菜单
        setConfiguration(); // 设置应用的颜色

        // 初始化按钮图片
        String voluImg = "volu";
        if(currentVolume < 50)
            voluImg = "midVol";
        String[] imageName = {"play", "stop", "full", voluImg, "rate", "play"};
        ImageView[] imageId = {img_play, img_stop, img_full, img_volu, img_rate, on_screen_center_play};
        changeBtnPicture.setImage(imageName, imageId);

        // 设置按钮不可用
        btn_mute.setDisable(true);
        btn_rate.setDisable(true);
        img_volu.setDisable(true);
            volumnControl.setDisable(true);
        mediaDuration.setDisable(true);

        // 设置播放速率按钮的点击事件
        Label[] speedId = {speedX50, speedX100, speedX125, speedX150, speedX175, speedX200};
        for(int i = 0; i < speedId.length; i++) {
            int finalI = i;
            speedId[i].setOnMouseClicked(e -> setPlayRate(speedId[finalI]));
        }

        // 设置音量
        volumnControl.adjustValue(currentVolume);

        // 设置播放进度的初始时间
        mediaDuration.adjustValue(0.0);

        playViewClickEvent();   // 设置播放器点击事件

        loadNormalVideo();  // 设置初始显示界面
    }
    // ** Application Loading Event End ** //

    // ** Media Player Start ** //
    // 设置鼠标滚轮滚动调整音量
    public void scrollView(){
        playView.setOnScroll(event -> {

            // 设置屏幕音量显示
            volumeShow.setVisible(true);
            border_pane_volumeShow.setVisible(true);

            if(event.getDeltaY() > 0)   // 当向上滚动
                volumeUp();
            else if (event.getDeltaY() < 0) // 当向下滚动
                volumeDown();

            // 设置隐藏控件
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    volumeShow.setVisible(false);
                    border_pane_volumeShow.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
    // ** Media Player End ** //

    // ** Control Event Start ** //
    // 设置播放或暂停视频
    public void playOrPause(){

        // 判断路径是否为空 若为空 则打开文件
        if(mediaUrl == null){
            openFile();
            return;
        }

        // 判断按钮是否为重播状态
        if(isToReplay) {
            if(isMusicPlay) musicPlayer.play(); // 判断是否为音乐文件
            changeBtnPicture.changeBtnPicture("pause", on_screen_center_play);  // 更改第一个按钮的图片为重播样式
            isPlaying = btnControl.replayAction(mediaPlayer, img_play, isPlaying, mediaDuration);   // 重新播放
            newThread(500, on_screen_center_play, border_pane_volumeShow);  // 新建线程 半秒后隐藏播放器中心的图标
            isToReplay = false; // 将是否为重播更改为 false
        } else {
            // 判断是否为音频文件
            if(isMusicPlay)
                if(isPlaying)
                    musicPlayer.pause();
                else
                    musicPlayer.play();

            // 更改播放状态
            isPlaying = btnControl.playOrPause(mediaPlayer, isPlaying, img_play, border_pane_volumeShow, on_screen_center_play, mediaName);
        }
    }
    // 设置停止播放
    public void mediaStop(){

        btn_play.requestFocus();    // 点按此按钮后 将焦点聚回 播放 按钮 以达到空格键播放和暂停

        // 若 mediaUrl 为空 则不执行任何语句
        if(mediaUrl == null)
            return;

        mediaName.setText("");  // 将显示当前播放的 Label 清空

        // 判断是否为音频文件
        if(isMusicPlay) {
            musicPlayer.stop();
            isMusicPlay = false;
        }

        mediaPlayer.stop(); // 停止播放
        isPlaying = false;  // 更改状态
        changeBtnPicture.changeBtnPicture("play", img_play);    // 更改图标
        mediaUrl = null;    // 设置路径
        total_time.setText("00:00:00"); // 设置总时长为 0

        // 将控制按钮设为不可用
        btn_mute.setDisable(true);
        img_volu.setDisable(true);
        volumnControl.setDisable(true);
        btn_rate.setDisable(true);
        mediaDuration.setDisable(true);

        loadNormalVideo();  // 加载待机动画
    }
    // ** Control Event End ** //

    public void volumeUp(){
        scrollView.volumeUp(volumnControl, volumeShow);
    }
    public void volumeDown(){
        volumeShow.setVisible(true);
        border_pane_volumeShow.setVisible(true);
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

        mediaPlayer.setOnReady(this::mediaPlayerReady);  // 设置 onready 事件
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

        fileName = fileName.replace("." + fileExtension, "");
        if(fileName.length() > 10)
            fileName = fileName.substring(0, 9) + "...";
        mediaName.setText("Now Playing:  " + fileName);    // 显示文件名称

        // 判断文件是音频文件还是视频文件
        if(fileExtension.equals("mp3")){
            String musicUrl = Objects.requireNonNull(getClass().getResource("./RESOURCES/VIDEO/musicBack.mp4")).toString();
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

        // 如果用户未选择文件
        if(file == null)
            return;

        fileName = file.getName();  // 获取媒体文件的名称
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

    // 设置全屏模式
    public void setFullScreen(){
        btn_play.requestFocus();

        Stage mainStage = (Stage) parentPane.getScene().getWindow();    // 获取 Stage 容器
        isFullMode = btnControl.fullScreenMode(isFullMode, mainStage, menu_fullScreen, img_full);

        // playView.setOnMouseMoved(e -> System.out.println(10));
    }

    public void setPopUp(){
        Stage mainStage = (Stage) parentPane.getScene().getWindow();    // 获取 Stage 容器
        if(isPopUp){
            mainStage.setAlwaysOnTop(false);
            menu_popUp.setText("Pop Up");
        }else{
            isPopUp = true;
            menu_popUp.setText("Exit Pop Up");
            mainStage.setHeight(480);
            mainStage.setWidth(720);
            mainStage.setAlwaysOnTop(true);
        }
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

    public void mouseIdle(Scene scene){
        if(!isPlaying)
            return;
        scene.setCursor(Cursor.NONE);
        menu_bar.setVisible(false);
        control_bar.setVisible(false);
        mediaName.setVisible(false);

    }

    public void mouseNotIdle(Scene scene){
        scene.setCursor(Cursor.DEFAULT);
        menu_bar.setVisible(true);
        control_bar.setVisible(true);
        mediaName.setVisible(true);
    }

    private void newThread(int Time, ImageView imgView, BorderPane borderPane){
        new Thread(() -> {
            try {
                Thread.sleep(Time);
                imgView.setVisible(false);
                borderPane.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 关闭程序
    public void closeApp(){
        if(mediaUrl != null) {
            playOrPause();
        }


        try{
            Class.forName("org.sqlite.JDBC");

            Connection connection = DriverManager.getConnection("jdbc:sqlite:./Configuration.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("update volume set value=" + currentVolume + " where volume = 'volume'");

            connection.close();
            statement.close();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("Close");
        Platform.exit();
    }

    // 播放器绑定调整宽高
    public void mediaViewBind(Scene mediaViewScene){

        playView.fitHeightProperty().bind(mediaViewScene.heightProperty());   // 绑定播放器高度
        playView.fitWidthProperty().bind(mediaViewScene.widthProperty());   // 绑定播放器宽度
    }

    public void mediaPlayerVol(){

        mediaPlayer.volumeProperty().bind(volumnControl.valueProperty().divide(100));
    }

    public void openAbout(ActionEvent actionEvent) throws IOException {
        if(mediaUrl != null)
            playOrPause();
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    loadAnother.about(actionEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        thread.start();
    }

    public void openTheme(){
        if(mediaUrl != null)
            playOrPause();
        try {
            loadAnother.themePick();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openHowToUse(){
        if(mediaUrl != null){
            playOrPause();
        }
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    loadAnother.howToUse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        thread.start();
    }
}