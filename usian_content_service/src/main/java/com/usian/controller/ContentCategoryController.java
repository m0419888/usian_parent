package com.usian.controller;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentCategoryService;
import com.usian.utils.AdNode;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/contentCategory")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 根据父节点 ID 查询子节点
     */
    @RequestMapping("/selectContentCategoryByParentId")
    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        List<TbContentCategory> list = contentCategoryService.selectContentCategoryByParentId(id);
        return list;
    }

    @RequestMapping("/insertContentCategory")
    public Integer insertContentCategory(@RequestBody TbContentCategory tbContentCategory){
        Integer num = contentCategoryService.insertContentCategory(tbContentCategory);
        return num;
    }

    @RequestMapping("deleteContentCategoryById")
    public Integer deleteContentCategoryById(Long categoryId){
        Integer num = contentCategoryService.deleteContentCategoryById(categoryId);
        return num;
    }

    @RequestMapping("updateContentCategory")
    public Integer updateContentCategory(@RequestBody TbContentCategory tbContentCategory){
        Integer num = contentCategoryService.updateContentCategory(tbContentCategory);
        return num;
    }

    @RequestMapping("selectTbContentAllByCategoryId")
    public PageResult selectTbContentAllByCategoryId(Integer page,Integer rows,Long categoryId){
        PageResult pageResult = contentCategoryService.selectTbContentAllByCategoryId(page,rows,categoryId);
        return pageResult;
    }

    @RequestMapping("insertTbContent")
    public Integer insertTbContent(@RequestBody TbContent tbContent){
        Integer num = contentCategoryService.insertTbContent(tbContent);
        return num;
    }

    @RequestMapping("deleteContentByIds")
    public Integer deleteContentByIds(Long ids){
        Integer num = contentCategoryService.deleteContentByIds(ids);
        return num;
    }

    @RequestMapping("selectFrontendContentByAD")
    public List<AdNode> selectFrontendContentByAD(){
        List<AdNode> list = contentCategoryService.selectFrontendContentByAD();
        return list;
    }
}
