package com.example.asim.amr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.ContextThemeWrapper;
import android.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class news_bulletin extends AppCompatActivity {

    SimpleCursorAdapter myCursorAdapter;
    GridView myGV;
    TextView txtIssNo,txtRemarks;
    String strIssNo;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();

        setContentView(R.layout.news_bulletin);
        context=this.getBaseContext();
        populateGridViewFromDB();

        myGV=(GridView) findViewById(R.id.GV);
        txtIssNo=(TextView) findViewById(R.id.txtNB_IssNo);
        txtRemarks=(TextView) findViewById(R.id.txtNB_Remarks);


        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("News");

        final Button btnBack=(Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        myGV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                long viewId=view.getId();
                if (viewId==R.id.txtNB_IssNo || viewId==R.id.txtNB_Remarks) {
                    TextView myTV=(TextView)  findViewById(R.id.txtNB_IssNo);
                    Map<String,String> data =(HashMap<String,String>)myGV.getItemAtPosition(i);
                    strIssNo=data.get("IssNo");
                    Intent myIntent = new Intent(getApplicationContext(), news_bulletin_detail.class);
                    myIntent.putExtra("IssNo",strIssNo);
                    startActivity(myIntent);
                } else if (viewId==R.id.ImgNB){
                    Map<String,String> data =(HashMap<String,String>)myGV.getItemAtPosition(i);
                    strIssNo=data.get("IssNo");
                    Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenu);
                    PopupMenu popup = new PopupMenu(wrapper, view);
                    //PopupMenu popup = new PopupMenu(context, view);

                    /** Adding menu items to the popumenu */
                    popup.getMenuInflater().inflate(R.menu.menu_nb, popup.getMenu());

                    /** Defining menu item click listener for the popup menu */
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId()==R.id.mnuAddRemark){

                                Intent myIntent = new Intent(getApplicationContext(), add_remarks.class);
                                myIntent.putExtra("IssNo",strIssNo);
                                startActivity(myIntent);

                            } else if (item.getItemId()==R.id.mnuDailyTasks){

                                Intent myIntent = new Intent(getApplicationContext(), daily_tasks.class);
                                myIntent.putExtra("IssNo",strIssNo);
                                startActivity(myIntent);
                            } else if (item.getItemId()==R.id.mnuCheckOnce){

                                Intent myIntent = new Intent(getApplicationContext(), check_once.class);
                                myIntent.putExtra("IssNo",strIssNo);
                                startActivity(myIntent);
                            } else if (item.getItemId()==R.id.mnuArchive) {
                                updateArchiveStatus();
                            } else if (item.getItemId()==R.id.mnuMarkPriority) {
                                updatePriorityStatus();
                            } else if (item.getItemId()==R.id.mnuTasks) {
                                Intent myIntent = new Intent(getApplicationContext(), tasks.class);
                                myIntent.putExtra("IssNo",strIssNo);
                                startActivity(myIntent);
                            }else if (item.getItemId()==R.id.mnuViewSlip) {
                                Intent myIntent = new Intent(getApplicationContext(), view_slip.class);
                                myIntent.putExtra("IssNo",strIssNo);
                                startActivity(myIntent);
                            }
                            return true;
                        }
                    });

                    /** Showing the popup menu */
                    popup.show();

                    }
                    return false;
            }
        });
    }

    private void populateGridViewFromDB()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        //String query = "SELECT ProcessEntryID,T1.*,ISNULL(WOIWD_Details.Priority,0) AS Priority FROM workorderItemWorkDone LEFT JOIN (SELECT * FROM WOIWD_News_Bulletin WHERE EntryID IN(SELECT MAX(EntryID) FROM WOIWD_News_Bulletin GROUP BY WOIWD_RefID)) T1 ON workorderItemWorkDone.EntryID=T1.WOIWD_RefID LEFT JOIN WOIWD_Details ON workorderItemWorkDone.EntryID=WOIWD_Details.WOIWD_RefID WHERE EntryType=0 AND ISNULL(Archive,0)=0";
        String query = "SELECT * FROM VWOIWD_News_Bulletin";

        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            Map<String, String> datanum = new HashMap<String, String>();
            String strRemarks,strNewsColor,strEntryID,strPriority,strForeColor;
            String strPackedProcessEntryID;
            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("IssNo",rs.getString("ProcessEntryID"));
                strRemarks=rs.getString("Remarks");
                if (strRemarks==null){
                    strRemarks="";
                }
                strForeColor="16777215";
                strPackedProcessEntryID=rs.getString("PackedProcessEntryID");
                if (strPackedProcessEntryID.equals("")==false){
                    strNewsColor="15108356";
                    strForeColor="16722432";
                } else if (rs.getString("RcvProcessEntryID").equals("")==false){
                    strNewsColor=rs.getString("NewsColor");
                    strForeColor="65520";
                } else if (rs.getInt("AlterationReceived")!=0){
                    strNewsColor="65280";
                } else if (rs.getInt("Alteration")!=0){
                    strNewsColor="255";
                } else {
                    strNewsColor="15108356";
                }

                strNewsColor=rs.getString("NewsColor");
                if (strNewsColor==null){
                    strNewsColor="15108356";
                }
                strEntryID=rs.getString("EntryID");
                if (strEntryID==null){
                    strEntryID="";
                }
                strPriority=rs.getString("Priority");
                if (strPriority==null){
                    strPriority="0";
                }
                datanum.put("Remarks",strRemarks);
                datanum.put("Color",strNewsColor);
                datanum.put("EntryID",strEntryID);
                datanum.put("Priority",strPriority);
                datanum.put("ForeColor",strForeColor);
                data.add(datanum);
            }
            String[] fromwhere = { "IssNo","Remarks"};

            int[] toViewIDs = new int[] {R.id.txtNB_IssNo,R.id.txtNB_Remarks};

            SimpleAdapter_NB NoCoreAdapter=new SimpleAdapter_NB(this,data,R.layout.news_bulletin_layout, fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.news_bulletin_layout);

            GridView myGV;
            myGV=(GridView) findViewById(R.id.GV);

            myGV.setAdapter(NoCoreAdapter);
        } catch (Exception e) {Message.message(this.getBaseContext(),""+e);}

    }

    private void updateArchiveStatus(){
        //updatePriorityStatus
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        int iWOIWD_RefID;
        iWOIWD_RefID = getWOIWD_EntryID();
        try {
            String strQuery = "UPDATE WOIWD_Details SET Archive=1 WHERE WOIWD_RefID=?";
            PreparedStatement stmt = MyCon.prepareStatement(strQuery);

            int iRecAffect;

            stmt.setInt(1, iWOIWD_RefID);

            iRecAffect=stmt.executeUpdate();

            if (iRecAffect==0){
                strQuery = "INSERT INTO WOIWD_Details(WOIWD_RefID,Archive) VALUES(?,1)";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setInt(1, iWOIWD_RefID);
                iRecAffect=stmt.executeUpdate();
            }
                    /*stmt.addBatch();
                    stmt.executeBatch();*/
            Message.message(getApplicationContext(),"Successfuly Updated.");
            populateGridViewFromDB();

        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
        }
    }

    private void updatePriorityStatus(){

        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        int iWOIWD_RefID;
        iWOIWD_RefID = getWOIWD_EntryID();
        try {
            String strQuery = "UPDATE WOIWD_Details SET Priority=1 WHERE WOIWD_RefID=?";
            PreparedStatement stmt = MyCon.prepareStatement(strQuery);

            int iRecAffect;

            stmt.setInt(1, iWOIWD_RefID);

            iRecAffect=stmt.executeUpdate();

            if (iRecAffect==0){
                strQuery = "INSERT INTO WOIWD_Details(WOIWD_RefID,Priority) VALUES(?,1)";
                stmt = MyCon.prepareStatement(strQuery);
                stmt.setInt(1, iWOIWD_RefID);
                iRecAffect=stmt.executeUpdate();
            }
                    /*stmt.addBatch();
                    stmt.executeBatch();*/
            Message.message(getApplicationContext(),"Successfuly Updated.");
            populateGridViewFromDB();

        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
        }
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
