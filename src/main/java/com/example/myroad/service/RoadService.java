package com.example.myroad.service;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @ClassName: RoadService
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/3/15 14:14
 * @Version: v1.0
 */
@Service
public interface RoadService {
    Object roadPlan(JSONObject roadData);
    cn.hutool.json.JSONObject test();
}
