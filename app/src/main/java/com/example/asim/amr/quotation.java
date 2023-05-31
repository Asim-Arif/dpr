
package com.example.asim.amr;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;

import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class quotation extends Activity {

    EditText txtTableNo,txtServer,txtOrderFrom,txtOrderDuration;
    Spinner cmbHall;
    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    SimpleAdapter myCursorAdapter;
    Button cmdBack,cmdFamilyHall,cmdLaibaHall,cmdGentsHall,cmdCafe,cmdTakeAway,cmdEventHall;
    Context context;
    ArrayList data;
    Map<String, String> datanum;
    Button cmdClear,cmdQuotation,cmdSave;
    ListView myLV;
    int iAmount=0,lPendingSaleEntryID=0;
    LinearLayout ll_Family_Hall,ll_Laiba_HHall,ll_Gents_Hall,ll_Cafe,ll_TakeAway,ll_EventHall;
    //Button[] myTables=new Button[100];;
    class tableData{
        Button btnTable;
        int iTableIndex;
        int iEntryID;
        int iStatus;//0=No Sales Data,1=Yellow Color Sales Data,2=Blue Color,3=Invoice
    }
    ArrayList<tableData> TableData_List=new ArrayList<tableData>();
    //String strDeviceName="Emulator";
    String strDeviceName = BluetoothAdapter.getDefaultAdapter().getName();
    int iSalesTax;
    int iMilliSeconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        setContentView(R.layout.quotation);
        context = this.getBaseContext();

        cmbHall=(Spinner) findViewById(R.id.cmbHall);
        fillHalls();

        cmbHall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strHall=cmbHall.getSelectedItem().toString();
                if (strHall.equals("Select Hall")==true)
                {
                    //nothing to do here.
                }
                else
                {
                    updateTotalAmount();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtTableNo=(EditText) findViewById(R.id.txtTableno);
        txtServer=(EditText)  findViewById(R.id.txtServer);
        txtOrderFrom=(EditText) findViewById(R.id.txtOrderfrom);
        txtOrderDuration=(EditText) findViewById(R.id.txtOrderduration);

        txtOrderDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar currentDate = Calendar.getInstance();
                final Calendar date=Calendar.getInstance();
                new DatePickerDialog(view.getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                //Log.v(TAG, "The choosen one " + date.getTime());
                                //Message.message(context,date.getTime().toString());
                                SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                txtOrderDuration.setText(sdf.format(date.getTime()));
                                sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                txtOrderDuration.setTag(sdf.format(date.getTime()));
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });

        data = new ArrayList<Map<String, String>>();

        String[] fromwhere = { "ItemName","Qty","Amount"};

        int[] toViewIDs = new int[] {R.id.txtItemName,R.id.txtQty,R.id.txtAmt};

        myCursorAdapter=new SimpleAdapter(context,data,R.layout.quotation_lv_layout,fromwhere,toViewIDs);
        myCursorAdapter.setDropDownViewResource(R.layout.quotation_lv_layout);

        cmdSave=(Button) findViewById(R.id.cmdSave);

        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuotation();
            }
        });


        myLV=(ListView) findViewById(R.id.LV);


        myLV.setClickable(true);
        myLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Message.message(context,Integer.toString(i));
            }
        });
        myLV.setAdapter(myCursorAdapter);

        /*iMilliSeconds = Integer.parseInt(utility_functions.getSingleStringValue("DataValue","GeneralData"," WHERE DataName='Tables_Refresh_Seconds'",context));
        iMilliSeconds = iMilliSeconds *1000;*/

        loadMainMenus();

    }


    private void loadMainMenus()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT * FROM MenuGroups ORDER BY SortNo";
        String strMenu_Urdu="";
        int iID;
        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next())
            {
                GridLayout menuLayout = (GridLayout) findViewById(R.id.ll_Menus);
                Button b1 = new Button(this);
                strMenu_Urdu=rs.getString("UrduDescription");
                iID=rs.getInt("ID");
                //b1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                b1.setText(strMenu_Urdu);
                b1.setId(iID);
                GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                params.rightMargin = 2;
                params.bottomMargin=2;
                params.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
                params.width=0;
                params.rowSpec=GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
                params.height=70;
                //b1.setHeight(70);
                //b1.setWidth(130);

                int iFontSize=8;
                /*if (strMenu_Urdu.length()>=9)
                    iFontSize=5;
                else if (strMenu_Urdu.length()>8)men
                    iFontSize=7;
                else
                    iFontSize=8;*/
                String pattern = "^[A-Za-z0-9. ]+$";
                int iHeight=0;
                int iWidht=0;
                Rect bounds = new Rect();
                Paint textPaint = b1.getPaint();
                textPaint.getTextBounds(strMenu_Urdu, 0, strMenu_Urdu.length(), bounds);
                iHeight = bounds.height();
                iWidht = bounds.width();
                String strFirst=strMenu_Urdu.substring(0,1);
                if (strFirst.matches(pattern)) {
                    iFontSize=getFontSize(strMenu_Urdu,strMenu_Urdu.length(),iWidht);
                    //Reduce to 60%
                    iFontSize=iFontSize-(iFontSize*40/100);
                }
                else
                    iFontSize=getFontSize(strMenu_Urdu,strMenu_Urdu.length(),iWidht);

                b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,iFontSize);
                //b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
                //b1.setTypeface(null,Typeface.BOLD);
                b1.setTextColor(Color.WHITE);
                int iBKColor;

                iBKColor=rs.getInt("GrpColor");
                String strColor=utility_functions.covertToAndroidColor(String.valueOf(iBKColor));
                try{
                    iBKColor=Color.parseColor(strColor);
                } catch(Exception e){
                    //Do Nothing...
                    iBKColor=Color.GRAY;
                }
                b1.setBackgroundColor(iBKColor);
                b1.setTag(iBKColor);


                menuLayout.addView(b1,params);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int iID=v.getId();

                        if (isSaleAvailable()==false)
                            return;

                        TextView txtSubMenu=(TextView) findViewById(R.id.tvSubmenu);
                        Button btnSelected = (Button)v;
                        txtSubMenu.setText(btnSelected.getText().toString().toUpperCase());

                        //Firstly, Remove all sub menus...
                        GridLayout menuLayout = (GridLayout) findViewById(R.id.ll_SubMenus);
                        menuLayout.removeAllViews();
                        loadSubMenus(iID,Integer.parseInt(btnSelected.getTag().toString()));

                    }
                });
            }

        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }
    private void loadSubMenus(int iGroupID, int iBGColor)
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT  * FROM VProductsForPOS WHERE GroupID="+Integer.toString(iGroupID)+" AND InActive=0 ORDER BY SortNo";
        String strMenu_Urdu="";
        int iID;
        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next())
            {
                GridLayout menuLayout = (GridLayout) findViewById(R.id.ll_SubMenus);
                Button b1 = new Button(this);
                strMenu_Urdu=rs.getString("UrduName");
                iID=rs.getInt("EntryID");
                //b1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                b1.setText(strMenu_Urdu);
                b1.setId(iID);

                b1.setSingleLine(true);
                int iFontSize=8;
                String pattern = "^[A-Za-z0-9. ]+$";
                int iHeight=0;
                int iWidht=0;
                Rect bounds = new Rect();
                Paint textPaint = b1.getPaint();
                textPaint.getTextBounds(strMenu_Urdu, 0, strMenu_Urdu.length(), bounds);
                iHeight = bounds.height();
                iWidht = bounds.width();
                String strFirst=strMenu_Urdu.substring(0,1);
                if (strFirst.matches(pattern)) {
                    iFontSize =getFontSize(strMenu_Urdu, strMenu_Urdu.length(),iWidht);
                    //b1.setTextSize(TypedValue.COMPLEX_UNIT_PT, iFontSize);
                    b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, iFontSize);
                }
                else {
                    iFontSize = getFontSize(strMenu_Urdu, strMenu_Urdu.length(),iWidht);
                    //b1.setTextSize(TypedValue.COMPLEX_UNIT_PT,iFontSize);
                    b1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,iFontSize);
                    b1.setTypeface(null,Typeface.BOLD);
                }

                b1.setBackgroundColor(iBGColor);
                GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                params.rightMargin = 1;
                params.bottomMargin=1;
                //params.height=70;
                params.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
                params.width=0;
                params.rowSpec=GridLayout.spec(GridLayout.UNDEFINED,GridLayout.FILL,1f);
                params.height=70;
                //params.width=218;

                /*b1.setHeight(70);
                b1.setWidth(218);*/
                b1.setTextColor(Color.WHITE);
                if (rs.getBoolean("Not_Available")){
                    b1.setBackgroundColor(Color.BLACK);
                }
                menuLayout.addView(b1,params);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int iID=v.getId();
                        //Message.message(context,Integer.toString(v.getTop()));
                        addItemToLV(iID);
                    }
                });
            }
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }
    private void addItemToLV(int iFMID)
    {
        boolean bNotAvailable=false;
        bNotAvailable = utility_functions.getSingleBooleanValue("Not_Available","FinalMaterials"," WHERE EntryID="+Integer.toString(iFMID),context);
        if (bNotAvailable)
        {
            Message.message(context,"Not Available");
            return;
        }
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT * FROM FinalMaterials WHERE EntryID="+Integer.toString(iFMID);
        String strFMName="";
        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            if (rs.next())
            {
                int i;
                int iQty=0;
                int dAmt=0;
                for (i=0;i<data.size();i++)
                {
                    //datanum=data[i];
                    Map<String, String> datanum=(Map<String, String>) data.get(i);
                    int iEntryID=Integer.parseInt(datanum.get("FM_EntryID"));
                    if (iEntryID==iFMID)
                    {
                        iQty=Integer.parseInt(datanum.get("Qty"));
                        iQty=iQty+1;
                        dAmt=iQty*Integer.parseInt(datanum.get("Rate"));
                        //datanum.entrySet("Qty",Integer.toString(iQty));
                        datanum.put("Qty",Integer.toString(iQty));
                        datanum.put("Amount",Integer.toString(dAmt));
                        data.set(i,datanum);
                        myCursorAdapter.notifyDataSetChanged();
                        updateTotalAmount();
                        return;
                    }
                    //Check if Item already exists, just increment the qty.
                }
                int dRate=0;
                strFMName=rs.getString("UrduName");
                dRate=rs.getInt("Price");
                //int iFontSize=getFontSize(strFMName.length());
                /*if (rs.getBoolean("AmountWiseSales") || rs.getBoolean("QtyWiseSales"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    if (rs.getBoolean("AmountWiseSales"))
                        builder.setTitle("Enter Amount");
                    else
                        builder.setTitle("Enter Qty");
                    // Set up the input

                    final EditText input = new EditText(this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            iAmount = Integer.parseInt(input.getText().toString());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                    Message.message(context,"Test");
                }
                else
                {
                    datanum=new HashMap<String, String>();
                    datanum.put("ItemName",strFMName);
                    datanum.put("Qty","1");
                    datanum.put("Rate",Double.toString(dRate));
                    datanum.put("EntryID",Integer.toString(iFMID));
                    data.add(datanum);

                    myCursorAdapter.notifyDataSetChanged();
                }*/
                datanum=new HashMap<String, String>();
                datanum.put("ItemName",strFMName);
                datanum.put("Qty","1");
                datanum.put("Rate",Integer.toString(dRate));
                datanum.put("FM_EntryID",Integer.toString(iFMID));
                datanum.put("EntryID","0");
                datanum.put("PreviousQty","0");
                datanum.put("TabQty","0");
                datanum.put("Column6Tag","");
                datanum.put("QtyPrinted","0");
                datanum.put("Amount",Integer.toString(dRate));
                data.add(datanum);

                myCursorAdapter.notifyDataSetChanged();
                updateTotalAmount();
                myLV.setSelection(myCursorAdapter.getCount() - 1);

            }
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }
    }
    public void tbl_btn_click(View view){

        int iSaleType=0;
        Button cmdSelected=(Button) findViewById(view.getId());
        String strTableNo=cmdSelected.getText().toString();
        int iTableNo=Integer.parseInt(strTableNo);
        tableData tableData_Obj=(tableData) TableData_List.get(iTableNo-1);
        //If iStatus=1, it means it holds some data, we need to load pending sale.
        if (tableData_Obj.iStatus==1){
            loadPendingSale(iTableNo,tableData_Obj);
            return;
        }
        else if (tableData_Obj.iStatus==3){ //If iStatis=3, it means i's Invoice, No need to do anything in tablet.
            return;
        }
        if (!txtTableNo.getText().toString().equals(""))
        {
            if (!strTableNo.equals(txtTableNo.getText().toString()))
            {
                Message.message(context,"This sale is pending from "+txtTableNo.getText().toString());
                return;
            }
        }
        if (data.size()==0)
        {
            Message.message(context,"No Sale Data !!!");
            return;
        }
        if (txtServer.getText().toString().equals("")){    //New Pending Sale
            String strTableLocked_ComputerName = utility_functions.getSingleStringValue("MachineName","Tables_Locked"," WHERE TableNo='"+strTableNo+"' AND MachineName<>'"+strDeviceName+"'",context);
            if (strTableLocked_ComputerName!="")
            {
                Message.message(context,"Table is locked from "+strTableLocked_ComputerName+", Please user other table.");
                return;
            }
            String strServer=utility_functions.getSingleStringValue("WaiterName","POS_Settings"," WHERE MachineName='"+strDeviceName+"'",context);
            txtServer.setText(strServer);
            txtTableNo.setText(strTableNo);
        }
        else
        {
            /*
            strServer = lblServer.Caption
                strTableNo = lblTableNo.Caption
                iSaleType = lblServer.Tag
            */

        }
        final java.util.Calendar CurrentTime=java.util.Calendar.getInstance();
        java.util.Date DT=null;
        DT=utility_functions.getCurrentDate(context);
        String strDT="";
        Date DTInvoice=null;
        if (CurrentTime.get(Calendar.HOUR)<7)
        {
            CurrentTime.add(Calendar.DATE,-1);
            strDT=utility_functions.convertDateToString(CurrentTime.getTime(),"MMM-dd-yyyy");
            //DTInvoice=utility_functions.convertStringToSQLDate(strDT,context);
        }
        else
        {
            strDT=utility_functions.convertDateToString(DT,"yyyy-MM-dd HH:mm:ss z");
            //DTInvoice=utility_functions.convertStringToSQLDate(strDT,context);
        }
        int lInvNoFromPending=0,lInvNoFromSales=0,lInvoiceNo=0;
        if (txtServer.getTag().toString().equals(""))
        {
            lInvNoFromPending = utility_functions.getSingleIntValue("MAX(InvoiceNo)","PendingSales","",context);
            lInvNoFromSales = utility_functions.getSingleIntValue("MAX(InvoiceNo)", "ItemSales", " WHERE DT='" + strDT + "'",context);
            if (lInvNoFromPending > lInvNoFromSales)
                lInvoiceNo = lInvNoFromPending;
            else
                lInvoiceNo = lInvNoFromSales;

            lInvoiceNo = lInvoiceNo + 1;
        }
        else{
            lInvoiceNo=Integer.parseInt(txtServer.getTag().toString());
        }
        String strOrderFrom_ComputerName="";
        if (txtOrderFrom.getText().toString().equals("")){
            strOrderFrom_ComputerName = strDeviceName;
        }
        else{
            strOrderFrom_ComputerName = txtOrderFrom.getText().toString();
        }
        txtOrderFrom.setText("");
        txtOrderDuration.setText("");
        int iTotalAmount=0;
        int i;
        for (i=0;i<data.size();i++){
            Map<String, String> datanum=(Map<String, String>) data.get(i);
            int iRate=Integer.parseInt(datanum.get("Rate"));
            int iQty=Integer.parseInt(datanum.get("Qty"));
            iTotalAmount=iTotalAmount+(iQty*iRate);
        }
        boolean bSTax_Enabled;
        bSTax_Enabled = utility_functions.getSingleBooleanValue("Sales_Tax_Applicable", "Hall_List", " WHERE " + Integer.toString(iTableNo) + " BETWEEN TableFrom AND TableTo",context);
        iSalesTax=0;
        if (bSTax_Enabled){
            double dSalesTaxPer;
            dSalesTaxPer = Double.parseDouble(utility_functions.getSingleStringValue("DataValue","GeneralData"," WHERE DataName='SalesTax'",context)) ;
            iSalesTax=(int) Math.round(iTotalAmount*(dSalesTaxPer /100));
        }

        boolean bOrderChanged_New=false;
        String strUserName=utility_functions.getSingleStringValue("UserName","POS_Settings"," WHERE MachineName='"+strDeviceName+"'",context);
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String strQuery="";
        int iPS_EntryID=0;
        PreparedStatement stmt;
        try {
            MyCon.setAutoCommit(false);
            if (lPendingSaleEntryID==0) {
                strQuery = "INSERT INTO PendingSales(ButtonNumber,Server,TableNo,Payable,Received,Status,UserName,MachineName,SaleType,DrinksUpsize,FriesUpsize,DrinksUpsizeRate,FriesUpsizeRate,InvoiceNo,ManualSTax,STaxAmt) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setInt(1, Integer.parseInt((txtTableNo.getText().toString())));
                stmt.setString(2, txtServer.getText().toString());
                stmt.setString(3, txtTableNo.getText().toString());
                stmt.setInt(4, iTotalAmount);
                stmt.setInt(5, 0);
                stmt.setInt(6, 0);
                stmt.setString(7, strUserName);
                stmt.setString(8, strDeviceName);
                stmt.setInt(9, iSaleType);
                stmt.setInt(10, 0);
                stmt.setInt(11, 0);
                stmt.setInt(12, 0);
                stmt.setInt(13, 0);
                stmt.setInt(14, lInvoiceNo);
                stmt.setInt(15, 0);
                stmt.setInt(16, iSalesTax);
                stmt.addBatch();
                stmt.executeBatch();

                iPS_EntryID = utility_functions.getSingleIntValue(MyCon, "MAX(EntryID)", "PendingSales", " WHERE MachineName='" + strDeviceName + "'", context);
            }
            else{
                strQuery = "UPDATE PendingSales SET Payable=?,STaxAmt=? WHERE EntryID=?";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setInt(1, iTotalAmount);
                stmt.setInt(2, lPendingSaleEntryID);
                stmt.setInt(3, iSalesTax);
                stmt.addBatch();
                stmt.executeBatch();
                iPS_EntryID=lPendingSaleEntryID;
            }
            for (i=0;i<data.size();i++){
                Map<String, String> datanum=(Map<String, String>) data.get(i);
                int iEntryID=Integer.parseInt(datanum.get("EntryID"));
                String strItemName=datanum.get("ItemName");
                int iFM_EntryID=Integer.parseInt(datanum.get("FM_EntryID"));
                int iRate=Integer.parseInt(datanum.get("Rate"));
                int iQty=Integer.parseInt(datanum.get("Qty"));
                int iPreviouseQty=Integer.parseInt(datanum.get("PreviousQty"));
                int iTabQty_Saved=Integer.parseInt(datanum.get("TabQty"));
                String strColumn6Tag=datanum.get("Column6Tag");     //will have "saved" once loaded.
                int iQtyPrinted=Integer.parseInt(datanum.get("QtyPrinted"));
                int iTabQty=iTabQty_Saved+iQty-iPreviouseQty;
                if (iEntryID==0){
                    strQuery = "INSERT INTO PendingSalesDetail(RefID,Column1,Column1Key,Column1Tag,Column2,Column2Tag,Column3,Column3Tag,Column4,Column4Tag,Column5,Column5Tag,Column6,Column6Tag,QtyPrinted,MachineName_Last,QtyTab,MachineName_Last_Tab) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = MyCon.prepareStatement(strQuery);
                    stmt.setInt(1, iPS_EntryID);
                    stmt.setString(2,strItemName);
                    stmt.setString(3, Integer.toString(iFM_EntryID)+"'FM");
                    stmt.setString(4,"");
                    stmt.setString(5,Integer.toString(iQty));
                    stmt.setString(6,"FALSE");
                    stmt.setString(7,Integer.toString(iRate));
                    stmt.setString(8,"");
                    stmt.setString(9,"0");
                    stmt.setString(10,"");
                    stmt.setString(11,Integer.toString(iQty*iRate));
                    stmt.setString(12,"");
                    stmt.setString(13,"");
                    stmt.setString(14,strColumn6Tag);
                    stmt.setString(15,Integer.toString(iQtyPrinted));
                    stmt.setString(16,strDeviceName);
                    stmt.setString(17,Integer.toString(iQty));
                    stmt.setString(18,strDeviceName);
                    stmt.addBatch();
                    stmt.executeBatch();
                    bOrderChanged_New=true;
                }
                else{
                    strQuery = "UPDATE PendingSalesDetail SET Column1=?,Column1Key=?,Column1Tag=?,Column2=?,Column2Tag=?,Column3=?,Column3Tag=?,Column4=?,Column4Tag=?,Column5=?,Column5Tag=?,Column6=?,Column6Tag=?,MachineName_Last=?,QtyTab=?,MachineName_Last_Tab=? WHERE EntryID=?";
                    stmt = MyCon.prepareStatement(strQuery);
                    stmt.setString(1,strItemName);
                    stmt.setString(2, Integer.toString(iFM_EntryID)+"'FM");
                    stmt.setString(3,"");
                    stmt.setString(4,Integer.toString(iQty));
                    stmt.setString(5,"FALSE");
                    stmt.setString(6,Integer.toString(iRate));
                    stmt.setString(7,"");
                    stmt.setString(8,"0");
                    stmt.setString(9,"");
                    stmt.setString(10,Integer.toString(iQty*iRate));
                    stmt.setString(11,"");
                    stmt.setString(12,"");
                    stmt.setString(13,strColumn6Tag);
                    stmt.setString(14,strDeviceName);
                    stmt.setString(15,Integer.toString(iQty));
                    stmt.setString(16,strDeviceName);
                    stmt.setInt(17,iEntryID);
                    stmt.addBatch();
                    stmt.executeBatch();
                    if (iQty>iPreviouseQty){
                        bOrderChanged_New=true;
                    }
                }

            }
            strQuery = "DELETE FROM Tables_Locked WHERE MachineName=?";
            stmt = MyCon.prepareStatement(strQuery);
            stmt.setString(1, strDeviceName);
            stmt.addBatch();
            stmt.executeBatch();
            if (bOrderChanged_New){
                strQuery = "UPDATE PendingSales SET DTEntry_Last=getDate() WHERE EntryID=?";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setInt(1, lPendingSaleEntryID);
                stmt.addBatch();
                stmt.executeBatch();
            }
            lPendingSaleEntryID = 0;

            MyCon.commit();
            data.clear();
            myCursorAdapter.notifyDataSetChanged();
            txtTableNo.setText("");
            txtServer.setText("");
            tableData_Obj.iStatus=1;
            tableData_Obj.iEntryID=iPS_EntryID;
            //tableData_Obj.btnTable.setBackgroundColor(Color.YELLOW);
            tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_yellow);


        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
        }

    }
    private void refreshTables(){
        int i;
        tableData tableData_Obj;
        for (i=0;i<TableData_List.size();i++){
            tableData_Obj=(tableData) TableData_List.get(i);
            if (tableData_Obj.iStatus!=2){
                tableData_Obj.iStatus=0;
                tableData_Obj.iEntryID=0;
                tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_grey);
            }
        }
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query = "SELECT EntryID,ButtonNumber FROM PendingSales WHERE PostedInvoiceNo IS NULL";
        int iIndex;

        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next())
            {
                iIndex=rs.getInt("ButtonNumber")-1;
                tableData_Obj=(tableData) TableData_List.get(iIndex);
                if (tableData_Obj.iStatus!=2){
                    tableData_Obj.iStatus=1;
                    tableData_Obj.iEntryID=rs.getInt("EntryID");
                    //tableData_Obj.btnTable.setBackgroundColor(Color.YELLOW);
                    tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_yellow);
                }
            }
            rs.close();
            query="SELECT EntryID,TableNo,Pending_Table FROM VItemSalesList WHERE (CashRcvd=0 AND (TotalAmount+ServiceCharges+SalesTaxAmt-Discount)>0) AND SaleType NOT IN(2,3,4,5) AND ISNULL(TableNo,'')<>''";
            stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();
            while (rs.next()){
                iIndex=rs.getInt("TableNo")-1;
                tableData_Obj=(tableData) TableData_List.get(iIndex);
                tableData_Obj.iStatus=3;
                tableData_Obj.iEntryID=rs.getInt("EntryID");
                //tableData_Obj.btnTable.setBackgroundColor(Color.MAGENTA);
                tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_magenta);
            }
            rs.close();
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }
    }
    private void assignTables(@NonNull LinearLayout ll, int iStart){
        int i,iCount,iIndex;
        tableData tableData_Obj;
        iCount=ll.getChildCount()+iStart;
        for(i=iStart;i<iCount;i++){
            iIndex=i-iStart;
            View  child=ll.getChildAt(iIndex);
            if (child instanceof Button){
                Button button=(Button) child;
                tableData_Obj=new tableData();
                tableData_Obj.btnTable=button;
                tableData_Obj.iEntryID=0;
                tableData_Obj.iStatus=0;
                tableData_Obj.iTableIndex=i;
                tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_grey);
                TableData_List.add(i,tableData_Obj);
                //Message.message(context,"Please Help me, God !!!");
            }
        }

    }
    private void loadPendingSale(int iTableIndex,tableData tableData_Obj){
        if (data.size()>0){
            Message.message(context,"Please Settle Existing Sale.");
            return;
        }
        String strTableLocked_ComputerName = utility_functions.getSingleStringValue("MachineName","Tables_Locked", " WHERE TableNo='" + Integer.toString(iTableIndex) + "' AND MachineName<>'" + strDeviceName + "'",context);
        if (!strTableLocked_ComputerName.equals("")){
            Message.message(context,"Table is locked from "+strTableLocked_ComputerName);
            return;
        }
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        int iEntryID=tableData_Obj.iEntryID;
        String query = "SELECT DATEDIFF(MINUTE,DTEntry,getDATE()) AS TotalMinutes,* FROM PendingSales WHERE EntryID="+Integer.toString(iEntryID);
        int iIndex;
        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();

            if (!rs.next())
            {
                Message.message(context,"Table data has been changed, Please Refresh Tables");
            }
            else
            {
                lPendingSaleEntryID = iEntryID;

                txtServer.setText(rs.getString("Server"));
                txtTableNo.setTag(rs.getInt("SaleType"));
                txtTableNo.setText(rs.getString("TableNo"));
                txtServer.setTag(rs.getInt("InvoiceNo"));
                txtOrderFrom.setText(rs.getString("MachineName"));
                txtOrderDuration.setText(rs.getString("TotalMinutes")+" Mins.");
                iSalesTax= rs.getInt("STaxAmt");
                rs.close();
                //Lock the Table for others to See...
                String strQuery = "INSERT INTO Tables_Locked(TableNo,MachineName) VALUES(?,?)";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setString(1,Integer.toString(iTableIndex));
                stmt.setString(2,strDeviceName);
                stmt.addBatch();
                stmt.executeBatch();
                //MyCon.commit();
                //Lock the Table for others to See...

                query="SELECT * FROM PendingSalesDetail WHERE RefID="+Integer.toString(iEntryID)+" ORDER BY EntryID";
                stmt=MyCon.prepareStatement(query);
                rs=stmt.executeQuery();
                while (rs.next()){
                    datanum=new HashMap<String, String>();
                    datanum.put("ItemName",rs.getString("Column1"));
                    datanum.put("Qty",rs.getString("Column2"));
                    datanum.put("Rate",rs.getString("Column3"));

                    String strFMID=rs.getString("Column1Key");
                    strFMID=strFMID.substring(0,strFMID.indexOf("'"));

                    datanum.put("FM_EntryID",strFMID);
                    datanum.put("EntryID",Integer.toString(rs.getInt("EntryID")));
                    datanum.put("PreviousQty",rs.getString("Column2"));
                    datanum.put("TabQty",Integer.toString(rs.getInt("QtyTab")));
                    datanum.put("Column6Tag","Saved");
                    datanum.put("QtyPrinted",Integer.toString(rs.getInt("QtyPrinted")));
                    data.add(datanum);
                }
                myCursorAdapter.notifyDataSetChanged();

                tableData_Obj.iStatus=2;
                tableData_Obj.iEntryID=iEntryID;
                //tableData_Obj.btnTable.setBackgroundColor(Color.BLUE);
                tableData_Obj.btnTable.setBackgroundResource(R.drawable.btn_blue);
                //res.getColor(R.color.your_special_color));
            }

        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }

    public void reload(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshTables();
                handler.postDelayed(this, iMilliSeconds);
            }
        }, iMilliSeconds);
    }
    public void cmdPlus_click(View v)
    {
        //get the row the clicked button is in
        if (isSaleAvailable()==false)
            return;

        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        datanum=(Map<String, String>) data.get(position);
        int iFM_EntryID=Integer.parseInt(datanum.get("FM_EntryID"));

        boolean bNotAvailable=false;
        bNotAvailable = utility_functions.getSingleBooleanValue("Not_Available","FinalMaterials"," WHERE EntryID="+Integer.toString(iFM_EntryID),context);
        if (bNotAvailable)
        {
            Message.message(context,"Not Available");
            return;
        }
        int iQty=Integer.parseInt(datanum.get("Qty"));
        int dRate=Integer.parseInt(datanum.get("Rate"));
        iQty++;
        datanum.put("Qty",Integer.toString(iQty));
        datanum.put("Amount",Integer.toString(iQty*dRate));
        data.set(position,datanum);
        myCursorAdapter.notifyDataSetChanged();
        updateTotalAmount();
    }
    public void cmdMinus_click(View v)
    {
        //get the row the clicked button is in
        if (isSaleAvailable()==false)
            return;

        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        datanum=(Map<String, String>) data.get(position);
        int iEntryID=Integer.parseInt(datanum.get("EntryID"));
        int iQty=Integer.parseInt(datanum.get("Qty"));
        if (iEntryID!=0){
            //if it's already loaded sale, don't allow to remove
            int iPrevQty=Integer.parseInt(datanum.get("PreviousQty"));
            if (iQty<=iPrevQty)
                return;
        }

        iQty--;
        if (iQty==0){
            datanum.clear();
            data.remove(position);
        }
        else{
            int dRate=Integer.parseInt(datanum.get("Rate"));
            datanum.put("Qty",Integer.toString(iQty));
            datanum.put("Amount",Integer.toString(iQty*dRate));
            data.set(position,datanum);
        }

        myCursorAdapter.notifyDataSetChanged();
        updateTotalAmount();
    }
    private int getFontSize(String string,int iLen,int iTextWidth) {
        int iFontSize = 9;
        String pattern = "^[A-Za-z0-9. ]+$";

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int iScreenWidth = size.x;
        int iScreenHeight = size.y;
        int iScreenHeight_Idea = 752;
        int iScreenRatio = iScreenHeight - iScreenHeight_Idea;
        float ydpi=getResources().getDisplayMetrics().ydpi;
        float ydpi_ideal=196;
        String strFirst = string.substring(0, 1);
        if (strFirst.matches(pattern)){
            /*if (iLen>=33)
                iFontSize=8;
            else if (iLen>=30)
                iFontSize=9;
            else if (iLen>=26)
                iFontSize=10;
            else if (iLen>=22)
                iFontSize=12;
            else if (iLen>=18)
                iFontSize=13;
            else if (iLen>=14)
                iFontSize=14;
            else if (iLen>=10)
                iFontSize=18;
            else
                iFontSize=20;*/
            if (iTextWidth >= 150){
                iFontSize = 9;
                iScreenRatio=iScreenRatio/35;
            }
            else if (iTextWidth >= 140) {
                iFontSize = 11;
                iScreenRatio=iScreenRatio/28;
            }
            else if (iTextWidth >= 120){
                iFontSize = 12;
                iScreenRatio=iScreenRatio/30;
            }
            else if (iTextWidth >= 100){
                iFontSize = 12;
                iScreenRatio=iScreenRatio/32;
            }
            else if (iTextWidth <= 40){
                iFontSize = 17;
                iScreenRatio=iScreenRatio/30;
            }
            else{
                iFontSize = 14;
                iScreenRatio=iScreenRatio/30;
            }
        }
        else {
            /*if (iLen>=20)
                iFontSize=5;
            else if(iLen>=14)
                iFontSize=6;
            else if(iLen>=10)
                iFontSize=7;
            else
                iFontSize=8;*/
            if (iTextWidth >= 120){
                iFontSize = 13;
                iScreenRatio=iScreenRatio/25;
            }
            else if (iTextWidth >= 110) {
                iFontSize = 14;
                iScreenRatio=iScreenRatio/25;
            }
            else if (iTextWidth >= 100) {
                iFontSize = 15;
                iScreenRatio=iScreenRatio/25;
            }
            else if (iTextWidth >= 80) {
                iFontSize = 15;
                iScreenRatio=iScreenRatio/25;
            }
            else if (iTextWidth >= 60) {
                iFontSize = 15;
                iScreenRatio=iScreenRatio/25;
            }
            else{
                iFontSize = 16;
                iScreenRatio=iScreenRatio/20;
            }
        }

        if ((ydpi-ydpi_ideal)==0){
            iFontSize=iFontSize;
        }else{
            ydpi=ydpi-ydpi_ideal;
            ydpi=ydpi/30;
            //iFontSize=iFontSize+iScreenRatio+((int) ydpi);
            //iFontSize=iFontSize+((int) ydpi);
            //iFontSize=iFontSize+iScreenRatio+((int) ydpi);
        }
        iFontSize=iFontSize+5;
        return iFontSize;
    }
    private int getFontSize_MM(String string,int iLen){
        int iFontSize=9;
        String pattern = "^[A-Za-z0-9. ]+$";
        float fSD=getResources().getDisplayMetrics().density;
        if (string.matches(pattern))
            if (iLen>=20)
                iFontSize=6;
            else if (iLen>=14)
                iFontSize=8;
            else if (iLen>=10)
                iFontSize=10;
            else
                iFontSize=12;
        else
        if (iLen>=20)
            iFontSize=5;
        else if(iLen>=14)
            iFontSize=6;
        else if(iLen>=10)
            iFontSize=7;
        else
            iFontSize=8;
        return iFontSize;
    }
    private boolean isSaleAvailable()
    {
        int iPostedInvNo=utility_functions.getSingleIntValue("PostedInvoiceNo","PendingSales"," WHERE EntryID="+String.valueOf(lPendingSaleEntryID),context);
        if (iPostedInvNo>0)
        {
            Message.message(context,"Selected Sale is not available.");
            lPendingSaleEntryID = 0;
            data.clear();
            myCursorAdapter.notifyDataSetChanged();
            txtTableNo.setText("");
            txtServer.setText("");
            return false;
        }
        else
            return true;
    }
    private void updateTotalAmount()
    {
        String strHallName=cmbHall.getSelectedItem().toString();

        int iHallID=utility_functions.getSingleIntValue("EntryID","Hall_List"," WHERE Hall_Name='"+ strHallName+"'",context);
        String strHallID=String.valueOf(iHallID);

        boolean bSTaxApplicable=false,bServiceChargesApplicable=false;
        double dSales_Tax_Rate=0,dService_Charges_Rate=0,dSales_Tax=0,dService_Charges=0;
        bSTaxApplicable=utility_functions.getSingleBooleanValue("Sales_Tax_Applicable","Hall_List"," WHERE EntryID="+strHallID,context);
        bServiceChargesApplicable=utility_functions.getSingleBooleanValue("Service_Charges_Applicable","Hall_List"," WHERE EntryID="+strHallID,context);
        if (bSTaxApplicable){
            dSales_Tax_Rate=utility_functions.getSingleDoubleValue("Sales_Tax_Rate","Hall_List"," WHERE EntryID="+strHallID,context);
        }
        if (bServiceChargesApplicable){
            dService_Charges_Rate=utility_functions.getSingleDoubleValue("Service_Charges_Rate","Hall_List"," WHERE EntryID="+strHallID,context);
        }

        int i;
        double dAmt=0,dTotalAmt=0;
        for (i=0;i<data.size();i++)
        {
            //datanum=data[i];
            Map<String, String> datanum=(Map<String, String>) data.get(i);
            dAmt=dAmt+Integer.parseInt(datanum.get("Amount"));

            if (dSales_Tax_Rate>0)
                dSales_Tax=(dAmt/100)*dSales_Tax_Rate;

            if (dService_Charges_Rate>0)
                dService_Charges=(dAmt/100)*dService_Charges_Rate;

            dTotalAmt=dAmt+dSales_Tax+dService_Charges;
            txtTableNo.setText(Double.toString(dTotalAmt));
            txtTableNo.setTag(Double.toString(dAmt));
        }
    }
    private void saveQuotation()
    {


        String strHallName=cmbHall.getSelectedItem().toString();
        if (strHallName.equals("Select Hall"))
        {
            Message.message(context,"Please Select Hall");
            return;
        }

        int iHallID=utility_functions.getSingleIntValue("EntryID","Hall_List"," WHERE Hall_Name='"+ strHallName+"'",context);
        String strHallID=String.valueOf(iHallID);

        boolean bSTaxApplicable=false,bServiceChargesApplicable=false;
        double dSales_Tax_Rate=0,dService_Charges_Rate=0,dSales_Tax=0,dService_Charges=0,dAmt=0;
        bSTaxApplicable=utility_functions.getSingleBooleanValue("Sales_Tax_Applicable","Hall_List"," WHERE EntryID="+strHallID,context);
        bServiceChargesApplicable=utility_functions.getSingleBooleanValue("Service_Charges_Applicable","Hall_List"," WHERE EntryID="+strHallID,context);
        if (bSTaxApplicable){
            dSales_Tax_Rate=utility_functions.getSingleDoubleValue("Sales_Tax_Rate","Hall_List"," WHERE EntryID="+strHallID,context);
        }
        if (bServiceChargesApplicable){
            dService_Charges_Rate=utility_functions.getSingleDoubleValue("Service_Charges_Rate","Hall_List"," WHERE EntryID="+strHallID,context);
        }
        dAmt=Double.parseDouble(txtTableNo.getTag().toString());
        if (bSTaxApplicable)
            dSales_Tax=(dAmt/100)*dSales_Tax_Rate;
        if (bServiceChargesApplicable)
            dService_Charges=(dAmt/100)*dService_Charges_Rate;

        /*Calendar c=Calendar.getInstance();
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-dd-MM");*/

        String strDate=txtOrderDuration.getTag().toString();
        Date DT=Date.valueOf(strDate.substring(0,10));
        Timestamp DTWithTime=Timestamp.valueOf(strDate);

        /*strDate=strDate.substring(6,4)+"-"+strDate.substring(0,2)+"-"+;
        Date DT=Date.valueOf(strDate.substring(0,11));
        Date DTWithTime=Date.valueOf(strDate);*/

        //Date DT=new java.sql.Date(sdf.parse(txtOrderDuration.toString(),pos));

        int i,iFM_EntryID,iRate,iQty;
        int iQuot_EntryID;
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String strQuery="";
        PreparedStatement stmt;
        try {
            MyCon.setAutoCommit(false);
            strQuery = "INSERT INTO Quotations(Total_Guests,Order_From,Order_Duration,Sales_Tax_Rate,Service_Charges_Rate,Sales_Tax,Service_Charges,DT,DTWithTime,HL_RefID) VALUES(?,?,?,?,?,?,?,?,?,?)";
            stmt = MyCon.prepareStatement(strQuery);
            stmt.setString(1, txtServer.getText().toString());
            stmt.setString(2, txtOrderFrom.getText().toString());
            stmt.setString(3,txtOrderDuration.getText().toString());

            stmt.setDouble(4,dSales_Tax_Rate);
            stmt.setDouble(5,dService_Charges_Rate);
            stmt.setDouble(6,dSales_Tax);
            stmt.setDouble(7,dService_Charges);

            stmt.setDate(8,DT);
            stmt.setObject(9,DTWithTime);
            stmt.setInt(10,iHallID);

            stmt.addBatch();
            stmt.executeBatch();

            iQuot_EntryID = utility_functions.getSingleIntValue(MyCon, "MAX(EntryID)", "Quotations", "", context);

            for (i=0;i<data.size();i++)
            {
                Map<String, String> datanum=(Map<String, String>) data.get(i);
                iFM_EntryID=Integer.parseInt(datanum.get("FM_EntryID"));
                iRate=Integer.parseInt(datanum.get("Rate"));
                iQty=Integer.parseInt(datanum.get("Qty"));

                strQuery = "INSERT INTO Quotations_Detail(RefID,FM_RefID,Rate,Qty) VALUES(?,?,?,?)";
                stmt = MyCon.prepareStatement(strQuery);

                stmt.setInt(1, iQuot_EntryID);
                stmt.setInt(2, iFM_EntryID);
                stmt.setInt(3, iRate);
                stmt.setInt(4, iQty);

                stmt.addBatch();
                stmt.executeBatch();
            }
            MyCon.commit();
            data.clear();

            myCursorAdapter.notifyDataSetChanged();
            txtTableNo.setText("");
            txtServer.setText("");
            txtOrderFrom.setText("");
            txtOrderDuration.setText("");
        }
        catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
        }


    }
    private void fillHalls(){

        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT EntryID,Hall_Name FROM Hall_List ORDER BY EntryID";
        int iIndex;
        List<String> Halls = new ArrayList<String>();
        Halls.add("Select Hall");
        try {
            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next()){
                Halls.add(rs.getString("Hall_Name"));
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Halls);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cmbHall.setAdapter(dataAdapter);
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }
}
