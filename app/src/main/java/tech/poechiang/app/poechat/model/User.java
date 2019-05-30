package tech.poechiang.app.poechat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
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
    private @DrawableRes int mPhotoRes;

    private String mNick;
    private Bitmap mPhotoBitmap =null;
    public  User(@Nullable JSONObject data) throws JSONException {
        super(data);
        if(data!=null) {
            mPhoto = data.getString("photo");
            mNick = data.getString("nick");
        }
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
    public @DrawableRes int getPhotoRes(){
        return mPhotoRes;
    }
    public void setPhoto(@DrawableRes int photo){
        mPhotoRes = photo;
    }
    public String getNick(){
        return mNick;
    }

    public void setNick(String nick){
        mNick = nick;
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
