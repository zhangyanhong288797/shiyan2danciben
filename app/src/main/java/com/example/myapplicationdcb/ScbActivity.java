package com.example.myapplicationdcb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.lang.String;
import java.util.ArrayList;
import java.util.Map;

public class ScbActivity extends AppCompatActivity {
private String strId1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_scb );
        Intent intent = getIntent();
        String strId1 = intent.getStringExtra("extra_data");
        // Log.d("Main2Activity",data);
        TextView  textId =(TextView)itemView.findViewById(R.id.textId);
        text.setText( data );

    }
}
