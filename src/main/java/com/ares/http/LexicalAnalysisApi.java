package com.ares.http;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import java.util.Map;

/**
 * Created by ares on 2017/12/20.
 */
public interface LexicalAnalysisApi {



    @GET("/v2/index.php")
    Observable<TextSentimentResult> getResult(@QueryMap Map<String,Object> map);



    @GET
    Observable<TextSentimentResult> getEmotionResult(@Url String url, @QueryMap Map<String,Object> map);
}
