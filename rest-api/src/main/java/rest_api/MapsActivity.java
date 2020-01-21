package rest_api;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.Marker;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, LocationListener, GoogleMap.OnMarkerClickListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    private GoogleMap mMap;
    HashMap<String, String> coordinatesMapToMap = new HashMap<>();
    TextView text_map;
    Button btn_maps_back;

    Double startlat, startlon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final DataBaseManager dataBaseManager = new DataBaseManager(this);

        text_map = findViewById(R.id.text_map);

        btn_maps_back = findViewById(R.id.btn_maps_back);
        btn_maps_back.setOnClickListener(this);

        getRestData();

        text_map.setText("Number of views " );
    }

    private void getRestData() {
        HashMap<String, String> coordinatesMap = new HashMap<>();
        List<Integer> idList = new ArrayList<>();
        DataBaseManager dataBaseManager = new DataBaseManager(this);


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

                    int list_id = Integer.parseInt(response.body().get(i).getAsJsonObject().get("id").toString());
                    String list_stationName = response.body().get(i).getAsJsonObject().get("stationName").toString();
                    String list_gegrLat = response.body().get(i).getAsJsonObject().get("gegrLat").toString();
                    String list_gegrLon = response.body().get(i).getAsJsonObject().get("gegrLon").toString();
                    coordinatesMap.put(cleanString(list_gegrLat), cleanString(list_gegrLon));
                    idList.add(list_id);

                }
                if (!(dataBaseManager.getTableCount("STATION") > 0)) {
                    for (int i = 0; i < response.body().size(); i++) {

                        int list_id = Integer.parseInt(response.body().get(i).getAsJsonObject().get("id").toString());
                        String list_stationName = response.body().get(i).getAsJsonObject().get("stationName").toString();
                        String list_gegrLat = response.body().get(i).getAsJsonObject().get("gegrLat").toString();
                        String list_gegrLon = response.body().get(i).getAsJsonObject().get("gegrLon").toString();

                        int list_city_id = Integer.parseInt(response.body().get(i).getAsJsonObject().get("city").getAsJsonObject().get("id").toString());
                        String list_city_name = response.body().get(i).getAsJsonObject().get("city").getAsJsonObject().get("name").toString();
                        String list_city_commune_communeName = response.body().get(i).getAsJsonObject().get("city").getAsJsonObject().get("commune").getAsJsonObject().get("communeName").toString();
                        String list_city_commune_districtName = response.body().get(i).getAsJsonObject().get("city").getAsJsonObject().get("commune").getAsJsonObject().get("districtName").toString();
                        String list_city_commune_provinceName = response.body().get(i).getAsJsonObject().get("city").getAsJsonObject().get("commune").getAsJsonObject().get("provinceName").toString();
                        String list_addressStreet = response.body().get(i).getAsJsonObject().get("addressStreet").toString();

                        //text_map.append(cleanString(list_gegrLat));
                        dataBaseManager.addFindAllData(list_id, list_stationName, list_gegrLat, list_gegrLon,
                                list_city_id, list_city_name, list_city_commune_communeName, list_city_commune_districtName,
                                list_city_commune_provinceName, list_addressStreet);
                        idList.add(list_id);
                    }

                    for (int i = 0; i < idList.size(); i++) {
                        getDataByStationId(idList.get(i));
                        getSensorDataByStationId(idList.get(i));
                    }

                    dataBaseManager.close();
                }

                placeAllMarkersOnMap(coordinatesMap);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                text_map.setText("Code" + t.getMessage());

            }
        });
    }

    private String cleanString( String input) {
        String output = input.substring(1, input.length()-1);
        return output;
    }

    private void getDataByStationId(int id) {
        HashMap<String, String> coordinatesMap = new HashMap<>();
        DataBaseManager dataBaseManager = new DataBaseManager(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gios.gov.pl/pjp-api/rest/station/sensors/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<JsonArray> call = jsonPlaceHolderApi.getDataByStationId(id);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (!response.isSuccessful()) {
                    text_map.setText("Code" + response.code());
                    return;
                }

                for (int i = 0; i < response.body().size(); i++) {

                    int list_id = Integer.parseInt(response.body().get(i).getAsJsonObject().get("id").toString());
                    int list_stationId = Integer.parseInt(response.body().get(i).getAsJsonObject().get("stationId").toString());

                    String list_param_paramName = response.body().get(i).getAsJsonObject().get("param").getAsJsonObject().get("paramName").toString();
                    String list_param_paramFormula = response.body().get(i).getAsJsonObject().get("param").getAsJsonObject().get("paramFormula").toString();
                    String list_param_paramCode = response.body().get(i).getAsJsonObject().get("param").getAsJsonObject().get("paramCode").toString();
                    int list_param_idParam = Integer.parseInt(response.body().get(i).getAsJsonObject().get("param").getAsJsonObject().get("idParam").toString());

//                    text_map.append(response.body().get(i).getAsJsonObject().get("id").toString());
                    dataBaseManager.addDataStationById(list_id, list_stationId, list_param_paramName, list_param_paramFormula, list_param_paramCode, list_param_idParam);
                }
                dataBaseManager.close();

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                text_map.setText("Code" + t.getMessage());

            }
        });
    }

    private void getSensorDataByStationId(int id) {

        DataBaseManager dataBaseManager = new DataBaseManager(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.gios.gov.pl/pjp-api/rest/data/getData/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<JsonObject> call = jsonPlaceHolderApi.getSensorDataByStationId(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    text_map.setText("Code" + response.code());
                    return;
                }

                    String key = response.body().get("key").toString();
                    for (int j = 0; j < response.body().getAsJsonObject().get("values").getAsJsonArray().size(); j++) {
                        String values_date = response.body().getAsJsonObject().get("values").getAsJsonArray().get(j).getAsJsonObject().get("date").toString();
                        String values_value = response.body().getAsJsonObject().get("values").getAsJsonArray().get(j).getAsJsonObject().get("value").toString();
                        dataBaseManager.addSensorDataByStationId(id, key, values_date, values_value);

                    }

                dataBaseManager.close();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.062439, 19.936183),10));
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

    @Override
    public void onLocationChanged(Location location) {
        startlat = location.getLatitude();
        startlon = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Integer clickAction = (Integer) marker.getTag();
        int count = 0;



        return false;
    }
}
