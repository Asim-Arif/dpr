package com.example.asim.amr;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class add_remarks extends AppCompatActivity {

    SimpleCursorAdapter myCursorAdapter;
    String strIssNo;
    Button cmdColor;
    Button cmdBack,cmdFinish;
    Context context;
    EditText txtRemarks;
    ListView lv;
    TextView txtCharacters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        strIssNo = b.getString("IssNo");

        setContentView(R.layout.add_remark);

        context=this.getBaseContext();

        TextView txtActivityTitle=(TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("Add Remark");

        txtCharacters=(TextView) findViewById(R.id.txtCharacters);
        txtRemarks=(EditText) findViewById(R.id.txtRemarks_AR);
        cmdColor=(Button) findViewById(R.id.cmdColor_AR);
        cmdFinish=(Button) findViewById(R.id.cmdFinish_AR);

        txtRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strRemarks;
                strRemarks=txtRemarks.getText().toString();
                int iCharacters=strRemarks.length();
                iCharacters=110-iCharacters;
                txtCharacters.setText(Integer.toString(iCharacters));
            }
        });
        cmdColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ColorCodes[] ={"1567225","4496911","15108356","13019387","2825726","4210816","9741730","3244039"};
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(add_remarks.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.color_list, null);
                alertDialog.setView(convertView);

                int toViewIDs =  R.id.txtColor;
                lv= (ListView) convertView.findViewById(R.id.LVColors);
                ArrayAdapter_NB_AR  adapter = new ArrayAdapter_NB_AR (context,R.layout.add_remarks_colors_layout,toViewIDs,ColorCodes);
                adapter.setDropDownViewResource(R.layout.add_remarks_colors_layout);
                lv.setAdapter(adapter);
                final AlertDialog MyDialog=alertDialog.show();

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String strSelected=lv.getItemAtPosition(position).toString();
                        String strColor_Android=utility_functions.covertToAndroidColor(strSelected);
                        cmdColor.setBackgroundColor(Color.parseColor(strColor_Android));
                        cmdColor.setTag(strSelected);
                        MyDialog.dismiss();
                    }
                });
                MyDialog.show();

            }
        });
        final Button btnBack=(Button) findViewById(R.id.cmdBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cmdFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cmdColor.getTag()==null){
                    Message.message(context,"Please Select Color");
                    return;
                }
                DBHelper myDBH = new DBHelper();
                Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
                int iWOIWD_RefID;
                iWOIWD_RefID = getWOIWD_EntryID();
                try {
                    String strQuery = "INSERT INTO WOIWD_News_Bulletin(WOIWD_RefID,Remarks,UserName,MachineName,NewsColor) VALUES(?,?,?,?,?)";
                    PreparedStatement stmt = MyCon.prepareStatement(strQuery);

                    stmt.setInt(1, iWOIWD_RefID);
                    stmt.setString(2,txtRemarks.getText().toString());
                    stmt.setString(3, "Android");
                    stmt.setString(4, "Android");
                    stmt.setInt(5, Integer.parseInt(cmdColor.getTag().toString()));
                    stmt.addBatch();
                    stmt.executeBatch();
                    Message.message(getApplicationContext(),"Successfuly Saved.");
                    onBackPressed();

                } catch (SQLException e) {

                    e.printStackTrace();
                    Message.message(context,e.toString());
                }
            }
        });
        cmdBack=(Button) findViewById(R.id.cmdBack_AR);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
}