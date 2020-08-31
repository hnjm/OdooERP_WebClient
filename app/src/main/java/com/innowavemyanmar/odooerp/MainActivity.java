package com.innowavemyanmar.odooerp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView wb = findViewById(R.id.web_view);
        final ProgressBar pgb = findViewById(R.id.progress_bar);
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        wb.loadUrl(get_url());
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setDomStorageEnabled(true);

        wb.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wb.setVisibility(View.VISIBLE);
                pgb.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                fab.setVisibility(View.VISIBLE);
                String offlineMessageHtml = "<html><body><center><p style='margin-top:100px;'>Error Loading</p><br><span>1.Check your internet connection.<br>2.Check your server address.<br>3.Contact your software provider.<br>Your Server Address : " +
                        get_url() +
                        "</span></center></body></html>";
                wb.loadData(offlineMessageHtml, "text/html", "utf-8");
                Toast.makeText(getApplicationContext(),"NOPE",Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        adb.setView(R.layout.exit);
        adb.setCancelable(false);

        final AlertDialog ad = adb.create();
        ad.show();

        Button yes = ad.findViewById(R.id.yes);
        Button no = ad.findViewById(R.id.no);

        assert yes != null;
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        assert no != null;
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    public void showAlert(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        adb.setView(R.layout.get_url);
        adb.setCancelable(false);

        final AlertDialog ad = adb.create();
        ad.show();

        Button save = ad.findViewById(R.id.save);
        Button cancel = ad.findViewById(R.id.cancel);
        final EditText url = ad.findViewById(R.id.url);

        assert save != null;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert url != null;
                if (!url.getText().toString().trim().equals("")){
                    SharedPreferences.Editor shp = getSharedPreferences("url",MODE_PRIVATE).edit();
                    shp.putString("url",url.getText().toString().trim());
                    shp.apply();
                    Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                    ad.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Empty URL",Toast.LENGTH_SHORT).show();
                }
            }
        });

        assert cancel != null;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    private String get_url(){
        SharedPreferences shp = getSharedPreferences("url",MODE_PRIVATE);
        return shp.getString("url","http://192.168.100.24:8069/web");
    }
}