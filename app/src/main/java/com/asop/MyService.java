package com.asop;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by 黄海 on 2017/5/15.
 */

public class MyService extends Service {
    private static final String TAG ="MyService" ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
