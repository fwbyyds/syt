package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //分页显示医院列表
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable("page") Integer page,@PathVariable("limit") Integer limit, HospitalQueryVo hospitalSetQueryVo){
        Page<Hospital> hospitalPage=hospitalService.selectHospPage(page,limit,hospitalSetQueryVo);
        return Result.ok(hospitalPage);
    }
    //更新医院状态
    @ApiOperation("更新医院状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable("id") String id,@PathVariable("status") Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }
}
