package com.usian.controller;

import com.github.pagehelper.Page;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemParam;
import com.usian.service.ItemService;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import com.usian.utils.ItemAll;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**查询商品信息
     * 根据商品id
     * @param itemId
     * @return
     */
    @RequestMapping("/selectItemInfo")
    public TbItem selectItemInfo(Long itemId){
        return itemService.selectItemInfo(itemId);
    }

    @RequestMapping("/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(Integer page,Integer rows){
        PageResult pageResult = itemService.selectTbItemAllByPage(page,rows);
        return pageResult;
    }

    @RequestMapping("/selectItemCategoryByParentId")
    public List<TbItemCat> selectItemCategoryByParentId(Long id){
        List<TbItemCat> tbItemCatList = itemService.selectItemCategoryByParentId(id);
        return tbItemCatList;
    }

    @RequestMapping("selectItemParamByItemCatId")
    public TbItemParam selectItemParamByItemCatId(Long itemCatId){
        TbItemParam tbItemParam = itemService.selectItemParamByItemCatId(itemCatId);
        return tbItemParam;
    }

    @RequestMapping("insertTbItem")
    public Integer insertTbItem(@RequestBody TbItem tbItem, String desc, String itemParams){
        Integer num =  itemService.insertTbItem(tbItem,desc,itemParams);
        return num;
    }

    @RequestMapping("deleteItemById")
    public Integer deleteItemById(Long itemId){
        Integer num = itemService.deleteItemById(itemId);
        return num;
    }

    @RequestMapping("preUpdateItem")
    public ItemAll  preUpdateItem(Long itemId){
        ItemAll itemAll = itemService.preUpdateItem(itemId);
        return itemAll;
    }

    @RequestMapping("updateTbItem")
    public Integer updateTbItem(@RequestBody TbItem tbItem, String desc, String itemParams){
        Integer num =  itemService.updateTbItem(tbItem,desc,itemParams);
        return num;
    }

    @RequestMapping("selectItemParamAll")
    public PageResult selectItemParamAll(Integer page,Integer rows){
        PageResult pageResult = itemService.selectItemParamAll(page,rows);
        return pageResult;
    }

    @RequestMapping("insertItemParam")
    public Integer insertItemParam(Long itemCatId, String paramData){
        Integer num = itemService.insertItemParam(itemCatId,paramData);
        return num;
    }

    @RequestMapping("deleteItemParamById")
    public Integer deleteItemParamById(Long id){
        Integer num = itemService.deleteItemParamById(id);
        return num;
    }

    @RequestMapping("selectItemCategoryAll")
    public CatResult selectItemCategoryAll(){
        CatResult catResult = itemService.selectItemCategoryAll();
        return catResult;
    }
}