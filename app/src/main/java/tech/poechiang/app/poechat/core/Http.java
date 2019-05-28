package tech.poechiang.app.poechat.core;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.poechiang.app.poechat._interface.IHttpCompleteListener;
import tech.poechiang.app.poechat._interface.IHttpFailListener;
import tech.poechiang.app.poechat._interface.IHttpParam;
import tech.poechiang.app.poechat._interface.IHttpReadyListener;
import tech.poechiang.app.poechat._interface.IHttpResult;
import tech.poechiang.app.poechat._interface.IHttpSuccessListener;

public class Http {
    private static final int SUCCESS=1;
    private static final int FAIL=0;

    private HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private static final String TAG = "HTTP";
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
            cookieStore.put(httpUrl.host(), list);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }).build();

    private HttpMethod mMethod = HttpMethod.POST;
    private Boolean mIsSync = false;
    private String mUrl;
    private HttpDataType mDataType = HttpDataType.JSON;
    private Response mRes;
    private HttpResult Hr = null;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS:
                    callOnSuccess(Hr);
                    callOnComplete(Hr);
                    break;
                case FAIL:
                    callOnFail(Hr);
                    callOnComplete(Hr);
                default:
                    break;
            }
        }
    };

    private HttpListenerInfo mListenerInfo;
    protected HttpListenerInfo getListenerInfo(){
        if (mListenerInfo==null){
            mListenerInfo = new HttpListenerInfo();
        }
        return mListenerInfo;
    }
    public Http setReadyListener(@Nullable IHttpReadyListener ready) {
        getListenerInfo().mHttpReadyListener = ready;
        return this;
    }
    public Http ready(@Nullable IHttpReadyListener ready) {
        return setReadyListener(ready);
    }
    protected boolean callOnReady(){

        HttpListenerInfo li = mListenerInfo;
        if (li != null && li.mHttpReadyListener != null) {
            li.mHttpReadyListener.onReady(this);
            return true;
        }
        return false;
    }
    public Http setCompleteListener(IHttpCompleteListener callback) {
        getListenerInfo().mHttpCompleteListener = callback;
        return this;
    }
    public Http complete(IHttpCompleteListener callback){
        return setCompleteListener(callback);
    }
    public Http setSuccessListener( IHttpSuccessListener callback) {
        getListenerInfo().mHttpSuccessListener = callback;
        return this;
    }
    public Http success(IHttpSuccessListener callback){
        return setSuccessListener(callback);
    }
    public Http setFailListener( IHttpFailListener callback) {
        getListenerInfo().mHttpFailListener = callback;
        return this;
    }
    public Http fail(IHttpFailListener callback){
        return setFailListener(callback);
    }
    protected boolean callOnComplete(HttpResult hr){

        HttpListenerInfo li = mListenerInfo;
        if (li != null && li.mHttpCompleteListener != null) {
            li.mHttpCompleteListener.onComplete(hr);
            return true;
        }
        return false;
    }
    protected boolean callOnSuccess(HttpResult hr){

        HttpListenerInfo li = mListenerInfo;
        if (li != null && li.mHttpSuccessListener != null) {
            li.mHttpSuccessListener.onSuccess(hr);
            return true;
        }
        return false;
    }
    protected boolean callOnFail(HttpResult hr){

        HttpListenerInfo li = mListenerInfo;
        if (li != null && li.mHttpFailListener != null) {
            li.mHttpFailListener.onFail(hr);
            return true;
        }
        return false;
    }
    public Http method(HttpMethod method){
        this.mMethod = method;
        return this;
    }
    public Http sync(){
        this.mIsSync = true;
        return this;
    }
    public Http asyn(){
        this.mIsSync = false;
        return this;
    }
    public Http url(String url){
        this.mUrl = url;
        return this;
    }
    public Http dataType(HttpDataType hdt){
        this.mDataType = hdt;
        return this;
    }
    public void requestData(String url, IHttpParam param,@Nullable IHttpCompleteListener callback, @Nullable HttpMethod method,@Nullable Boolean sync){


        this.url(url);
        if (callback!=null){
            this.setCompleteListener(callback);
        }

        if (method != null){
            this.method(method);
        }
        if (sync != null){
            if(sync){
                this.sync();
            }
            else{
                this.asyn();
            }
        }

        callOnReady();

        Log.d(TAG,this.mMethod.toString()+(this.mIsSync?" sync ":" asyn ")+"request reading");
        final Call call = buildCall(param);
        if (this.mIsSync){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Response res = call.execute();
                        Message m = new Message();
                        if (res.isSuccessful()){
                            Log.i("HTTP",call.request().method()+" sync request success");
                            Hr=HttpResult.from(res.body());
                            if(Hr.status()==1){
                                m.what = SUCCESS;
                            }
                            else{
                                m.what = FAIL;
                            }
                        }
                        else{
                            Log.e("HTTP",call.request().method()+" sync request fail!");
                            Hr = new HttpResult(-res.code(),res.body().string());

                            m.what = FAIL;
                        }

                        handler.sendMessage(m);
                        Log.i("HTTP", call.request().method() + " syn request complete");

                    } catch (IOException e) {

                        Log.e("HTTP",call.request().method()+" sync request fail: "+e.getLocalizedMessage());
                    } catch (JSONException e) {
                        Log.e("HTTP",call.request().method()+" sync request fail: "+e.getLocalizedMessage());
                    }
                }
            }).start();
        }
        else{
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Log.e("HTTP",call.request().method()+" asyn request fail: "+e.getLocalizedMessage());
                    Hr=new HttpResult(-1,e.getLocalizedMessage());
                    Message m = new Message();
                    m.what = FAIL;
                    handler.sendMessage(m);
                }

                @Override
                public void onResponse(Call call, Response res) throws IOException {
                    try {
                        Message m = new Message();
                        if (res.isSuccessful()) {
                            Log.i("HTTP", call.request().method() + " asyn request success");

                            Hr = HttpResult.from(res.body());

                            if (Hr.status() == 1) {

                                m.what = SUCCESS;
                            } else {
                                m.what = FAIL;
                            }


                        } else {
                            Hr = new HttpResult(-res.code(), res.body().string());
                            m.what = FAIL;
                            Log.e("HTTP", call.request().method() + " asyn request fail");
                        }
                        handler.sendMessage(m);
                        Log.i("HTTP", call.request().method() + " asyn request complete");
                    } catch (JSONException e) {
                        Log.e("HTTP",call.request().method()+" sync request fail: "+e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }


            });
        }
    }
    public void requestData(String url, IHttpParam param,@Nullable IHttpCompleteListener callback, @Nullable HttpMethod method) {
        requestData(url,param,callback,method,null);
    }
    public void requestData(String url, IHttpParam param,@Nullable IHttpCompleteListener callback, @Nullable Boolean sync) {
        requestData(url,param,callback,null,sync);
    }
    public void requestData(String url, IHttpParam param,@Nullable IHttpCompleteListener callback) {
        requestData(url,param,callback,null,null);
    }
    public void requestData(String url, IHttpParam param) {
        requestData(url,param,null,null,null);
    }
    public void requestData(String url, IHttpParam param,@Nullable HttpMethod method,@Nullable Boolean sync) {
        requestData(url,param,null,method,sync);
    }
    public void requestData(String url, IHttpParam param,@Nullable Boolean sync) {
        requestData(url,param,null,null,sync);
    }
    public void requestData(String url, IHttpParam param,@Nullable HttpMethod method) {
        requestData(url,param,null,method,null);
    }
    public void requestData(String url) {
        requestData(url,null,null,null,null);
    }
    public void requestData(String url, @Nullable IHttpCompleteListener callback) {
        requestData(url,null,callback,null,null);
    }
    public void requestData(String url, @Nullable HttpMethod method) {
        requestData(url,null,null,method,null);
    }
    public void requestData(String url, @Nullable Boolean sync) {
        requestData(url,null,null,null,sync);
    }
    public void requestData(String url, @Nullable HttpMethod method,@Nullable Boolean sync) {
        requestData(url,null,null,method,sync);
    }
    public void requestData(String url, @Nullable IHttpCompleteListener callback,@Nullable Boolean sync) {
        requestData(url,null,callback,null,sync);
    }
    public void requestData(String url, @Nullable IHttpCompleteListener callback,@Nullable HttpMethod method) {
        requestData(url,null,callback,method,null);
    }
    public void requestData(String url, @Nullable IHttpCompleteListener callback,@Nullable HttpMethod method,@Nullable Boolean sync) {
        requestData(url,null,callback,method,sync);
    }

    protected boolean sendData(String url,Object data){
        return false;
    }


    private Call buildCall(@Nullable IHttpParam param) {

        if (this.mMethod==HttpMethod.GET){
            if (param!=null){
                this.mUrl = this.mUrl+ '?'+param.toQueryParam();
            }
        }
        Request.Builder builder = new Request.Builder().url(this.mUrl);
        if(this.mMethod ==HttpMethod.POST ){
            RequestBody body =param == null ?RequestBody.create(null, new byte[0]):param.toRequestBody();

            builder = builder.post(body);

        }
        return okHttpClient.newCall(builder.build());
    }

    private Call buildCall() {
        return buildCall(null);
    }

}
