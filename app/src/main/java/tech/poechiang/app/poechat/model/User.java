package tech.poechiang.app.poechat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.ResponseBody;
import tech.poechiang.app.poechat._interface.IHttpCompleteListener;

public class User extends Model {
    private String mPhoto = null;
    private String mNick;
    private String mMsg;
    private String mDate;
    private Bitmap mPhotoBitmap =null;
    public  User(JSONObject data) throws JSONException {
        super(data);
        mPhoto = data.getString("photo");
        mNick = data.getString("nick");
        mMsg = data.getString("msg");
        mDate = "2019-05-10";
    }
    public Bitmap getPhoto(){
        if (mPhotoBitmap==null && mPhoto!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mPhotoBitmap =getURLimage(mPhoto);
                    Message msg = new Message();
                    callDataChanged(mPhotoBitmap,null);
                }
            }).start();
            return null;
        }
        return mPhotoBitmap;
    }

    public String getNick(){
        return mNick;
    }
    public String getMsg(){
        return mMsg;
    }
    public String getDate(){
        return mDate;
    }
    public void setDate(String d){
        mDate = d;
    }
    public static User parse(JSONObject data) {
        try {
            return new User(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }




}
