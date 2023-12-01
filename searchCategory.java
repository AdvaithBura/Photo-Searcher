package edu.ncssm.bura24a.camera_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.gridlayout.widget.GridLayout;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class searchCategory extends AppCompatActivity {
    private Button home;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category);
        GridLayout theimage = findViewById(R.id.theimageGrid);
        TextView textview = findViewById(R.id.SearchTitle);
        home = findViewById(R.id.home);
        back = findViewById(R.id.back);

        back.setOnClickListener(this::onClick);
        home.setOnClickListener(this::onClick);

        Intent intent = getIntent();
        String theClickedCategory = intent.getStringExtra("category");

        ImageView testImage = new ImageView(this);
        testImage.setImageResource(R.drawable.food);
        textview.setText(theClickedCategory);
        theimage.addView(testImage);
    }

    public void onClick(View v){
        if(v==home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if(v==back){
            Intent intent = new Intent(this, theSearch.class);
            startActivity(intent);
        }
    }


}