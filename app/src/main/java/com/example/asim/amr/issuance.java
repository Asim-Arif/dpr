package com.example.asim.amr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class issuance extends Activity {

    TextView txtActivityTitle;
    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner cmbProcess,cmbVendor,cmbItem;

    static Button cmdReturnDate,cmdDate;
    SimpleCursorAdapter myCursorAdapter;
    Button cmdSave,cmdBack;
    TextView lblLotNo,lblIssNo;
    EditText txtLotNo,txtQty,txtIssID;
    Context context;
    static String strDateCallBack="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();


        setContentView(R.layout.issuance);
        context = this.getBaseContext();

        txtActivityTitle = (TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("Process Issuance");

        txtLotNo=(EditText) findViewById(R.id.txtIss_LotNo);

        txtLotNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    //Search for Lot No. & Update Qty...
                    String strLotNo=txtLotNo.getText().toString();
                    verifyLotForIssuance(strLotNo);


                }
            }
        });

        lblLotNo=(TextView) findViewById(R.id.lblIss_LotNo);
        lblLotNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ScanBarcode.class);
                startActivityForResult(intent,0);
                String strLotNo=txtLotNo.getText().toString();
                verifyLotForIssuance(strLotNo);
            }
        });
        cmdBack=(Button) findViewById(R.id.cmdIss_Back);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cmdSave=(Button) findViewById(R.id.cmdIss_Save);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cmbVendor.getSelectedItemPosition() == 0) {
                    Message.message(getApplicationContext(), "Please Select Vendor");
                    return;
                }

                HashMap<String, String> data = (HashMap<String, String>) cmbVendor.getSelectedItem();
                data = (HashMap<String, String>) cmbVendor.getSelectedItem();
                int iVendID = Integer.parseInt(data.get("VendID"));

                data = (HashMap<String, String>) cmbProcess.getSelectedItem();
                int iProcessID=Integer.parseInt(data.get("ProcessID"));

                data = (HashMap<String, String>) cmbItem.getSelectedItem();
                String strItemID=data.get("ItemID");

                String strmyDT = cmdDate.getText().toString();
                String strReturnDT = cmdReturnDate.getText().toString();

                java.sql.Date DTIssue = utility_functions.convertStringToSQLDate(strmyDT, context);
                java.sql.Date DTReturn = utility_functions.convertStringToSQLDate(strReturnDT, context);

                String strRecieptID = "",strMakerIssNo="";
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yy");

                int iLast,iLastIssNo;
                iLastIssNo= utility_functions.getSingleIntValue("MAX(CAST(RIGHT(RecieptID,Len(RecieptID)-12) As Int))", "VendIssued", " WHERE CONVERT(VARCHAR(50),DT,6)='" + sdf1.format(DTIssue).toString() + "'", context);
                iLast= utility_functions.getSingleIntValue("Max(Cast(right(RecieptID,Len(RecieptID)-10) As Int))", "VMakerIssItems", " WHERE CONVERT(VARCHAR(50),DT,6)='" + sdf1.format(DTIssue).toString() + "' AND LEFT(RecieptID,3)<>'HSR'", context);
                iLastIssNo= iLastIssNo+ 1;
                iLast=iLast+1;
                sdf1.applyPattern("ddMMyy");
                strRecieptID = "ISU-" + sdf1.format(DTIssue).toString() + Integer.toString(iLast);
                strMakerIssNo= "M-ISU-" + sdf1.format(DTIssue).toString() + Integer.toString(iLastIssNo);
                String strLotNo="",strIssID="";
                int iDetailEntryID;

                strLotNo = txtLotNo.getText().toString();
                iDetailEntryID = utility_functions.getSingleIntValue("MAX(EntryID)", "VendRcvdDetail", " WHERE LotNo='" + strLotNo + "'", context);

                String strOrderNo=utility_functions.getSingleStringValue("OrderNo","VendRcvdDetail"," WHERE VendRcvdDetail.EntryID="+Integer.toString(iDetailEntryID),context);

                int iLotQty=0,iLotBuffer=0,iQtyUsed=0,iQtyToUse=0;

                double dRate=utility_functions.getSingleDoubleValue("Rate","VendAssItems"," WHERE VendID="+Integer.toString(iVendID)+" AND ProcessID="+Integer.toString(iProcessID)+" AND ItemID='"+strItemID+"'",context);
                int iQty=utility_functions.getSingleIntValue("RcvdQty","VendRcvdDetail"," WHERE EntryID="+Integer.toString(iDetailEntryID),context);
                int iVR_EntryID;
                DBHelper myDBH = new DBHelper();
                Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

                try {
                    MyCon.setAutoCommit(false);
                    String strQuery = "INSERT INTO VendIssued(VendID,DT,RecieptID,ProcessID,ItemID,UserName,MachineName,Authorized) VALUES(?,?,?,?,?,?,?,?)";
                    PreparedStatement stmt = MyCon.prepareStatement(strQuery);
                    stmt.setInt(1, iVendID);
                    stmt.setDate(2,DTIssue);
                    stmt.setString(3,strMakerIssNo);
                    stmt.setInt(4,iProcessID);
                    stmt.setString(5, strItemID);
                    stmt.setString(6, "Android");
                    stmt.setString(7, "Android");
                    stmt.setBoolean(8,true);
                    stmt.addBatch();
                    stmt.executeBatch();

                    iVR_EntryID=utility_functions.getSingleIntValue(MyCon,"MAX(EntryID)","VendIssued","",context);

                    strQuery="INSERT INTO VendIssdDetail(RefID,RecieptID,ItemCode,Rate,IssQty,ReqAuth,OrderNo,RcvProcessID,ReturnDT,Rcvd_RefID,LotNo,ReWorkLot,Repair_RefID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = MyCon.prepareStatement(strQuery);
                    stmt.setInt(1,iVR_EntryID);
                    stmt.setString(2,strRecieptID);
                    stmt.setString(3,strItemID);
                    stmt.setDouble(4,dRate);
                    stmt.setInt(5,iQty);
                    stmt.setBoolean(6,true);
                    stmt.setString(7,strOrderNo);
                    stmt.setInt(8,iProcessID);
                    stmt.setDate(9,DTReturn);
                    stmt.setInt(10,iDetailEntryID);
                    stmt.setString(11,strLotNo);
                    stmt.setBoolean(12,false);
                    stmt.setInt(13,0);
                    stmt.addBatch();
                    stmt.executeBatch();

                        strQuery="EXEC SP_UpdateForIARNew ?,?,?,?,?";
                    stmt = MyCon.prepareStatement(strQuery);
                    stmt.setString(1,strItemID);
                    stmt.setInt(2,iProcessID);
                    stmt.setInt(3,iQty);
                    stmt.setString(4,strOrderNo);
                    stmt.setInt(5,iDetailEntryID);

                    stmt.addBatch();
                    stmt.executeBatch();
                    MyCon.commit();


                    Message.message(getApplicationContext(),"Successfuly Saved.");
                    onBackPressed();

                } catch (SQLException e) {

                    e.printStackTrace();
                    Message.message(context,e.toString());
                }

            }
        });


        cmdDate=(Button)findViewById(R.id.cmdIss_Date);
        cmdReturnDate=(Button)findViewById(R.id.cmdIss_ReturnDate);
        final java.util.Calendar c=java.util.Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String myDT;

        myDT=String.format("%02d",month)+'-'+String.format("%02d",day)+'-'+Integer.toString(year);
        cmdReturnDate.setText(myDT);
        cmdDate.setText(myDT);

        cmbProcess = (Spinner) findViewById(R.id.cmbIss_Process);
        cmbVendor = (Spinner) findViewById(R.id.cmbIss_Vendor);
        cmbItem = (Spinner) findViewById(R.id.cmbIss_Item);

        cmdReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                strDateCallBack="ReturnDate";
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"ReturnDate");

                //newFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });
        cmdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                strDateCallBack="Date";
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"Date");

                //newFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });

        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database



        final Button btnBack = (Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final java.util.Calendar c=java.util.Calendar.getInstance();
            //final android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c=Calendar.getInstance();
            c.set(year,month,day);

            String myDT;
            //myDT=Integer.toString(month)+'/'+Integer.toString(day)+'/'+Integer.toString(year);
            myDT=String.format("%02d",month)+'-'+String.format("%02d",day)+'-'+Integer.toString(year);
            if (strDateCallBack.equals("Date"))
                cmdDate.setText(myDT);
            else
                cmdReturnDate.setText(myDT);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0 || requestCode==1){
            if (resultCode== CommonStatusCodes.SUCCESS){
                if (data != null){
                    Barcode barcode=data.getParcelableExtra("barcode");
                    if (requestCode==0)
                        txtLotNo.setText(barcode.displayValue);
                    else
                        txtIssID.setText(barcode.displayValue);
                }
                else{
                    /*if (requestCode==0)
                        txtLotNo.setText("Barcode Not Found!!");
                    else
                        txtIssID.setText("Barcode Not Found!!");*/
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private boolean verifyLotForIssuance(String strLotNo){
        int iDetailEntryID=utility_functions.getSingleIntValue("MAX(EntryID)","VendRcvdDetail"," WHERE LotNo='"+strLotNo+"'",context);
        int iCount=utility_functions.getSingleIntValue("COUNT(Rcvd_RefID)","VendIssdDetail"," WHERE Rcvd_RefID="+Integer.toString(iDetailEntryID)+" AND LotNo='"+strLotNo+"'",context);
        if (iCount>0){
            Message.message(context,"Lot Already Issued");
            return false;
        }

        int iProcessID=utility_functions.getSingleIntValue("NextProcessID","VendRcvdDetail"," WHERE EntryID="+Integer.toString(iDetailEntryID),context);
        String strItemID=utility_functions.getSingleStringValue("ItemCode","VendRcvdDetail"," WHERE EntryID="+Integer.toString(iDetailEntryID),context);
        fillProcesses(iProcessID);
        fillItems(strItemID);
        fillVendors(strItemID,iProcessID,strLotNo);

        return true;
    }

    private void fillProcesses(int iProcessID){
        String query = "select ProcessID,Description from Processes WHERE ProcessID="+Integer.toString(iProcessID);
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            int[] toViewIDs = new int[] {R.id.txtSpinnerControl};
            Map<String, String> datanum = new HashMap<String, String>();

            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("Description",rs.getString("Description"));
                datanum.put("ProcessID",rs.getString("ProcessID"));

                data.add(datanum);
            }
            String[] fromwhere = { "Description" };

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.spinner_control,fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.spinner_item);
            cmbProcess.setAdapter(NoCoreAdapter);


        } catch (SQLException e) {

            Message.message(this,e.getMessage().toString());

        }
    }
    private void fillItems(String strItemID){
        String query = "select ItemID,ItemName from Items WHERE ItemID='"+strItemID+"'";
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            int[] toViewIDs = new int[] {R.id.txtSpinnerControl};
            Map<String, String> datanum = new HashMap<String, String>();

            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("ItemID",rs.getString("ItemID"));
                datanum.put("Name",rs.getString("ItemName"));

                data.add(datanum);
            }
            String[] fromwhere = { "Name" };

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.spinner_control,fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.spinner_item);
            cmbItem.setAdapter(NoCoreAdapter);


        } catch (SQLException e) {

            Message.message(this,e.getMessage().toString());

        }
    }
    private void fillVendors(String strItemID,int iProcessID,String strLotNo){
        int iOperation=utility_functions.getSingleIntValue("Operation","Processes"," WHERE ProcessID="+Integer.toString(iProcessID),context);
        Boolean bRepairLot=utility_functions.getSingleBooleanValue("ReWorkLot","VendRcvdDetail"," WHERE LotNo='"+strLotNo+"'",context);
        String strFactoryMakerID=utility_functions.getSingleStringValue("DataValue","GeneralData"," WHERE DataName='FactoryMaker'",context);
        String query ="";
        if (iOperation == 0){
            query = "select VendID,VendID1,VenderName from VMakers WHERE VendID="+strFactoryMakerID;
        }else if (iOperation==1){
            //query = "select VendID,VendID1,VenderName from VMakers WHERE VendID<>"+strFactoryMakerID+" AND VendID IN(SELECT VendID FROM MakerProcesses WHERE ProcessID="+Integer.toString(iProcessID)+" AND Active=1";
            query = "select VendID,VendID1,VenderName from VMakers WHERE VendID<>"+strFactoryMakerID+" AND VendID IN(SELECT VendID FROM VendAssItems WHERE ItemID='"+strItemID+"' AND ProcessID="+Integer.toString(iProcessID)+") AND Active=1";
        }else if (iOperation==2){
            query = "select VendID,VendID1,VenderName from VMakers WHERE VendID IN(SELECT VendID FROM VendAssItems WHERE ItemID='"+strItemID+"' AND ProcessID="+Integer.toString(iProcessID)+") AND Active=1";
        }
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            int[] toViewIDs = new int[] {R.id.txtSpinnerControl};
            Map<String, String> datanum = new HashMap<String, String>();

            datanum.put("VendID","0");
            datanum.put("Name","Vendor");

            data.add(datanum);

            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("VendID",rs.getString("VendID"));
                datanum.put("Name",rs.getString("VendID1")+" - "+rs.getString("VenderName"));

                data.add(datanum);
            }
            String[] fromwhere = { "Name" };

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.spinner_control,fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.spinner_item);
            cmbVendor.setAdapter(NoCoreAdapter);


        } catch (SQLException e) {

            Message.message(this,e.getMessage().toString());

        }
    }
}
