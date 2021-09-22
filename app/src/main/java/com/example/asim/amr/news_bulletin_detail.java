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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class news_bulletin_detail extends AppCompatActivity {

    SimpleCursorAdapter myCursorAdapter;
    ListView LVD;
    String strIssNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        strIssNo = b.getString("IssNo");

        setContentView(R.layout.news_bulletin_detail);
        final Context context;
        context=this.getBaseContext();
        populateListViewFromDB();

        LVD=(ListView) findViewById(R.id.LV_NBD);

        LVD.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String strToShow;

                HashMap<String,String> data =(HashMap<String,String>)LVD.getItemAtPosition(i);

                //((TextView)view.findViewById(R.id.txtNBD_Remarks)).setText(strDate);

                TextView myTxt=(TextView)view.findViewById(R.id.txtNBD_Remarks);
                if (myTxt.getTag()=="Date"){
                    strToShow=data.get("Remarks");
                    myTxt.setTag("Remarks");
                }
                else{
                    strToShow=data.get("DTEntry");
                    myTxt.setTag("Date");
                }

                myTxt.setText(strToShow);
                //Message.message(context,strDate);
                return false;
            }
        });

        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText(strIssNo);

        final Button btnBack=(Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void populateListViewFromDB()
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT ProcessEntryID,T1.* FROM workorderItemWorkDone INNER JOIN WOIWD_News_Bulletin T1 ON workorderItemWorkDone.EntryID=T1.WOIWD_RefID WHERE workorderItemWorkDone.ProcessEntryID='"+strIssNo+"' ORDER BY T1.EntryID";

        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            Map<String, String> datanum = new HashMap<String, String>();
            String strDTEntry="";
            /*Date myDT;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm");*/
            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("Remarks",rs.getString("Remarks").toString());
                strDTEntry=rs.getString("DTEntry").toString();
                strDTEntry=strDTEntry.substring(0,16);
                datanum.put("DTEntry",strDTEntry);
                datanum.put("Color",rs.getString("NewsColor").toString());
                data.add(datanum);
            }
            String[] fromwhere = { "Remarks","DTEntry"};

            int[] toViewIDs = new int[] {R.id.txtNBD_Remarks,R.id.txtNBD_Date};

            SimpleAdapter_NBD NoCoreAdapter=new SimpleAdapter_NBD(this,data,R.layout.news_bulletin_detail_layout, fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.news_bulletin_detail_layout);

            ListView LVD;
            LVD=(ListView) findViewById(R.id.LV_NBD);

            LVD.setAdapter(NoCoreAdapter);
        } catch (Exception e) {Message.message(this.getBaseContext(),""+e);}

    }
}
