package com.MediaPlayer.Controller.theme;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;

public class theme_Main {
    @FXML
    AnchorPane parentPane;

    @FXML
    AnchorPane topBar;
        @FXML
        Label theme_title;
        @FXML
        JFXButton btn_ok;
        @FXML
        JFXButton btn_cancel;

    @FXML
    JFXButton btn_FF3B3B;
    @FXML
    JFXButton btn_FFB43B;
    @FXML
    JFXButton btn_F7FF3B;
    @FXML
    JFXButton btn_80FF3B;
    @FXML
    JFXButton btn_3BFF8A;
    @FXML
    JFXButton btn_3BEFFF;
    @FXML
    JFXButton btn_3BC8FF;
    @FXML
    JFXButton btn_483BFF;
    @FXML
    JFXButton btn_A43BFF;
    @FXML
    JFXButton btn_FA3BFF;
    @FXML
    JFXButton btn_FF3B91;
    @FXML
    JFXButton btn_000000;

    int r, g, b;

    public void loading(){
        try{
            Class.forName("org.sqlite.JDBC");

            Connection connection = DriverManager.getConnection("jdbc:sqlite:./Configuration.db");
            Statement statement = connection.createStatement();

            ResultSet color = statement.executeQuery("select * from color");

            String backgroundStyle = "-fx-background-color: rgba(" + color.getInt("red") + "," + color.getInt("green") + "," + color.getInt("blue") + ", 0.8);";
            topBar.setStyle(backgroundStyle);

            r = color.getInt("red");
            g = color.getInt("green");
            b = color.getInt("blue");

            connection.close();
            statement.close();
            color.close();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        btn_FF3B3B.setOnAction(e -> colorButtonClick(btn_FF3B3B));
        btn_FFB43B.setOnAction(e -> colorButtonClick(btn_FFB43B));
        btn_F7FF3B.setOnAction(e -> colorButtonClick(btn_F7FF3B));
        btn_80FF3B.setOnAction(e -> colorButtonClick(btn_80FF3B));
        btn_3BFF8A.setOnAction(e -> colorButtonClick(btn_3BFF8A));
        btn_3BEFFF.setOnAction(e -> colorButtonClick(btn_3BEFFF));
        btn_3BC8FF.setOnAction(e -> colorButtonClick(btn_3BC8FF));
        btn_483BFF.setOnAction(e -> colorButtonClick(btn_483BFF));
        btn_A43BFF.setOnAction(e -> colorButtonClick(btn_A43BFF));
        btn_FA3BFF.setOnAction(e -> colorButtonClick(btn_FA3BFF));
        btn_FF3B91.setOnAction(e -> colorButtonClick(btn_FF3B91));
        btn_000000.setOnAction(e -> colorButtonClick(btn_000000));

        btn_ok.setOnAction(e -> updateTheme());
        btn_cancel.setOnAction(e -> closeStage());
    }

    private void colorButtonClick(JFXButton button){
        String buttonColor = button.getText();
        String rgb = buttonColor.replace("#", "");
        int[] ret = new int[3];
        for (int i = 0; i < 3; i++) {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        r = ret[0]; g = ret[1]; b = ret[2];
        topBar.setStyle("-fx-background-color: rgba(" + r + "," + g + "," + b + ", 0.8)");
    }

    private void updateTheme(){

        String Message = "You have changed the theme color and need to restart the application to take effect. ( Need self restart! )";
        Alert alert = new Alert(Alert.AlertType.WARNING, Message, ButtonType.OK);
        alert.showAndWait();
        try{
            Class.forName("org.sqlite.JDBC");

            Connection connection = DriverManager.getConnection("jdbc:sqlite:./Configuration.db");
            Statement statement = connection.createStatement();

            statement.executeUpdate("update color set red=" + r + ",green=" + g + ",blue=" + b + " where color='color'");

            connection.close();
            statement.close();

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        closeStage();
    }

    private void closeStage(){
        Stage colorStage = (Stage) parentPane.getScene().getWindow();
        colorStage.close();
    }
}
