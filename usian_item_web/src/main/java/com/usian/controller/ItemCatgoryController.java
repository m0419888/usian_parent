package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItemCat;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/item")
public class ItemCatgoryController {

    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    //查询类目信息
    @RequestMapping("/selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(defaultValue = "0") Long id){
        List<TbItemCat> tbItemCatList = itemServiceFeignClient.selectItemCategoryByParentId(id);
        if (tbItemCatList != null && tbItemCatList.size()>0){
            return Result.ok(tbItemCatList);
        }
        return Result.error("查无结果");
    }
}
