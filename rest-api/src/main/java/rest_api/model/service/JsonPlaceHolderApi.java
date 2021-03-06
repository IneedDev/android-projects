package rest_api.model.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import rest_api.model.Currancy;
import rest_api.model.Post;
import rest_api.model.Rates;
import rest_api.model.Table;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

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

    @GET("findAll?format=json")
    Call<JsonArray> getAllStations();

    @GET("{stationId}?format=json")
    Call<JsonArray> getDataByStationId(@Path("stationId") int stationId);

    @GET("{stationId}?format=json")
    Call<JsonObject> getSensorDataByStationId(@Path("stationId") int stationId);

}
