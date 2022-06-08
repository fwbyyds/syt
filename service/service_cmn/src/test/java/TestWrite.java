import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {
        List<UserData> list=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UserData userData=new UserData();
            userData.setId(1);
            userData.setUsername("fwb");
            userData.setNick("无敌");
            list.add(userData);
        }
        EasyExcel.write("D:\\test\\02.xlsx",UserData.class).sheet("我的信息").doWrite(list);

    }
}
