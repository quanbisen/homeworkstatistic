package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.setTitle("HomeworkStatistic");
        primaryStage.getIcons().add(new Image("/image/ApplicationIcon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // 获取屏幕可视化的宽高（Except TaskBar），把窗体设置在可视化的区域中间
        primaryStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - primaryStage.getWidth()) / 2.0);
        primaryStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - primaryStage.getHeight()) / 2.0);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
