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

public class issuance_list extends AppCompatActivity {

    SimpleCursorAdapter myCursorAdapter;
    ListView myLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();


        setContentView(R.layout.issuance_list);
        final Context context;
        context=this.getBaseContext();
        populateListViewFromDB();

        myLV=(ListView) findViewById(R.id.LV_Iss_List);

        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);

        txtActivityTitle.setText("Issuance List");


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

        String query = "SELECT TOP 5000 VenderName,MasterPONo,DT,Description,ItemID,TotalIssQty,0 AS Wastage,LotNo,OrderNo FROM VVendIssued ORDER BY EntryID DESC";

        try {

            PreparedStatement stmt=MyCon.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();

            Map<String, String> datanum = new HashMap<String, String>();

            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("VendorName",rs.getString("VenderName"));
                datanum.put("MasterPONo",rs.getString("MasterPONo"));
                datanum.put("DT",rs.getDate("DT").toString());
                datanum.put("Process",rs.getString("Description"));
                datanum.put("ItemCode",rs.getString("ItemID"));
                datanum.put("Qty",rs.getString("TotalIssQty"));
                datanum.put("Wastage",rs.getString("Wastage"));
                datanum.put("LotNo",rs.getString("LotNo"));
                datanum.put("OrderNo",rs.getString("OrderNo"));
                data.add(datanum);
            }
            String[] fromwhere = { "VendorName","MasterPONo","DT", "Process","ItemCode","Qty","Wasstage","LotNo","OrderNo"};

            int[] toViewIDs = new int[] {R.id.txtIss_List_Maker,R.id.txtIss_List_PONo,R.id.txtIss_List_Date,R.id.txtIss_List_Process,R.id.txtIss_List_ItemID,R.id.txtIss_List_RcvdQty,R.id.txtIss_List_RejQty,R.id.txtIss_List_LotNo,R.id.txtIss_List_OrderNo};

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.issuance_list_layout, fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.issuance_list_layout);

            ListView myLV;
            myLV=(ListView) findViewById(R.id.LV_Iss_List);

            myLV.setAdapter(NoCoreAdapter);
        } catch (Exception e) {
            Message.message(this.getBaseContext(),""+e);
        }

    }
}
