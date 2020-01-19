package rest_api;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.rest_api.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import okhttp3.ResponseBody;
import rest_api.model.Lat;
import rest_api.model.Table;
import rest_api.model.service.JsonPlaceHolderApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    HashMap<String, String> coordinatesMapToMap = new HashMap<>();
    TextView text_map;
    Button btn_maps_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        text_map = findViewById(R.id.text_map);

        btn_maps_back = findViewById(R.id.btn_maps_back);
        btn_maps_back .setOnClickListener(this);

        getRestData();
    }

    private void getRestData() {
        HashMap<String, String> coordinatesMap = new HashMap<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gios.gov.pl/pjp-api/rest/station/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<JsonArray> call = jsonPlaceHolderApi.getAllStations();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (!response.isSuccessful()) {
                    text_map.setText("Code" + response.code());
                    return;
                }

                for (int i = 0; i < response.body().size(); i++) {
                    String gegrLat = response.body().get(i).getAsJsonObject().get("gegrLat").toString().substring(1,response.body().get(i).getAsJsonObject().get("gegrLat").toString().length()-1 );
                    String gegrLon = response.body().get(i).getAsJsonObject().get("gegrLon").toString().substring(1,response.body().get(i).getAsJsonObject().get("gegrLat").toString().length()-1 );
                    coordinatesMap.put(gegrLat, gegrLon);
                }

                placeAllMarkersOnMap(coordinatesMap);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                text_map.setText("Code" + t.getMessage());

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void placeAllMarkersOnMap(HashMap<String, String> coordinatesMap) {
        Iterator iterator = coordinatesMap.entrySet().iterator();

        while (iterator.hasNext()) {
              Map.Entry mapElement = (Map.Entry)iterator.next();
              LatLng mapCoordinates = new LatLng(Double.valueOf(mapElement.getKey().toString()), Double.valueOf(mapElement.getValue().toString()));

              mMap.addMarker(new MarkerOptions()
                      .position(mapCoordinates).title("tetstetst")
                      .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                      .snippet(mapCoordinates.toString()));
          }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.173398, 19.201387),6));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_maps_back:
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
