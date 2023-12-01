package edu.ncssm.bura24a.camera_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
//import android.widget.GridLayout;

public class theSearch extends AppCompatActivity {
    Button back;
    private List<Button> theCategory = new ArrayList<Button>();
    private String[] categories = {"nature", "family","friends","work","entertainment", "special events"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_search);

        back = findViewById(R.id.home);

        back.setOnClickListener(this::onClick);

        updateButtons();
    }

    public void onClick(View v){
        if(v==back) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        for(int i=0;i<theCategory.size();i++){
            if(v.getTag()==theCategory.get(i).getTag()) {
                String ClickedCategory = categories[i];

                Intent intent = new Intent(theSearch.this, searchCategory.class);
                intent.putExtra("category",ClickedCategory);
                startActivity(intent);
            }
        }
    }

    public void updateButtons(){
        GridLayout layout = findViewById(R.id.searchButtons);
        layout.setColumnCount(2);

        for(int numButtons=0;numButtons<categories.length;numButtons++){
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.setGravity(Gravity.FILL);
            lp.rowSpec=GridLayout.spec(GridLayout.UNDEFINED,1.0f);
            lp.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1.0f);
            Button button= new Button(this);
            button.setLayoutParams(lp);
            button.setMinimumHeight(0);
            button.setMinimumWidth(0);
            button.setMinHeight(0);
            button.setMinWidth(0);
            button.setTag(categories[numButtons]);
            button.setText(categories[numButtons]);
            theCategory.add(button);
            button.setOnClickListener(this::onClick);
            layout.addView(button);
        }
    }
}