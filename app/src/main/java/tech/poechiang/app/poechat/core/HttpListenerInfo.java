package tech.poechiang.app.poechat.core;
import tech.poechiang.app.poechat._interface.IHttpCompleteListener;
import tech.poechiang.app.poechat._interface.IHttpFailListener;
import tech.poechiang.app.poechat._interface.IHttpReadyListener;
import tech.poechiang.app.poechat._interface.IHttpSuccessListener;

public class HttpListenerInfo {
    public IHttpReadyListener mHttpReadyListener;
    public IHttpCompleteListener mHttpCompleteListener;
    public IHttpSuccessListener mHttpSuccessListener;
    public IHttpFailListener mHttpFailListener;
}
