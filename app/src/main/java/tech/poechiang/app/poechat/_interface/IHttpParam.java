package tech.poechiang.app.poechat._interface;

import okhttp3.RequestBody;

public interface IHttpParam {
    public String toQueryParam();

//    RequestBody requestBody = new FormBody.Builder().add("mobile", "18888888888").add("type", "1").build();
    public RequestBody toRequestBody();
}
