// 用于更改控制按钮的图标
package com.MediaPlayer.Controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class changeButtonPicture {

    //     更改按钮的图片
    public void changeBtnPicture(String imageName, ImageView btnId){
        String path = System.getProperty("user.dir")+ "\\RESOURCES\\ICON\\";
        String toChangeUrl = path + imageName + ".png";    // 获取路径

        // 将路径转换为文字
        File fileChange = new File(toChangeUrl);
        String stringChange = fileChange.toURI().toString();

        // 设置按钮的图像
        Image imageChange = new Image(stringChange);
        btnId.setImage(imageChange);
    }

    public void setImage(String[] imgName, ImageView[] imgId){
        for(int i = 0; i < imgName.length; i++)
            changeBtnPicture(imgName[i], imgId[i]);
    }
}
