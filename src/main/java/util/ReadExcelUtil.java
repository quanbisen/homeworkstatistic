package util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**文件名:ReadExcelUtil
 * 说明：主界面的控制器类
 * @author super lollipop
 * @date 2019/8/30
 */
public final class ReadExcelUtil {

    /**读取Excel文件的函数
     * @param excelFile Excel文件的路径
     * @return List<List<String>> 行集合，每一个行也是一个列集合
     * */
    public static List<List<String>> readExcelFile(File excelFile){
        List<List<String>> rowList = null;  //Excel文件行记录集合
        Workbook workbook = ReadExcelUtil.getWorkbook(excelFile);  //获取Excel文件Workbook对象
        if (workbook!=null){
            //实例化记录每一行字符串的集合
            rowList = new ArrayList<>();
            //取第一个工作本
            Sheet sheet = workbook.getSheetAt(0);
            //取行记录数
            int rowCount = sheet.getPhysicalNumberOfRows();
            //取列记录数
            int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();

            for (int i = 1;i<rowCount;i++){   //循环遍历每一行
                //取当前行
                Row row = sheet.getRow(i);
                //实例化记录一行中所有单元格字符串的集合
                List<String> cellList = new ArrayList();

                for (int j = 0;j<columnCount;j++){  //遍历当前行的所有列（单元格）
                    //取出当前单元格的值
                    String cellValue = ReadExcelUtil.getCellFormatValue(row.getCell(j));
                    //添加到cellList中
                    cellList.add(cellValue);
                }
                //把记录一行中所有单元格的cellList添加到行记录集合中
                rowList.add(cellList);
            }
        }
        //返回记录每一行字符串的集合
        return rowList;
    }

    /**获取Excel文件Workbook工作本的函数
     * @param excelFile Excel文件的路径
     * @return workbook Excel文件Workbook工作本
     * */
    private static Workbook getWorkbook(File excelFile){

        if(!excelFile.exists()){  //文件不存在，返回为空
            return null;
        }
        else {  //否则，文件存在执行获取workbook
            //声明workbook
            Workbook workbook = null;
            //获取文件扩展名
            String extendName = excelFile.getAbsolutePath().substring(excelFile.getAbsolutePath().lastIndexOf("."));
            try {
                //创建文件输入流
                InputStream is = new FileInputStream(excelFile);
                if(extendName.equals(".xls")){  //扩展名为.xls的处理
                    workbook = new HSSFWorkbook(is);
                }
                else if(extendName.equals(".xlsx")){  //扩展名为.xlsx的处理
                    workbook = new XSSFWorkbook(is);
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return workbook;
        }
    }
    /**获取Excel单元格字符串的函数
     * @param cell Excel单元格对象
     * @return cellValue Excel单元格的字符串
     * */
    private static String getCellFormatValue(Cell cell){
        String cellValue = null;
        if(cell == null){ //如果cell对象为null，单元格的值置为空字符
            cellValue = "";
        }
        else{  //否则取出单元格的值
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }
        return cellValue;
    }
}
