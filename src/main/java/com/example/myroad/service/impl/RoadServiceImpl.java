package com.example.myroad.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.myroad.service.RoadService;
import com.example.myroad.utils.Algorithm.MyRoute;
import com.example.myroad.utils.HttpsUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.RouteMatcher;
import com.example.myroad.utils.*;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
    static String carUrl = "https://restapi.amap.com/v3/direction/driving?strategy=0&show_fields=cost";//?parameters
    static String walkUrl = "https://restapi.amap.com/v5/direction/walking?show_fields=cost";
    static String bicycleUrl = "https://restapi.amap.com/v5/direction/bicycling?show_fields=cost";
    static String electrobikeUrl = "https://restapi.amap.com/v5/direction/electrobike?show_fields=cost";
    static String integratedUrl = "https://restapi.amap.com/v5/direction/transit/integrated?show_fields=cost";//公交


    @Override
    public cn.hutool.json.JSONObject test() {
        return HttpsUtil.getResponse();
    }

    @Override
    public Object roadPlan(JSONObject roadDataList) {
        System.out.println(roadDataList.get("timeSpan"));
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
        JSONArray location = roadDataList.getJSONArray("location");
        int dataLen = location.size();
        double[][]needTime=new double[dataLen][dataLen];



        for (int i = 0; i < dataLen; i++) {
            for (int j = 0; j < dataLen; j++) {
                if (i != j) {
// TODO @郑启帆修改一下这里

                    //设置参数
                    Map params = new HashMap();

                    params.put("origin", location.get(i));
                    params.put("destination", location.get(j));
//                    input.append("&key=").append(URLEncoder.encode(key, "UTF-8")) ;
                    try {
                        params.put("key",URLEncoder.encode(key, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String result = HttpsUtil.httpsRequest(url, params);
                    System.out.println(result);
                    cn.hutool.json.JSONObject jsonObject = new JSONObject(result);
                    System.out.println(jsonObject);

                    //提取出时间
                    JSONArray paths= (JSONArray) ((Map) jsonObject.get("route")).get("paths");
                    double min=Double.MAX_VALUE;
                    for (int k = 0; k < paths.size(); k++) {
                        double val=Double.parseDouble(((Map)paths.get(k)).get("duration").toString());
                        if(val<min)
                        {
                            min=val;
                        }
                    }
                    needTime[i][j]=min;


                }

            }
        }
        String str1=roadDataList.getStr("timeSpan");
        String str2=str1.substring(1);
        String []timeSpanStr=str2.substring(0,str2.length()-1).split(",");


        double[][] timeSpan=new double[dataLen][2];
        for (int i = 0; i < dataLen*2; i++) {
            if(i<dataLen)
            {
                timeSpan[i][0]=Double.parseDouble(timeSpanStr[i]);
            }
            else
            {
                timeSpan[i%dataLen][1]=Double.parseDouble(timeSpanStr[i]);
            }
        }

        str1=roadDataList.getStr("sequence");
        str2=str1.substring(1);
        String []seqStr=str2.substring(0,str2.length()-1).split(",");
        int[]seqence=new int[dataLen];
        for (int i = 0; i < dataLen; i++) {
            seqence[i]=Integer.parseInt(seqStr[i]);
        }

        MyRoute myRoute=new MyRoute(dataLen,needTime,timeSpan,seqence,roadDataList.getInt("startPoint"),roadDataList.getInt("endPoint"));
        myRoute.carculate();

        //TODO：计算出myroute之后，根据前端要求返回数据
        return null;
    }
}
