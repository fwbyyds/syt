import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
        //调用方法实现读取操作
        EasyExcel.read("D:\\test\\02.xlsx",UserData.class,new ExcelListener()).sheet().doRead();
    }
}
