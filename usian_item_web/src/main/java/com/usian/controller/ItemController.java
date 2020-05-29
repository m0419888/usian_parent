package com.usian.controller;

import com.usian.feign.ItemServiceFeignClient;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemParam;
import com.usian.utils.ItemAll;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/item")
public class ItemController {
    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    /**
     * 查询商品基本信息
     */
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId) {
        TbItem tbItem = itemServiceFeignClient.selectItemInfo(itemId);
        if (tbItem != null) {
            return Result.ok(tbItem);
        }
        return Result.error("查无结果");
    }
    //查询全部商品信息
    @RequestMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer rows){
        PageResult pageResult = itemServiceFeignClient.selectTbItemAllByPage(page,rows);
        if (pageResult != null && pageResult.getResult() != null && pageResult.getResult().size()>0){
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }

    //添加商品
    @RequestMapping("/insertTbItem")
    public Result insertTbItem(TbItem tbItem,String desc,String itemParams){
        Integer insertTbItemNum = itemServiceFeignClient.insertTbItem(tbItem, desc, itemParams);
        if(insertTbItemNum==3){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    //删除商品
    @RequestMapping("/deleteItemById")
    public Result deleteItemById(@RequestParam(required = true) Long itemId){
        Integer num = itemServiceFeignClient.deleteItemById(itemId);
        if (num == 1){
            return Result.ok();
        }
        return Result.error("删除失败");
    }

    //预更新商品接口
    @RequestMapping("preUpdateItem")
    public Result preUpdateItem(Long itemId){
        ItemAll itemAll = itemServiceFeignClient.preUpdateItem(itemId);
        if (itemAll != null){
            return Result.ok(itemAll);
        }
        return Result.error("查无结果");
    }

    //修改商品信息接口
    @RequestMapping("updateTbItem")
    public Result updateTbItem(TbItem tbItem, String desc, String itemParams){
        Integer updateTbItemNum = itemServiceFeignClient.updateTbItem(tbItem, desc, itemParams);
        if(updateTbItemNum==3){
            return Result.ok();
        }
        return Result.error("更新异常");
    }
}