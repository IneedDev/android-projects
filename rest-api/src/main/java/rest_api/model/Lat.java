package rest_api.model;

import java.util.HashMap;

public class Lat {

    private static HashMap<String, String> hashMap;

    public Lat() {
    }

    public static HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public static void setHashMap(HashMap<String, String> hashMap) {
        Lat.hashMap = hashMap;
    }


}
