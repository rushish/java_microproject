package com.vidyalankar.hackathon;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView view;
    private ProgressBar progressbar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SETTINGS//
        view = findViewById(R.id.webview);
        view.getSettings().setAppCacheEnabled(true);
        WebSettings webSettings= view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        progressbar=findViewById(R.id.progressbar);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
            Dialog dialog = new Dialog(this);
            dialog.setContentView((R.layout.no_internet_dialog));
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations =
                    android.R.style.Animation_Dialog;
            Button btnretry = dialog.findViewById(R.id.btn_try_again);
            btnretry.setOnClickListener(v -> recreate());

            dialog.show();

        } else{
            view.loadUrl("https://rush-media.in/");
        }

        // Error Dialog Box //

        view.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view,
                                        WebResourceRequest request,
                                        WebResourceError error) {
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()){
                    Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView((R.layout.no_internet_dialog));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations =
                            android.R.style.Animation_Dialog;
                    Button btnretry = dialog.findViewById(R.id.btn_try_again);
                    btnretry.setOnClickListener(v -> recreate());

                    dialog.show();
                    MainActivity.this.view.setVisibility(View.GONE);

                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressbar.setVisibility(View.VISIBLE);

                // EMAIL & PHONE NUMBER EXTERNAL LINKS //

                if(url.contains("mailto:") || url.contains("sms:") || url.contains("tel:") )
                {
                    MainActivity.this.view.stopLoading();
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(View.GONE);

                // EMAIL & PHONE NUMBER EXTERNAL LINKS //

                if(url.contains("mailto:") || url.contains("sms:") || url.contains("tel:") )
                {
                    MainActivity.this.view.stopLoading();
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }
        });


    }



    @Override
    public void onBackPressed(){
        if(view.canGoBack()) {
            view.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}