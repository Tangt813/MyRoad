package com.example.myroad.controller;


import com.example.myroad.service.RoadService;
import com.example.myroad.service.impl.RoadServiceImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RoadController
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/3/15 13:58
 * @Version: v1.0
 */
@RestController
@SpringBootApplication
@RequestMapping("/road")
public class RoadController {


    private RoadService roadService;

    public RoadController() {
        this.roadService = new RoadServiceImpl();
    }

    @GetMapping("/roadPlan")
    public Object roadPlan(@Param("point")Object[]pointData,int wayType)//
    {
        Object result=roadService.roadPlan(pointData);
        return result;

    }

}
