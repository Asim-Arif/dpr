package com.example.asim.amr;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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


public class tasks extends AppCompatActivity {

    SimpleCursorAdapter myCursorAdapter;
    ListView myLV;
    //TextView txtIssNo,txtRemarks;
    String strIssNo;
    Context context;
    int[] RadioButtonValues;
    String[] strRemarks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        strIssNo = b.getString("IssNo");
        setContentView(R.layout.tasks);
        context=this.getBaseContext();
        populateListViewFromDB();

        myLV=(ListView) findViewById(R.id.LV);

        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("Random");

        final Button btnBack=(Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        myLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long viewId=view.getId();
                Map<String,String> data =(HashMap<String,String>)myLV.getItemAtPosition(i);
                if (viewId==R.id.RD_Yes) {
                    RadioButtonValues[i]=1;
                }else if (viewId==R.id.RD_No) {
                    RadioButtonValues[i]=0;
                }else if (viewId==R.id.cmdPost){
                    //TextView txtRemarks=(TextView) view.findViewById(R.id.txtTasks_Remarks);
                    String strEntryID=data.get("EntryID");
                    int iEntryID;
                    iEntryID=Integer.parseInt(strEntryID);
                    updateNewsBulletin(strRemarks[i],iEntryID,i);
                }

            }
        });

    }

    private void populateListViewFromDB()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        //String query = "SELECT ProcessEntryID,T1.*,ISNULL(WOIWD_Details.Priority,0) AS Priority FROM workorderItemWorkDone LEFT JOIN (SELECT * FROM WOIWD_News_Bulletin WHERE EntryID IN(SELECT MAX(EntryID) FROM WOIWD_News_Bulletin GROUP BY WOIWD_RefID)) T1 ON workorderItemWorkDone.EntryID=T1.WOIWD_RefID LEFT JOIN WOIWD_Details ON workorderItemWorkDone.EntryID=WOIWD_Details.WOIWD_RefID WHERE EntryType=0 AND ISNULL(Archive,0)=0";
        String query="";
        int iRecCount=0;
        int iWOIWD_RefID=getWOIWD_EntryID();
        if (firstTaskDone(iWOIWD_RefID)==1){
            query = "SELECT News_Bulletin_Tasks_Random.*,ISNULL(T1.YesNo,-1) AS YesNo,T1.Remarks FROM News_Bulletin_Tasks_Random LEFT JOIN (SELECT * FROM News_Bulletin_Tasks_entry WHERE WOIWD_RefID="+Integer.toString(iWOIWD_RefID)+") T1 ON News_Bulletin_Tasks_Random.EntryID=T1.NBT_RefID ORDER BY Task_SortNo";
            iRecCount=6;
        }else {
            query = "SELECT News_Bulletin_Tasks_Random.*,ISNULL(T1.YesNo,-1) AS YesNo,T1.Remarks FROM News_Bulletin_Tasks_Random LEFT JOIN (SELECT * FROM News_Bulletin_Tasks_entry WHERE WOIWD_RefID="+Integer.toString(iWOIWD_RefID)+") T1 ON News_Bulletin_Tasks_Random.EntryID=T1.NBT_RefID WHERE News_Bulletin_Tasks_Random.EntryID=1";
            iRecCount=1;
        }

        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            Map<String, String> datanum = new HashMap<String, String>();
            String strTask_Heading;
            String strPackedProcessEntryID;

            RadioButtonValues=new int[iRecCount];
            strRemarks=new String[iRecCount];
            int iIndex=0;
            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("Description",rs.getString("Task_Description"));
                datanum.put("TaskColor",rs.getString("Task_Color"));
                datanum.put("Remarks","");
                datanum.put("EntryID",rs.getString("EntryID"));

                data.add(datanum);
                RadioButtonValues[iIndex]=rs.getInt("YesNo");
                strRemarks[iIndex]=rs.getString("Remarks");
                iIndex=iIndex+1;
            }

            String[] fromwhere = { "Description"};

            int[] toViewIDs = new int[] {R.id.txtTasks_Description};

            SimpleAdapter_Tasks NoCoreAdapter=new SimpleAdapter_Tasks(this,data,R.layout.tasks_layout, fromwhere,toViewIDs,RadioButtonValues,strRemarks);

            NoCoreAdapter.setDropDownViewResource(R.layout.tasks_layout);

            ListView myLV;
            myLV=(ListView) findViewById(R.id.LV);

            myLV.setAdapter(NoCoreAdapter);
        } catch (Exception e) {Message.message(this.getBaseContext(),""+e);}

    }
    private void updateNewsBulletin(String strRemarks,int iTaskEntryID,int iPosition){
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        int iWOIWD_RefID;
        iWOIWD_RefID = getWOIWD_EntryID();
        try {

            int iColor=255;
            if (iTaskEntryID==1){
                if (RadioButtonValues[iPosition]==1){
                    iColor=3244039;
                    strRemarks="Working ("+strRemarks+")";
                }else if (RadioButtonValues[iPosition]==0){
                    iColor=4210816;
                    strRemarks="Not Working ("+strRemarks+")";
                }
            }else if (iTaskEntryID==2) {
                if (RadioButtonValues[iPosition] == 1) {
                    iColor = 2825726;
                    strRemarks = "Material Issue (" + strRemarks + ")";
                } else if (RadioButtonValues[iPosition] == 0) {
                    iColor = 4210816;
                    strRemarks = "Stop";
                }
            }else if (iTaskEntryID==3) {
                if (RadioButtonValues[iPosition] == 1) {
                    iColor = 2825726;
                    strRemarks = "Suspect (" + strRemarks + ")";
                } else if (RadioButtonValues[iPosition] == 0) {
                    iColor = 4210816;
                    strRemarks = "Stop";
                }
            }else if (iTaskEntryID==4) {
                if (RadioButtonValues[iPosition] == 1) {
                    iColor = 4496911;
                    strRemarks = "Boss (" + strRemarks + ")";
                } else if (RadioButtonValues[iPosition] == 0) {
                    iColor = 4210816;
                    strRemarks = "Stop";
                }
            }else if (iTaskEntryID==5) {
                if (RadioButtonValues[iPosition] == 1) {
                    iColor = 4496911;
                    strRemarks = "Stop";
                } else if (RadioButtonValues[iPosition] == 0) {
                    iColor = 4210816;
                    strRemarks = "NoQC11";
                }
            }else if (iTaskEntryID==6) {
                if (RadioButtonValues[iPosition] == 1) {
                    iColor = 13019387;
                    strRemarks = "Completed";
                } else if (RadioButtonValues[iPosition] == 0) {
                    iColor = 4210816;
                    strRemarks = "Stop";
                }
            }
            String strQuery="";
            PreparedStatement stmt;
            if (strRemarks.equals("Stop")==false) {
                strQuery = "INSERT INTO WOIWD_News_Bulletin(WOIWD_RefID,Remarks,UserName,MachineName,NewsColor,TaskEntryID) VALUES(?,?,?,?,?,?)";
                stmt= MyCon.prepareStatement(strQuery);
                stmt.setInt(1, iWOIWD_RefID);

                stmt.setString(2, strRemarks);
                stmt.setString(3, "Android");
                stmt.setString(4, "Android");

                stmt.setInt(5, iColor);
                stmt.setInt(6, iTaskEntryID);
                stmt.addBatch();
                stmt.executeBatch();
            }
            strQuery = "INSERT INTO News_Bulletin_Tasks_Entry(WOIWD_RefID,NBT_RefID,YesNo,Remarks) VALUES(?,?,?,?)";
            stmt = MyCon.prepareStatement(strQuery);
            stmt.setInt(1,iWOIWD_RefID);
            stmt.setInt(2,iTaskEntryID);
            stmt.setInt(3,RadioButtonValues[iPosition]);
            stmt.setString(4,strRemarks);
            stmt.addBatch();
            stmt.executeBatch();

            Message.message(getApplicationContext(),"Successfuly Saved.");
            //onBackPressed();

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
            return iEntryID;
        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
            return 0;
        }
    }
    public int firstTaskDone(int iWOIWD_RefID) {
        DBHelper myDBH = new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT YesNo FROM News_Bulletin_Tasks_Entry WHERE WOIWD_RefID=" +  Integer.toString(iWOIWD_RefID) ;
        int iYesNo=-1;
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                iYesNo=rs.getInt("YesNo");
            }
            return iYesNo;
        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
            return -1;
        }
    }
}
