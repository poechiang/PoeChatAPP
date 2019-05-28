package tech.poechiang.app.poechat.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InvalidClassException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;

public abstract class Model {
    private int mId;

    public Model  (JSONObject o){}

    public int getId(){
        return mId;
    }
    public static Model parse(JSONObject o) {
       return null;
    }
    //加载图片
    protected Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);//读取图像数据
            //读取文本数据
            //byte[] buffer = new byte[100];
            //inputStream.read(buffer);
            //text = new String(buffer);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    protected Handler dataChanged;
    public void onDataChanged(Handler handler){
        dataChanged = handler;
    }
    protected void callDataChanged(@Nullable Object obj,@Nullable Integer what){
        Message msg = new Message();
        if(what!=null) {
            msg.what = what;
        }
        msg.obj = obj;
        if(dataChanged!=null)dataChanged.sendMessage(msg);
    }
}
