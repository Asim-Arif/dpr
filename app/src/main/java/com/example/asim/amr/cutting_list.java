package com.example.asim.amr;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cutting_list extends AppCompatActivity {

    SimpleCursorAdapter myCursorAdapter;
    ListView myLV;
    int iProcessID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();

        iProcessID=b.getInt("ProcessID");

        setContentView(R.layout.cutting_list);
        final Context context;
        context=this.getBaseContext();
        populateListViewFromDB();

        myLV=(ListView) findViewById(R.id.LV);

        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);
        if (iProcessID==2)
            txtActivityTitle.setText("Cutting List");
        else if (iProcessID==6)
            txtActivityTitle.setText("Stitching List");


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

        String query = "SELECT T1.ProcessEntryID,T1.DT,T2.Name from WorkOrderItemWorkDone T1 INNER JOIN Employees T2 ON T1.ContID=T2.EmpID WHERE T1.ProcessID="+Integer.toString(iProcessID)+" AND EntryType=0 ORDER BY EntryID DESC";

        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            Map<String, String> datanum = new HashMap<String, String>();

            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("Name",rs.getString("Name"));
                datanum.put("DT",rs.getDate("DT").toString());
                datanum.put("ProcessEntryID",rs.getString("ProcessEntryID"));
                data.add(datanum);
            }
            String[] fromwhere = { "Name","DT", "ProcessEntryID"};

            String[] fromFieldNames=new String[] {"Name","DT","ProcessEntryID"};
            int[] toViewIDs = new int[] {R.id.txtEmpName,R.id.txtDateTime,R.id.txtIssuance};

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.cutting_list_layout, fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.cutting_list_layout);

            ListView myLV;
            myLV=(ListView) findViewById(R.id.LV);

            myLV.setAdapter(NoCoreAdapter);
        } catch (Exception e) {Message.message(this.getBaseContext(),""+e);}

    }
}
