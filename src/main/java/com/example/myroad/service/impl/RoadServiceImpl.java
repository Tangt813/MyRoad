package com.example.myroad.service.impl;

import com.example.myroad.service.RoadService;
import com.example.myroad.utils.HttpsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import cn.hutool.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: RoadServiceImpl
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/3/15 14:13
 * @Version: v1.0
 */
@Service
public class RoadServiceImpl implements RoadService {
    static String key = "dac759bbca955bfb55c0bcdac3618995";
    static String carUrl = "https://restapi.amap.com/v3/direction/driving?strategy=0";//?parameters
    static String walkUrl = "https://restapi.amap.com/v5/direction/walking?";
    static String bicycleUrl = "https://restapi.amap.com/v5/direction/bicycling?";
    static String electrobikeUrl = "https://restapi.amap.com/v5/direction/electrobike?";
    static String integratedUrl = "https://restapi.amap.com/v5/direction/transit/integrated?";//公交


    @Override
    public cn.hutool.json.JSONObject test(){
        return HttpsUtil.getResponse();
    }

    @Override
    public Object roadPlan(JSONObject roadDataList) {
        String url = carUrl;//驾车规划
        switch (roadDataList.getInt("wayType")) {
            case 0:
                url = carUrl;
                break;
            case 1:
                url = walkUrl;
                break;
            case 2:
                url = bicycleUrl;
                break;
            case 3:
                url = electrobikeUrl;
                break;
            case 4:
                url = integratedUrl;
                break;

        }
        JSONArray location= roadDataList.getJSONArray("location");
        int dataLen = location.size();


        for (int i = 0; i < dataLen; i++) {
            for (int j = 0; j < dataLen; j++) {
                if (i != j) {
                    try {
                        URL restServiceURL = new URL(url);
                        try {
//                            JsonTest jsonTest = JSONObject.parseObject(json,JsonTest.class);
// TODO @郑启帆修改一下这里

                            //设置参数
                            Map params = new HashMap();

                            params.put("origin",location.getString(i));
                            params.put("destination",location.getString(j));

                            String result=HttpsUtil.httpsRequest(url,params);
                            cn.hutool.json.JSONObject jsonObject= new JSONObject(result);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return null;
    }
}
