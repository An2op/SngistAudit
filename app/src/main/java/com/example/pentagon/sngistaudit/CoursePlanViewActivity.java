package com.example.pentagon.sngistaudit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import static com.example.pentagon.sngistaudit.Utility.Courseplanactivity;

public class CoursePlanViewActivity extends AppCompatActivity {
    private ArrayList<CoursePlanData> station;

    private RecyclerViewAdapterCoursePlan adapter1;
    private String noid="-1";
    private String sem="-1";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);
        recyclerView=(RecyclerView) findViewById(R.id.sub);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        if(getIntent().getStringExtra("id")!=null){
            noid=getIntent().getStringExtra("id");
            sem=getIntent().getStringExtra("sid");
        }
        ((TextView)findViewById(R.id.textView)).setText("Course plans");
      Button add=  ((Button)findViewById(R.id.add));
        if(Utility.USERTYPE.equals("FACULTY")){

           add.setVisibility(View.VISIBLE);
        }
       add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CoursePlanViewActivity.this,AddCoursePlanActivity.class);

                startActivity(i);
                finish();
            }
        });
        Courseplanactivity=this;
        getNotification();
    }
    public void getNotification() {


        Map<String, String> params = new HashMap<>();
        //{"subject_id":"-1","staff_id":"-1"}
        params.put("subject_id", noid);

        params.put("staff_id", sem);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.courseplanurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(CoursePlanViewActivity.this, new ResponseCallback() {
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

                    station=new ArrayList<>();
                    try {
                        if (jsonObj.has("courseplan")) {
                            JSONArray login_array = jsonObj.getJSONArray("courseplan");
                            //{"courseplan":[{"course_plan_id":"1","title":"test","date":"2018-03-21 17:37:36","subject_id":"1","staff_id":"bindhu","file_path":"http:\/\/lapisapps.in\/auditing\/courseplan\/1521634056.pdf"}]}
                            for(int i=0;i<login_array.length();i++){
                                CoursePlanData dd=new CoursePlanData();
                                actor = login_array.getJSONObject(i);
                                dd.setCourse_plan_id(actor.getString("course_plan_id"));

                                dd.setTitle(actor.getString("title"));
                                dd.setDate(actor.getString("date"));
                                dd.setSubject_id(actor.getString("subject_name"));
                                dd.setStaff_id(actor.getString("staff_name"));
                                dd.setFile_path(actor.getString("file_path"));

//                                dd.setCourse_id(actor.getString("course_id"));
//                                dd.setSemester(actor.getString("semester"));
                                station.add(dd);

                            } }
                        //    {"station":[{"station_id":"kattappana","pincode":"686687",
//            "district_id":"3","address":"Kattappana","phone":"04898745854","station_type":"2","password":""},


                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(CoursePlanViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(CoursePlanViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
    private void setView() {


        adapter1 = new RecyclerViewAdapterCoursePlan(CoursePlanViewActivity.this,station);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(CoursePlanViewActivity.this, 1,GridLayoutManager.VERTICAL,false);
        // mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManager);
        //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(CoursePlanViewActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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
//Utility.showpdf(CoursePlanViewActivity.this,station.get(position).getFile_path());
            }

        }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(CoursePlanViewActivity.this,HomeActivity.class);
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
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }




            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
