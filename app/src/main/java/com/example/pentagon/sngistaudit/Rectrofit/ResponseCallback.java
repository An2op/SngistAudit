package com.example.pentagon.sngistaudit.Rectrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;

/**
 * Created by az-sys on 19/8/17.
 */

public interface ResponseCallback {

    public void getResponse(int code, JsonObject jsonObject);

   public void  getFailure(Call<JsonObject> call, int code);


}
