package com.gtk.smartmanagement.tool;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.gtk.smartmanagement.data.ResultData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class GsonParser {
    private static final String TAG = GsonParser.class.getSimpleName();
    private static Gson gson;

    public static GsonBuilder getBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(String.class, new StringDeserializer())
                .registerTypeAdapter(JSONObject.class, new JsonObjectDeserializer())
                .registerTypeAdapter(JSONArray.class, new JsonArrayDeserializer())
                .registerTypeAdapter(int.class, new IntegerDeserializer())
                .registerTypeAdapter(Integer.class, new IntegerDeserializer());
        return builder;
    }

    public static Gson getParser() {
        if (gson == null) {
            gson = getBuilder().create();
        }
        return gson;
    }

    public static String parser(String str, Type type) {
        return getParser().fromJson(str, type).toString();
    }

    public static ResultData.OrgData[] parserOrg(String str) {
        ResultData.OrgJsonBean json = getParser().fromJson(str, ResultData.OrgJsonBean.class);
        return json.getOrgData();
    }

    public static <T> T deserializeFromJson(String str, Class<T> tClass) {
        try {
            return getParser().fromJson(str, tClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T deserializeFromJson(JSONObject jsonObject, Class<T> tClass) {
        if (jsonObject == null)
            return null;

        try {
            return getParser().fromJson(jsonObject.toString(), tClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> deserializeFromJsonArray(String jsonArrayStr, Class<T> tClass) {
        List<T> retList = new ArrayList<T>();
        if (TextUtils.isEmpty(jsonArrayStr))
            return retList;

        try {
            JsonArray jsonArray = new JsonParser().parse(jsonArrayStr).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                retList.add(getParser().fromJson(jsonElement, tClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retList;
    }

    public static <T> List<T> deserializeFromJsonArray(JSONArray jsonArray, Class<T> tClass) {
        return deserializeFromJsonArray(String.valueOf(jsonArray), tClass);
    }

    public static <T> JSONObject serializeToJson(T data) {
        try {
            String jsonStr = getParser().toJson(data);
            return new JSONObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> JSONArray serializeToJson(List<T> data) {
        try {
            String jsonStr = getParser().toJson(data);
            return new JSONArray(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class StringDeserializer implements JsonDeserializer<String> {
        @Override
        public String deserialize(JsonElement jsonElement, Type type,
                                  JsonDeserializationContext context) throws JsonParseException {
            try {
                return jsonElement.getAsString();
            } catch (Exception e) {
                return jsonElement.toString();
            }
        }
    }

    private static class JsonObjectDeserializer implements JsonDeserializer<JSONObject> {
        @Override
        public JSONObject deserialize(JsonElement jsonElement, Type type,
                                      JsonDeserializationContext context) throws JsonParseException {
            try {
                return new JSONObject(jsonElement.toString());
            } catch (JSONException e) {
                Log.e(TAG, "JsonObject Deserialize fail, string = " + jsonElement.toString());
            }
            return null;
        }
    }

    private static class JsonArrayDeserializer implements JsonDeserializer<JSONArray> {
        @Override
        public JSONArray deserialize(JsonElement jsonElement, Type type,
                                     JsonDeserializationContext context) throws JsonParseException {
            try {
                return new JSONArray(jsonElement.toString());
            } catch (JSONException e) {
                Log.e(TAG, "Array Deserialize fail, string = " + jsonElement.toString());
            }
            return null;
        }
    }

    private static class IntegerDeserializer implements JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement jsonElement, Type type,
                                   JsonDeserializationContext context) throws JsonParseException {
            try {
                return jsonElement.getAsInt();
            } catch (Exception e) {
                try {
                    return (int) jsonElement.getAsDouble();
                } catch (Exception e1) {
                    throw new JsonParseException(e.getMessage());
                }
            }
        }
    }
}
