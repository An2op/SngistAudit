package com.example.pentagon.sngistaudit;

import android.content.Context;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class RecyclerViewAdapterAttendence extends RecyclerView.Adapter<RecyclerViewAdapterAttendence.MyViewHolder> {

    private Context mContext;
    private List<AtttendenceData> albumList;
private int type;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView hid,sem,vacancy,status;
        public ImageView thumbnail, btnrequest;
        public EditText reply;
        public Button delete;
        public MyViewHolder(View view) {
            super(view);
            hid = (TextView) view.findViewById(R.id.hid);

            sem = (TextView) view.findViewById(R.id.sem);
            vacancy = (TextView) view.findViewById(R.id.vacancy);
            status = (TextView) view.findViewById(R.id.status);
            delete = (Button) view.findViewById(R.id.delete);
            //status = (TextView) view.findViewById(R.id.status);

        }
    }


    public RecyclerViewAdapterAttendence(Context mContext, List<AtttendenceData> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.type=type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendence_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
       final AtttendenceData product = albumList.get(position);
        holder.hid.setText(product.getDate());
        holder.sem.setText(product.getHour());
        holder.vacancy.setText(product.getStudent());
        Log.i("stid",product.getStudent()+"");
        holder.status.setText(product.getStatus());
        if(Utility.USERTYPE.equals("FACULTY"))
            holder.delete.setVisibility(View.VISIBLE);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletearrang(product.getAttendance_id());
            }
        });
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
        //jObj = new JSONObject();
        params.put("attendanceid", noidd);



        Call<JsonObject> jsonObjectCall = new Retrofit_Helper().getRetrofitBuilder().getfromServer(Utility.attremoveurl,params);
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
                        if (jsonObj.has("attendance")) {
                            if(jsonObj.getString("attendance").equals("true")){
                                Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
//for(int i=0;i<station.size();i++){
//
//    if(station.get(i).getFaculty_id().equals(noid)){
//        station.remove(i);
//        break;
//
//    }
//}
                                Utility.attendenceViewActivity.getNotification();
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
}
