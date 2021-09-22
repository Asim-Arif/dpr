package com.example.asim.amr;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class check_once extends AppCompatActivity {

    SimpleCursorAdapter myCursorAdapter;
    String strIssNo;

    Button cmdBack,cmdFinish;
    Context context;

    CheckBox chkLabels,chkBarcodes,chkCutting,chkTracing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        strIssNo = b.getString("IssNo");

        setContentView(R.layout.check_once);

        context=this.getBaseContext();

        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("Check Once");

        chkLabels=(CheckBox) findViewById(R.id.chkLabels);
        chkBarcodes=(CheckBox) findViewById(R.id.chkBarcodes);
        chkCutting=(CheckBox) findViewById(R.id.chkCutting);
        chkTracing=(CheckBox) findViewById(R.id.chkTracing);

        cmdFinish=(Button) findViewById(R.id.cmdFinish_CO);

        final Button btnBack=(Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cmdFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper myDBH = new DBHelper();
                Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
                int iWOIWD_RefID;
                iWOIWD_RefID = getWOIWD_EntryID();
                try {
                    String strQuery = "UPDATE WOIWD_Details SET Labels=?,Barcodes=?,CuttingKharcha=?,Tracing=? WHERE WOIWD_RefID=?";
                    PreparedStatement stmt = MyCon.prepareStatement(strQuery);
                    boolean bLabels,bBarcodes,bCutting,bTracing;
                    int iRecAffect;
                    bLabels=chkLabels.isChecked();
                    bBarcodes=chkBarcodes.isChecked();
                    bCutting=chkCutting.isChecked();
                    bTracing=chkTracing.isChecked();

                    stmt.setBoolean(1,bLabels);
                    stmt.setBoolean(2,bBarcodes);
                    stmt.setBoolean(3,bCutting);
                    stmt.setBoolean(4,bTracing);
                    stmt.setInt(5, iWOIWD_RefID);

                    iRecAffect=stmt.executeUpdate();

                    if (iRecAffect==0){
                        strQuery = "INSERT INTO WOIWD_Details(Labels,Barcodes,CuttingKharcha,Tracing,WOIWD_RefID) VALUES(?,?,?,?,?)";
                        stmt = MyCon.prepareStatement(strQuery);
                        stmt.setBoolean(1,bLabels);
                        stmt.setBoolean(2,bBarcodes);
                        stmt.setBoolean(3,bCutting);
                        stmt.setBoolean(4,bTracing);
                        stmt.setInt(5, iWOIWD_RefID);
                        iRecAffect=stmt.executeUpdate();
                    }
                    /*stmt.addBatch();
                    stmt.executeBatch();*/
                    Message.message(getApplicationContext(),"Successfuly Saved.");
                    onBackPressed();

                } catch (SQLException e) {

                    e.printStackTrace();
                    Message.message(context,e.toString());
                }
            }
        });
        cmdBack=(Button) findViewById(R.id.cmdBack_CO);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadExistingData();
    }
    public int getWOIWD_EntryID() {
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT EntryID FROM WorkOrderItemWorkDone WHERE ProcessEntryID='" + strIssNo + "'";
        int iEntryID=0;
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                iEntryID=rs.getInt("EntryID");
            }
            rs.close();
            return iEntryID;
        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
            return 0;
        }
    }
    public void loadExistingData(){
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT WOIWD_Details.WOIWD_RefID,WOIWD_Details.Labels,WOIWD_Details.Barcodes,WOIWD_Details.CuttingKharcha,WOIWD_Details.Tracing FROM WorkOrderItemWorkDone INNER JOIN WOIWD_Details ON WorkOrderItemWorkDone.EntryID=WOIWD_Details.WOIWD_RefID WHERE ProcessEntryID='" + strIssNo + "'";
        boolean bLabels,bBarcodes,bCutting,bTracing;
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                bLabels=rs.getBoolean("Labels");
                bBarcodes=rs.getBoolean("Barcodes");
                bCutting=rs.getBoolean("CuttingKharcha");
                bTracing=rs.getBoolean("Tracing");

                chkLabels.setChecked(bLabels);
                chkBarcodes.setChecked(bBarcodes);
                chkCutting.setChecked(bCutting);
                chkTracing.setChecked(bTracing);
            }
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());

        }
    }
}