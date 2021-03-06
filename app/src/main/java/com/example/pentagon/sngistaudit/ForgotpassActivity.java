package com.example.pentagon.sngistaudit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.pentagon.sngistaudit.Rectrofit.ResponseCallback;
import com.example.pentagon.sngistaudit.Rectrofit.ResponseHandler;
import com.example.pentagon.sngistaudit.Rectrofit.Retrofit_Helper;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;





public class ForgotpassActivity extends AppCompatActivity {
    EditText uname,pass,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        uname=(EditText)findViewById(R.id.username);
        pass=(EditText)findViewById(R.id.password);
        type=(EditText)findViewById(R.id.usertype);
        type.setFocusable(false);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ForgotpassActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popupfile, popup.getMenu());

                // popup.getMenu().add("ADMIN");

                popup.getMenu().add("STUDENT");
                popup.getMenu().add("FACULTY");
//                popup.getMenu().add("HOD");
//                popup.getMenu().add("PRINCIPAL");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        type.setText(menuItem.getTitle());

//                        if(menuItem.getTitle().equals("Remove")){
//                            //  Toast.makeText(StudentViewActivity.this, "Remove", Toast.LENGTH_SHORT).show();
//
//                        }
//
//
//
//                        else if(menuItem.getTitle().equals("Edit")){
//                            //  Toast.makeText(StudentViewActivity.this, "Remove", Toast.LENGTH_SHORT).show();
//
//                        }
                        if(menuItem.getTitle().equals("STUDENT")){

                            uname.setHint("User name");
                        }else
                            uname.setHint("User name");
                        return false;
                    }
                });

                popup.show();
            }
        });
    }
    public void tologin(View view) {
        int log=0;
        if(TextUtils.isEmpty(uname.getText())){
            log=1;
            uname.setError("Field required");
        }

        if(TextUtils.isEmpty(pass.getText())){
            pass.setError("Field required");
            log=1;

        }
        if(TextUtils.isEmpty(type.getText())){
            type.setError("Field required");
            log=1;

        }
        if(log==0) login(uname.getText().toString(),pass.getText().toString());
    }
    public void login(String uname, String pword){


        Map<String, String> params = new HashMap<>();
        //type => STUDENT
//        regno
//                email
//
//
//        type => STAFF
//                staffcode
//        email

        params.put("uid", uname);
        params.put("email", pass.getText().toString());
        params.put("type", type.getText().toString());

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer("forgotpwd.php",params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(ForgotpassActivity.this, new ResponseCallback() {
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
                        if (jsonObj.has("status")) {
                            // JSONArray login_array = jsonObj.getJSONArray("status");
                            //       actor = jsonObj.getJSONObject("user");
//                            Log.i("new_loginid_456", "" + actor.getInt("USER_ID"));
                            if(jsonObj.getString("status").equals("true")){


                                //  {"user":[{"staff_id":"1","name":"benny","address":"piravom","phone":"9847131977","department_id":"1","date_of_joining":"2018-03-20 00:00:00","experience":"2","username":"aaa","password":"123"}]}

                                Intent i = null;
                                Toast.makeText(ForgotpassActivity.this, "Check Your email for password", Toast.LENGTH_SHORT).show();
                                i = new Intent(ForgotpassActivity.this, LoginActivity.class);


                                startActivity(i);
                                finish();


                            }else {

                                Toast.makeText(ForgotpassActivity.this, "Retry", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(ForgotpassActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ForgotpassActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ForgotpassActivity.this, LoginActivity.class);
        startActivity(i);

        finish();

    }
}