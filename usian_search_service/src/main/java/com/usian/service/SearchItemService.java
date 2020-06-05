package com.usian.service;

import com.usian.pojo.SearchItem;

import java.io.IOException;
import java.util.List;

public interface SearchItemService {

    Boolean importAll();

    List<SearchItem> selectByQ(String q, long page, Integer pageSize);

    int insertDocument(String msg) throws IOException;
}
