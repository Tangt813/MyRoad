package com.example.myroad.controller;


import com.example.myroad.entity.RoadData;
import com.example.myroad.service.RoadService;
import com.example.myroad.service.impl.RoadServiceImpl;
import net.sf.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.util.SaResult;

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


    private final RoadService roadService;

    public RoadController() {
        this.roadService = new RoadServiceImpl();
    }

    @GetMapping("/roadPlan")
    public SaResult roadPlan(@RequestBody JSONObject pointData)//TODO 更多用户选择
            //wayType:0-->驾车，1-->步行，2-->骑行，3-->公交
    {
        Object result=roadService.roadPlan(pointData);
        return SaResult.code(200).setData(result);

    }

    @GetMapping("/test")
    public SaResult test()
    {
        return SaResult.code(200).setData(roadService.test());
    }

}
