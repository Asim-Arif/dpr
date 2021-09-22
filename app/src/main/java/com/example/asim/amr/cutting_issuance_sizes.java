package com.example.asim.amr;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Asim on 3/17/2017.
 */

public class cutting_issuance_sizes extends AppCompatActivity {
    TextView txtXXS,txtXS,txtS,txtM,txtL,txtXL,txtXXL,txt3XL,txt4XL,txt5XL,txt11th,txt12th,txt13th,txt14th,txt15th;
    TextView txtActivityTitle;
    String strOrderNo, strItemID, strOperationID, strEmpID, strmyDT;
    String strSize_XXS,strSize_XS,strSize_S,strSize_M,strSize_L,strSize_XL,strSize_XXL,strSize_3XL,strSize_4XL,strSize_5XL,strSize_11th,strSize_12th,strSize_13th,strSize_14th,strSize_15th;
    Spinner cmbSize_XXS,cmbSize_XS,cmbSize_S,cmbSize_M,cmbSize_L,cmbSize_XL,cmbSize_XXL,cmbSize_3XL,cmbSize_4XL,cmbSize_5XL,cmbSize_11th,cmbSize_12th,cmbSize_13th,cmbSize_14th,cmbSize_15th;
    boolean bFirstClick_XXS,bFirstClick_XS,bFirstClick_S,bFirstClick_M,bFirstClick_L,bFirstClick_XL,bFirstClick_XXL,bFirstClick_3XL,bFirstClick_4XL,bFirstClick_5XL,bFirstClick_11th,bFirstClick_12th,bFirstClick_13th,bFirstClick_14th,bFirstClick_15th;
    Button cmdBack,cmdFinish;
    int iProcessID;
    ResultSet rs;
    int iSelectedColor;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        strOrderNo = b.getString("OrderNo");
        strItemID = b.getString("ItemID");
        strOperationID = b.getString("OperationID");
        strEmpID = b.getString("EmpID");
        strmyDT = b.getString("strmyDT");
        iProcessID=b.getInt("ProcessID");
        iSelectedColor= Color.parseColor("#3A5F4D");
        setContentView(R.layout.cutting_issuance_sizes);

        context = this.getBaseContext();

