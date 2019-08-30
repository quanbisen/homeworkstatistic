package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import util.ToastUtil;

import java.io.File;

public class MainController {

    @FXML
    private StackPane stackPane;     //窗体的根root容器
    @FXML
    private TextField textFieldExcel;  //显示选择的班级名单的Excel文件路径
    /*    @FXML
        private Label labelChooseFile;     //选择Excel文件的Label*/
    @FXML
    private TextField textFieldChoseFolder;  //显示班级的作业目录路径
/*    @FXML
    private Label labelChooseFolder;     //选择班级的作业目录的Label*/

    /**选择Excel文件的事件处理
     * @param mouseEvent
     * */
    @FXML
    public void onClickedChooseFile(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){  //鼠标左键单击执行
            FileChooser fileChooser = new FileChooser();  //创建文件选择对话框
            fileChooser.setTitle("选择班级Excel文件");  //设置标题
            //设置文件选择筛选，只能是".xlsx"和"*.xls"文件。
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel文件","*.xlsx","*.xls"));
            File openFile = fileChooser.showOpenDialog(textFieldExcel.getScene().getWindow());
            if (openFile!=null){  //如果有选择文件，设置TextFiled组件显示选择的路径。
                textFieldExcel.setText(openFile.getAbsolutePath());
            }
        }
    }

    /**选择作业目录的事件处理
     * @param mouseEvent*/
    @FXML
    public void onClickedChooseFolder(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){  //鼠标左键单击执行
            DirectoryChooser directoryChooser = new DirectoryChooser(); //创建目录选择对话框
            directoryChooser.setTitle("选择班级作业目录");  //设置标题
            File choseFolder = directoryChooser.showDialog(textFieldChoseFolder.getScene().getWindow());
            if (choseFolder!=null){ //如果有选择目录，设置TextFiled组件显示选择的路径。
                textFieldChoseFolder.setText(choseFolder.getAbsolutePath());
            }
        }
    }

    /**统计按钮的事件处理
     * @param mouseEvent*/
    @FXML
    public void onClickedStatistic(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            //取出选择的文件和目录
            File openFile = new File(textFieldExcel.getText());
            File choseFolder = new File(textFieldChoseFolder.getText());
            //判断选择的文件目录是否存在，不存在提示
            if (!openFile.exists()){
                ToastUtil.toastInformation(stackPane,new Label("选择班级Excel文件"));
            }
            else if (!choseFolder.exists()){
                ToastUtil.toastInformation(stackPane,new Label("请选择班级作业目录"));
            }
            else {  //开始执行统计操作

            }
        }
    }
}
