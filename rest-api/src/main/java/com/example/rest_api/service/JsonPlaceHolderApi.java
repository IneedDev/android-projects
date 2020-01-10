package com.example.rest_api.service;

import com.example.rest_api.model.Currancy;
import com.example.rest_api.model.Post;
import com.example.rest_api.model.Rates;
import com.example.rest_api.model.Table;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("gbp/?format=json")
    Call<Currancy> getCurancy();

    @GET("?format=json")
    Call<List<Table>> getTable();

    @GET("?format=json")
    Call<Rates> getRates();

    @GET("{cod}/2012-01-01/2012-01-22/?format=json")
    Call<Rates> getRatesByCodAndDate(@Path("cod") String cod );

}
