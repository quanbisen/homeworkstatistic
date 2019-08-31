package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 文件名:Main
 * 说明：程序的入口类
 * @author super lollipop
 * @date 2019-8-30
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //装载布局
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        //设置窗体标题
        primaryStage.setTitle("HomeworkStatistic");
        //设置窗体的图标
        primaryStage.getIcons().add(new Image("/image/ApplicationIcon.png"));
        //设置舞台场景
        primaryStage.setScene(new Scene(root));
        //主舞台显示
        primaryStage.show();

        // 获取屏幕可视化的宽高（Except TaskBar），把窗体设置在可视化的区域中间
        primaryStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - primaryStage.getWidth()) / 2.0);
        primaryStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - primaryStage.getHeight()) / 2.0);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
