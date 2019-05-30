package tech.poechiang.app.poechat;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import tech.poechiang.app.poechat._interface.IHttpSuccessListener;
import tech.poechiang.app.poechat.core.Http;
import tech.poechiang.app.poechat.core.HttpResult;
import tech.poechiang.app.poechat.model.Session;
import tech.poechiang.app.poechat.model.SessionAdapter;
import tech.poechiang.app.poechat.model.User;
import tech.poechiang.app.poechat.model.UserAdapter;


public class MainActivity extends NavigationBarActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListView listView=(ListView)findViewById(R.id.msglist);
        final ListView listView2=(ListView)findViewById(R.id.contlist);


        ///可以一直添加，在真机运行后可以下拉列表

        Http http=new Http();
        http.success(new IHttpSuccessListener() {
            @Override
            public void onSuccess(HttpResult hr) {
                try {
                    JSONArray arr = hr.list();

                    List<Session> list = new ArrayList<Session>();
                    Log.i("HTTP2", "" + arr.length());
                    for (int i = 0; i < arr.length(); i++) {
                        list.add(Session.parse(arr.getJSONObject(i)));
                    }

                    SessionAdapter adapter = new SessionAdapter(MainActivity.this, R.layout.msg_list_item_view, list);
                    listView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).requestData("https://api.shibu365.com/open/index/android_test");

//        http.success(new IHttpSuccessListener() {
//            @Override
//            public void onSuccess(HttpResult hr) {
//
//            }
//        }).requestData("https://api.shibu365.com/andr/contact/all_users");

        List<User> userList = new ArrayList<User>();

        try {
            for (int i = 0;i<20;i++){
                User u = null;
                u = new User(null);
                u.setNick("昵称"+i);
                u.setPhoto(R.drawable.ic_notifications_black_24dp);
                userList.add(u);
            }

            UserAdapter adapter = new UserAdapter(MainActivity.this, R.layout.cont_list_item_view, userList);
            listView2.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.main_action_toolbar);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
