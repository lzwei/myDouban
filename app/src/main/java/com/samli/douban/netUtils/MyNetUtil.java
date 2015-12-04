package com.samli.douban.netUtils;

import com.squareup.okhttp.OkHttpClient;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by sam.li on 2015/12/3.
 */
public class MyNetUtil {

    private static MyNetUtil myNetUtil;
    private OkHttpClientManager okHttpClientManager;
    private OkHttpClient okHttpClient;
    private String baseUrl = "http://192.168.119.125:8080/didi_truck/";


    private MyNetUtil(){
        if(okHttpClientManager==null) {
            okHttpClientManager = OkHttpClientManager.getInstance();
        }
        if(okHttpClient==null) {
            okHttpClient = okHttpClientManager.getOkHttpClient();
        }
        okHttpClient.setConnectTimeout(2000, TimeUnit.MILLISECONDS);

    }

    public static MyNetUtil getInstance()
    {
        if(myNetUtil==null){
            myNetUtil=new MyNetUtil();
        }
        myNetUtil.baseUrl="http://192.168.130.166:8103/ws/";
        return myNetUtil;
    }


    public OkHttpRequest getUser(Map<String, String> params,ResultCallback callback){
       return new OkHttpRequest.Builder().url(this.baseUrl+"api/anonymous/register")
               .encodeParams(params,false).post(callback);
    }
}
