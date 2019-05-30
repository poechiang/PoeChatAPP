package tech.poechiang.app.poechat.model;

import android.graphics.Bitmap;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

public class Session extends Model {
    private String mPhoto = null;
    private String mNick;
    private String mMsg;
    private String mDate;
    private Bitmap mPhotoBitmap =null;
    public  Session(JSONObject data) throws JSONException {
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
    public static Session parse(JSONObject data) {
        try {
            return new Session(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
