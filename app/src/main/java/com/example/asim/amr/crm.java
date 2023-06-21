
package com.example.asim.amr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;

import android.util.TypedValue;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class crm extends Activity {


    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    SimpleAdapter myCursorAdapter;

    CheckBox chkFoodAverage,chkFoodGood,chkFoodExellent;
    CheckBox chkPAverage,chkPGood,chkPExellent;
    CheckBox chkSAverage,chkSGood,chkSExellent;
    CheckBox chkDAverage,chkDGood,chkDExellent;
    CheckBox chkAAverage,chkAGood,chkAExellent;
    RatingBar rbFood,rbPortion,rbService,rbDecor,rbAtmosphere;
    Context context;
    static EditText  txtDate;
    Button cmdCancel,cmdUpdate;
    EditText txtTableNo,txtName,txtProfession,txtContact,txtAddress,txtEmail,txtSuggestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        setContentView(R.layout.crm);
        context = this.getBaseContext();

        cmdCancel=(Button) findViewById(R.id.cmdCancel);
        cmdUpdate=(Button) findViewById(R.id.cmdUpdate);

        rbFood=(RatingBar) findViewById(R.id.foodrating);

        rbService=(RatingBar) findViewById(R.id.servicerating);


        /*chkFoodAverage=(CheckBox) findViewById(R.id.Average);
        chkFoodGood=(CheckBox) findViewById(R.id.Good);
        chkFoodExellent=(CheckBox) findViewById(R.id.Excellent);

        chkPAverage=(CheckBox) findViewById(R.id.PAverage);
        chkPGood=(CheckBox) findViewById(R.id.PGood);
        chkPExellent=(CheckBox) findViewById(R.id.PExcellent);

        chkSAverage=(CheckBox) findViewById(R.id.SAverage);
        chkSGood=(CheckBox) findViewById(R.id.SGood);
        chkSExellent=(CheckBox) findViewById(R.id.SExcellent);

        chkDAverage=(CheckBox) findViewById(R.id.DAverage);
        chkDGood=(CheckBox) findViewById(R.id.DGood);
        chkDExellent=(CheckBox) findViewById(R.id.DExcellent);

        chkAAverage=(CheckBox) findViewById(R.id.AAverage);
        chkAGood=(CheckBox) findViewById(R.id.AGood);
        chkAExellent=(CheckBox) findViewById(R.id.AExcellent);
        */

        txtDate=(EditText) findViewById(R.id.txtDate);
        //txtDate.setText(utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"yyyy-MM-dd"));
        txtDate.setText(utility_functions.convertDateToString(Calendar.getInstance().getTime(),"yyyy-MM-dd HH:mm"));

        txtDate.setTag(utility_functions.convertDateToString(utility_functions.getCurrentDate(context),"yyyy-MM-dd"));

        txtTableNo=(EditText) findViewById(R.id.txtTableNo);
        txtName=(EditText) findViewById(R.id.txtName);
        txtContact=(EditText) findViewById(R.id.txtContact);
        txtSuggestion=(EditText) findViewById(R.id.txtSuggestion);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"");

            }
        });

        cmdCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cmdUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper myDBH = new DBHelper();
                Connection MyCon = myDBH.connectionclass(getApplicationContext());        // Connect to database
                String strQuery="";
                PreparedStatement stmt;
                try {
                    MyCon.setAutoCommit(false);
                    strQuery = "INSERT INTO Customer_Feedback(DT,Name,Profession,Contact,Address,Email,Suggestion,Breakfast,Lunch,Dinner,Other" +
                            ",Food_Rating,Portion_Rating,Service_Rating,Decor_Rating,Atmosphere_Rating,UserName,MachineName,Table_No) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = MyCon.prepareStatement(strQuery);
                    java.sql.Date myDT=java.sql.Date.valueOf(txtDate.getTag().toString());//utility_functions.convertStringToSQLDate(txtDate.getText().toString(),context);//null;//java.sql.Date.valueOf(txtDate.getText().toString());
                    //utility_functions.convertStringToSQLDate();
                    stmt.setDate(1, myDT);
                    stmt.setString(2, txtName.getText().toString());
                    stmt.setString(3, "");
                    stmt.setString(4, txtContact.getText().toString());
                    stmt.setString(5, "");
                    stmt.setString(6, "");
                    stmt.setString(7, txtSuggestion.getText().toString());
                    stmt.setBoolean(8,false);
                    stmt.setBoolean(9,false);
                    stmt.setBoolean(10,false);
                    stmt.setBoolean(11,false);

                    stmt.setDouble(12, rbFood.getRating());
                    stmt.setDouble(13, 0.0);
                    stmt.setDouble(14, rbService.getRating());
                    stmt.setDouble(15, 0.0);
                    stmt.setDouble(16, 0.0);

                    stmt.setString(17,"Android");
                    stmt.setString(18,"Android");
                    stmt.setString(19,txtTableNo.getText().toString());
                    stmt.addBatch();
                    stmt.executeBatch();
                    MyCon.commit();
                    Message.message(context,"Successfully Saved.");
                    onBackPressed();
                } catch (SQLException e) {

                    e.printStackTrace();
                    Message.message(context,e.toString());
                }

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
            month=month+1;
            //myDT=Integer.toString(month)+'/'+Integer.toString(day)+'/'+Integer.toString(year);
            myDT=Integer.toString(year)+'-'+Integer.toString(month)+'-'+Integer.toString(day);
            txtDate.setText(myDT);

        }
    }
}
