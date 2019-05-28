package tech.poechiang.app.poechat;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import tech.poechiang.app.poechat._interface.IHttpSuccessListener;
import tech.poechiang.app.poechat.core.Http;
import tech.poechiang.app.poechat.core.HttpResult;
import tech.poechiang.app.poechat.model.User;
import tech.poechiang.app.poechat.model.UserAdapter;


public class MainActivity extends NavigationBarActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListView listView=(ListView)findViewById(R.id.msglist);


        ///可以一直添加，在真机运行后可以下拉列表

        Http http=new Http();
        http.success(new IHttpSuccessListener() {
            @Override
            public void onSuccess(HttpResult hr) {
                try {
                    JSONArray arr = hr.list();

                    List<User> list = new ArrayList<User>();
                    Log.i("HTTP2", "" + arr.length());
                    for (int i = 0; i < arr.length(); i++) {
                        list.add(User.parse(arr.getJSONObject(i)));
                    }

                    UserAdapter adapter = new UserAdapter(MainActivity.this, R.layout.msg_list_item_view, list);
                    listView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        http.requestData("https://api.shibu365.com/open/index/android_test");

    }


    @Override
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.main_action_toolbar);
    }

//    @Override
//    public boolean isNavigateBackShowing(){
//        return false;
//    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
