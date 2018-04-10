package com.example.pentagon.sngistaudit.Rectrofit;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by az-sys on 19/8/17.
 */

public interface Web_Interface {
   @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
   @POST("{id}")
   Call<JsonObject> getfromServer(@Path("id") String groupId, @Body Map<String, String> params);//@Field("id")String id, @Field("phone_number")String phone_number

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("{id}")
    Call<JsonObject> getfromServersave(@Path("id") String groupId, @Body String params);//@Field("id")String id, @Field("phone_number")String phone_number

    @Headers("Cache-Control: max-age=640000")
    @GET("{id}")
    Call<JsonObject> getcategory(@Path("id") String groupId);//@Field("id")String id, @Field("phone_number")String phone_number





}
