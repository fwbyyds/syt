package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;


import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    //科室接口
    @Autowired
    private DepartmentService departmentService;

    //排版接口
    @Autowired
    private ScheduleService scheduleService;
    //上传排班
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String,String[]> requestMap= request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //1.获取医院系统传递过来的签名,签名已经进行MD5加密
        String sign =(String) paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode =(String) paramMap.get("hoscode");
        String signkey=hospitalSetService.getSignkey(hoscode);
        //将从数据库得到的key进行MD5加密
        String md5Signkey = MD5.encrypt(signkey);
        //判断签名是否一致
        if(!sign.equals(md5Signkey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(paramMap);
        return Result.ok();
    }

    //查询排版接口
    @PostMapping("schedule/list")
    public Result getSchedule(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String,String[]> requestMap= request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //医院编号
        String hoscode =(String) paramMap.get("hoscode");
        //科室编号
        String depcode =(String) paramMap.get("depcode");
        //当前页和每页记录数
        int page=StringUtils.isEmpty(paramMap.get("page")) ? 1: Integer.parseInt((String) paramMap.get("page"));
        int limit=StringUtils.isEmpty(paramMap.get("page")) ? 1: Integer.parseInt((String) paramMap.get("limit"));
        //签名校验
        //1.获取医院系统传递过来的签名,签名已经进行MD5加密
        String sign =(String) paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signkey=hospitalSetService.getSignkey(hoscode);
        //将从数据库得到的key进行MD5加密
        String md5Signkey = MD5.encrypt(signkey);
        //判断签名是否一致
        if(!sign.equals(md5Signkey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        ScheduleOrderVo scheduleOrdervo=new ScheduleOrderVo();
        scheduleOrdervo.setHoscode(hoscode);
        scheduleOrdervo.setDepcode(depcode);
        //调用service方法
        Page<Schedule> pageModel=scheduleService.findPageDepartment(page,limit,scheduleOrdervo);
        return Result.ok(pageModel);
    }
    //删除排版接口
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String,String[]> requestMap= request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //根据传递过来的医院编码，查询数据库，查询签名
        String hoscode =(String) paramMap.get("hoscode");
        String hosScheduleId =(String) paramMap.get("hosScheduleId");
        //1.获取医院系统传递过来的签名,签名已经进行MD5加密
        String sign =(String) paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signkey=hospitalSetService.getSignkey(hoscode);
        //将从数据库得到的key进行MD5加密
        String md5Signkey = MD5.encrypt(signkey);
        //判断签名是否一致
        if(!sign.equals(md5Signkey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }
    //删除科室接口
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String,String[]> requestMap= request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //根据传递过来的医院编码，查询数据库，查询签名
        String hoscode =(String) paramMap.get("hoscode");
        String depcode =(String) paramMap.get("depcode");
        //1.获取医院系统传递过来的签名,签名已经进行MD5加密
        String sign =(String) paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名

        String signkey=hospitalSetService.getSignkey(hoscode);
        //将从数据库得到的key进行MD5加密
        String md5Signkey = MD5.encrypt(signkey);
        //判断签名是否一致
        if(!sign.equals(md5Signkey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }

    //上传科室
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String,String[]> requestMap= request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //1.获取医院系统传递过来的签名,签名已经进行MD5加密
        String sign =(String) paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode =(String) paramMap.get("hoscode");
        String signkey=hospitalSetService.getSignkey(hoscode);
        //将从数据库得到的key进行MD5加密
        String md5Signkey = MD5.encrypt(signkey);
        //判断签名是否一致
        if(!sign.equals(md5Signkey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //调用service的方法
        departmentService.save(paramMap);
        return Result.ok();
    }
    //查询科室
    @PostMapping("department/list")
    public Result getDepartment(HttpServletRequest request){
        //获取医院传递过来的科室信息
        Map<String,String[]> requestMap= request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //医院编号
        String hoscode =(String) paramMap.get("hoscode");
        //当前页和每页记录数
        int page=StringUtils.isEmpty(paramMap.get("page")) ? 1: Integer.parseInt((String) paramMap.get("page"));
        int limit=StringUtils.isEmpty(paramMap.get("page")) ? 1: Integer.parseInt((String) paramMap.get("limit"));
        //签名校验
        //1.获取医院系统传递过来的签名,签名已经进行MD5加密
        String sign =(String) paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signkey=hospitalSetService.getSignkey(hoscode);
        //将从数据库得到的key进行MD5加密
        String md5Signkey = MD5.encrypt(signkey);
        //判断签名是否一致
        if(!sign.equals(md5Signkey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        DepartmentQueryVo departmentQueryVo=new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用service方法
        Page<Department> pageModel=departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }
    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String,String[]> requestMap= request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //1.获取医院系统传递过来的签名,签名已经进行MD5加密
        String sign =(String) paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode =(String) paramMap.get("hoscode");
        String signkey=hospitalSetService.getSignkey(hoscode);
        //将从数据库得到的key进行MD5加密
        String md5Signkey = MD5.encrypt(signkey);
        //判断签名是否一致
        if(!sign.equals(md5Signkey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //传输过程中“+”转换成“ ”，因此我们要转换回来
        String logoData=(String) paramMap.get("logoData");
        String s = logoData.replaceAll(" ", "+");
        paramMap.put("logoData",s);
        //调用service方法
        hospitalService.save(paramMap);
        return  Result.ok();
    }
    //查询医院
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String,String[]> requestMap= request.getParameterMap();
        Map<String,Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //1.获取医院系统传递过来的签名,签名已经进行MD5加密
        String sign =(String) paramMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode =(String) paramMap.get("hoscode");
        String signkey=hospitalSetService.getSignkey(hoscode);
        //3 将从数据库得到的key进行MD5加密
        String md5Signkey = MD5.encrypt(signkey);
        //4 判断签名是否一致
        if(!sign.equals(md5Signkey)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        //调用service方法实现根据医院编号查询
        Hospital hospital=hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

}
