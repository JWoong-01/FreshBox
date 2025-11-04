package com.example.myapplication.data.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterRequest extends JsonObjectRequest {

    private static final String URL = ServerConfig.BASE_URL + "/api/auth/register";

    public RegisterRequest(String userID, String userPassword, String userName, int userAge,
                           Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener) {
        super(Method.POST, URL, createBody(userID, userPassword, userName, userAge), listener, errorListener);
    }

    private static JSONObject createBody(String userID, String userPassword, String userName, int userAge) {
        JSONObject body = new JSONObject();
        try {
            body.put("userId", userID);
            body.put("password", userPassword);
            body.put("name", userName);
            body.put("age", userAge);
        } catch (JSONException e) {
            throw new IllegalArgumentException("회원가입 요청 바디 생성 실패", e);
        }
        return body;
    }
}
