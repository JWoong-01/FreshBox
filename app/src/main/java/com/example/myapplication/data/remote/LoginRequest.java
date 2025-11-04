package com.example.myapplication.data.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest extends JsonObjectRequest {

    private static final String URL = ServerConfig.BASE_URL + "/api/auth/login";

    public LoginRequest(String userID, String userPassword,
                        Response.Listener<JSONObject> listener,
                        Response.ErrorListener errorListener) {
        super(Method.POST, URL, createBody(userID, userPassword), listener, errorListener);
    }

    private static JSONObject createBody(String userID, String userPassword) {
        JSONObject body = new JSONObject();
        try {
            body.put("userId", userID);
            body.put("password", userPassword);
        } catch (JSONException e) {
            throw new IllegalArgumentException("로그인 요청 바디 생성 실패", e);
        }
        return body;
    }
}
