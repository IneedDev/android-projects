package rest_api.model;

import com.google.gson.annotations.SerializedName;

public class Post {
    private int userId;

    private int id;


    @SerializedName("body")
    private String text;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }


    public String getText() {
        return text;
    }
}
