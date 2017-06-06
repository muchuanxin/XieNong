package com.xidian.xienong.network;

import android.os.Handler;
import android.os.Looper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by koumiaojuan on 2017/6/5.
 */

public class OKHttp {
    private static OKHttp mOkHttpInstance;
    private static OkHttpClient mClientInstance;
    protected Handler mHandler;
    private Gson mGson;
    /**
     * 单例模式，私有构造函数，构造函数里面进行一些初始化
     */
    private OKHttp(){
        mClientInstance = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

     /**
     * 获取实例
     *
     * @return
     */
     public static  OKHttp getInstance(){
         if(mOkHttpInstance == null){
             synchronized (OKHttp.class){
                 if(mOkHttpInstance == null){
                     mOkHttpInstance = new OKHttp();
                 }
             }
         }
         return mOkHttpInstance;
     }

     /**
     * 封装一个request方法，不管post或者get方法中都会用到
     */
    public void request(final Request request, final BaseCallback callback) {        //在请求之前所做的事，比如弹出对话框等
        callback.onRequestBefore();

        mClientInstance.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackFailure(request, callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {                    //返回成功回调
                    String resString = response.body().string();
                    if (callback.mType == String.class) {                        //如果我们需要返回String类型
                        callbackSuccess(response, resString, callback);
                    } else {                        //如果返回的是其他类型，则利用Gson去解析
                        try {
                            Object o = mGson.fromJson(resString, callback.mType);
                            callbackSuccess(response, o, callback);
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                            callbackError(response, callback, e);
                        }
                    }

                } else {                    //返回错误
                    callbackError(response, callback, null);
                }

            }
        });
    }    /**
     * 在主线程中执行的回调
     *
     * @param response
     * @param callback
     */
    private void callbackSuccess(final Response response, final Object o, final BaseCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, o);
            }
        });
    }    /**
     * 在主线程中执行的回调
     * @param response
     * @param callback
     * @param e
     */
    private void callbackError(final Response response, final BaseCallback callback, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }    /**
     * 在主线程中执行的回调
     * @param request
     * @param callback
     * @param e
     */
    private void callbackFailure(final Request request, final BaseCallback callback, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }    /**
     * 对外公开的get方法
     *
     * @param url
     * @param callback
     */
    public void get(String url, BaseCallback callback) {
        Request request = buildRequest(url, null, HttpMethodType.GET);
        request(request, callback);
    }    /**
     * 对外公开的post方法
     *
     * @param url
     * @param params
     * @param callback
     */
    public void post(String url, Map<String, String> params, BaseCallback callback) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        request(request, callback);
    }    /**
     * 构建请求对象
     *
     * @param url
     * @param params
     * @param type
     * @return
     */
    private Request buildRequest(String url, Map<String, String> params, HttpMethodType type) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (type == HttpMethodType.GET) {
            builder.get();
        } else if (type == HttpMethodType.POST) {
            builder.post(buildRequestBody(params));
        }        return builder.build();
    }    /**
     * 通过Map的键值对构建请求对象的body
     *
     * @param params
     * @return
     */
    private RequestBody buildRequestBody(Map<String, String> params) {

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entity : params.entrySet()) {
                builder.add(entity.getKey(), entity.getValue());
            }
        }
        return builder.build();
    }    /**
     * 这个枚举用于指明是哪一种提交方式
     */
    enum HttpMethodType {
        GET,
        POST
    }
}
