package com.example.pentagon.sngistaudit;

import android.content.Context;
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

public class ChangePassActivity extends AppCompatActivity {
    EditText uname,pass,type,oldpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        uname=(EditText)findViewById(R.id.username);
        pass=(EditText)findViewById(R.id.password);
        oldpass=(EditText)findViewById(R.id.oldpassword);
        type=(EditText)findViewById(R.id.usertype);
        type.setFocusable(false);
//        type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popup = new PopupMenu(ChangePassActivity.this, view);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.popupfile, popup.getMenu());
//
//                // popup.getMenu().add("ADMIN");
//
//                popup.getMenu().add("STUDENT");
//                popup.getMenu().add("STAFF");
////                popup.getMenu().add("HOD");
////                popup.getMenu().add("PRINCIPAL");
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        type.setText(menuItem.getTitle());
//
////                        if(menuItem.getTitle().equals("Remove")){
////                            //  Toast.makeText(StudentViewActivity.this, "Remove", Toast.LENGTH_SHORT).show();
////
////                        }
////
////
////
////                        else if(menuItem.getTitle().equals("Edit")){
////                            //  Toast.makeText(StudentViewActivity.this, "Remove", Toast.LENGTH_SHORT).show();
////
////                        }
//                        if(menuItem.getTitle().equals("STUDENT")){
//
//                            uname.setHint("Register number");
//                        }else
//                            uname.setHint("User name");
//                        return false;
//                    }
//                });
//
//                popup.show();
//            }
//        });
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
        if(TextUtils.isEmpty(oldpass.getText())){
            pass.setError("Field required");
            log=1;

        }
        if(log==0) {
            if(oldpass.getText().toString().equals(Utility.PASSWORD))

                login(uname.getText().toString(),pass.getText().toString());
            else
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
        }
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
//        npwd
//                opwd

        params.put("npwd", pass.getText().toString());
        params.put("opwd", oldpass.getText().toString());
        params.put("uid", uname);
//        if(type.getText().toString().equals("STUDENT")) {
//            params.put("type", "STUDENT");
//            params.put("regno", uname);
//        }else{
//            params.put("staff_id", Utility.USER_NAME);
//            params.put("uname", uname);
//            params.put("type", "STAFF");
//
//        }
        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer("changepwd.php",params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(ChangePassActivity.this, new ResponseCallback() {
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
                                Utility.USERID="";
                                Utility.USER_NAME ="";
                                Utility.PASSWORD = "";
                                Utility.USERTYPE = "";
                                new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_USERNAME, Context.MODE_PRIVATE)).setPropertyUsername(Utility.USER_NAME);
                                new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_PASSWORD, Context.MODE_PRIVATE)).setPropertyPassword(Utility.PASSWORD);
                                new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_TYPE, Context.MODE_PRIVATE)).setPropertyType(Utility.USERTYPE);
                                new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_TYPE, Context.MODE_PRIVATE)).setPropertyUserid(Utility.USERID);
                                Intent i = null;
                                Toast.makeText(ChangePassActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();
                                i = new Intent(ChangePassActivity.this, LoginActivity.class);


                                startActivity(i);
                                finish();


                            }else {

                                Toast.makeText(ChangePassActivity.this, "Retry", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(ChangePassActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ChangePassActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ChangePassActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }
}

