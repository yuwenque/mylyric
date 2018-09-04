package com.ares.http;

import com.ares.entity.Actress;
import com.ares.entity.ActressDetail;
import com.ares.entity.BaseSearchItem;
import com.ares.entity.MovieSearchItem;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ArtworkApi {




    @GET("/artwork/actresses/{page}")
    Observable<List<Actress>> getActressList(@Path("page") int page);

    @GET("/artwork/actressInfo")
    Observable<ActressDetail> getActressDetail(@Query("id") String id);

    @GET("/artwork/search/{keyword}/{page}")
    Observable< List<BaseSearchItem>> getSearchList(@Path("keyword") String keyword,@Path("page") int page,@Query(value = "type") int type);


    @GET("/artwork/{code}")
    Observable<MovieSearchItem> searchArtWork(@Path("code")String code);
}
