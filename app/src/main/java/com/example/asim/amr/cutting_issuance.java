package com.example.asim.amr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class cutting_issuance extends AppCompatActivity {

    TextView txtActivityTitle;
    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner cmbOrder;
    //AutoCompleteTextView cmbEmployee;
    Spinner cmbEmployee;
    Spinner cmbArticle;
    Spinner cmbOperation;
    static Button cmdReturnDate;
    SimpleCursorAdapter myCursorAdapter;
    Button cmdNext,cmdBack;
    int iProcessID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        iProcessID = b.getInt("ProcessID");

        setContentView(R.layout.cutting_issuance);
        final Context context;
        context = this.getBaseContext();

        txtActivityTitle = (TextView) findViewById(R.id.txtAtivityTitle);

        if (iProcessID==2)
            txtActivityTitle.setText("Cutting Issuance");
        else
            txtActivityTitle.setText("Stitching Issuance");

        cmbOperation=(Spinner) findViewById(R.id.cmbci_Opeartion);
        fillOperations("");
        cmbArticle=(Spinner) findViewById(R.id.cmbci_Article);
        fillArticles("");
        cmdBack=(Button) findViewById(R.id.cmdci_Back);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cmdNext=(Button) findViewById(R.id.cmdci_Next);
        cmdNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cmbOrder.getSelectedItemPosition()==0)
                {
                    Message.message(getApplicationContext(),"Please Select Order No.");
                    return;
                }
                if (cmbEmployee.getSelectedItemPosition()==0)
                {
                    Message.message(getApplicationContext(),"Please Select Employee");
                    return;
                }
                if (cmbArticle.getSelectedItemPosition()==0)
                {
                    Message.message(getApplicationContext(),"Please Select Article");
                    return;
                }
                if (cmbOperation.getSelectedItemPosition()==0)
                {
                    Message.message(getApplicationContext(),"Please Select Operation");
                    return;
                }
                /*if (cmdReturnDate.getText().toString()=="Return Date")
                {
                    Message.message(getApplicationContext(),"Please Select Return Date");
                    return;
                }
                Message.message(getApplicationContext(),cmdReturnDate.getText().toString());*/
                String strOrderNo=cmbOrder.getSelectedItem().toString();

                HashMap<String,String> data =(HashMap<String,String>)cmbArticle.getSelectedItem();
                String strItemID=data.get("ItemID");

                data =(HashMap<String,String>)cmbEmployee.getSelectedItem();
                String strEmpID=data.get("EmpID");

                data =(HashMap<String,String>)cmbOperation.getSelectedItem();
                String strOperationID=data.get("OperationID");

                String strmyDT=cmdReturnDate.getText().toString();

                Intent i = new Intent(getApplicationContext(), cutting_issuance_sizes.class);
                i.putExtra("OrderNo",strOrderNo);
                i.putExtra("ItemID",strItemID);
                i.putExtra("EmpID",strEmpID);
                i.putExtra("OperationID",strOperationID);
                i.putExtra("DT",strmyDT);
                i.putExtra("ProcessID",iProcessID);
                startActivity(i);
            }
        });


        cmbArticle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strItemID;
                HashMap<String,String> data =(HashMap<String,String>)cmbArticle.getSelectedItem();

                strItemID=data.get("ItemID");
                fillOperations(strItemID);
                //myItem=cmbArticle.getSelectedItem().toString();
                //Message.message(getApplicationContext(),"OrderNo:"+strOrderNo+" Item:"+strItemID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        cmdReturnDate=(Button)findViewById(R.id.cmdci_ReturnDate);
        final java.util.Calendar c=java.util.Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String myDT;
        myDT=Integer.toString(month)+'/'+Integer.toString(day)+'/'+Integer.toString(year);
        cmdReturnDate.setText(myDT);

        cmbOrder = (Spinner)findViewById(R.id.cmbci_Order);
        cmbOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String strOrderNo;
                strOrderNo=cmbOrder.getSelectedItem().toString();
                fillArticles(strOrderNo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //cmbEmployee = (AutoCompleteTextView)findViewById(R.id.cmbci_Employee);
        cmbEmployee = (Spinner) findViewById(R.id.cmbci_Employee);
        cmdReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"");

                //newFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });


        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "select OrderNo from FCustomerOrders WHERE OrderShipped=0";

        try {
            stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            String strOrderNo="Order No.";
            data.add(strOrderNo);
            while (rs.next())
            {
                strOrderNo=rs.getString("OrderNo");
                data.add(strOrderNo);
            }
            //data.add("Order No.");
            String[] array= data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter=new ArrayAdapter(this,R.layout.spinner_control,data);
            NoCoreAdapter.setDropDownViewResource(R.layout.spinner_item);
            cmbOrder.setAdapter(NoCoreAdapter);

            //cmbOrder.setSelection(cmbOrder.getCount()-1);

        } catch (SQLException e) {

        e.printStackTrace();

        }

        query = "select EmpID,Name from Employees where Active=1 AND Contractor=1";

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

        }

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
            myDT=Integer.toString(month)+'/'+Integer.toString(day)+'/'+Integer.toString(year);

            cmdReturnDate.setText(myDT);

        }
    }
    private void fillArticles(String strOrderNo )
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database

        String query = "SELECT FOrderItems.ItemID,Items.ArticleNo FROM FOrderItems INNER JOIN Items ON FOrderItems.ItemID=Items.ItemID where orderno='"+strOrderNo+"'";

        try {
            stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();
            String[] fromFieldNames=new String[] {"ArticleNo","ItemID"};

            int[] toViewIDs = new int[] {R.id.txtSpinnerControl};

            Map<String, String> datanum = new HashMap<String, String>();
            datanum.put("ArticleNo","Article");
            datanum.put("ItemID","");

            data.add(datanum);
            while (rs.next())
            {
                datanum = new HashMap<String, String>();

                datanum.put("ArticleNo",rs.getString("ArticleNo"));
                datanum.put("ItemID",rs.getString("ItemID"));

                data.add(datanum);
            }
            String[] fromwhere = { "ArticleNo" };

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.spinner_control,fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.spinner_item);
            cmbArticle.setAdapter(NoCoreAdapter);

            //cmbOrder.setSelection(cmbOrder.getCount()-1);

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }
    private void fillOperations(String strItemID)
    {
        DBHelper myDBH=new DBHelper();
        Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
        String query="";
        if (iProcessID==2)
            query = "SELECT OperationID,OperationName FROM VItemOperations where OperationCatID='02' AND ItemID='"+strItemID+"'";
        else if (iProcessID==6)
            query = "SELECT OperationID,OperationName FROM VItemOperations where OperationCatID='03' AND ItemID='"+strItemID+"'";

        try {
            stmt=MyCon.prepareStatement(query);
            rs=stmt.executeQuery();

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();
            Map<String, String> datanum = new HashMap<String, String>();

            int[] toViewIDs = new int[] {R.id.txtSpinnerControl};
            datanum.put("OperationName","Operation");
            datanum.put("OperationID","");

            data.add(datanum);
            while (rs.next())
            {
                datanum = new HashMap<String, String>();
                datanum.put("OperationName",rs.getString("OperationName"));
                datanum.put("OperationID",rs.getString("OperationID"));

                data.add(datanum);
            }
            String[] fromwhere = { "OperationName" };

            SimpleAdapter NoCoreAdapter=new SimpleAdapter(this,data,R.layout.spinner_control,fromwhere,toViewIDs);
            NoCoreAdapter.setDropDownViewResource(R.layout.spinner_item);
            cmbOperation.setAdapter(NoCoreAdapter);


        } catch (SQLException e) {

            e.printStackTrace();

        }
    }
}
