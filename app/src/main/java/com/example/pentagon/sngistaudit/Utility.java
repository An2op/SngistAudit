package com.example.pentagon.sngistaudit;

/**
 * Created by asus on 14-Sep-17.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by root on 17/4/17.
 */

public class Utility {

    public static String USER_NAME = "";
    public static String PASSWORD = "";
    public static String SKIP = "";
    public static String USERID="";

    public static String loginurl="login.php";

    public static String USERTYPE="";
public static Context backactivity;
    public static boolean alert=false;
    public static String studentsurl="viewstudent.php";
    public static AttendenceViewActivity attendenceViewActivity;
    public static String addatturl="addattendance.php";
    public static String attremoveurl="removeattendance.php";
    public static String attendenceurl="viewattendance.php";
    public static MarkViewActivity MarkViewActivity;
    public static String subjecturl="getdata.php";
    public static String markremoveurl="removemark.php";
    public static String addmarkurl="addmark.php";
    public static String markssurl="viewmark.php";
    public static AssignmentViewActivity AssignmentActivity;
    public static String assignmentsurl="viewassignmentmark.php";
    public static String assremoveurl="removeassignmentmark.php";
    public static String addassurl="addassignmentmark.php";
    public static String courseplanurl="viewcourseplan.php";
    public static String courseremoveurl="removecourseplan.php";
    public static CoursePlanViewActivity Courseplanactivity;
    public static String remdysurl="viewremedialclass.php";
    public static String tutorialsurl="viewtutorialclass.php";
    public static String addtutorialurl="addtutorialclass.php";
    public static String addremdyurl="addremedialclass.php";
    public static String remodyremoveurl="removeclass.php";
    public static String tutorialremoveurl="removeclass.php";
    public static TClassViewActivity classActivity;
    public static RClassViewActivity classActivityr;

//    public static DataUser userdata=null;
    //  public static ArrayList<Dataloc> dataloc;
   // public static ArrayList<DataComplaint> datacomplaint;


    public static void nextActivity(Activity activity, Class nextClass){
        Intent intent = new Intent(activity.getApplicationContext(),nextClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.getApplicationContext().startActivity(intent);
        activity.finish();
    }

    public static void toastShow(Context context, String msg){
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
    }


    public static AlertDialog.Builder alertdialog(Context c, String title, String msg)
    {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //alertDialog.setView(inflater.inflate(R.layout.deleteallmessagerequest_dialog, null));
        assert inflater != null;
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.alertdialog, null);
        alertDialog.setView(dialogView);

        TextView tt=(TextView)dialogView.findViewById(R.id.titleq);
        tt.setText(title);
        TextView ms=(TextView)dialogView.findViewById(R.id.msg);
        ms.setText(msg);

        return alertDialog;
    }
//public static void setproblemcheck(Context context){
//
//    context.startService(new Intent(context, ProblemService.class));
//    Calendar cal = Calendar.getInstance();
//    Intent intent = new Intent(context, ProblemService.class);
//    PendingIntent pintent = PendingIntent
//            .getService(context, 0, intent, 0);
//
//    AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//    // Start service every 20 seconds
//    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//            20* 10, pintent);
//
//}

public static void showpdf(final Activity context, String path){

    final Dialog dd=new Dialog(context);
    dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dd.setContentView(R.layout.dialog_pdfview);

            WebView webviewer = (WebView)dd.findViewById(R.id.webview);
        webviewer.getSettings().setJavaScriptEnabled(true);

    webviewer.setWebViewClient(new WebViewClient() {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
        }
        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
        }
    });


        webviewer.loadUrl(path);
      //  dd.setContentView(webviewer);
    dd.setCancelable(true);

    dd.show();

}
}