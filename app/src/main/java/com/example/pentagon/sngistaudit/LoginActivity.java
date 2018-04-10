package com.example.pentagon.sngistaudit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.pentagon.sngistaudit.Rectrofit.ResponseCallback;
import com.example.pentagon.sngistaudit.Rectrofit.ResponseHandler;
import com.example.pentagon.sngistaudit.Rectrofit.Retrofit_Helper;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {
    EditText uname,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uname=(EditText)findViewById(R.id.username);
        pass=(EditText)findViewById(R.id.password);

        try {


            Utility.USERTYPE=new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_TYPE, Context.MODE_PRIVATE)).getPropertyType();


            Utility.USER_NAME=new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_USERNAME, Context.MODE_PRIVATE)).getPropertyUsername();

            Utility.PASSWORD=new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_PASSWORD, Context.MODE_PRIVATE)).getPropertyPassword();

            if(!Utility.USER_NAME.equals("")){
                Intent i = null;

               i = new Intent(LoginActivity.this, HomeActivity.class);



                startActivity(i);

                finish();


            }


        }catch (Exception e){}



        ((Button)findViewById(R.id.login))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tologin();
                    }
                });

    }
    public void login(String uname, String pword){


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("uid", uname);
        params.put("upwd", pword);


        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.loginurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(LoginActivity.this, new ResponseCallback() {
            @Override
            public void getResponse(int code, JsonObject jsonObject) {

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//{"users":[{"admin_id":"admin","password":"admin"}]}

                //JSONObject jsonObj = new JSONObject(datafromserver);

                if (jsonObj != null) {
                    Log.d("getfromserver", "b4 extract");
                    JSONObject actor = null;
                    try {
                        if (jsonObj.has("user")) {
                            JSONArray login_array = jsonObj.getJSONArray("user");
                            //       actor = jsonObj.getJSONObject("user");
//                            Log.i("new_loginid_456", "" + actor.getInt("USER_ID"));
                            if(login_array.length()>0){
                                actor = login_array.getJSONObject(0);
                              //  {"user":[{"user_id":"admin","password":"admin","role":"ADMIN"}]}
                                Utility.USER_NAME=actor.getString("user_id");
                                Utility.PASSWORD=actor.getString("password");
                                Utility.USERTYPE=actor.getString("role");
                                new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_USERNAME, Context.MODE_PRIVATE)).setPropertyUsername(Utility.USER_NAME);
                                new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_PASSWORD, Context.MODE_PRIVATE)).setPropertyPassword(Utility.PASSWORD);
                                new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_TYPE, Context.MODE_PRIVATE)).setPropertyType(Utility.USERTYPE);
                               Intent i=null;

                                    i = new Intent(LoginActivity.this, HomeActivity.class);




startActivity(i);
                                finish();}else {

                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                            }
                        }



                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }

                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(LoginActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }

    public void gotsignup(View view) {

    }

    public void tologin() {
        int log=0;
        if(TextUtils.isEmpty(uname.getText())){
            log=1;
            uname.setError("Enter user name");
        }

        if(TextUtils.isEmpty(pass.getText())){
            pass.setError("Enter password");
            log=1;

        }
        if(log==0) login(uname.getText().toString(),pass.getText().toString());
    }

    public void forgotpass(View view) {
        Intent i = new Intent(LoginActivity.this, ForgotpassActivity.class);


        startActivity(i);

        finish();

    }

//    public void signup(View view) {
//
//        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
//        Bundle bb=new Bundle();
//       // i.putExtra("type", "new");
//        startActivity(i);
//
//        finish();
//    }

}