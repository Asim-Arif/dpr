package com.example.asim.amr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class daily_tasks extends AppCompatActivity {

    String strIssNo;

    Button cmdBack;
    Context context;

    TextView txtAGW,txtAGW_Time,txtFIT,txtFIT_Time,txtFOT,txtFOT_Time,txtSQCI,txtSQCI_Time,txtSQCII,txtSQCII_Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        strIssNo = b.getString("IssNo");

        setContentView(R.layout.daily_tasks);

        context = this.getBaseContext();

        TextView txtActivityTitle = (TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("Daily Tasks");

        final Button btnBack = (Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtAGW=(TextView) findViewById(R.id.txtAGW);
        txtAGW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("All Group Working","0");
            }
        });
        txtAGW_Time=(TextView) findViewById(R.id.txtAGW_Time);
        txtAGW_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("All Group Working","0");
            }
        });
        txtFIT=(TextView) findViewById(R.id.txtFIT);
        txtFIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("Factory in time","1");
            }
        });
        txtFIT_Time=(TextView) findViewById(R.id.txtFIT_Time);
        txtFIT_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("Factory in time","1");
            }
        });
        txtFOT=(TextView) findViewById(R.id.txtFOT);
        txtFOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("Factory out time","2");
            }
        });
        txtFOT_Time=(TextView) findViewById(R.id.txtFOT_Time);
        txtFOT_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("Factory out time","2");
            }
        });

        txtSQCI=(TextView) findViewById(R.id.txtSQCI);
        txtSQCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("Supervisor QC","3");
            }
        });
        txtSQCI_Time=(TextView) findViewById(R.id.txtSQCI_Time);
        txtSQCI_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("Supervisor QC","3");
            }
        });

        txtSQCII=(TextView) findViewById(R.id.txtSQCII);
        txtSQCII.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("Supervisor QC","4");
            }
        });
        txtSQCII_Time=(TextView) findViewById(R.id.txtSQCII_Time);
        txtSQCII_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyTasksDetail("Supervisor QC","4");
            }
        });
    }


    private void showDailyTasksDetail(String strDailyTaskType,String strIndex){
        //call Intent by Sending it IssNo & All Group Working Status...
        Intent myIntent = new Intent(getApplicationContext(), daily_tasks_detail.class);
        myIntent.putExtra("IssNo",strIssNo);
        myIntent.putExtra("DailyTask_Type",strDailyTaskType);
        myIntent.putExtra("Index",strIndex);
        startActivity(myIntent);
    }
}