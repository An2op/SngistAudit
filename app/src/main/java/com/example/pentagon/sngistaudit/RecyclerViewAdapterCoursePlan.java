package com.example.pentagon.sngistaudit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pentagon.sngistaudit.Rectrofit.ResponseCallback;
import com.example.pentagon.sngistaudit.Rectrofit.ResponseHandler;
import com.example.pentagon.sngistaudit.Rectrofit.Retrofit_Helper;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class RecyclerViewAdapterCoursePlan extends RecyclerView.Adapter<RecyclerViewAdapterCoursePlan.MyViewHolder> {

    private Context mContext;
    private List<CoursePlanData> albumList;
private int type;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView stationid,address,phone,pincode,stationtype;
        public ImageView thumbnail, btnrequest;
public EditText reply;
public Button dlt,dwld;
        public MyViewHolder(View view) {
            super(view);
            stationid = (TextView) view.findViewById(R.id.station_id);

            phone = (TextView) view.findViewById(R.id.phone);
            address = (TextView) view.findViewById(R.id.address);
            pincode = (TextView) view.findViewById(R.id.pincode);
            stationtype = (TextView) view.findViewById(R.id.station_type);
            dlt=(Button)view.findViewById(R.id.delete);
            dwld=(Button)view.findViewById(R.id.downlord);
            //status = (TextView) view.findViewById(R.id.status);

        }
    }


    public RecyclerViewAdapterCoursePlan(Context mContext, List<CoursePlanData> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.type=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courseplan_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
       final CoursePlanData product = albumList.get(position);
       if(Utility.USERTYPE.equals("STUDENT")) {
           holder.stationid.setText("Title:" + product.getTitle());
           holder.stationtype.setText("Staff::"+product.getStaff_id());

       }else{

           holder.dlt.setVisibility(View.VISIBLE);
           holder.stationid.setText(product.getTitle());}
        holder.stationtype.setVisibility(View.GONE);
        holder.address.setText("Subject:"+product.getSubject_id());
        holder.pincode.setText("Date:"+product.getDate());

        holder.dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
deletearrang(product.getCourse_plan_id());
            }
        });
        holder.dwld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Log.e("ddd","ddd");
try{
                    new Download(mContext, product.getFile_path()).execute();
                }catch (Exception e){e.printStackTrace();}
            }
        });
    //    holder.phone.setText("Email:"+product.getEmail());
     //   holder.file.setTag(product.getImage());
        //  holder.shipaddress.setText("shipping_address:"+product.getOrder_shipping_address());
    }



    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        popup.getMenuInflater().inflate(R.menu.s, popup.getMenu());
////        MenuInflater inflater = popup.getMenuInflater();
////        inflater.inflate(R.menu.menu_product, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
    private void deletearrang(final String noidd) {


        Map<String, String> params = new HashMap<>();
        //{"course_plan_id":""}
        params.put("course_plan_id", noidd);



        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.courseremoveurl,params);
        jsonObjectCall.clone().enqueue(new ResponseHandler(mContext, new ResponseCallback() {
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
                        if (jsonObj.has("plan")) {
                            if(jsonObj.getString("plan").equals("true")){
                                Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
//for(int i=0;i<station.size();i++){
//
//    if(station.get(i).getFaculty_id().equals(noid)){
//        station.remove(i);
//        break;
//
//    }
//}
                                Utility.Courseplanactivity.getNotification();
                                //  Log.i("ddd",station.size()+"");
//adapter1.notifyDataSetChanged();
//setView();

                                //  JSONArray login_array = jsonObj.getJSONArray("faculty");

                            }else {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();


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

                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mContext, "Can't Connect with server", Toast.LENGTH_SHORT).show();

                }
                if( ResponseHandler.progressDialog!=null)
                    ResponseHandler.progressDialog.dismiss();

            }


        }, jsonObjectCall,1));
    }

    private class Download extends AsyncTask<String, Void, String>
    {
        ProgressDialog mProgressDialog;

        Context context;
        String urlDownload;

        public Download(Context context,String url)
        {
            this.context = context;
            this.urlDownload=url;
        }

        protected void onPreExecute()
        {
            mProgressDialog = ProgressDialog.show(context, "","Please wait");
            Log.v("DOWNLOAD", "Wait for downloading url : " + urlDownload);
        }

        protected String doInBackground(String... params)
        {
            try
            {
                //URL url = new URL("http://www.mediacollege.com/downloads/sound-effects/urban/factory/Factory_External_01.mp3");
                URL url = new URL(urlDownload);

                Log.w( "DOWNLOAD" , "URL TO CALL : " + url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                File folder = new File(Environment.getExternalStorageDirectory()+ "/"+context.getString(R.string.app_name)) ;

                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdir();
                }

                File file = new File(folder,"Course"+urlDownload.substring(urlDownload.lastIndexOf("/")+1));

                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file
                int totalSize = urlConnection.getContentLength();
                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the prgress, like this maybe
                    //updateProgress(downloadedSize, totalSize);
                    Log.w( "DOWNLOAD" , "progress " + downloadedSize + " / " + totalSize);

                }
                //close the output stream when done
                fileOutput.close();

                //catch some possible errors...
            }
            catch (MalformedURLException e)
            {
                Log.e( "DOWNLOAD" , "ERROR : " + e );
            }
            catch (IOException e)
            {
                Log.e( "DOWNLOAD" , "ERROR : " + e );
            }
            return "done";
        }

        private void publishProgress( int i )
        {
            Log.v("DOWNLOAD", "PROGRESS ... " + i);
        }

        protected void onPostExecute(String result)
        {
            if (result.equals("done"))
                mProgressDialog.dismiss();
            Toast.makeText(mContext, "Downloaded", Toast.LENGTH_SHORT).show();
        }
    }
}
