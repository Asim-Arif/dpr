package com.example.asim.amr;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class view_slip extends AppCompatActivity {

    String strIssNo;
    TextView txtDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        strIssNo = b.getString("IssNo");

        setContentView(R.layout.view_slip);
        final Context context;
        context=this.getBaseContext();
        txtDetails=(TextView) findViewById(R.id.txtViewSlip);



        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("View Slip");
        ShowDetail();
        final Button btnBack=(Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void ShowDetail()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT * FROM VWOIWD_News_Bulletin WHERE ProcessEntryID='"+strIssNo+"'";

        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();

            String strDetails="Issuance Number:"+strIssNo+"\n";

            while (rs.next())
            {
                strDetails=strDetails+"Contractor:"+rs.getString("name")+"\n";
                strDetails=strDetails+"Article:"+rs.getString("ArticleNo")+"\n";
                strDetails=strDetails+"Order in Article:"+rs.getString("OrderQty")+"\n";
                strDetails=strDetails+"Operation:"+rs.getString("OperationName")+"\n";
                strDetails=strDetails+"Pcs:"+rs.getString("Qty")+"\n";
                strDetails=strDetails+"Size:"+"L"+"\n";
                strDetails=strDetails+"Order Number:"+rs.getString("OrderNo")+"\n";
                strDetails=strDetails+"Target:"+rs.getString("ReturnDT")+"\n";
            }
            txtDetails.setText(strDetails);

        } catch (Exception e) {Message.message(this.getBaseContext(),""+e);}

    }
}
