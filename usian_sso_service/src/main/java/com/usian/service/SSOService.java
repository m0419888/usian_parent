package com.usian.service;

import com.usian.pojo.TbUser;

import java.util.Map;

public interface SSOService {
    boolean checkUserInfo(String checkValue, Integer checkFlag);

    Integer userRegister(TbUser user);

    Map userLogin(String username, String password);

    TbUser getUserByToken(String token);

    Boolean logOut(String token);
}
