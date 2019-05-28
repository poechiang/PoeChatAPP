package tech.poechiang.app.poechat._interface;

import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface IHttpResult {
    Integer status();
    JSONObject data();
    JSONArray list();
    String msg();
    String raw();
}
