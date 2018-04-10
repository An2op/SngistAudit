package com.example.pentagon.sngistaudit;

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
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class MarkViewActivity extends AppCompatActivity {
    private ArrayList<MarksData> station,stback;
    Spinner spstudent,spsubject;
    private RecyclerViewAdapterMarks adapter1;
   public String noid="-1";
    private String sem="-1";
    private RecyclerView recyclerView;
    private Spinner sphall,sptype;
    String as1[]={"All","1","2"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_view);
        LinearLayout ll=(LinearLayout)findViewById(R.id.linearLayout3) ;
        recyclerView=(RecyclerView) findViewById(R.id.sub);
        if(getIntent().getStringExtra("id")!=null){
            noid=getIntent().getStringExtra("id");
            sem=getIntent().getStringExtra("sid");
        }
        Utility.MarkViewActivity=this;

        String as[]={"All","1","2","3","4","5","6","7","8"};

        sphall=(Spinner)findViewById(R.id.hall) ;
        sptype=(Spinner)findViewById(R.id.hall1) ;
        if(!Utility.USERTYPE.equals("STUDENT"))
ll.setVisibility(View.VISIBLE);
else
            ll.setVisibility(View.GONE);


        sptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {




                Object item1 = adapterView.getItemAtPosition(i);
                if (item1 instanceof String) {
                    sem = (String) item1;
                    if(sem.equals("All")){
                        sem="-1";
                        if(station.size()!=stback.size()){
                       station=stback;
                       setView();}
                    }

else {
                        stback=station;
                        station=new ArrayList<>();
                        for(int k=0;k<stback.size();k++){
                            MarksData mm=new MarksData();
                            mm=stback.get(k);
                            if(mm.getInternal_type().equals(sem)){
                                station.add(mm);
                            }

                        }

                        if(station.size()!=stback.size()){

                            setView();}
                    }

                  //


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sphall.setAdapter(
                new ArrayAdapter<String>(MarkViewActivity.this,R.layout.support_simple_spinner_dropdown_item,as));



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
        Button add=((Button)findViewById(R.id.add));
        if(Utility.USERTYPE.equals("FACULTY"))
            add.setVisibility(View.VISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMark();
            }
        });
    }

    private void addMark() {
        final Dialog dd=new Dialog(MarkViewActivity.this);
        dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dd.setContentView(R.layout.dialogadd_mark);
        //   dd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dd.setCancelable(true);
        spstudent=(Spinner)dd.findViewById(R.id.spstudent);
        spsubject=(Spinner)dd.findViewById(R.id.hall);
        final EditText internal=(EditText)dd.findViewById(R.id.internal);
        final EditText mark=(EditText)dd.findViewById(R.id.mark);

        getStudents("-1");
        getSubject();
        final EditText sem=(EditText)dd.findViewById(R.id.sem);

        ((Button)dd.findViewById(R.id.add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(sem.getText().toString())||TextUtils.isEmpty(internal.getText().toString())||TextUtils.isEmpty(mark.getText().toString()))
                {
                    Toast.makeText(MarkViewActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();

                }
                else{

                    newmark(((StudentData2)spstudent.getSelectedItem()).getStudent_id(),((SubjectData)spsubject.getSelectedItem()).getSubject_id(),sem.getText().toString(),mark.getText().toString(),internal.getText().toString());
                    dd.dismiss();
                }
            }
        });
        dd.show();

    }

    public void getNotification() {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("student_id", noid);
        params.put("sem", sem);
       // {"stid":"-1","sem":"-1"}

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.markssurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(MarkViewActivity.this, new ResponseCallback() {
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
                        if (jsonObj.has("mark")) {
                            JSONArray login_array = jsonObj.getJSONArray("mark");
//                            {"mark":[{"mark_id":"1","student_id":"std"
//                                    ,"semester":"3","subject_id":"1","mark":"45","internal_type":"1"}]}

                            for(int i=0;i<login_array.length();i++){
                                MarksData dd=new MarksData();
                                actor = login_array.getJSONObject(i);
                                dd.setMark_id(actor.getString("mark_id"));
                                dd.setStudent_id(actor.getString("student_id"));
                                dd.setSemester(actor.getString("semester"));
                                dd.setSubject_id(actor.getString("subject_id"));
                                dd.setMark(actor.getString("mark"));
                                dd.setInternal_type(actor.getString("internal_type"));
                               // dd.setSubject(actor.getString("subject"));
                               // dd.setName(actor.getString("student"));
                                station.add(dd);

                            } }
                            stback=station;
                        //    {"station":[{"station_id":"kattappana","pincode":"686687",
//            "district_id":"3","address":"Kattappana","phone":"04898745854","station_type":"2","password":""},
                        sptype.setAdapter(
                                new ArrayAdapter<String>(MarkViewActivity.this,R.layout.support_simple_spinner_dropdown_item,as1));


                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(MarkViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MarkViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
    private void setView() {


        adapter1 = new RecyclerViewAdapterMarks(MarkViewActivity.this,station);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MarkViewActivity.this, 1,GridLayoutManager.VERTICAL,false);
        // mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(mLayoutManager);
        //  recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MarkViewActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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
        Intent i=new Intent(MarkViewActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
    }
    private void getStudents(String noid) {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("id", "-1");

        params.put("sem", "-1");

        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.studentsurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(MarkViewActivity.this, new ResponseCallback() {
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
                             //   dd.setJoin_date(actor.getString("join_date"));
                              //  dd.setCourse_id(actor.getString("course_id"));
                              //  dd.setSemester(actor.getString("semester"));
                                station.add(dd);

                            } }
                        //    {"station":[{"station_id":"kattappana","pincode":"686687",
//            "district_id":"3","address":"Kattappana","phone":"04898745854","station_type":"2","password":""},
                        spstudent.setAdapter(
                                new ArrayAdapter<StudentData2>(MarkViewActivity.this,R.layout.support_simple_spinner_dropdown_item,station));


                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    //setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(MarkViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MarkViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,0));
    }
    private void newmark(String stid, String subid, String sem, String mark, String internal) {
        // {"student_id":"","sem":"",”internal”:””,”subject_id”:””,”mark”:””}
        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("student_id", stid);
        params.put("subject_id", subid);
        params.put("sem", sem);

        params.put("mark", mark);
        params.put("internal", internal);
        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.addmarkurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(MarkViewActivity.this, new ResponseCallback() {
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
                        if (jsonObj.has("mark")) {
                            if(jsonObj.getString("mark").equals("true")){
                                Toast.makeText(MarkViewActivity.this, "Added", Toast.LENGTH_SHORT).show();

                                getNotification();
                                Log.i("ddd",station.size()+"");


                            }else {
                                Toast.makeText(MarkViewActivity.this, "Failed", Toast.LENGTH_SHORT).show();


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

                    Toast.makeText(MarkViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MarkViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

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
        jsonObjectCall.clone().enqueue(new ResponseHandler(MarkViewActivity.this, new ResponseCallback() {
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
                            //  ,"subject":[{"subject_id":"1","course_id":"1","name":"Networking","semester":"2"}]
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
                                new ArrayAdapter<SubjectData>(MarkViewActivity.this,R.layout.support_simple_spinner_dropdown_item,station));



                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    //   setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(MarkViewActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MarkViewActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
}
