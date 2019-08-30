package util;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public final class ToastUtil {

    /**业务逻辑的提示函数
     * @param stackPane 窗体的root根容器
     * @param fading 淡出的Label*/

    public static void toastInformation(StackPane stackPane, Label fading){
        fading.getStylesheets().add("css/FadingLabelStyle.css");
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2.5),fading);
        fadeTransition.setFromValue(1);  //不透明度从1变到0,from 1 to 0.
        fadeTransition.setToValue(0);
        stackPane.getChildren().add(fading);
        //动画完成后移除label组件
        fadeTransition.setOnFinished(fade->{
            stackPane.getChildren().remove(fading);
        });
        //开始播放渐变动画提示
        fadeTransition.play();
    }

}
