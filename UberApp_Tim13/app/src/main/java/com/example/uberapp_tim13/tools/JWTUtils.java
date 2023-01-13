package com.example.uberapp_tim13.tools;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class JWTUtils {
    public static String decode(String token){
        try {
            String[] split = token.split("\\.");
            String header = getJson(split[0]);
            String body = getJson(split[1]);
            Log.d("REZ", body);
            return body;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserRoleFromToken(String jsonToken) throws JSONException {
        JSONObject tokenObject = new JSONObject(jsonToken);
        JSONArray roleArray = tokenObject.getJSONArray("role");
        JSONObject roleObject = roleArray.getJSONObject(0);
        String role = roleObject.getString("authority");
        return role.split("_")[1].toLowerCase(Locale.ROOT);
    }

    public static int getUserIdFromToken(String jsonToken) throws JSONException {
        JSONObject tokenObject = new JSONObject(jsonToken);
        int id = tokenObject.getInt("id");
        return id;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
