package com.usian.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("usian-search-service")
public interface SearchItemFeign {

    @RequestMapping("/service/search/importAll")
    public Boolean importAll();
}