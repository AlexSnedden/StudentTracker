package net.pinaz993.studenttracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class OneDriveHandlerDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_drive_handler_demo);

        WebView webview = new WebView(this);
        setContentView(webview);

        OnedriveHandler onedrivehandler = new OnedriveHandler();
        webview.loadUrl(onedrivehandler.getUserAuthenticationUrl());

    }

}