        cmdBack=(Button) findViewById(R.id.cmdci_s_Back);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final Button btnBack = (Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        cmdFinish=(Button) findViewById(R.id.cmdci_s_Finish);
        cmdFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iQty_XXS, iQty_XS, iQty_S, iQty_M, iQty_L, iQty_XL, iQty_XXL, iQty_3XL, iQty_4XL, iQty_5XL, iQty_11th, iQty_12th, iQty_13th, iQty_14th, iQty_15th,iTotalQty;
                iQty_XXS = Integer.parseInt(cmbSize_XXS.getSelectedItem().toString());
                iQty_XS = Integer.parseInt(cmbSize_XS.getSelectedItem().toString());
                iQty_S = Integer.parseInt(cmbSize_S.getSelectedItem().toString());
                iQty_M = Integer.parseInt(cmbSize_M.getSelectedItem().toString());
                iQty_L = Integer.parseInt(cmbSize_L.getSelectedItem().toString());
                iQty_XL = Integer.parseInt(cmbSize_XL.getSelectedItem().toString());
                iQty_XXL = Integer.parseInt(cmbSize_XXL.getSelectedItem().toString());
                iQty_3XL = Integer.parseInt(cmbSize_3XL.getSelectedItem().toString());
                iQty_4XL = Integer.parseInt(cmbSize_4XL.getSelectedItem().toString());
                iQty_5XL = Integer.parseInt(cmbSize_5XL.getSelectedItem().toString());
                iQty_11th = Integer.parseInt(cmbSize_11th.getSelectedItem().toString());
                iQty_12th = Integer.parseInt(cmbSize_12th.getSelectedItem().toString());
                iQty_13th = Integer.parseInt(cmbSize_13th.getSelectedItem().toString());
                iQty_14th = Integer.parseInt(cmbSize_14th.getSelectedItem().toString());
                iQty_15th = Integer.parseInt(cmbSize_15th.getSelectedItem().toString());
                int iBackColor;
                iBackColor=utility_functions.getBackgroundColor(txtXXS);
                if (iBackColor!=iSelectedColor)
                    iQty_XXS=0;
                iBackColor=utility_functions.getBackgroundColor(txtXS);
                if (iBackColor!=iSelectedColor)
                    iQty_XS=0;
                iBackColor=utility_functions.getBackgroundColor(txtS);
                if (iBackColor!=iSelectedColor)
                    iQty_S=0;
                iBackColor=utility_functions.getBackgroundColor(txtM);
                if (iBackColor!=iSelectedColor)
                    iQty_M=0;
                iBackColor=utility_functions.getBackgroundColor(txtL);
                if (iBackColor!=iSelectedColor)
                    iQty_L=0;
                iBackColor=utility_functions.getBackgroundColor(txtXL);
                if (iBackColor!=iSelectedColor)
                    iQty_XL=0;
                iBackColor=utility_functions.getBackgroundColor(txtXXL);
                if (iBackColor!=iSelectedColor)
                    iQty_XXL=0;
                iBackColor=utility_functions.getBackgroundColor(txt3XL);
                if (iBackColor!=iSelectedColor)
                    iQty_3XL=0;
                iBackColor=utility_functions.getBackgroundColor(txt4XL);
                if (iBackColor!=iSelectedColor)
                    iQty_4XL=0;
                iBackColor=utility_functions.getBackgroundColor(txt5XL);
                if (iBackColor!=iSelectedColor)
                    iQty_5XL=0;
                iBackColor=utility_functions.getBackgroundColor(txt11th);
                if (iBackColor!=iSelectedColor)
                    iQty_11th=0;
                iBackColor=utility_functions.getBackgroundColor(txt12th);
                if (iBackColor!=iSelectedColor)
                    iQty_12th=0;
                iBackColor=utility_functions.getBackgroundColor(txt13th);
                if (iBackColor!=iSelectedColor)
                    iQty_13th=0;
                iBackColor=utility_functions.getBackgroundColor(txt14th);
                if (iBackColor!=iSelectedColor)
                    iQty_14th=0;
                iBackColor=utility_functions.getBackgroundColor(txt15th);
                if (iBackColor!=iSelectedColor)
                    iQty_15th=0;
                iTotalQty=iQty_XXS+iQty_XS+iQty_S+iQty_M+iQty_L+iQty_XL+iQty_XXL+iQty_3XL+iQty_4XL+iQty_5XL+iQty_11th+iQty_12th+iQty_13th+iQty_14th+iQty_15th;

                DBHelper myDBH = new DBHelper();
                Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
                final java.util.Calendar c=java.util.Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                String myDT;
                //myDT=Integer.toString(month)+'/'+Integer.toString(day)+'/'+Integer.toString(year);
                //myDT=DateFormat.getDateInstance().format(new Date());
                myDT=DateFormat.getDateInstance().format(new Date());
                Calendar calendar = Calendar.getInstance();
                Date MyCurrentDate=calendar.getTime();
                java.sql.Date CurrentDate=new java.sql.Date(MyCurrentDate.getTime());
                String strIssEntryID="";

                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MMM/yyyy");
                    //CurrentDate= dateFormat.parse(myDT);
                    dateFormat.format(CurrentDate);
                    strIssEntryID=getProcessIssuanceEntryID(CurrentDate,MyCon);
                double dOperationRate=getOperationRate();
                String query = "INSERT INTO WorkOrderItemWorkDone(ProcessEntryID,OrderNo,ItemID,OperationID,DT,Qty,ProcessID,Qty_XXS,Qty_XS,Qty_S" +
                        ",Qty_M,Qty_L,Qty_XL,Qty_XXL,Qty_3XL,Qty_4XL,Qty_5XL,Qty_11th,Qty_12th,Qty_13th,Qty_14th,Qty_15th,Rate,ContID" +
                        ",ManualNo,UserName,MachineName,ReturnDT) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                /*String query = "INSERT INTO WorkOrderItemWorkDone(ProcessEntryID,OrderNo,ItemID,OperationID,DT,Qty,ProcessID,Qty_XXS,Qty_XS,Qty_S" +
                        ",Qty_M,Qty_L,Qty_XL,Qty_XXL,Qty_3XL,Qty_4XL,Qty_5XL,Qty_11th,Qty_12th,Qty_13th,Qty_14th,Qty_15th,Rate,ContID" +
                        ",ManualNo,UserName,MachineName,ReturnDT) VALUES('"+strIssEntryID+"','"+strOrderNo+"','"+strItemID+"','"+strOperationID+"','"+
                        myDT+"',"+Integer.toString(iTotalQty)+",2,"+Integer.toString(iQty_XXS)+","+Integer.toString(iQty_XS)+","+Integer.toString(iQty_S)
                        +","+Integer.toString(iQty_M)+","+Integer.toString(iQty_L)+","+Integer.toString(iQty_XL)+","+Integer.toString(iQty_XXL)+","
                        +Integer.toString(iQty_3XL)+","+Integer.toString(iQty_4XL)+","+Integer.toString(iQty_5XL)+","+Integer.toString(iQty_11th)
                        +","+Integer.toString(iQty_12th)+","+Integer.toString(iQty_13th)+","+Integer.toString(iQty_14th)+","+Integer.toString(iQty_15th)
                        +",0,'"+strEmpID+"','','Android','Android','"+strmyDT+"')";*/
                try {
                    PreparedStatement stmt = MyCon.prepareStatement(query);

                    stmt.setString(1,strIssEntryID);
                    stmt.setString(2,strOrderNo);
                    stmt.setString(3,strItemID);
                    stmt.setString(4,strOperationID);
                    stmt.setDate(5,CurrentDate);
                    stmt.setInt(6,iTotalQty);
                    stmt.setInt(7,iProcessID);
                    stmt.setInt(8,iQty_XXS);
                    stmt.setInt(9,iQty_XS);
                    stmt.setInt(10,iQty_S);
                    stmt.setInt(11,iQty_M);
                    stmt.setInt(12,iQty_L);
                    stmt.setInt(13,iQty_XL);
                    stmt.setInt(14,iQty_XXL);
                    stmt.setInt(15,iQty_3XL);
                    stmt.setInt(16,iQty_4XL);
                    stmt.setInt(17,iQty_5XL);
                    stmt.setInt(18,iQty_11th);
                    stmt.setInt(19,iQty_12th);
                    stmt.setInt(20,iQty_13th);
                    stmt.setInt(21,iQty_14th);
                    stmt.setInt(22,iQty_15th);
                    stmt.setDouble(23,dOperationRate);
                    stmt.setString(24,strEmpID);
                    stmt.setString(25,"");
                    stmt.setString(26,"Android");
                    stmt.setString(27,"Android");
                    stmt.setDate(28,CurrentDate);
                    stmt.addBatch();
                    stmt.executeBatch();
                    /*Now Generate Barcodes--for Stitching Only.*/
                    if (iProcessID==6) {
                        int iWOIWD_EntryID=getWOIWD_EntryID();
                        //iWOIWD_EntryID=iWOIWD_EntryID+1;
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_XXS,strSize_XXS,"Size1");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_XS,strSize_XS,"Size2");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_S,strSize_S,"Size3");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_M,strSize_M,"Size4");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_L,strSize_L,"Size5");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_XL,strSize_XL,"Size6");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_XXL,strSize_XXL,"Size7");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_3XL,strSize_3XL,"Size8");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_4XL,strSize_4XL,"Size9");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_5XL,strSize_5XL,"Size10");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_11th,strSize_11th,"Size11");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_12th,strSize_12th,"Size12");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_13th,strSize_13th,"Size13");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_14th,strSize_14th,"Size14");
                        generateBarcodes(MyCon,iWOIWD_EntryID,iQty_15th,strSize_15th,"Size15");
                    }


                    Message.message(getApplicationContext(),"Successfuly Saved.");
                    GoHome();

                } catch (SQLException e) {

                    e.printStackTrace();
                    Message.message(getApplicationContext(),"At Save:" + e.toString());

                }
            }
        });
        cmbSize_XXS = (Spinner) findViewById(R.id.cmbci_s_xxs);
        cmbSize_XS = (Spinner) findViewById(R.id.cmbci_s_xs);
        cmbSize_S = (Spinner) findViewById(R.id.cmbci_s_s);
        cmbSize_M = (Spinner) findViewById(R.id.cmbci_s_m);
        cmbSize_L = (Spinner) findViewById(R.id.cmbci_s_l);
        cmbSize_XL = (Spinner) findViewById(R.id.cmbci_s_xl);
        cmbSize_XXL = (Spinner) findViewById(R.id.cmbci_s_xxl);
        cmbSize_3XL = (Spinner) findViewById(R.id.cmbci_s_3xl);
        cmbSize_4XL = (Spinner) findViewById(R.id.cmbci_s_4xl);
        cmbSize_5XL = (Spinner) findViewById(R.id.cmbci_s_5xl);
        cmbSize_11th = (Spinner) findViewById(R.id.cmbci_s_11th);
        cmbSize_12th = (Spinner) findViewById(R.id.cmbci_s_12th);
        cmbSize_13th = (Spinner) findViewById(R.id.cmbci_s_13th);
        cmbSize_14th = (Spinner) findViewById(R.id.cmbci_s_14th);
        cmbSize_15th = (Spinner) findViewById(R.id.cmbci_s_15th);
        txtXXS=(TextView) findViewById(R.id.txtXXS);
        txtXXS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_XXS.performClick();
            }
        });
        txtXS=(TextView) findViewById(R.id.txtXS);
        txtXS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_XS.performClick();
            }
        });
        txtS=(TextView) findViewById(R.id.txtS);
        txtS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_S.performClick();
            }
        });
        txtM=(TextView) findViewById(R.id.txtM);
        txtM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_M.performClick();
            }
        });
        txtL=(TextView) findViewById(R.id.txtL);
        txtL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_L.performClick();
            }
        });
        txtXL=(TextView) findViewById(R.id.txtXL);
        txtXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_XL.performClick();
            }
        });
        txtXXL=(TextView) findViewById(R.id.txtXXL);
        txtXXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_XXL.performClick();
            }
        });
        txt3XL=(TextView) findViewById(R.id.txt3XL);
        txt3XL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_3XL.performClick();
            }
        });
        txt4XL=(TextView) findViewById(R.id.txt4XL);
        txt4XL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_4XL.performClick();
            }
        });
        txt5XL=(TextView) findViewById(R.id.txt5XL);
        txt5XL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_5XL.performClick();
            }
        });
        txt11th=(TextView) findViewById(R.id.txt11th);
        txt11th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_11th.performClick();
            }
        });
        txt12th=(TextView) findViewById(R.id.txt12th);
        txt12th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_12th.performClick();
            }
        });
        txt13th=(TextView) findViewById(R.id.txt13th);
        txt13th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_13th.performClick();
            }
        });
        txt14th=(TextView) findViewById(R.id.txt14th);
        txt14th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_14th.performClick();
            }
        });
        txt15th=(TextView) findViewById(R.id.txt15th);
        txt15th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbSize_15th.performClick();
            }
        });
        txtActivityTitle = (TextView) findViewById(R.id.txtAtivityTitle);

        cmbSize_XXS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_XXS==false)
                    bFirstClick_XXS=true;
                else
                    txtXXS.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtXXS.setBackgroundColor(iSelectedColor);
            }
        });
        cmbSize_XS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_XS==false)
                    bFirstClick_XS=true;
                else
                    txtXS.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_S.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (bFirstClick_S==false)
                        bFirstClick_S=true;
                    else
                        txtS.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_M.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_M==false)
                    bFirstClick_M=true;
                else
                    txtM.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_L.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_L==false)
                    bFirstClick_L=true;
                else
                    txtL.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_XL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_XL==false)
                    bFirstClick_XL=true;
                else
                    txtXL.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_XXL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_XXL==false)
                    bFirstClick_XXL=true;
                else
                    txtXXL.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_3XL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_3XL==false)
                    bFirstClick_3XL=true;
                else
                    txt3XL.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_4XL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_4XL==false)
                    bFirstClick_4XL=true;
                else
                    txt4XL.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_5XL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_5XL==false)
                    bFirstClick_5XL=true;
                else
                    txt5XL.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_11th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_11th==false)
                    bFirstClick_11th=true;
                else
                    txt11th.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_12th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_12th==false)
                    bFirstClick_12th=true;
                else
                    txt12th.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_13th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_13th==false)
                    bFirstClick_13th=true;
                else
                    txt13th.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_14th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_14th==false)
                    bFirstClick_14th=true;
                else
                    txt14th.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cmbSize_15th.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bFirstClick_15th==false)
                    bFirstClick_15th=true;
                else
                    txt15th.setBackgroundColor(iSelectedColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (iProcessID==2)
            txtActivityTitle.setText("Cutting Issuance");
        else
            txtActivityTitle.setText("Stitching Issuance");

        setSizeHeadings();
        UpdateSizeWise();
    }

    private void UpdateSizeWise() {
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT VFOrderItemSizes.*,TDone.Qty_XXS_Done,TDone.Qty_XS_Done,TDone.Qty_S_Done,TDone.Qty_M_Done,TDone.Qty_L_Done,TDone.Qty_XL_Done,TDone.Qty_XXL_Done,TDone.Qty_3XL_Done,TDone.Qty_4XL_Done,TDone.Qty_5XL_Done,TDone.Qty_11th_Done,TDone.Qty_12th_Done,TDone.Qty_13th_Done,TDone.Qty_14th_Done,TDone.Qty_15th_Done FROM VFOrderItemSizes LEFT OUTER JOIN (SELECT OrderNo,ItemID,SUM(Qty_XXS) AS Qty_XXS_Done,SUM(Qty_XS) AS Qty_XS_Done,SUM(Qty_S) AS Qty_S_Done,SUM(Qty_M) AS Qty_M_Done,SUM(Qty_L) AS Qty_L_Done,SUM(Qty_XL) AS Qty_XL_Done,SUM(Qty_XXL) AS Qty_XXL_Done,SUM(Qty_3XL) AS Qty_3XL_Done,SUM(Qty_4XL) AS Qty_4XL_Done,SUM(Qty_5XL) AS Qty_5XL_Done,SUM(Qty_11th) AS Qty_11th_Done,SUM(Qty_12th) AS Qty_12th_Done,SUM(Qty_13th) AS Qty_13th_Done,SUM(Qty_14th) AS Qty_14th_Done,SUM(Qty_15th) AS Qty_15th_Done FROM WorkOrderItemWorkDone where OrderNo='"+strOrderNo+"' AND ItemID='" + strItemID + "' AND OperationID='"+strOperationID+"' and EntryType=0 GROUP BY OrderNo,ItemID) TDone ON VFOrderItemSizes.OrderNo=TDone.OrderNo AND VFOrderItemSizes.ItemID=TDone.ItemID where VFOrderItemSizes.OrderNo='" + strOrderNo + "' AND VFOrderItemSizes.ItemID='" + strItemID + "'";

        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();


            ArrayList<String> data = new ArrayList<String>();
            ArrayAdapter NoCoreAdapter;

            int[] toViewIDs = new int[]{R.id.txtSpinnerControl};
            while (rs.next()) {
                int iSizeQty_XXS,iSizeQty_XS,iSizeQty_S,iSizeQty_M,iSizeQty_L,iSizeQty_XL,iSizeQty_XXL,iSizeQty_3XL,iSizeQty_4XL,iSizeQty_5XL,iSizeQty_11th,iSizeQty_12th,iSizeQty_13th,iSizeQty_14th,iSizeQty_15th;


                iSizeQty_XXS = rs.getInt("Qty_XXS")-rs.getInt("Qty_XXS_Done");
                iSizeQty_XS = rs.getInt("Qty_XS")-rs.getInt("Qty_XS_Done");
                iSizeQty_S = rs.getInt("Qty_S")-rs.getInt("Qty_S_Done");
                iSizeQty_M = rs.getInt("Qty_M")-rs.getInt("Qty_M_Done");
                iSizeQty_L = rs.getInt("Qty_L")-rs.getInt("Qty_L_Done");
                iSizeQty_XL = rs.getInt("Qty_XL")-rs.getInt("Qty_XL_Done");
                iSizeQty_XXL = rs.getInt("Qty_XXL")-rs.getInt("Qty_XXL_Done");
                iSizeQty_3XL = rs.getInt("Qty_3XL")-rs.getInt("Qty_3XL_Done");
                iSizeQty_4XL = rs.getInt("Qty_4XL")-rs.getInt("Qty_4XL_Done");
                iSizeQty_5XL = rs.getInt("Qty_5XL")-rs.getInt("Qty_5XL_Done");
                iSizeQty_11th = rs.getInt("Qty_11th")-rs.getInt("Qty_11th_Done");
                iSizeQty_12th = rs.getInt("Qty_12th")-rs.getInt("Qty_12th_Done");
                iSizeQty_13th = rs.getInt("Qty_13th")-rs.getInt("Qty_13th_Done");
                iSizeQty_14th = rs.getInt("Qty_14th")-rs.getInt("Qty_14th_Done");
                iSizeQty_15th = rs.getInt("Qty_15th")-rs.getInt("Qty_15th_Done");


                FillSpinner(iSizeQty_XXS,cmbSize_XXS);
                FillSpinner(iSizeQty_XS,cmbSize_XS);
                FillSpinner(iSizeQty_S,cmbSize_S);
                FillSpinner(iSizeQty_M,cmbSize_M);
                FillSpinner(iSizeQty_L,cmbSize_L);
                FillSpinner(iSizeQty_XL,cmbSize_XL);
                FillSpinner(iSizeQty_XXL,cmbSize_XXL);
                FillSpinner(iSizeQty_3XL,cmbSize_3XL);
                FillSpinner(iSizeQty_4XL,cmbSize_4XL);
                FillSpinner(iSizeQty_5XL,cmbSize_5XL);
                FillSpinner(iSizeQty_11th,cmbSize_11th);
                FillSpinner(iSizeQty_12th,cmbSize_12th);
                FillSpinner(iSizeQty_13th,cmbSize_13th);
                FillSpinner(iSizeQty_14th,cmbSize_14th);
                FillSpinner(iSizeQty_15th,cmbSize_15th);



            }


        } catch (SQLException e) {

            e.printStackTrace();

        }
    }
    private void FillSpinner(int iQtyToUse,Spinner cmbToUse)
    {
        ArrayList<String> data = new ArrayList<String>();
        ArrayAdapter NoCoreAdapter;
        data.clear();
        if (iQtyToUse> 0) {
            for (int i = 0; i <= iQtyToUse; i++) {
                data.add(Integer.toString(i));
            }
        }
        else{
            data.add("0");
        }

        NoCoreAdapter = new ArrayAdapter(this, R.layout.spinner_control, data);
        NoCoreAdapter.setDropDownViewResource(R.layout.spinner_item);
        cmbToUse.setAdapter(NoCoreAdapter);
        if (cmbToUse.getCount()>1)
            cmbToUse.setSelection(1);
        else
            cmbToUse.setSelection(0);
    }
    private String getProcessIssuanceEntryID(Date CurrentDT,Connection MyCon){
        String strIssEntryID="";
        String query;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        Date myDT;
        SimpleDateFormat DF_S = new SimpleDateFormat("yyMMdd");

            //myDT = dateFormat.parse(strmyDT);   //Convert String to Date
            SimpleDateFormat mydateFormat = new SimpleDateFormat("MM/dd/yy");

            mydateFormat.format(CurrentDT);          //Change Format

        try {
            query="SELECT MAX(CAST(RIGHT(ProcessEntryID,3) AS INT)) FROM WorkOrderItemWorkDone WHERE EntryType=0 AND ProcessID="+Integer.toString(iProcessID) + " AND Convert(Char(8),DT,1)='"+mydateFormat.format(CurrentDT).toString()+"'";
            PreparedStatement stmt = MyCon.prepareStatement(query);
            stmt.executeQuery();
            rs=stmt.executeQuery();
            if (rs.next())  //If Data Returned
            {
                int myID;
                String strmyID;
                myID=rs.getInt(1);
                myID=myID+1;
                /*strmyID=Integer.toString(myID);
                strmyID=String.format("%03d",strmyID);*/
                strmyID=String.format("%03d",myID);
                //DF_S.format(CurrentDT).toString();
                if (iProcessID==2)
                    strIssEntryID="ISS-Cut-"+DF_S.format(CurrentDT).toString()+"-"+strmyID;
                else
                    strIssEntryID="ISS-Sti-"+DF_S.format(CurrentDT).toString()+"-"+strmyID;
                //Message.message(this.getApplicationContext(),mydateFormat.format(CurrentDT).toString());
            }
        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(getApplicationContext(),"Get ID:"+e.toString());
            return "Error";
        }
        return strIssEntryID;
    }
    private void GoHome()
    {
        my_globals myGlog;

        myGlog = new my_globals(this);
        myGlog.goToHomeActivity();
    }
    private void setSizeHeadings()
    {
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        PreparedStatement stmt;
        ResultSet rs;
        String query;
        int iFormatting=0;
        query = "SELECT Formatting FROM Items WHERE ItemID='"+strItemID+"'";
        try {
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                iFormatting = rs.getInt("Formatting");
            }
        }
        catch(SQLException e) {

            e.printStackTrace();

        }

        query = "SELECT * FROM SizesFormats where FormatType=" + Integer.toString(iFormatting);

        try {

            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            int[] toViewIDs = new int[]{R.id.txtSpinnerControl};
            while (rs.next()) {



                strSize_XXS = rs.getString("Size1");
                strSize_XS = rs.getString("Size2");
                strSize_S = rs.getString("Size3");
                strSize_M = rs.getString("Size4");
                strSize_L = rs.getString("Size5");
                strSize_XL = rs.getString("Size6");
                strSize_XXL = rs.getString("Size7");
                strSize_3XL = rs.getString("Size8");
                strSize_4XL = rs.getString("Size9");
                strSize_5XL = rs.getString("Size10");
                strSize_11th = rs.getString("Size11");
                strSize_12th = rs.getString("Size12");
                strSize_13th = rs.getString("Size13");
                strSize_14th = rs.getString("Size14");
                strSize_15th = rs.getString("Size15");

                txtXXS.setText(strSize_XXS);
                txtXS.setText(strSize_XS);
                txtS.setText(strSize_S);
                txtM.setText(strSize_M);
                txtL.setText(strSize_L);
                txtXL.setText(strSize_XL);
                txtXXL.setText(strSize_XXL);
                txt3XL.setText(strSize_3XL);
                txt4XL.setText(strSize_4XL);
                txt5XL.setText(strSize_5XL);
                txt11th.setText(strSize_11th);
                txt12th.setText(strSize_12th);
                txt13th.setText(strSize_13th);
                txt14th.setText(strSize_14th);
                txt15th.setText(strSize_15th);
            }


        } catch (SQLException e) {

            e.printStackTrace();

        }
    }
    public int getWOIWD_EntryID() {
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT MAX(EntryID) AS EntryID FROM WorkOrderItemWorkDone";
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
    public double getOperationRate() {
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT Rate FROM ItemOperations WHERE ItemID='"+strItemID+"' AND OperationID='"+strOperationID+"'";
        double dRate=0;
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                dRate=rs.getInt("Rate");
            }
            rs.close();
            return dRate;
        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
            return 0;
        }
    }
    private void generateBarcodes(Connection MyCon,int iWOIWD_EntryID,int Qty,String strSize,String strSizeName){
        String query;
        PreparedStatement stmt=null;
        query="INSERT INTO WorkOrderItemWorkDone_Barcodes(WOIWD_RefID,Size,SizeField) VALUES(?,?,?)";
        try{
            for (int i=0;i<Qty;i++){
                stmt = MyCon.prepareStatement(query);
                stmt.setInt(1,iWOIWD_EntryID);
                stmt.setString(2,strSize);
                stmt.setString(3,strSizeName);
                stmt.execute();
            }

        }catch (SQLException e) {

            e.printStackTrace();
            Message.message(getApplicationContext(),"At Save_Barcode:" + e.toString());
        }

    }
}