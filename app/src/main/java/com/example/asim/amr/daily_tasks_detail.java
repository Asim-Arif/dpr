package com.example.asim.amr;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class daily_tasks_detail extends AppCompatActivity {

    SimpleCursorAdapter myCursorAdapter;
    ListView LVD;
    String strIssNo,strDailyTask_Type,strIndex;
    Context context;
    Button cmdBack,cmdFinish;
    Boolean[] ChkBoxValues=new Boolean[30]; //0 Based for 31
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        strIssNo = b.getString("IssNo");
        strDailyTask_Type= b.getString("DailyTask_Type");
        strIndex= b.getString("Index");
        setContentView(R.layout.daily_tasks_detail);


        context=this.getBaseContext();
        populateListView();

        LVD=(ListView) findViewById(R.id.LV_DTD);

        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText(strDailyTask_Type);

        final Button btnBack=(Button) findViewById(R.id.cmdBack);

        LVD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox chk=(CheckBox) view.findViewById(R.id.chkDTD);
                Map<String,String> data =(HashMap<String,String>)LVD.getItemAtPosition(position);
                data.put("CheckBox",Boolean.toString(chk.isChecked()));
                //Message.message(context,"Position:"+Integer.toString(position) + " = " + Boolean.toString(chk.isChecked()));
                ChkBoxValues[position]=chk.isChecked();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cmdBack=(Button) findViewById(R.id.cmdBack_DTD);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cmdFinish=(Button) findViewById(R.id.cmdFinish_DTD);
        cmdFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper myDBH = new DBHelper();
                Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
                int iWOIWD_RefID;
                iWOIWD_RefID = getWOIWD_EntryID();
                HashMap<String,String> data;
                CheckBox CB;
                try {
                    String strQuery = "UPDATE WOIWD_Details SET ";

                    for (int i = 0; i < LVD.getCount(); i++) {
                        strQuery=strQuery+"DailyTasks"+strIndex+"_"+Integer.toString(i)+"=?,";
                    }
                    strQuery=strQuery.substring(0,strQuery.length()-1);
                    strQuery=strQuery+" WHERE WOIWD_RefID=?";
                    PreparedStatement stmt = MyCon.prepareStatement(strQuery);
                    for (int i=0;i<LVD.getCount();i++){
                        stmt.setBoolean(i+1,ChkBoxValues[i]);
                    }
                    stmt.setInt(LVD.getCount()+1,iWOIWD_RefID);
                    int iRecAffect=0;

                    iRecAffect=stmt.executeUpdate();

                    if (iRecAffect==0){
                        strQuery = "INSERT INTO WOIWD_Details(";
                        for (int i = 0; i < LVD.getCount(); i++) {
                            strQuery=strQuery+"DailyTasks"+strIndex+"_"+Integer.toString(i)+",";
                        }
                        strQuery=strQuery+"WOIWD_RefID) VALUES(";
                        for (int i = 0; i < LVD.getCount(); i++) {
                            strQuery=strQuery+"?,";
                        }
                        strQuery=strQuery+"?)";
                        stmt = MyCon.prepareStatement(strQuery);

                        for (int i=0;i<LVD.getCount();i++){
                            stmt.setBoolean(i+1,ChkBoxValues[i]);
                        }
                        stmt.setInt(LVD.getCount()+1,iWOIWD_RefID);
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

    }

    private void populateListView()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM");
        java.util.Date myDTFrom=calendar.getTime();

        String strYear,strMonth;
        strMonth=dateFormat.format(myDTFrom).toString();
        dateFormat=new SimpleDateFormat("yyyy");
        int iWOIWD_RefID=getWOIWD_EntryID();
        strYear=dateFormat.format(myDTFrom).toString();
        //String query = "SELECT DateFull,WOIWD_Details.* FROM DateLookup CROSS JOIN WOIWD_Details WHERE MonthName='"+strMonth+"' AND FullYear='"+strYear+"' AND WOIWD_RefID="+Integer.toString(iWOIWD_RefID)+" ORDER BY DateFull";
        String query = "SELECT DateFull,T1.* FROM DateLookup LEFT JOIN (SELECT WOIWD_Details.* FROM WOIWD_Details WHERE WOIWD_RefID="+Integer.toString(iWOIWD_RefID)+") T1 ON T1.WOIWD_RefID="+Integer.toString(iWOIWD_RefID)+" WHERE MonthName='"+strMonth+"' AND FullYear='"+strYear+"' ORDER BY DateFull";
        PreparedStatement stmt;
        ResultSet rs;

        try {

            stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            Map<String, String> datanum = new HashMap<String, String>();
            String strDateFull="";
            Date myDT;
            dateFormat = new SimpleDateFormat("EEEE-dd");
            SimpleDateFormat dateFormat_Full=new SimpleDateFormat("MMM/dd/yyyy");
            int i=0;
            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                myDT=rs.getDate("DateFull");

                datanum.put("DateToShow",dateFormat.format(myDT).toString());

                datanum.put("DateToSave",dateFormat_Full.format(myDT).toString());
                datanum.put("CheckBox",rs.getString("DailyTasks"+strIndex+"_"+Integer.toString(i)));
                ChkBoxValues[i]=rs.getBoolean("DailyTasks"+strIndex+"_"+Integer.toString(i));
                data.add(datanum);
                i=i+1;
            }
            String[] fromwhere = { "DateToShow"};

            int[] toViewIDs = new int[] {R.id.txtDTD};

            SimpleAdapter_DTD NoCoreAdapter=new SimpleAdapter_DTD(this,data,R.layout.daily_tasks_detail_layout, fromwhere,toViewIDs,ChkBoxValues);
            NoCoreAdapter.setDropDownViewResource(R.layout.daily_tasks_detail_layout);

            LVD=(ListView) findViewById(R.id.LV_DTD);

            LVD.setAdapter(NoCoreAdapter);


        } catch (Exception e) {Message.message(this.getBaseContext(),""+e);}

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
}
