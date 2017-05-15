package com.backbase.weather_challenge.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.backbase.weather_challenge.R;

public class HelpActivity extends AppCompatActivity {

    private Context myContext;
    private WebView helpWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        myContext = this;
        helpWebView = (WebView) findViewById(R.id.helpWebView);
        helpWebView.getSettings().setJavaScriptEnabled(true);
        helpWebView.loadUrl("file:///android_asset/hello.html");

    }


}
