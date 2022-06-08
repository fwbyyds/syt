package com.atguigu.yygh.cmn.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.cmn.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Api(value = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;


    //根据dictCode获取下级节点
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list=dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
      List<Dict> list=dictService.findChildData(id);
      return Result.ok(list);
    }

    //导出数据字典接口
    @ApiOperation(value="导出")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportData(response);

    }
    @ApiOperation(value = "导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }
    //根据dictcode和value进行查询
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,@PathVariable String value){
        String dictName=dictService.getDictName(dictCode,value);
        return dictName;
    }
    //根据value进行查询
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value){
        String dictName=dictService.getDictName("",value);
        return dictName;
    }
}
