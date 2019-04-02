package per.niejun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.niejun.entity.User;
import per.niejun.service.UserService;

import java.util.HashMap;

/**
 * Created by 8000115097 聂钧 on 2019-03-31.
 */
@RestController// @Controller与 ResponseBody合二为一注解
@RequestMapping("/user")//配置URL映射，作用在类级别上
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/index")//配置URL映射，作用在处理器方法上
    public String hello(){
        return "Welcome to SpringBoot, this is my first springboot!!!";
    }

//    @GetMapping(value = "/login")//接受GET请求
    @PostMapping("/login")
    public HashMap login(User user){
        HashMap map = new HashMap();
        System.out.println ( "微信小程序调用接口！！！用户名:" + user.getUname() + " 密码:" + user.getPassword() );
        boolean login = userService.login ( user );
        if (login) {
            map.put("code", 0);

        }
        else {
            map.put("code",1);
        }

        return map;
    }
}
