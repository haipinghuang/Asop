package com.asop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clk(View v) {
        Log.d(TAG, "clk() called with: v = [" + v + "]");
//        startService(new Intent(this,MyService.class));
        startActivity(new Intent("com.asop.subactivity"));
    }
}

