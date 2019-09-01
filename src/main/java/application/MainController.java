package application;

import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import util.ReadExcelUtil;
import util.ToastUtil;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * 文件名:MainController
 * 说明：主界面的控制器类
 * @author super lollipop
 * @date 2019-8-30
 */
public class MainController implements Initializable {

    /**窗体的根root容器*/
    @FXML
    private StackPane stackPane;
    
    /**显示选择的班级名单的Excel文件路径*/
    @FXML
    private TextField textFieldExcel;
    
    /**显示班级的作业目录路径*/
    @FXML
    private TextField textFieldChoseFolder;
    
    /**显示统计结果的VBox容器*/
    @FXML
    private VBox vBoxResult;
    
    /**声明记录未交作业的人数*/
    private  int memberCount;
    
    /**声明存储学生信息的map
     * @param 学号
     * @param 姓名
     * */
    private Map<String,String> map;

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
                //因为班级名单的文件一般情况下都是同一个，所以保存路径供下一次运行
                //创建JSON对象存储选择的Excel文件路径
                JSONObject object = new JSONObject();
                object.put("ExcelFilePath",openFile.getAbsolutePath());
                //获取当前运行目录
                String path = new File("").getAbsolutePath();
                try {
                    //创建字符文件写入
                    FileWriter fileWriter = new FileWriter(path + File.separator+"config.json");
                    //把JSON对象转换成字符串写入文件
                    fileWriter.write(object.toJSONString());
                    fileWriter.flush();
                    fileWriter.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**选择作业目录的事件处理
     * @param mouseEvent
     * */
    @FXML
    public void onClickedChooseFolder(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){  //鼠标左键单击执行
            //创建目录选择对话框
            DirectoryChooser directoryChooser = new DirectoryChooser();
            //设置标题
            directoryChooser.setTitle("选择班级作业目录");
            //打开目录选择对话框
            File choseFolder = directoryChooser.showDialog(textFieldChoseFolder.getScene().getWindow());
            if (choseFolder!=null){ //如果有选择目录，设置TextFiled组件显示选择的路径。
                textFieldChoseFolder.setText(choseFolder.getAbsolutePath());
            }
        }
    }

    /**统计按钮的事件处理
     * @param mouseEvent
     * */
    @FXML
    public void onClickedStatistic(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){ //鼠标左键单击执行
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
                //读取excel文件
                List<List<String>> rowList = ReadExcelUtil.readExcelFile(openFile);
                //列出选择的目录的所有文件
                String[] files = choseFolder.list();
                //实例化升序的treeMap存放读取的学生名单
                map = new TreeMap<>((o1, o2) -> o1.compareTo(o2));
                for (List<String> columns : rowList){
                    map.put(columns.get(0),columns.get(1));
                }
                //取出map中的学号数据集
                Set<String> keySet = map.keySet();
                //取出文件的学号,fileKeySet是存储的数据集，叫文件学号数据集
                Set<String> fileKeySet = new HashSet<>();
                for (String file:files){
                    //提取文件的学号
                    int beginIndex = 5;
                    String fileKey = file.substring(beginIndex,beginIndex+2);
                    fileKeySet.add(fileKey);
                }
                //遍历数据集进行比对
                for (String key:keySet){
                    if (!fileKeySet.contains(key)){  //如果文件的学号没有包含哪个key，证明这个key的同学没有上交作业
                        //为这个key对应的value插入“未交”标记
                        map.replace(key,map.get(key)+"未交");
                    }
                }
                /*统计逻辑处理完成，下面更新GUI组件作显示*/
                //首先取出提交作业的命名中的专业班级字符串
                String majorClass = files[0].substring(0,5);
                //如果vBoxResult不为空先清空,并且重置memberCount
                if(vBoxResult.getChildren().size()!=0){
                    vBoxResult.getChildren().remove(0,vBoxResult.getChildren().size());
                    memberCount = 0;
                }
                //根据map中的key遍历value，对包含“未交”标签的记录进行GUI显示
                for (String key:keySet){
                    if (map.get(key).contains("未交")){
                        memberCount++;
                        //创建label组件
                        Label label = new Label(majorClass+key+map.get(key));
                        //添加样式名student，详细样式设置在MainStyle.css文件中
                        label.getStyleClass().add("student");
                        //添加到VBox容器
                        vBoxResult.getChildren().add(label);
                    }
                }
                
                //提示显示未交人数
                ToastUtil.toastInformation(stackPane,new Label("未交人数："+ memberCount));
            }
        }
    }

    /**保存结果的事件处理类
     * @param mouseEvent*/
    @FXML
    public void onClickedSaveResult(MouseEvent mouseEvent){
        if (mouseEvent.getButton() == MouseButton.PRIMARY){  //鼠标左键单击执行
            if (map == null){  //统计按钮没按下时提示信息
                ToastUtil.toastInformation(stackPane,new Label("还没有统计结果"));
            }
            else if (memberCount == 0){  //未交作业人数为0，提示不需要保存文件。
                ToastUtil.toastInformation(stackPane,new Label("未交作业人数为0，不需要保存"));
            }
            else {  //否则，执行保存文件操作

                //实例化文件句柄
                File saveFile = new File(textFieldChoseFolder.getText()+File.separator+"未交文件名单.txt");
                try {
                    FileWriter fileWriter = new FileWriter(saveFile);
                    ObservableList<Node> nodes = vBoxResult.getChildren();
                    for (Node node:nodes){
                        fileWriter.write(((Label)node).getText());
                        fileWriter.write("\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                if (saveFile.exists()&&saveFile.length()>0){  //文件存在并且文件的字节大小大于0
                    //提示保存成功
                    ToastUtil.toastInformation(stackPane,new Label("保存到作业目录成功"));
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //获取当前运行目录
        String path = new File("").getAbsolutePath();
        //创建文件句柄
        File propertiesFile = new File(path + File.separator+"config.json");
        if (propertiesFile.exists()){  //如果配置文件存在，读取并解析，最后设置GUI的TextField显示
            //初始化JSONString并使用BufferReader读取
            String jsonString = "";
            try {
                //先创建字符文件读取器
                FileReader fileReader = new FileReader(propertiesFile);
                //使用高级输入流BufferedReader
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                //读取
                String temp = bufferedReader.readLine();
                while (temp!=null){
                    jsonString += temp;
                    temp = bufferedReader.readLine();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            //解析jsonString
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            //设置GUI显示
            textFieldExcel.setText(jsonObject.get("ExcelFilePath").toString());
            //设置组件聚焦在输入作业路径的TextField组件
            Platform.runLater(()->{
                textFieldChoseFolder.requestFocus();
            });
        }
    }
}
