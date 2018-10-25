package com.ares.http

import com.ares.entity.*
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtworkApi {


    @GET("/artwork/actresses/{page}")
    fun getActressList(@Path("page") page: Int): Observable<List<Actress>>

    @GET("/artwork/actressInfo")
    fun getActressDetail(@Query("id") id: String): Observable<ActressDetail>

    @GET("/artwork/search/{keyword}/{page}")
    fun getSearchList(@Path("keyword") keyword: String, @Path("page") page: Int, @Query(value = "type") type: Int): Observable<List<BaseSearchItem>>


    @GET("/artwork/{code}")
    fun searchArtWork(@Path("code") code: String): Observable<MovieSearchItem>

    @GET("/artwork/video/{code}")
    fun searchArtWorkVideo(@Path("code") code: String): Observable<List<VideoSearchItem>>

}
