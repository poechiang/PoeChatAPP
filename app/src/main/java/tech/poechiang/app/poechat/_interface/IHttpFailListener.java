package tech.poechiang.app.poechat._interface;

import org.json.JSONObject;

import tech.poechiang.app.poechat.core.HttpResult;

public interface IHttpFailListener {
    void onFail(HttpResult hr);
}
