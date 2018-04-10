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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class AttendenceViewActivity extends AppCompatActivity {
    private ArrayList<AtttendenceData> station;
Spinner spstudent,spsubject;
    private RecyclerViewAdapterAttendence adapter1;
    private String noid="-1";
    private String sem="-1";
    Spinner sphall;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_view);
        recyclerView=(RecyclerView) findViewById(R.id.sub);
        if(getIntent().getStringExtra("id")!=null){
            noid=getIntent().getStringExtra("id");
            sem=getIntent().getStringExtra("sid");

        }
        Utility.attendenceViewActivity=this;
        String as[]={"All","1","2","3","4","5","6","7","8"};
        sphall=(Spinner)findViewById(R.id.hall) ;
        sphall.setAdapter(
                new ArrayAdapter<String>(AttendenceViewActivity.this,R.layout.support_simple_spinner_dropdown_item,as));



        sphall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {




                Object item1 = adapterView.getItemAtPosition(i);
                if (item1 instanceof String) {

                    sem = (String) item1;
                    if(sem.equals("All"))
sem="-1";


                    getNotification();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
      //  getNotification(noid);

        Button add=((Button)findViewById(R.id.add));
        if(Utility.USERTYPE.equals("FACULTY"))
            add.setVisibility(View.VISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAttendence();
            }
        });
    }
    public void getNotification() {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("stid", noid);
        params.put("sem", sem);
       // {"stid":"-1","sem":"-1"}


        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.attendenceurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(AttendenceViewActivity.this, new ResponseCallback() {
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
                        if (jsonObj.has("attendance")) {
                            JSONArray login_array = jsonObj.getJSONArray("attendance");
                            //    {"mark":[{"attendance_id":"1","student_id":"std","subject_id":"1"
//            ,"date":"2018-03-02 00:00:00","hour":"3","status":"Present","semester":"3"}]}
                            for(int i=0;i<login_array.length();i++){
                                AtttendenceData dd=new AtttendenceData();
                                actor = login_array.getJSONObject(i);
                                dd.setAttendance_id(actor.getString("attendance_id"));
                                dd.setStudent_id(actor.getString("student_id"));
                               dd.setStudent(actor.getString("student_name"));
                                dd.setSubject_id(actor.getString("subject_id"));
                                dd.setDate(actor.getString("date"));
                                dd.setHour(actor.getString("hour"));
                                dd.setStatus(actor.getString("status"));
                                dd.setSemester(actor.getString("semester"));
//                                dd.setStatus(actor.getString("experience"));
//                                dd.setStatus(actor.getString("experience"));
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

                    Toast.makeText(AttendenceViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AttendenceViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
    private void setView() {


        adapter1 = new RecyclerViewAdapterAttendence(AttendenceViewActivity.this,station);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(AttendenceViewActivity.this, 1,GridLayoutManager.VERTICAL,false);
        // mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManager);
        //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(AttendenceViewActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
//                Intent i = new Intent(StudentViewActivity.this, CompanyAllActivity.class);
//                i.putExtra("id",station.get(position).getStudent_id());
//                startActivity(i);
//
//                finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(AttendenceViewActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }
    public void addAttendence() {
        final Dialog dd=new Dialog(AttendenceViewActivity.this);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.setContentView(R.layout.dialogadd_attendence);
        //   dd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dd.setCancelable(true);
        spstudent=(Spinner)dd.findViewById(R.id.spstudent);
         spsubject=(Spinner)dd.findViewById(R.id.hall);
        final EditText date=(EditText)dd.findViewById(R.id.date);
        date.setFocusable(false);
        final EditText time=(EditText)dd.findViewById(R.id.time);
        final RadioButton pre=(RadioButton)dd.findViewById(R.id.present);
        RadioButton abs=(RadioButton)dd.findViewById(R.id.absent);
getStudents("-1");
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
                DatePickerDialog dd = new DatePickerDialog(AttendenceViewActivity.this, new DatePickerDialog.OnDateSetListener() {

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
                if(TextUtils.isEmpty(sem.getText().toString())||TextUtils.isEmpty(time.getText().toString()))
                {
                    Toast.makeText(AttendenceViewActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();

                }
                else{
                    String status;
                    if(pre.isChecked())
                        status="Present";
                    else
                        status="Absent";
                    newatt(((StudentData2)spstudent.getSelectedItem()).getStudent_id(),spsubject.getSelectedItem().toString(),sem.getText().toString(),date.getText().toString(),time.getText().toString(),status);
                    dd.dismiss();
                }
            }
        });
        dd.show();

    }
    private void getStudents(String noid) {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("id", "-1");

        params.put("sem", "-1");

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.studentsurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(AttendenceViewActivity.this, new ResponseCallback() {
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

                    ArrayList<StudentData2>   station=new ArrayList<>();
                    try {
                        if (jsonObj.has("student")) {
                            JSONArray login_array = jsonObj.getJSONArray("student");
//    [{"student_id":"std","name":"stdn","address":"addr","phone":"654125"
//            ,"email":"std@gmail.com","join_date":"2018-06-03","course_id":"bsc","semester":"6"}]}

                            for(int i=0;i<login_array.length();i++){
                                StudentData2 dd=new StudentData2();
                                actor = login_array.getJSONObject(i);
                                dd.setStudent_id(actor.getString("student_id"));
                                dd.setName(actor.getString("name"));
                                dd.setAddress(actor.getString("address"));
                                dd.setPhone(actor.getString("phone"));
                                dd.setEmail(actor.getString("email"));
//                                dd.setJoin_date(actor.getString("join_date"));
                              //  dd.setCourse_id(actor.getString("course_id"));
                             //   dd.setSemester(actor.getString("semester"));
                                station.add(dd);

                            } }
                        //    {"station":[{"station_id":"kattappana","pincode":"686687",
//            "district_id":"3","address":"Kattappana","phone":"04898745854","station_type":"2","password":""},
                        spstudent.setAdapter(
                                new ArrayAdapter<StudentData2>(AttendenceViewActivity.this,R.layout.support_simple_spinner_dropdown_item,station));


                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    //setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(AttendenceViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AttendenceViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,0));
    }
    private void newatt(String stid, String subid, String sem, String date, String time, String status) {
       // {"student_id":"","sem":"",”date”:””,”subject_id”:””,”hour”:””,”status”:””}
        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("student_id", stid);
        params.put("subject_id", subid);
        params.put("sem", sem);
        params.put("status", status);
        params.put("date", date);
        params.put("hour", time);
        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.addatturl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(AttendenceViewActivity.this, new ResponseCallback() {
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
                        if (jsonObj.has("attendance")) {
                            if(jsonObj.getString("attendance").equals("true")){
                                Toast.makeText(AttendenceViewActivity.this, "Added", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AttendenceViewActivity.this, "Failed", Toast.LENGTH_SHORT).show();


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

                    Toast.makeText(AttendenceViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AttendenceViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
    private void getSubject() {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("sem", "-1");



        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.subjecturl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(AttendenceViewActivity.this, new ResponseCallback() {
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
                                new ArrayAdapter<SubjectData>(AttendenceViewActivity.this,R.layout.support_simple_spinner_dropdown_item,station));



                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    //   setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(AttendenceViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AttendenceViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
}
