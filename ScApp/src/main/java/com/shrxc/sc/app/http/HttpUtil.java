package com.shrxc.sc.app.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.shrxc.sc.app.utils.AppUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CH on 2017/2/17.
 */

public class HttpUtil {


    public static String URL = "http://101.37.119.225:8888/api/";

    private OkHttpClient httpClient;
    private static HttpUtil instance;


    private HttpUtil() {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        Handler handler = new Handler(Looper.getMainLooper());

        if (true) {
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }

        httpClient = okHttpClientBuilder
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static HttpUtil getInstance() {

        if (instance == null) {
            instance = new HttpUtil();
        }
        return instance;
    }

    public void setCertificates(InputStream... certificates) {
        httpClient = httpClient.newBuilder()
                .sslSocketFactory(HttpsUtil.getSslSocketFactory(certificates, null, null))
                .build();
    }

    /**
     * get 请求
     *
     * @param context 发起请求的context
     * @param url     url
     * @param params  参数
     */
    public void get(Context context, final String url, final Map<String, String> params, RequestCallback callback) {

        if (!AppUtil.IsNetConnect(context)) {
            Toast.makeText(context, "网络连接异常", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //拼接url
        String get_url = url;
        if (params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }
        Request request;
        //发起request
        if (context == null) {
            request = new Request.Builder()
                    .url(get_url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(get_url)
                    .tag(context)
                    .build();
        }

        httpClient.newCall(request).enqueue(new MyCallback(new Handler(), callback));
    }

    /**
     * post 请求
     *
     * @param context 发起请求的context
     * @param url     url
     * @param params  参数
     */
    public void post(Context context, final String url, final Map<String, String> params, RequestCallback callback) {

        if (!AppUtil.IsNetConnect(context)) {
            Toast.makeText(context, "网络连接异常", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //post builder 参数
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request;

        //发起request
        if (context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .tag(context)
                    .build();
        }

        httpClient.newCall(request).enqueue(new MyCallback(new Handler(), callback));
    }

    /**
     * 上传文件
     */
    public void upload(Context context, String url, Map<String, String> params, Map<String, File> files, RequestCallback callback) {

        if (!AppUtil.IsNetConnect(context)) {
            Toast.makeText(context, "网络连接异常", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

//        if(file != null){
//            // MediaType.parse() 里面是上传的文件类型。
//            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//            String filename = file.getName();
//            // 参数分别为， 请求key ，文件名称 ， RequestBody
//            requestBody.addFormDataPart("headImage", file.getName(), body);
//        }
//        if (map != null) {
//            // map 里面是请求中所需要的 key 和 value
//            for (Map.Entry entry : map.entrySet()) {
//                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
//            }
//        }
//        Request request = new Request.Builder().url("请求地址").post(requestBody.build()).tag(context).build();


        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\"" ),
                        RequestBody.create(null, params.get(key)));
            }
        }

        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                multipartBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"" ),
                        fileBody);
            }
        }

        Request request;
        if (context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(multipartBuilder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(multipartBuilder.build())
                    .tag(context)
                    .build();
        }

        httpClient.newCall(request).enqueue(new MyCallback(new Handler(), callback));
    }

    private class MyCallback implements Callback {

        private RequestCallback requestCallback;
        private Handler mHandler;

        public MyCallback(Handler handler, RequestCallback callback) {
            mHandler = handler;
            requestCallback = callback;
            requestCallback.onStart();
        }

        @Override
        public void onFailure(final Call call, final IOException e) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    requestCallback.onErro(e.getMessage());
                    requestCallback.onFinish();
                }
            });

        }

        @Override
        public void onResponse(final Call call, final Response response) throws IOException {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        int responseCode = response.code();
                        if (responseCode == 404 || responseCode >= 500) {
                            requestCallback.onErro(response.message());
                            requestCallback.onFinish();
                            return;
                        }
//                        JSONObject result = JSONObject.parseObject(response.body().string());
//                        System.out.println("------RequestResult------>" + response.body().string());
//                        String state = result.getString("Status");
//                        String msg = result.getString("description");
//                        String data = result.getString("data");
                        requestCallback.onStringResult(response.body().string());
                        requestCallback.onFinish();
                    } catch (IOException e) {
                        requestCallback.onErro(e.getMessage());
                        requestCallback.onFinish();
                    }
                }
            });
        }
    }

    private class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.e("requestUrl==========>", "请求的url:" + request.url());
            Response response = chain.proceed(request);
//            Log.e("responseUrl==========>", "响应的url:" + response.request().url());
            return response;
        }
    }

    //获取mime type
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
