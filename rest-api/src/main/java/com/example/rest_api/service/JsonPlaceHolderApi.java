package com.example.rest_api.service;

import com.example.rest_api.model.Currancy;
import com.example.rest_api.model.Post;
import com.example.rest_api.model.Rates;
import com.example.rest_api.model.RatesAll;
import com.example.rest_api.model.Table;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("gbp/?format=json")
    Call<Currancy> getCurancy();

    @GET("?format=json")
    Call<List<Table>> getTable();

}
