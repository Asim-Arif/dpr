package com.example.asim.amr;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class authorization extends Activity {

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
    Button cmdSave,cmdBack,cmdDecline;
    TextView lblLotNo,lblIssNo;
    EditText txtLotNo,txtReWorkQty,txtWastage;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        setContentView(R.layout.authorization);
        context = this.getBaseContext();

        txtActivityTitle = (TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("Authorization");

        txtLotNo=(EditText) findViewById(R.id.txtAuth_LotNo);
        txtReWorkQty=(EditText) findViewById(R.id.txtAuth_ReWorkQty);
        txtWastage=(EditText) findViewById(R.id.txtAuth_Wastage);
        txtLotNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    //Search for Lot No. & Update Rcvd. Qty...
                    String strLotNo=txtLotNo.getText().toString();
                    //verifyLotForReceiving(strLotNo);
                }
            }
        });

        lblLotNo=(TextView) findViewById(R.id.lblAuth_LotNo);
        lblLotNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(context, ScanBarcode.class);
                txtQty.setEnabled(false);
                startActivityForResult(intent,0);*/
            }
        });
        cmdBack=(Button) findViewById(R.id.cmdci_Back);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cmdDecline=(Button) findViewById(R.id.cmdAuth_Decline);
        cmdSave=(Button) findViewById(R.id.cmdAuth_Authorize);
        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strLotNo;
                int iReWorkQty,iWastage;
                strLotNo=txtLotNo.getText().toString();

                iReWorkQty=Integer.parseInt(txtReWorkQty.getText().toString());

                iWastage=Integer.parseInt(txtWastage.getText().toString());
                Saved(strLotNo,iReWorkQty,iWastage);
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
                return false;
            }
            iCount=utility_functions.getSingleIntValue("COUNT(*)","FCustomerCatalog"," WHERE ItemID='"+strCode+"' AND CustCode NOT IN('HICO','Stock')",context);
            if (iCount==0){
                lblLotNo.setText("Data not found !!!");
                return false;
            }
        }

        //return Saved(strCode,iQty,strSerialNo);
        return true;
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
    private boolean Saved(String strLotNo,int iReWorkQty,int iWastage){

        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        int iRcvdQty=0,iProcessID=0,iVendID=0,iVRD_EntryID=0,iRefID=0;
        double dRepairRate=0;
        boolean bOwnRepair=false,bFullLotAuthorization=false;
        String strItemCode="",strOrderNo="";
        String query = "SELECT ProcessID,RcvdQty,VendID,EntryID,OwnRepair,FullLotAuthorization,ItemCode,RefID,OrderNo FROM VVendRcvItemsrpt WHERE LotNo='"+strLotNo+"' AND ReqAuth=1";
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                iRcvdQty=rs.getInt("RcvdQty");
                iProcessID=rs.getInt("ProcessID");
                iVendID=rs.getInt("VendID");
                iVRD_EntryID=rs.getInt("EntryID");
                bOwnRepair=rs.getBoolean("OwnRepair");
                bFullLotAuthorization=rs.getBoolean("FullLotAuthorization");
                strItemCode=rs.getString("ItemCode");
                iRefID=rs.getInt("RefID");
                strOrderNo=rs.getString("OrderNo");
            }

        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
        }
        if (iRcvdQty==0){
            lblLotNo.setText("Invalid Lot #");
            return false;
        }
        if (iWastage+iReWorkQty>iRcvdQty){
            lblLotNo.setText("Invalid Wastage/ReWork");
            return false;
        }
        int iPassQty=iRcvdQty-iWastage-iReWorkQty;
        int iPolishingProcessID=0;
        String strPolishingProcessID="";
        strPolishingProcessID=utility_functions.getSingleStringValue("DataValue","GeneralData"," WHERE DataName='PolishingProcessID'",this.getApplicationContext());
        iPolishingProcessID=Integer.parseInt(strPolishingProcessID);

        String strDT=utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"MM-dd-yyyy");
        Date myDT=  utility_functions.convertStringToSQLDate(strDT,context) ;
        try{
            MyCon.setAutoCommit(false);
            if (iProcessID==iPolishingProcessID) {
                query = "INSERT INTO Store_In(EmpID,VendID,DT,UserName,MachineName,Store_Type) VALUES(?,?,?,?,?,?)";
                PreparedStatement stmt = MyCon.prepareStatement(query);
                stmt.setString(1, "");
                stmt.setInt(2, iVendID);
                stmt.setDate(3, myDT);
                stmt.setString(4, "Android");
                stmt.setString(5, "Android");
                stmt.setInt(6, 0);
                stmt.addBatch();
                stmt.executeBatch();

                int lS_I_EntryID = utility_functions.getSingleIntValue(MyCon, "MAX(EntryID)", "Store_In", "", context);

                query = "INSERT INTO Store_In_Detail(S_I_RefID,VRD_RefID,Qty) VALUES(?,?,?)";
                stmt = MyCon.prepareStatement(query);
                stmt.setInt(1, lS_I_EntryID);
                stmt.setInt(2, iVRD_EntryID);
                stmt.setInt(3, iPassQty);
                stmt.addBatch();
                stmt.executeBatch();
            }
            query="UPDATE VendRcvdDetail SET ReqAuth=?,Wastage=?,RepairAmt=?,LostQty=?,ReWorkQty=?,Insp_EmpID=?,AuthByUserName=?,AuthByMachineName=?,AuthByEntryID=getDATE() WHERE EntryID=?";
            stmt = MyCon.prepareStatement(query);
            stmt.setBoolean(1,false);
            stmt.setInt(2,iWastage);
            stmt.setInt(3,0);
            stmt.setInt(4,0);
            stmt.setInt(5,iReWorkQty);
            stmt.setString(6,"");
            stmt.setString(7,"Android");
            stmt.setString(8,"Android");
            stmt.setInt(9,iVRD_EntryID);
            stmt.addBatch();
            stmt.executeBatch();

            if (bOwnRepair==true){
                query="UPDATE VendRcvdDetail SET RcvdQty=RcvdQty+? WHERE EntryID=?";
                stmt = MyCon.prepareStatement(query);
                stmt.setInt(1,iReWorkQty);
                stmt.setInt(2,iVRD_EntryID);
                stmt.addBatch();
                stmt.executeBatch();
                dRepairRate=0;
            }else{
                dRepairRate=utility_functions.getSingleDoubleValue("RepairRate","VendAssItems"," WHERE VendID="+Integer.toString(iVendID),this.getApplicationContext());
            }
            if (iReWorkQty>0){
                query="INSERT INTO VendRcvdDetailReWorkDetail(VRD_RefID,Repair_RefID,Qty,RepairRate,OwnRepair,FullLotAuthorization,VRD_ChargeTo_RefID) VALUES(?,?,?,?,?,?,?)";
                stmt = MyCon.prepareStatement(query);
                stmt.setInt(1,iVRD_EntryID);
                stmt.setInt(2,0);
                stmt.setInt(3,iReWorkQty);
                stmt.setDouble(4,dRepairRate);
                stmt.setBoolean(5,bOwnRepair);
                stmt.setBoolean(6,bFullLotAuthorization);
                stmt.setInt(7,iVRD_EntryID);
                stmt.addBatch();
                stmt.executeBatch();
            }
            if (iWastage>0){
                query="INSERT INTO VendRcvdDetailWastageDetail(VRD_RefID,Wastage_RefID,Qty,WastageType,ReturnTo_VRD_RefID,VendID,EmpID) VALUES(?,?,?,?,?,?,?)";
                stmt = MyCon.prepareStatement(query);
                stmt.setInt(1,iVRD_EntryID);
                stmt.setInt(2,0);
                stmt.setInt(3,iWastage);
                stmt.setInt(4,0);
                stmt.setInt(5,0);
                stmt.setInt(6,0);
                stmt.setString(7,"");
                stmt.addBatch();
                stmt.executeBatch();
            }
            query="EXEC SP_InsertVendRcvdNew ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
            stmt = MyCon.prepareStatement(query);
            stmt.setString(1,strItemCode);
            stmt.setInt(2,iProcessID);
            if (bOwnRepair){
                stmt.setInt(3,iRcvdQty-iReWorkQty);
            }else{
                stmt.setInt(3,iRcvdQty);
            }
            stmt.setInt(4,iWastage);
            stmt.setInt(5,0);       //Repair Qty
            stmt.setInt(6,0);       //Lost Qty
            stmt.setInt(7,iRefID);    //@RefID
            stmt.setInt(8,iVendID);
            stmt.setString(9,strLotNo);
            stmt.setString(10,"");  //@RecID
            stmt.setDate(11,myDT);      //@RcvDT
            stmt.setBoolean(12,false); //@ReqAuth
            stmt.setInt(13,0);     //@UserID
            stmt.setBoolean(14,true); //@AuthorizeEntry
            stmt.setString(15,strOrderNo);
            stmt.setInt(16,0);//@DeliveryRefID
            stmt.setString(17,"");  //@CountedBy
            stmt.setInt(18,0);//@Issue_RefID
            stmt.setInt(19,0);//@Weight
            stmt.setInt(20,iReWorkQty);//@ReWorkQty
            stmt.setBoolean(21,false); //@ReWorkLot
            stmt.setInt(22,0);//@ReWorkType
            stmt.setInt(23,iVRD_EntryID);
            stmt.setBoolean(24,false); //@Rejection
            stmt.setString(25,"");  //@EmpID
            stmt.setString(26,"Android");
            stmt.setString(27,"Android");

            stmt.addBatch();
            stmt.executeBatch();
            MyCon.commit();
            lblLotNo.setText("Lot # " + strLotNo + " updated successfully.");
            txtReWorkQty.setText("0");
            txtWastage.setText("0");
            txtLotNo.setText("");
            txtLotNo.requestFocus();

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
