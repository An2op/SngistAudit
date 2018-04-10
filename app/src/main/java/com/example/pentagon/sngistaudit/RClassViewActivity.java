package com.example.pentagon.sngistaudit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class RClassViewActivity extends AppCompatActivity {
    private ArrayList<ClassData> station;
public static String head;
    private RecyclerViewAdapterClass adapter1;
    private String noid="-1";
    private String sem="-1";
    private RecyclerView recyclerView;
    private Spinner spstudent,spsubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);
        recyclerView=(RecyclerView) findViewById(R.id.sub);
        if(getIntent().getStringExtra("id")!=null){
            noid=getIntent().getStringExtra("id");
            sem=getIntent().getStringExtra("sid");
        }
        ((TextView)findViewById(R.id.textView)).setText("Remedial Class");
      Button add=  ((Button)findViewById(R.id.add));
        if(Utility.USERTYPE.equals("FACULTY")){

            add.setVisibility(View.VISIBLE);
        }
        Utility.classActivityr=this;
       add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
addAttendence();
            }
        });
        getNotification();
    }
    public void getNotification() {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("id", noid);

        params.put("sem", sem);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.remdysurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(RClassViewActivity.this, new ResponseCallback() {
            @Override
            public void getResponse(int code, JsonObject jsonObject) {

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//{"users":[{"admin_id":"admin","password":"admin"}]}

                //{"class":[{"batch_id":"1","staff_id":"1","subject_id":"1","semester":"1","course_id":"1","date":"2018-12-12","class_details":"tesstt","class_type":"Evening"}]}

                if (jsonObj != null) {
                    Log.d("getfromserver", "b4 extract");
                    JSONObject actor = null;

                    station=new ArrayList<>();
                    try {
                        if (jsonObj.has("class")) {
                            JSONArray login_array = jsonObj.getJSONArray("class");
//    [{"student_id":"std","name":"stdn","address":"addr","phone":"654125"
//            ,"email":"std@gmail.com","join_date":"2018-06-03","course_id":"bsc","semester":"6"}]}
//{"student":[{"student_id":"std","name":"stdn","address":"ekm","phone":"6541258","email":"std@gmail.com"}]}
                            for(int i=0;i<login_array.length();i++){
                                ClassData dd=new ClassData();
                                actor = login_array.getJSONObject(i);
                                dd.setBatch_id(actor.getString("batch_id"));

                                dd.setStaff_id(actor.getString("staff_id"));
                                dd.setSubject_id(actor.getString("subject_id"));
                                dd.setSemester(actor.getString("semester"));
                                dd.setCourse_id(actor.getString("course_id"));
                                dd.setDate(actor.getString("date"));
                                dd.setClass_details(actor.getString("class_details"));
                               // dd.setClass_type(actor.getString("class_type"));

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

                    Toast.makeText(RClassViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RClassViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,0));
    }
    private void setView() {


        adapter1 = new RecyclerViewAdapterClass(RClassViewActivity.this,station,"remedial");
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(RClassViewActivity.this, 1,GridLayoutManager.VERTICAL,false);
        // mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManager);
        //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(RClassViewActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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
        Intent i=new Intent(RClassViewActivity.this,HomeActivity.class);
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

    private void getCourse() {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("sem", "-1");



        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.subjecturl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(RClassViewActivity.this, new ResponseCallback() {
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

                    ArrayList<SubjectData>  station=new ArrayList<>();
                    try {
                        if (jsonObj.has("course")) {
                            JSONArray login_array = jsonObj.getJSONArray("course");
                            //  {"course":[{"course_id":"1","name":"BCA"}]
                            for(int i=0;i<login_array.length();i++){
                                SubjectData dd=new SubjectData();
                                actor = login_array.getJSONObject(i);
                                dd.setSubject_id(actor.getString("course_id"));
                                //     dd.setCourse_id(actor.getString("course_id"));
                                dd.setName(actor.getString("name"));
                                //  dd.setSemester(actor.getString("semester"));


                                station.add(dd);

                            } }
                        //    {"station":[{"station_id":"kattappana","pincode":"686687",
//            "district_id":"3","address":"Kattappana","phone":"04898745854","station_type":"2","password":""},

                        spstudent.setAdapter(
                                new ArrayAdapter<SubjectData>(RClassViewActivity.this,R.layout.support_simple_spinner_dropdown_item,station));



                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    //   setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(RClassViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RClassViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,0));
    }
    public void addAttendence() {
        final Dialog dd=new Dialog(RClassViewActivity.this);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.setContentView(R.layout.dialogaddassignment_class);
        //   dd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dd.setCancelable(true);
        ((TextView)dd.findViewById(R.id.head)).setText("Remedical Class");
        spstudent=(Spinner)dd.findViewById(R.id.spstudent);
        spsubject=(Spinner)dd.findViewById(R.id.hall);
        final EditText date=(EditText)dd.findViewById(R.id.date);
        date.setFocusable(false);
        final EditText type=(EditText)dd.findViewById(R.id.type);
        type.setVisibility(View.GONE);
        final EditText details=(EditText)dd.findViewById(R.id.details);


        getSubject();
        final EditText sem=(EditText)dd.findViewById(R.id.sem);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        date.setText(day + "-" + (month + 1) + "-" + year);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dd = new DatePickerDialog(RClassViewActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date.setText(day + "-" + (month + 1) + "-" + year);

                    }
                }, year, month, day);
//        DialogFragment newFragment = new SaleOrdersFragment.DatePickerFragment();
//
//        newFragment.show(getFragmentManager(),"timePicker");
                // tempdate=dateto;
                dd.show();

            }
        });
        ((Button)dd.findViewById(R.id.add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(sem.getText().toString())||TextUtils.isEmpty(details.getText().toString()))
                {
                    Toast.makeText(RClassViewActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();

                }
                else{

                    newatt(spstudent.getSelectedItem().toString(),spsubject.getSelectedItem().toString(),sem.getText().toString(),date.getText().toString(),details.getText().toString(),type.getText().toString());
                    dd.dismiss();
                }
            }
        });
        dd.show();

    }
    private void getSubject() {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("sem", "-1");



        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.subjecturl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(RClassViewActivity.this, new ResponseCallback() {
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

                    ArrayList<SubjectData>  station=new ArrayList<>();
                    try {
                        if (jsonObj.has("subject")) {
                            JSONArray login_array = jsonObj.getJSONArray("subject");
                            //  {"subject":[{"subject_id":"1","course_id":"1","name":"Networking","semester":"3"}]}
                            for(int i=0;i<login_array.length();i++){
                                SubjectData dd=new SubjectData();
                                actor = login_array.getJSONObject(i);
                                dd.setSubject_id(actor.getString("subject_id"));
                                dd.setCourse_id(actor.getString("course_id"));
                                dd.setName(actor.getString("name"));
                                dd.setSemester(actor.getString("semester"));


                                station.add(dd);

                            } }
                        //    {"station":[{"station_id":"kattappana","pincode":"686687",
//            "district_id":"3","address":"Kattappana","phone":"04898745854","station_type":"2","password":""},

                        spsubject.setAdapter(
                                new ArrayAdapter<SubjectData>(RClassViewActivity.this,R.layout.support_simple_spinner_dropdown_item,station));

                        getCourse();

                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    //   setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(RClassViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RClassViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }

    private void newatt(String stid, String subid, String sem, String date, String details, String type) {
        // {"staff_id":"","subject_id":"",”sem”:””,”date”:””,”details”:””,”type”:””,”course_id”:””}
        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("staff_id", Utility.USER_NAME);
        params.put("subject_id", subid);
        params.put("sem", sem);
        params.put("details", details);
        params.put("date", date);
        params.put("course_id", stid);
     //   params.put("type", type);

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.addremdyurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(RClassViewActivity.this, new ResponseCallback() {
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
                        if (jsonObj.has("class")) {
                            if(jsonObj.getString("class").equals("true")){
                                Toast.makeText(RClassViewActivity.this, "Added", Toast.LENGTH_SHORT).show();
//for(int i=0;i<station.size();i++){
//
//    if(station.get(i).getFaculty_id().equals(noid)){
//        station.remove(i);
//        break;
//
//    }
//}
                                getNotification();
                                Log.i("ddd",station.size()+"");
//adapter1.notifyDataSetChanged();
//setView();

                                //  JSONArray login_array = jsonObj.getJSONArray("faculty");

                            }else {
                                Toast.makeText(RClassViewActivity.this, "Failed", Toast.LENGTH_SHORT).show();


                            }}


                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    //     setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(RClassViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(RClassViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
}
