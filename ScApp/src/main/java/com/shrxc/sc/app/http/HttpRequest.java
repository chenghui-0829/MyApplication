package com.shrxc.sc.app.http;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by CH on 2018/4/2.
 */

public interface HttpRequest {

    /**
     * get请求
     *
     * @param url
     * @param params
     * @return
     */
    @GET("{url}")
    Call<String> httpGetRequest(@Path("url") String url, @QueryMap Map<String, Object> params);

    /**
     * get请求
     *
     * @param url
     * @return
     */
    @GET("{url}")
    Call<String> httpGetRequestByNoParams(@Path("url") String url);

    /**
     * post请求
     *
     * @param url
     * @param params
     * @return
     */
    @POST("{url}")
    Call<String> httpPostRequest(@Path("url") String url, @QueryMap Map<String, String> params);


}
