package com.example.asim.amr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class sql_settings extends AppCompatActivity {

    Button cmdUpdate,cmdCancel;
    EditText txtSQLIP,txtSQLPort;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();

        setContentView(R.layout.sql_settings);
        final Context context;
        context=this.getBaseContext();

        preferences=PreferenceManager.getDefaultSharedPreferences(context);

        txtSQLIP=(EditText) findViewById(R.id.txtSQLIP);
        txtSQLPort=(EditText) findViewById(R.id.txtSQLPort);

        String strSQLIP = preferences.getString("SQLIP", "");
        String strSQLPort = preferences.getString("SQLPort", "");

        txtSQLIP.setText(strSQLIP);
        txtSQLPort.setText(strSQLPort);

        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);

        txtActivityTitle.setText("Settings");

        cmdUpdate=(Button) findViewById(R.id.cmdUpdate);
        cmdUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = preferences.edit();
                String strSQLIP=txtSQLIP.getText().toString();
                String strSQLPort=txtSQLPort.getText().toString();

                editor.putString("SQLIP",strSQLIP);
                editor.putString("SQLPort",strSQLPort);
                editor.commit();
                onBackPressed();
            }
        });
        cmdCancel=(Button) findViewById(R.id.cmdCancel);
        cmdCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final Button btnBack=(Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

}
