package com.usian.service;

import com.usian.pojo.*;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import com.usian.utils.ItemAll;
import com.usian.utils.PageResult;

import java.util.List;

public interface ItemService {
    TbItem selectItemInfo(Long itemId);

    PageResult selectTbItemAllByPage(Integer page, Integer rows);

    List<TbItemCat> selectItemCategoryByParentId(Long id);

    TbItemParam selectItemParamByItemCatId(Long itemCatId);

    Integer insertTbItem(TbItem tbItem, String desc, String itemParams);

    Integer deleteItemById(Long itemId);

    ItemAll preUpdateItem(Long itemId);

    Integer updateTbItem(TbItem tbItem, String desc, String itemParams);

    PageResult selectItemParamAll(Integer page, Integer rows);

    Integer insertItemParam(Long itemCatId, String paramData);

    Integer deleteItemParamById(Long id);

    CatResult selectItemCategoryAll();

    TbItemDesc selectItemDescByItemId(Long itemId);

    TbItemParamItem selectTbItemParamItemByItemId(Long itemId);
}
