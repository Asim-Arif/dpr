package com.example.asim.amr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnReceiving=(Button) findViewById(R.id.cmdReceiving);
        btnReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), receiving.class);
                startActivity(i);
            }
        });

        Button btnAuthorization=(Button) findViewById(R.id.cmdAuthorization);
        btnAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), authorization.class);
                startActivity(i);
            }
        });

        Button btnReceivingList=(Button) findViewById(R.id.cmdReceivingList);
        btnReceivingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), receiving_list.class);
                startActivity(i);
            }
        });
        ImageButton cmdSettings = (ImageButton) findViewById(R.id.ImgBtnSettings);
        cmdSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), sql_settings.class);
                startActivity(i);
            }
        });
    }
}
