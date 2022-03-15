package com.example.myroad.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: IndexController
 * @Description: 代码测试
 * @Author: Tangt
 * @Date: 2022/3/13 17:34
 * @Version: v1.0
 */
@RestController
@SpringBootApplication
@RequestMapping("/index")
public class IndexController {

    @GetMapping("getUser")
    public Map<String,Object> getUser(){
        System.out.println("微信小程序正在调用...");
        Map<String,Object> map = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        list.add("Amy");
        list.add("Cathy");
        map.put("list",list);
        System.out.println("微信小程序调用完成...");
        return map;
    }

    @RequestMapping("")
    public String getTest(){
        return "Hello world";
    }

}
