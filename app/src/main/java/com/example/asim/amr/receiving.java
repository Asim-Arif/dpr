package com.example.asim.amr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/*import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Text;*/

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class receiving extends Activity {

    TextView txtActivityTitle;
    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner cmbOrder;
    Spinner cmbEmployee;
    Spinner cmbArticle;
    Spinner cmbOperation;
    static Button cmdReturnDate;
    SimpleCursorAdapter myCursorAdapter;
    Button cmdSave,cmdBack;
    TextView lblLotNo,lblIssNo;
    EditText txtLotNo;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        setContentView(R.layout.receiving);
        context = this.getBaseContext();

        txtActivityTitle = (TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("Receiving");

        txtLotNo=(EditText) findViewById(R.id.txtRcv_LotNo);

        //this.UpdateTotals();
        /*txtLotNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    //Search for Lot No. & Update Rcvd. Qty...
                    String strLotNo=txtLotNo.getText().toString();
                    verifyLotForReceiving(strLotNo);
                }
            }
        });*/
        lblLotNo=(TextView) findViewById(R.id.lblRcv_LotNo);
        lblLotNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(context, ScanBarcode.class);
                txtQty.setEnabled(false);
                startActivityForResult(intent,0);*/
            }
        });

        txtLotNo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String strLotNo=txtLotNo.getText().toString();
                    boolean rtn;
                    verifyLotForReceiving(strLotNo);
                    return true;
                }
                else{
                    return false;
                }
            }
        });

        cmdBack=(Button) findViewById(R.id.cmdci_Back);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cmdSave=(Button) findViewById(R.id.cmdRcv_Save);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        /*cmbEmployee = (Spinner) findViewById(R.id.cmbci_Employee);

        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "select EmpID,Name from Employees where Active=1";

        try {
            stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            int[] toViewIDs = new int[] {R.id.txtSpinnerControl};
            Map<String, String> datanum = new HashMap<String, String>();
            datanum.put("Name","Employee");
            datanum.put("EmpID","");

            data.add(datanum);
            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("Name",rs.getString("Name"));
                datanum.put("EmpID",rs.getString("EmpID"));

                data.add(datanum);
            }
            String[] fromwhere = { "Name" };

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.spinner_control,fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.spinner_item);
            cmbEmployee.setAdapter(NoCoreAdapter);


        } catch (SQLException e) {

            Message.message(this,e.getMessage().toString());

        }*/

        final Button btnBack = (Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


    /*@Override
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
                    if (requestCode==0)
                        txtLotNo.setText("Barcode Not Found!!");
                    else
                        txtIssID.setText("Barcode Not Found!!");
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/
    private boolean verifyLotForReceiving(String strLotNo){
        int iPos,iLastPos,iQty=0,iCount;
        String strCode="",strSerialNo="";
        iPos=strLotNo.indexOf("-");
        iLastPos=strLotNo.lastIndexOf("-");
        if (iPos>0){
            strCode=strLotNo.substring(0,iPos);
            strSerialNo=strLotNo.substring(iPos+1,iLastPos);
            strCode = strCode.trim();
            iQty=Integer.parseInt(strLotNo.substring(iLastPos+1));
            iCount=utility_functions.getSingleIntValue("COUNT(*)","RcvItemsSimple"," WHERE SerialNo='"+strSerialNo+"' AND CustItemID='"+strCode+"' AND Ignored=0",context);
            if (iCount>0) {
                lblLotNo.setText("Serial # " + strSerialNo + " is already scanned.");
                txtLotNo.getText().clear();
                //txtLotNo.requestFocus();
                //Message.message(this.context,"already Scanned.");
                return false;
            }
            iCount=utility_functions.getSingleIntValue("COUNT(*)","FCustomerCatalog"," WHERE ItemID='"+strCode+"' AND CustCode NOT IN('HICO','Stock')",context);
            if (iCount==0){
                lblLotNo.setText("Data not found !!!");
                return false;
            }
        }
        return Saved(strCode,iQty,strSerialNo);

    }
    private String getRcvNo(){
        int iMax;
        String myDT= utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"MM/dd/yyyy");
        iMax=utility_functions.getSingleIntValue("MAX(CAST(RIGHT(RcvNo,LEN(RcvNo)-11) AS INT))","RcvItemsSimple","WHERE DT='"+myDT+"'",context);
        iMax++;
        String strRcvNo,myDT_Formatted;
        myDT_Formatted=utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"yyMMdd");
        strRcvNo="RCV-"+myDT_Formatted+"-"+Integer.toString(iMax);
        return strRcvNo;
    }
    private boolean Saved(String strCode,int iQty,String strSerialNo){

        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String strRcvNo=getRcvNo();
        //String strDT=utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"MM/dd/yy");
        String strDT=utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"MM-dd-yyyy");
        Date myDT=  utility_functions.convertStringToSQLDate(strDT,context) ;
        String strCustCode=utility_functions.getSingleStringValue("CustCode","FCustomerCatalog"," WHERE ItemID='"+strCode+"' AND CustCode NOT IN('HICO','Stock')",context);
        String strCountry=utility_functions.getSingleStringValue("Country","FCustomerCatalog"," WHERE ItemID='"+strCode+"' AND CustCode NOT IN('HICO','Stock')",context);
        String strCompItemID=utility_functions.getSingleStringValue("CompItemID","FCustomerCatalog"," WHERE ItemID='"+strCode+"' AND CustCode NOT IN('HICO','Stock')",context);
        try {
            MyCon.setAutoCommit(false);
            String strQuery = "INSERT INTO RcvItemsSimple(RcvNo,DT,UserName,ComputerName,CustCode,Country,Qty,ItemID,CustItemID,Store_RefID,SerialNo,SNo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = MyCon.prepareStatement(strQuery);
            stmt.setString(1, strRcvNo);
            stmt.setDate(2,myDT);
            stmt.setString(3,"Android");
            stmt.setString(4,"Android");
            stmt.setString(5,strCustCode);
            stmt.setString(6, strCountry);
            stmt.setInt(7,iQty);
            stmt.setString(8, strCompItemID);
            stmt.setString(9, strCode);
            stmt.setInt(10,1);
            stmt.setString(11,strSerialNo);
            stmt.setInt(12,0);
            stmt.addBatch();
            stmt.executeBatch();

            int iRIS_EntryID=utility_functions.getSingleIntValue(MyCon,"MAX(EntryID)","RcvItemsSimple","",context);

            strQuery="EXEC Adjust_Production_Entries_Against_HnD_Receiving ?,?,?";
            stmt = MyCon.prepareStatement(strQuery);
            stmt.setString(1,strCompItemID);
            stmt.setInt(2,iRIS_EntryID);
            stmt.setInt(3,iQty);

            stmt.addBatch();
            stmt.executeBatch();

            //////Maker Billlinng
            double dRate;
            int iVendID;
            dRate=utility_functions.getSingleDoubleValue("VendID","VendAssItems"," WHERE ItemID='"+strCompItemID+"' AND ProcessID=278",context);
            iVendID=utility_functions.getSingleIntValue("Rate","VendAssItems"," WHERE ItemID='"+strCompItemID+"' AND ProcessID=278",context);
            int iMakerRcvNo;
            String strMakerRcvNo;
            iMakerRcvNo=utility_functions.getSingleIntValue("MAX(CAST(RIGHT(RecieptID,Len(RecieptID)-14) AS INT))","VendReceived","WHERE RecieptID<>'' AND ProcessID=278 AND Convert(VARCHAR,DT,1)='"+strDT+"'",context);
            iMakerRcvNo++;
            String strProcess="HOLDING & DISTRIBUTION";
            String myDT_Formatted=utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"ddMMyy");
            strMakerRcvNo =strProcess.substring(1,3)+"-RCV-"+myDT_Formatted+Integer.toString(iMakerRcvNo);

            int iRecieptID=utility_functions.getSingleIntValue("MAX(CAST(RIGHT(RecieptID,Len(RecieptID)-14) AS INT))", "VVendRcvdItems","WHERE RecieptID<>'' AND ProcessID=278 AND Convert(VARCHAR,DT,1)='"+strDT+"'",context);

            strQuery="INSERT INTO VendReceived(VendID,DT,RecieptID,UserID,ProcessID,OverTime,UserName,MachineName,TemperValue,EmpID) VALUES(?,?,?,?,?,?,?,?,?,?)";
            stmt = MyCon.prepareStatement(strQuery);
            stmt.setInt(1,iVendID);
            stmt.setDate(2,myDT);
            stmt.setString(3,strMakerRcvNo);
            stmt.setInt(4,1);
            stmt.setInt(5,278);
            stmt.setBoolean(6,false);
            stmt.setString(7,"Android");
            stmt.setString(8,"Android");
            stmt.setString(9,"");
            stmt.setString(10,"");
            stmt.addBatch();
            stmt.executeBatch();

            int iVI_EntryID=utility_functions.getSingleIntValue(MyCon,"MAX(EntryID)","VendReceived"," WHERE MachineName='Android'",context);
            String strVRD_IssNo;
            //int iRecieptID=utility_functions.getSingleIntValue("MAX(CAST(RIGHT(RecieptID,Len(RecieptID)-14) AS INT))", "VVendRcvdItems","WHERE RecieptID<>'' AND ProcessID=278 AND Convert(VARCHAR,DT,1)='"+strDT+"'",context);
            iRecieptID++;
            strVRD_IssNo = "HOL"+"-RCV-"+myDT_Formatted+Integer.toString(iRecieptID);
            strQuery="INSERT INTO VendRcvdDetail(RefID,RecieptID,ItemCode,NextProcessID,RcvdQty,Wastage,Rate,LotNo,ReqAuth,OrderNo,CountedBy,Issue_RefID,ProcessID,RcvdWeight,ReWorkLot,Repair_RefID,Rejection,RIS_EntryID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            stmt = MyCon.prepareStatement(strQuery);
            stmt.setInt(1,iVI_EntryID);
            stmt.setString(2,strVRD_IssNo);
            stmt.setString(3,strCompItemID);
            stmt.setInt(4,0);
            stmt.setInt(5,iQty);
            stmt.setInt(6,0);
            stmt.setDouble(7,dRate);
            stmt.setString(8,"");
            stmt.setBoolean(9,false);
            stmt.setString(10,"Stock-001");
            stmt.setString(11,"");
            stmt.setInt(12,0);
            stmt.setInt(13,278);
            stmt.setInt(14,0);
            stmt.setInt(15,0);
            stmt.setInt(16,0);
            stmt.setInt(17,0);
            stmt.setInt(18,iRIS_EntryID);

            stmt.addBatch();
            stmt.executeBatch();
            //////Maker Billlinng
            MyCon.commit();
            lblLotNo.setText("Serial # " + strSerialNo + " scanned successfully.");
            txtLotNo.getText().clear();
            //txtLotNo.setText("");

            this.UpdateTotals();
            //txtLotNo.requestFocus();
        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
        }
        return true;
    }
    private void UpdateTotals()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String strDT=utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"MM-dd-yyyy");
        String query = "SELECT CustCode,ItemID,COUNT(*) AS TotalPacks,SUM(Qty) AS TotalQty FROM RcvItemsSimple WHERE DT='"+strDT+"' GROUP BY CustCode,ItemID";

        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            Map<String, String> datanum = new HashMap<String, String>();

            int iPacks,iQty;
            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                //datanum.put("CustCode",rs.getString("CustCode"));
                datanum.put("ItemID",rs.getString("ItemID"));
                iPacks=rs.getInt("TotalPacks");
                datanum.put("Packs",Integer.toString(iPacks));
                iQty=rs.getInt("TotalQty");
                datanum.put("Qty",Integer.toString(iQty));

                data.add(datanum);
            }
            String[] fromwhere = { "ItemID","Packs","Qty"};

            int[] toViewIDs = new int[] {R.id.txtRcv_Layout_ItemID,R.id.txtRcv_Layout_Packs,R.id.txtRcv_Layout_Qty};

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.receiving_layout, fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.receiving_layout);

            ListView myLV;
            myLV=(ListView) findViewById(R.id.LV);

            myLV.setAdapter(NoCoreAdapter);
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }
}
