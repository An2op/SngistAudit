package com.example.pentagon.sngistaudit.Rectrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;


import com.example.pentagon.sngistaudit.Utility;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by az-sys on 19/8/17.
 */

public class ResponseHandler implements Callback<JsonObject> {

    Context context;
    ResponseCallback web_interface;
    Call<JsonObject> jsonObjectCall;
    int show;


    public static ProgressDialog progressDialog;



    public ResponseHandler(Context context, ResponseCallback web_interface, Call<JsonObject> jsonObjectCall, int n) {
        this.context = context;
        show=n;
        this.web_interface = web_interface;
        this.jsonObjectCall = jsonObjectCall;
        String ss=context.getClass().getName();
Log.i("acttt",context.getClass().getName());
//        Toast.makeText(context,ss, Toast.LENGTH_SHORT).show();

    //   if((context.getClass().getName().contains("LoginActivity"))||(context.getClass().getName().contains("RegistrationActivity"))||(context.getClass().getName().contains("SavedPcFragment"))) {
        if(show==1) {

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading.....");
            progressDialog.setCancelable(false);
            progressDialog.show();



//           progressDialog = new Dialog(context);
//           progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//           progressDialog.setContentView(R.layout.dialog);
//           ImageView imageView = (ImageView) progressDialog.findViewById(R.id.image);
//           GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
//           Glide.with(context).load(R.drawable.loadingcc1).into(imageViewTarget);
//         //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//           progressDialog.setCancelable(false);
//           progressDialog.show();

      }
    }



    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


        if(show==1) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        switch (response.code()) {
            case Responsecode.ok:
                if (web_interface != null) {
                    if (response.body() != null) {
                        web_interface.getResponse(response.code(), response.body());
                    }
                }

                break;

            case Responsecode.server_error:
               // Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Internal Server Error", Snackbar.LENGTH_SHORT);

                showalert("Server Error");
                break;

            case Responsecode.notfound:
                // Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Internal Server Error", Snackbar.LENGTH_SHORT);

                showalert("Not found");
                break;
        }

    }

    @Override
    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
//        if(context.getClass().getName().contains("LoginActivity")) {
//        if(progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }}
        int code;
        if (!Retrofit_Helper.isNetworkAvailable(context)) {

            // CustomDialogUI.dialog(context,"Try again","No Internet Connection");
            //   showalert("No Internet Connection");
//            MyAppUtility.showToast("No Internet Connection");
            code=1;
        } else {
            //  Utility.alertdialoguePasswordChange(context,"Sign_in","You have changed your password. Please login with your new password");

           // MyAppUtility.showToast("Can't Connect with server");
            code=2;
            //  showalert("Can't Connect with server");


        }
web_interface.getFailure(call,code);


    }


    private void showalert(String message) {
        final android.app.AlertDialog.Builder builder = Utility.alertdialog(context,"",message);
       // final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
       //
        // builder.setTitle("Autograde");
      //  builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                // new ResponseHandler(context,ResponseHandler.this,jsonObjectCall);
                jsonObjectCall.clone().enqueue(new ResponseHandler(context, web_interface, jsonObjectCall,show));

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


}
