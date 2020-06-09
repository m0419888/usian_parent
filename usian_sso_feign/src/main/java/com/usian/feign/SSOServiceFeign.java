package com.usian.feign;

import com.usian.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("usian-sso-service")
public interface SSOServiceFeign {

    @RequestMapping("/service/sso/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(@PathVariable String checkValue,@PathVariable Integer checkFlag);

    @RequestMapping("/service/sso/userRegister")
    public Integer userRegister(@RequestBody TbUser user);

    @RequestMapping("/service/sso/userLogin")
    public Map userLogin(@RequestParam String username, @RequestParam String password);

    @RequestMapping("/service/sso/getUserByToken/{token}")
    public TbUser getUserByToken(@PathVariable String token);

    @RequestMapping("/service/sso/logOut")
    public Boolean logOut(@RequestParam String token);
}