package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;
    //1.查询医院设置表所有信息
    @GetMapping("findAll")
    @ApiOperation("获取所有医院设置")
    public Result list(){
        List<HospitalSet> list = hospitalSetService.list();

        return Result.ok(list);
    }
    //2.根据id删除医院设置
    @DeleteMapping("{id}")
    @ApiOperation("逻辑删除医院设置")
    public Result removeHospSet(@PathVariable Long id){
        boolean b = hospitalSetService.removeById(id);
        if(b){
            return Result.ok();
        }
        else {
            return Result.fail();
        }

    }
    //3.条件查询带分页
    @ApiOperation("条件查询带分页")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> hospitalSetPage=new Page<>(current,limit);
        QueryWrapper<HospitalSet> queryWrapper=new QueryWrapper<>();
        String hoscode = hospitalSetQueryVo.getHoscode();//医院编号
        String hosname = hospitalSetQueryVo.getHosname();//医院名称
        if(!StringUtils.isEmpty(hoscode)){
           queryWrapper.eq("hoscode",hoscode);
        }
        if(!StringUtils.isEmpty(hosname)){
            queryWrapper.like("hosname",hosname);
        }
        Page<HospitalSet> page = hospitalSetService.page(hospitalSetPage, queryWrapper);
        return Result.ok(page);
    }

    //4.添加医院设置
    @PostMapping("saveHospitalSet")
    @ApiOperation("添加医院设置")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        //设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random=new Random();
        String key= MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000));
        hospitalSet.setSignKey(key);
        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        }
        else {
            return Result.fail();
        }
    }
    //5.根据id获取医院设置
    @ApiOperation("根据id获取医院设置")
    @PostMapping("getHospitalById/{id}")
    public Result getHospitalById(@PathVariable Long id){

        HospitalSet byId = hospitalSetService.getById(id);
        return Result.ok(byId);

    }
    //6.修改医院设置
    @ApiOperation("修改医院设置")
    @PostMapping("updateHospitalById")
    public Result updateHospitalById(@RequestBody HospitalSet hospitalSet){
        boolean update = hospitalSetService.updateById(hospitalSet);
        if(update){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }

    //7.批量删除医院设置
    @ApiOperation("批量删除医院设置")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList){
        boolean b = hospitalSetService.removeByIds(idList);
        if(b){
            return  Result.ok();
        }
        else {
            return Result.fail();
        }

    }

    //8.医院设置锁定和解锁
    @ApiOperation("医院设置锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,@PathVariable Integer status){
        HospitalSet byId = hospitalSetService.getById(id);
        byId.setStatus(status);
        hospitalSetService.updateById(byId);
        return Result.ok();
    }

    //9.发送签名密钥
    @ApiOperation("发送前面密钥")
    @PutMapping("senkey/{id}")
    public Result lockHospitalSet(@PathVariable Long id){
        HospitalSet byId = hospitalSetService.getById(id);
        String hoscode = byId.getHoscode();
        String signKey = byId.getSignKey();
        //TODD发送短信
        return Result.ok();

    }
}
