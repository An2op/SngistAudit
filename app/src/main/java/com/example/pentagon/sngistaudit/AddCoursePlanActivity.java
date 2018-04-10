package com.example.pentagon.sngistaudit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.example.pentagon.sngistaudit.Rectrofit.ResponseHandler.progressDialog;

public class AddCoursePlanActivity extends AppCompatActivity {
File file;
    private String imagepath;
TextView txtfile;
    String resp;
EditText title;
Spinner subject;
    private Spinner spsubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_plan);
        txtfile=(TextView) findViewById(R.id.textView2);
        spsubject=(Spinner)findViewById(R.id.subject) ;
        title=(EditText)findViewById(R.id.title) ;
        getSubject();
        txtfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtfile.getText().toString().equals("")){
                    viewpdfFromfile();
                }
                //Utility.showpdf(AddCoursePlanActivity.this,"");
            }
        });
        permission();

    }


    public void viewpdfFromfile(){

       // File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ filename);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }
    public void permission(){

        if (ContextCompat.checkSelfPermission(AddCoursePlanActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddCoursePlanActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(AddCoursePlanActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(AddCoursePlanActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {

            if (data == null)
                //Display an error
                return;
            Uri    uri = data.getData();
//FileProvider.getUriForFile(this,R.string.file_provider_authority,);
            Uri selectedImage = data.getData();
            Log.i("ColumnName32",selectedImage.toString());
            imagepath=getPath(this,uri);
            Log.i("ColumnName32",selectedImage.getPath());

            if(imagepath==null){

                Toast.makeText(AddCoursePlanActivity.this, "Select from another location", Toast.LENGTH_SHORT).show();
                return;}
            Log.d("FILEPATH9999",imagepath+"");



       file = new File(imagepath);
           // final Uri imageUri = Uri.fromFile(file1);

            txtfile.setText(imagepath);




        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddCoursePlanActivity.this, HomeActivity.class);
        startActivity(i);

        finish();
        super.onBackPressed();
    }

    public void selectpdf(View view) {

        if (ContextCompat.checkSelfPermission(AddCoursePlanActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {

                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),200);
            } catch (Exception ex) {
                System.out.println("browseClick :"+ex);//android.content.ActivityNotFoundException ex
            }

        }





        else {
            permission();
        }

    }
    ProgressDialog progressDialog;
    public void saveplan(View view) {


        JSONObject jsondata = new JSONObject();
        final JSONArray arr = new JSONArray();

        final String[] response = {null};

        final String[] res = {""};
        progressDialog = new ProgressDialog(AddCoursePlanActivity.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Thread th = new Thread() {
            public void run() {
                String charset = "UTF-8";
//                File uploadFile1 = null;
//                File uploadFile2 = null;
//                File uploadFile3 = null;


                //    File uploadFile2 = new File("e:/Test/PIC2.JPG");
                String requestURL = "";

                requestURL ="http://lapisapps.in/auditing/"+ "addcourseplan.php";
                Log.i("requestURL", requestURL);
                try {
                    //{"subject_id":"","title":"",”staff_id”:””,”photo”:””}
                    MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                    multipart.addHeaderField("User-Agent", "CodeJava");
                    multipart.addHeaderField("Test-Header", "Header-Value");
                    multipart.addFormField("staff_id", Utility.USER_NAME);
                    multipart.addFormField("title", title.getText().toString());
                    multipart.addFormField("subject_id",((SubjectData)spsubject.getSelectedItem()).getSubject_id());
//                    multipart.addFormField("latitude", Utility.lat);
//                    for(int j=0;j<imagefile.size();j++){
//
//                        multipart.addFilePart("photo"+j, imagefile.get(j).getImagename());
//                    }
                    multipart.addFilePart("photo", txtfile.getText().toString());

                    resp = multipart.finish();
                    Log.i("SERVERREPLIED:" , resp);
//                JSONObject obj = new JSONObject(resp);
//
//                response[0] = obj.getString("result");


                } catch (IOException ex) {
                    System.err.println(ex);
                }


                runOnUiThread(new Runnable() {
                    public void run() {
                        if(Integer.parseInt(resp)>0){
                            progressDialog.dismiss();
                            Toast.makeText(AddCoursePlanActivity.this, "Posted Sucess", Toast.LENGTH_SHORT).show();

                            txtfile.setText("");
                            imagepath="";
title.setText("");

//                            Intent i = new Intent(getContext(), MainActivity.class);
//                            startActivity(i);
//
//                            getActivity().finish();
                        }else {
                            Toast.makeText(AddCoursePlanActivity.this, "Unsucess", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }                  }
                });
            }
        };

        th.start();


    }
    private void getSubject() {


        Map<String, String> params = new HashMap<>();
        //jObj = new JSONObject();
        params.put("sem", "-1");



        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.subjecturl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(AddCoursePlanActivity.this, new ResponseCallback() {
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
                                new ArrayAdapter<SubjectData>(AddCoursePlanActivity.this,R.layout.support_simple_spinner_dropdown_item,station));



                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                    //   setView();
                }
            }
            @Override
            public void getFailure(Call<JsonObject> call, int code) {
                if (code==1) {

                    Toast.makeText(AddCoursePlanActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AddCoursePlanActivity.this, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }
    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else
                if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
