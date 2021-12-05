package com.MediaPlayer.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class about_Main {
    @FXML
    WebView webView;

    @FXML
    ImageView github_img;
    @FXML
    ImageView gitee_img;
    @FXML
    ImageView openInBrowser_img;
    @FXML
    ImageView language_img;

    @FXML
    JFXButton github_btn;
    @FXML
    JFXButton gitee_btn;
    @FXML
    JFXButton openInBrowser_btn;
    @FXML
    JFXButton language_btn;

    @FXML
    AnchorPane progress;
    @FXML
    AnchorPane progressBack;
    @FXML
    AnchorPane functionBar;
    @FXML
    AnchorPane parentPane;

    @FXML
    JFXButton okay_btn;

    changeButtonPicture changeButtonPicture = new changeButtonPicture();

    public void loading(){

        loadPage("https://seechen.github.io/lumacaMediaPlayer/aboutPage/about-en.html");

        changeButtonPicture.changeBtnPicture("github", github_img);
        changeButtonPicture.changeBtnPicture("gitee", gitee_img);
        changeButtonPicture.changeBtnPicture("webBrowser", openInBrowser_img);
        changeButtonPicture.changeBtnPicture("rate", language_img);

        setTooltip("View Project in Github", github_btn);
        setTooltip("View Project in Gitee", gitee_btn);
        setTooltip("Open this page in Default Browser", openInBrowser_btn);
        setTooltip("中文", language_btn);

        github_btn.setOnAction(e -> goTo("https://github.com/SeeChen/TermProject_MediaPlayer"));
        gitee_btn.setOnAction(e -> goTo("https://gitee.com/SeeChenLee/TermProject_MediaPlayer"));
        openInBrowser_btn.setOnAction(e -> {
            String url = webView.getEngine().getLocation();
            goTo(url);
        });
        language_btn.setOnAction(e -> {
            String[] text = new String[3];
            if(language_btn.getText().equals("中")){
                text[0] = "EN";
                text[1] = "English";
                text[2] = "https://seechen.github.io/lumacaMediaPlayer/aboutPage/about-zh.html";
            }else{
                text[0] = "中";
                text[1] = "中文";
                text[2] = "https://seechen.github.io/lumacaMediaPlayer/aboutPage/about-en.html";
            }
            language_btn.setText(text[0]);
            setTooltip(text[1], language_btn);
            loadPage(text[2]);
        });
    }

    private void setTooltip(String tips, JFXButton btnId){
        Tooltip tooltip = new Tooltip();
        tooltip.setText(tips);
        tooltip.setStyle("-fx-font-size: 12pt; -fx-background-color: #85DDFF; -fx-text-fill: white;");
        btnId.setTooltip(tooltip);
    }

    private void loadPage(String webUrl){
        webView.setVisible(false);
        progress.setVisible(true);
        progressBack.setVisible(true);

        functionBar.setVisible(false);
        okay_btn.setVisible(false);

        new Thread(() -> Platform.runLater(() -> {
            webView.getEngine().load(webUrl);

            webView.getEngine().getLoadWorker().progressProperty().addListener((observable, oldValue, newValue) -> {
                double num = newValue.doubleValue();
                progress.setPrefWidth(num * 343);
            });

            webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue == Worker.State.SUCCEEDED){
                    new Thread(() -> {
                        try {
                            Thread.sleep(500);
                            webView.setVisible(true);
                            progress.setVisible(false);
                            progressBack.setVisible(false);

                            functionBar.setVisible(true);
                            okay_btn.setVisible(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            });
        })).start();
    }

    private void goTo(String url){
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        Stage stage = (Stage) parentPane.getScene().getWindow();
        stage.close();
    }
}
