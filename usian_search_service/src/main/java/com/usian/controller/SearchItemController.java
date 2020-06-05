package com.usian.controller;

import com.usian.pojo.SearchItem;
import com.usian.service.SearchItemService;
import org.elasticsearch.action.search.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/search")
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/importAll")
    public Boolean importAll(){
        return searchItemService.importAll();
    }

    @RequestMapping("list")
    public List<SearchItem> selectByQ(@RequestParam String q, @RequestParam long page, @RequestParam Integer pageSize){
        return searchItemService.selectByQ(q,page,pageSize);
    }

}