package tech.poechiang.app.poechat.core;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import tech.poechiang.app.poechat._interface.IHttpResult;

public class HttpResult implements IHttpResult {
    private Integer mStatus;
    private JSONObject mData;
    private JSONArray mList;
    private String mMsg;
    private String mRaw;

    public HttpResult(){
    }
    public HttpResult(Integer code,String msg){
        this();
        this.mStatus = code;
        this.mMsg = msg;
    }
    @Override
    public Integer status() {
        return mStatus;
    }

    @Override
    public JSONObject data() {
        return mData;
    }

    @Override
    public JSONArray list() {
        return mList;
    }

    @Override
    public String msg() {
        return mMsg;
    }

    @Override
    public String raw() {
        return mRaw;
    }

    static HttpResult from(ResponseBody body) throws IOException, JSONException {
        String respData = body.string();
        JSONObject obj = new JSONObject(respData);
        HttpResult hr = new HttpResult();
        if (obj.has("status")) {
            hr.mStatus = obj.getInt("status");
        }
        if (obj.has("msg")) {
            hr.mMsg = obj.getString("msg");
        }
        if (obj.has("data")) {
            hr.mData = obj.getJSONObject("data");
        }
        if (obj.has("list")) {
            hr.mList = obj.getJSONArray("list");
        }

        Log.d("HTTPRESULT","status = "+hr.mStatus.toString());
        return hr;
    }
    static HttpResult from(Exception e) {

        HttpResult hr = new HttpResult();
        hr.mMsg = e.getMessage();
        hr.mStatus = 0;
        return hr;
    }
}
