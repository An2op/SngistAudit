package com.example.pentagon.sngistaudit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.pentagon.sngistaudit.Rectrofit.ResponseCallback;
import com.example.pentagon.sngistaudit.Rectrofit.ResponseHandler;
import com.example.pentagon.sngistaudit.Rectrofit.Retrofit_Helper;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class StudentViewActivity extends AppCompatActivity {
    private ArrayList<StudentData> station;
    private ArrayList<StudentData> stationback;
    private RecyclerViewAdapterStudents adapter1;
    private String noid="-1";
    private String sem="-1";
    String as[]={"All","1","2","3","4","5","6","7","8"};
    private RecyclerView recyclerView;
Spinner sphall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);
        recyclerView=(RecyclerView) findViewById(R.id.sub);
        if(getIntent().getStringExtra("id")!=null){
            noid=getIntent().getStringExtra("id");
            sem=getIntent().getStringExtra("sid");
        }
      Button add=  ((Button)findViewById(R.id.add));

        if(Utility.USERTYPE.equals("ADMIN")){

           // add.setVisibility(View.VISIBLE);
        }
       add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i=new Intent(StudentViewActivity.this,AddStudentActivity.class);
//Utility.backactivity=StudentViewActivity.this;
//                startActivity(i);
//                finish();
            }
        });

        getNotification(noid);


      //  getNotification(noid);
    }
    private void getNotification(String noid) {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("id", noid);

        params.put("sem", sem);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.studentsurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(StudentViewActivity.this, new ResponseCallback() {
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
                    stationback=new ArrayList<>();
                    station=new ArrayList<>();
                    try {
                        if (jsonObj.has("student")) {
                            JSONArray login_array = jsonObj.getJSONArray("student");
//    [{"student_id":"std","name":"stdn","address":"addr","phone":"654125"
//            ,"email":"std@gmail.com","join_date":"2018-06-03","course_id":"bsc","semester":"6"}]}
//{"student":[{"student_id":"std","name":"stdn","address":"ekm","phone":"6541258","email":"std@gmail.com"}]}
                            for(int i=0;i<login_array.length();i++){
                                StudentData dd=new StudentData();
                                actor = login_array.getJSONObject(i);
                                dd.setStudent_id(actor.getString("student_id"));

                                dd.setName(actor.getString("name"));
                                dd.setAddress(actor.getString("address"));
                                dd.setPhone(actor.getString("phone"));
                                dd.setEmail(actor.getString("email"));
//                                dd.setJoin_date(actor.getString("join_date"));
//                                dd.setCourse_id(actor.getString("course_id"));
//                                dd.setSemester(actor.getString("semester"));
                                station.add(dd);

                            } }
                        //    {"station":[{"station_id":"kattappana","pincode":"686687",
//            "district_id":"3","address":"Kattappana","phone":"04898745854","station_type":"2","password":""},
                        stationback=station;

                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                   setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(StudentViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(StudentViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
    private void setView() {


        adapter1 = new RecyclerViewAdapterStudents(StudentViewActivity.this,station);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(StudentViewActivity.this, 1,GridLayoutManager.VERTICAL,false);
        // mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManager);
        //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(StudentViewActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

//                    PopupMenu popup = new PopupMenu(StudentViewActivity.this, view);
//                    //Inflating the Popup using xml file
//                    popup.getMenuInflater().inflate(R.menu.popupfile, popup.getMenu());
//                if (Utility.USERTYPE.equals("ADMIN"))
//                    popup.getMenu().add("Remove");
//                    popup.getMenu().add("Attendence");
//                    popup.getMenu().add("Mark");
//                if (Utility.USERTYPE.equals("ADMIN"))
//                    popup.getMenu().add("Edit");
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem menuItem) {
//
//
//                            if (menuItem.getTitle().equals("Remove")) {
//                                //  Toast.makeText(StudentViewActivity.this, "Remove", Toast.LENGTH_SHORT).show();
//                                remove(station.get(position).getStudent_id());
//                            } else if (menuItem.getTitle().equals("Attendence")) {
//                                //  Toast.makeText(StudentViewActivity.this, "Remove", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(StudentViewActivity.this, AttendenceViewActivity.class);
//                                i.putExtra("id", station.get(position).getStudent_id());
//                                i.putExtra("sid", station.get(position).getSemester());
//
//                                startActivity(i);
//                                finish();
//                            } else if (menuItem.getTitle().equals("Mark")) {
//                                //  Toast.makeText(StudentViewActivity.this, "Remove", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(StudentViewActivity.this, MarkViewActivity.class);
//                                i.putExtra("id", station.get(position).getStudent_id());
//                                i.putExtra("sid", station.get(position).getSemester());
//
//                                startActivity(i);
//                                finish();
//                            } else if (menuItem.getTitle().equals("Edit")) {
//                                //  Toast.makeText(StudentViewActivity.this, "Remove", Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(StudentViewActivity.this, AddStudentActivity.class);
//                                i.putExtra("id", station.get(position).getStudent_id());
//                                i.putExtra("sid", station.get(position).getSemester());
//                                Utility.backactivity = StudentViewActivity.this;
//                                startActivity(i);
//                                finish();
//                            }
//                            return false;
//                        }
//                    });
//
//                    popup.show();

            }
            @Override
            public void onLongItemClick(View view, int position) {

            }

        }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(StudentViewActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }
//    private void remove(final String noidd) {
//
//
//        Map<String, String> params = new HashMap<>();
//        //jObj = new JSONObject();
//        params.put("id", noidd);
//
//        params.put("sem", sem);
//
//        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.studentremoveurl,params);
//        jsonObjectCall.clone().enqueue(new ResponseHandler(StudentViewActivity.this, new ResponseCallback() {
//            @Override
//            public void getResponse(int code, JsonObject jsonObject) {
//
//                JSONObject jsonObj = null;
//                try {
//                    jsonObj = new JSONObject(jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////{"users":[{"admin_id":"admin","password":"admin"}]}
//
//                //JSONObject jsonObj = new JSONObject(datafromserver);
//
//                if (jsonObj != null) {
//                    Log.d("getfromserver", "b4 extract");
//                    JSONObject actor = null;
//
//                    station=new ArrayList<>();
//                    try {
//                        if (jsonObj.has("student")) {
//                            if(jsonObj.getString("student").equals("true")){
//                                Toast.makeText(StudentViewActivity.this, "Removed", Toast.LENGTH_SHORT).show();
//                                getNotification(noid);
//                            }
//                            //  JSONArray login_array = jsonObj.getJSONArray("faculty");
////getNotification(noid);
//                        else {
//                            Toast.makeText(StudentViewActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//
//
//                        }}
//
//                        //    {"station":[{"station_id":"kattappana","pincode":"686687",
////            "district_id":"3","address":"Kattappana","phone":"04898745854","station_type":"2","password":""},
//
//
//                    } catch (Exception eee) {
//                        eee.printStackTrace();
//                    }
//                    setView();
//                }
//            }
//            @Override
//            public void getFailure(Call<JsonObject> call, int code) {
//                if (code==1) {
//
//                    Toast.makeText(StudentViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(StudentViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();
//
//                }
//                if( ResponseHandler.progressDialog!=null)
//                    ResponseHandler.progressDialog.dismiss();
//
//            }
//
//
//        }, jsonObjectCall,1));
//    }

    public void addstudent(View view) {

    }
}
