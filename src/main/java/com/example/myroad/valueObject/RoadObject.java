package com.example.myroad.valueObject;

/**
 * @ClassName: RoadObject
 * @Description: TODO
 * @Author: Tangt
 * @Date: 2022/3/22 13:29
 * @Version: v1.0
 */
public class RoadObject {
    public String key;
    public String origin;
    public String destination;

    public RoadObject()
    {
        key="";
        origin="";
        destination="";
    }

    public RoadObject(String key,String origin,String destination)
    {
        this.destination=destination;
        this.origin=origin;
        this.key=key;
    }

}
