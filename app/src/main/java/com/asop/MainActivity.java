package com.asop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asop.widget.LockView;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView tv;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
    }

    public void clk(View v) {
        Toast.makeText(this, "this si zhe base apk 基准包", Toast.LENGTH_SHORT).show();
//        CrashReport.testJavaCrash();
    }
}

