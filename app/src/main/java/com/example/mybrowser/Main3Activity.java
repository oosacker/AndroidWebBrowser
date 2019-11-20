package com.example.mybrowser;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {

    String openUrl;
    ListView historyListView;
    ArrayList<String> history;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("History");

        // Get the transferred data from source activity.
        Intent intent = getIntent();
        try{
            history = intent.getStringArrayListExtra("history");
            historyListView = findViewById(R.id.historyListView);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, history);
            historyListView.setAdapter(arrayAdapter);
            historyListView.setClickable(true);
            historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    openUrl = history.get(i);
                    Intent intent = new Intent(Main3Activity.this, MainActivity.class);
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
        Intent intent = new Intent(Main3Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
