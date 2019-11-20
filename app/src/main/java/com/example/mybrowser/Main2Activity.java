package com.example.mybrowser;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    String openUrl;
    ListView bookmarkListView;
    ArrayList<String> bookmarks;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("nat","1");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Bookmarks");

        //bookmarks = new ArrayList<>();

        // Get the transferred data from source activity.
        Intent intent = getIntent();
        try{
            bookmarks = intent.getStringArrayListExtra("bookmarks");

//            Log.d("nat", "print out sent array list");
//            for(String s : bookmarks){
//                Log.d("nat", s);
//            }

            bookmarkListView = findViewById(R.id.bookmarkListView);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bookmarks);
            bookmarkListView.setAdapter(arrayAdapter);
            bookmarkListView.setClickable(true);
            bookmarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    openUrl = bookmarks.get(i);
                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                    intent.putExtra("address",openUrl);
                    startActivity(intent);
                    finish();
                }
            });
        }
        catch(Exception e){
            Log.d("nat", "error");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
