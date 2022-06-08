import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserData {
    @ExcelProperty(value = "用户编号",index = 0)
    private int id;
    @ExcelProperty(value ="用户名称",index = 1)
    private  String username;
    @ExcelProperty(value ="用户昵称",index = 2)
    private String nick;
}
