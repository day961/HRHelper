package com.uqute.helper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import java.net.CookieManager;

/**
 * Created by suoday on 13-7-19.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);


    }
}
