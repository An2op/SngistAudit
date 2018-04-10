package com.example.pentagon.sngistaudit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button vstudent =(Button)findViewById(R.id.student);
        Button vremdy=(Button)findViewById(R.id.vclassremdy);
        Button vclass=(Button)findViewById(R.id.vclass);
        if(Utility.USERTYPE.equals("STUDENT")){
            ((TextView)findViewById(R.id.textView2)).setText("Home");
            vstudent.setVisibility(View.INVISIBLE);
          //  vremdy.setVisibility(View.GONE);
          //  vclass.setVisibility(View.GONE);
        }else
            ((TextView)findViewById(R.id.textView2)).setText("Home");
    }

    public void viewstudent(View view) {
        Intent i = new Intent(HomeActivity.this,StudentViewActivity.class);
        startActivity(i);

        finish();
    }

    public void viewmark(View view) {
        Intent i = new Intent(HomeActivity.this,MarkViewActivity.class);
        if(Utility.USERTYPE.equals("STUDENT"))
           i.putExtra("id",Utility.USER_NAME);
        startActivity(i);

        finish();
    }

    public void attendence(View view) {
        Intent i = new Intent(HomeActivity.this,AttendenceViewActivity.class);
        if(Utility.USERTYPE.equals("STUDENT"))
            i.putExtra("id",Utility.USER_NAME);
        startActivity(i);

        finish();
    }

    public void courseplan(View view) {
        Intent i = new Intent(HomeActivity.this,CoursePlanViewActivity.class);
        startActivity(i);

        finish();
    }

    public void viewclass(View view) {
        Intent i = new Intent(HomeActivity.this,TClassViewActivity.class);
        startActivity(i);

        finish();

    }

    public void logout(View view) {
        Intent i = new Intent(HomeActivity.this, LoginActivity.class);

        startActivity(i);
        new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_USERNAME, Context.MODE_PRIVATE)).setPropertyUsername("");
        new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_PASSWORD, Context.MODE_PRIVATE)).setPropertyPassword("");
        new SharedPreferenceClass(getSharedPreferences(SharedPreferenceClass.PROPERTY_TYPE, Context.MODE_PRIVATE)).setPropertyType("");
        finish();
    }

    public void assignment(View view) {
        Intent i = new Intent(HomeActivity.this,AssignmentViewActivity.class);
        if(Utility.USERTYPE.equals("STUDENT"))
            i.putExtra("id",Utility.USER_NAME);
        startActivity(i);

        finish();


    }

    public void remdycls(View view) {
        Intent i = new Intent(HomeActivity.this,RClassViewActivity.class);
        startActivity(i);

        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.Signout) {
            Intent dbmanager = new Intent(HomeActivity.this,ChangePassActivity.class);
            startActivity(dbmanager);
            finish();
            return true;
        }

//        if (id == R.id.action_settings) {
//      Utility.showUrl(this);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
