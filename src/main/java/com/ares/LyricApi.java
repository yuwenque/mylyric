package com.ares;

import io.reactivex.Flowable;
import retrofit2.http.*;

import java.util.Map;

/**
 * Created by ares on 2017/12/21.
 */
public interface LyricApi {



    @GET
    Flowable<Lyric> getSongLyric(@Url String apiUrl, @QueryMap Map<String,Object> map);


    @FormUrlEncoded
    @POST
    Flowable<SearchResult> search(@Url String url, @FieldMap Map<String,Object> map);

}
