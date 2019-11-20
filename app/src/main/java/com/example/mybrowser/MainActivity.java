package com.example.mybrowser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    EditText addrBar;
    String address = "";
    ProgressBar prog;
    String myTitle = "NatBrowserPro";
    Intent myIntent;
    Intent bookmarksIntent;
    Intent historyIntent;

    ArrayList<String> bookmarks;
    WebBackForwardList webHistory;
    ArrayList<String> webHistoryList;
    WebSettings webSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(myTitle);

        prog = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webview);
        addrBar = findViewById(R.id.addrBar);

        bookmarks = new ArrayList<>();
        webHistoryList = new ArrayList<>();
        //Log.d("nat", "new arraylist");

        webView.setWebViewClient(new MyWebViewClient());
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        addrBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KEYCODE_ENTER){
                    address = addrBar.getText().toString();
                    webView.loadUrl(address);
                    closeKeyboard();
                }
                return false;
            }
        });


        //closeKeyboard();
        address = addrBar.getText().toString();
        webView.loadUrl(address);
        //addrBar.clearFocus();

        myIntent = getIntent();
        try{
            address = myIntent.getStringExtra("address");
            webView.loadUrl(address);
        }
        catch(Exception e){
            Log.d("nat", "error in intent");
        }

        //fab = findViewById(R.id.fab);
        //fab.setOnClickListener(l -> webView.loadUrl("http://www.bing.com"));
        //fab.setVisibility(View.GONE);

        displaySnackbar("You are running NatBrowserPro developed by Nat.H, October 2019");
        prog.setVisibility(View.INVISIBLE);



    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("nat", "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("nat", "onResume");
        restoreState();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("nat", "onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("nats", "onStop");
        saveState();
    }

    private void saveState(){

        Log.d("nat", "save "+bookmarks.size());

        /* Save bookmarks to shared prefs */
        if(!bookmarks.isEmpty()){
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref1.edit();
            for(int i=0; i<bookmarks.size(); i++) {
                editor.putString(Integer.toString(i), bookmarks.get(i));
            }
            editor.putInt("bookmark_length", bookmarks.size());
            editor.commit();
        }

        /* Save history to shared prefs */
        if(!webHistoryList.isEmpty()){
            Log.d("nat", "save history");
            SharedPreferences pref2 = getApplicationContext().getSharedPreferences("history", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref2.edit();
            for(int i=0; i<webHistoryList.size(); i++) {
                editor.putString(Integer.toString(i), webHistoryList.get(i));
            }
            editor.putInt("history_length", webHistoryList.size());
            editor.commit();
        }

    }

    private void restoreState(){

        Log.d("nat", "retore "+bookmarks.size());

        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        int bookmark_size = pref1.getInt("bookmark_length",0);
        if(bookmark_size > 0){
            for(int i=0; i<bookmark_size; i++) {
                //String s = pref.getString(Integer.toString(i), "test"+i);
                bookmarks.add(pref1.getString(Integer.toString(i), "test"+i));
                //Log.d("nat", s);
            }
        }

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("history", Context.MODE_PRIVATE);
        int history_size = pref2.getInt("history_length",0);
        if(history_size > 0){
            for(int i=0; i<history_size; i++) {
                //String s = pref.getString(Integer.toString(i), "test"+i);
                webHistoryList.add(pref2.getString(Integer.toString(i), "test"+i));
                Log.d("nat", "load history "+pref2.getString(Integer.toString(i), "test"+i));
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("natsuki", "onDestroy");
    }


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void displayToastMsg(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void displaySnackbar(String msg){
        Snackbar bar = Snackbar.make(this.webView, msg, Snackbar.LENGTH_LONG);
        bar.show();
    }

    private void switchToBookmarkActivity(){
        Log.d("nat", "go to bookmarks "+bookmarks.size());

        if(!bookmarks.isEmpty()) {
            bookmarksIntent = new Intent(MainActivity.this, Main2Activity.class);
            bookmarksIntent.putStringArrayListExtra("bookmarks", bookmarks);

            startActivity(bookmarksIntent);
            finish();
        }
        else{
            displayToastMsg("You have no bookmarks");
            return;
        }

    }

    private void switchToHistoryActivity(){

        Log.d("nat", "go to history "+bookmarks.size());

        webHistory = webView.copyBackForwardList();

        // to prevent overwrite
        webHistoryList.addAll(convertWebHistory(webHistory));
        //webHistoryList = (convertWebHistory(webHistory));
        //webHistoryList = convertWebHistory(webHistory);
        //Log.d("nat", "update web hist");

        if(!webHistoryList.isEmpty()){
            //Log.d("nat", "open history page");
            historyIntent = new Intent(MainActivity.this, Main3Activity.class);
            historyIntent.putStringArrayListExtra("history", webHistoryList /*convertWebHistory(webHistory)*/);
            startActivity(historyIntent);
            finish();
        }
        else{
            displayToastMsg("You have no browsing history");
            return;
        }

    }

    private ArrayList<String> convertWebHistory(WebBackForwardList hist){

        ArrayList<String> data = new ArrayList<String>();

        for(int i=0; i<hist.getSize(); i++){
            data.add(i, hist.getItemAtIndex(i).getUrl());
        }

        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(R.id.searchMenuItem):{
                //webView.goBack();
                webView.loadUrl("http://www.bing.com");
                return true;
            }
            case(R.id.backMenuItem):{
                webView.goBack();
                return true;
            }
            case(R.id.forwardMenuItem):{
                webView.goForward();
                return true;
            }
            case(R.id.refreshMenuItem):{
                webView.reload();
                return true;
            }
            case(R.id.addBookmarkMenuItem):{
                addBookmark();
                return true;
            }
            case(R.id.bookmarkMenuItem):{
                switchToBookmarkActivity();
                return true;
            }
            case(R.id.clearBookmarkMenuItem):{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Do you want to clear bookmarks?").setCancelable(false)
                        .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                bookmarks.clear();

                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref1.edit();
                                editor.clear();
                                editor.commit();

                                displayToastMsg("Bookmarks cleared");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle("Clear bookmarks");
                alert.show();

                return true;
            }
            case(R.id.historyMenuItem):{
                switchToHistoryActivity();
                return true;
            }
            case(R.id.clearHistoryMenuItem):{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to clear your browsing history?").setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        webHistoryList.clear();

                                        SharedPreferences pref2 = getApplicationContext().getSharedPreferences("history", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref2.edit();
                                        editor.clear();
                                        editor.commit();

                                        displayToastMsg("History cleared");
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle("Clear history");
                alert.show();

                return true;
            }
            case(R.id.exitMenuItem):{
                finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // Check if the key event was the Back button and if there's history
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        }
//        // If it wasn't the Back key or there's no web page history, bubble up to the default
//        // system behavior (probably exit the activity)
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }


    private void addBookmark(){
        webHistory = webView.copyBackForwardList();
        address = webHistory.getCurrentItem().getUrl();

        if(!bookmarks.contains(address)) {
            bookmarks.add(address);
            displayToastMsg("Bookmark saved");
        }
        else{
            displayToastMsg("Page is already bookmarked");
        }
        Log.d("nat", webHistory.getCurrentItem().getUrl());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ((Uri.parse(url).getHost()) != null) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            prog.setVisibility(View.VISIBLE);
            prog.setProgress(50, true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            prog.setProgress(100, true);
            address = webView.getUrl();
            addrBar.setText(address);
            setTitle(webView.getTitle()+" ~ "+myTitle);

            prog.setVisibility(View.INVISIBLE); // GONE will remove the space also

            closeKeyboard();

        }
    }


}
