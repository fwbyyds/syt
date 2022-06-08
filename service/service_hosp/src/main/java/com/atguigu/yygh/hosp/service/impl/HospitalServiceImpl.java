package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.client.DictFeignClient;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {


    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    //更新医院状态
    @Override
    public void updateStatus(String id, Integer status) {
        //根据id查询医院信息
        Hospital hospital = hospitalRepository.findById(id).get();
        //设置修改的值
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    //上传医院接口
    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数map集合转换对象 Hospital
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        //判断是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist=hospitalRepository.getHospitalByHoscode(hoscode);


        //如果存在，进行修改
        if(hospitalExist!=null){
            hospitalExist.setStatus(hospital.getStatus());
            hospitalExist.setCreateTime(hospital.getCreateTime());
            hospitalExist.setUpdateTime(new Date());
            hospitalExist.setIsDeleted(0);
            hospitalRepository.save(hospitalExist);
        }
        else {//如果不存在，进行添加
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }
    //实现根据编号查询医院
    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospitalByHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospitalByHoscode;


    }
    //分页查询医院
    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalSetQueryVo) {
        //创建Pageable对象，设置当前页和每页记录数
        Pageable pageable= PageRequest.of(page-1,limit);
        //创建条件匹配器
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
       //hospitalQueryVo对象转换为hospital对象
        Hospital hospital=new Hospital();
        BeanUtils.copyProperties(hospitalSetQueryVo,hospital);
        //创建对象
        Example<Hospital> example=Example.of(hospital,matcher);
        Page<Hospital> all = hospitalRepository.findAll(example, pageable);
        //获取查询list集合，遍历进行医院等级封装
        all.getContent().stream().forEach(item->{
            this.setHospitalHosType(item);
        });
        return all;
    }
    //获取List集合，遍历进行医院等级封装
    private Hospital setHospitalHosType(Hospital hospital) {
        //根据dictcode和value获取医院等级名称
        String hostype = dictFeignClient.getName("Hostype", hospital.getHostype());
        //查询省、市、地区
        String provinceCode = dictFeignClient.getName(hospital.getProvinceCode());
        String citycode = dictFeignClient.getName(hospital.getCityCode());
        String districtCode = dictFeignClient.getName(hospital.getDistrictCode());
        hospital.getParam().put("fullAddress",provinceCode+citycode+districtCode);
        hospital.getParam().put("hostypeString",hostype);
        return hospital;

    }
}
