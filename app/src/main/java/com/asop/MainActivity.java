package com.asop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asop.widget.LockView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView tv;
    Button btn;
    LockView lockView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
        lockView = (LockView) findViewById(R.id.lockView);
        lockView.setInputGestureCode("123456").setCallback(new LockView.OnGestureCallback() {
            @Override
            public void onGestureInput(String gestureCode) {
                Log.d(TAG, "onGestureInput() called with: gestureCode = [" + gestureCode + "]");
            }

            @Override
            public void onCheckedResult(boolean result) {
                Log.d(TAG, "onCheckedResult() called with: result = [" + result + "]");
                lockView.clearDrawlineStart(500);
            }
        });
    }

    public void clk(View v) throws IOException {

    }
}

