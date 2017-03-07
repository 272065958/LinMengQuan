package net.kamfat.omengo.util;

import com.google.gson.Gson;

import net.kamfat.omengo.bean.api.DatumResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by cjx on 2016/7/13.
 */
public class JsonParser {
    static JsonParser instance;

    private JsonParser() {

    }

    public static JsonParser getInstance() {
        if (instance == null) {
            synchronized (JsonParser.class) {
                if (instance == null) {
                    instance = new JsonParser();
                }
            }
        }
        return instance;
    }

    /**
     * 解析服务器返回数据
     */
    public DatumResponse getDatumResponse(String response){
        if(response == null || response.length() == 0){
            return null;
        }
        DatumResponse rb = null;
        try {
            JSONObject obj = new JSONObject(response);
            rb = new DatumResponse();
            if(obj.has("code")){
                rb.code = obj.getInt("code");
            }
            if(obj.has("message")){
                rb.message = obj.getString("message");
            }
            if(obj.has("datum")){
                String datum = obj.getString("datum");
                rb.datum = datum.equals("null") ? null : datum;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rb;
    }

    public <T> T fromJson(String json, Type typeOfT){
        try{
            return new Gson().fromJson(json, typeOfT);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象转换成字符串
     * @param object 待转换的对象
     * @return
     */
    public String JsonToObject(Object object){
        return new Gson().toJson(object);
    }
}
