package com.usian.controller;

import com.usian.pojo.TbUser;
import com.usian.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户注册与登录
 */
@RestController
@RequestMapping("/service/sso")
public class SSOController {
    @Autowired
    private SSOService ssoService;

    /**
     * 对用户的注册信息(用户名与电话号码)做数据校验
     */
    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(String checkValue, Integer checkFlag) {
        return this.ssoService.checkUserInfo(checkValue, checkFlag);
    }

    @RequestMapping("/userRegister")
    public Integer userRegister(@RequestBody TbUser user) {
        return ssoService.userRegister(user);
    }

    @RequestMapping("/userLogin")
    public Map userLogin(String username, String password) {
        return ssoService.userLogin(username, password);
    }

    @RequestMapping("/getUserByToken/{token}")
    public TbUser getUserByToken(@PathVariable String token) {
        return ssoService.getUserByToken(token);
    }

    @RequestMapping("/logOut")
    public Boolean logOut(String token){
        return ssoService.logOut(token);
    }
}