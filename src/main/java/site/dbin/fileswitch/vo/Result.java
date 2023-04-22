package site.dbin.fileswitch.vo;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;

import java.util.Date;

public class Result extends JSONObject{

    @JSONField(serialize = false)
    JSONObject data=null;

    public Result(){
    }
    public static Result ok(){
        Result result = new Result();
        result.put("status",200);
        result.put("message","ok");
        result.put("timestamp",new Date());
        return result;
    }

    public static Result ok(String message){
        Result result = new Result();
        result.put("status",200);
        result.put("message",message);
        result.put("timestamp",new Date());
        return result;
    }

    public static Result ok(Integer status,String message){
        Result result = new Result();
        result.put("status",status);
        result.put("message",message);
        result.put("timestamp",new Date());
        return result;
    }

    public Result add(String key,Object value){
        if(data==null){
            this.data = new JSONObject();
            this.put("data", data);
        }
        this.data.put(key,value);
        return this;
    }

    public static Result error(){
        Result result = new Result();
        result.put("status",400);
        result.put("message","error");
        result.put("timestamp",new Date());
        return result;
    }

    public static Result error(String message){
        Result result = new Result();
        result.put("status",400);
        result.put("message",message);
        result.put("timestamp",new Date());
        return result;
    }
}
