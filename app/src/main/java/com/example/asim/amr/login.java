package com.example.asim.amr;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/*import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Text;*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class login extends Activity {

    TextView txtActivityTitle;
    TextView txtSQLIP,txtSQLPort;
    Connection MyCon;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner cmbOrder;
    Spinner cmbEmployee;
    Spinner cmbArticle;

    static Button cmdReturnDate;
    SimpleCursorAdapter myCursorAdapter;
    Button cmdSave, cmdBack;
    TextView lblLotNo, lblIssNo;
    EditText txtPassword, txtQty, txtIssID,txtUserName;
    Context context;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        setContentView(R.layout.login);
        context = this.getBaseContext();

        txtSQLIP=(TextView) findViewById(R.id.txtServerIPAddress);
        txtSQLPort=(TextView) findViewById(R.id.txtServerPortNo);
        txtUserName=(EditText) findViewById(R.id.txtUserName);
        //fillUsers();
        preferences= PreferenceManager.getDefaultSharedPreferences(context);

        String strSQLIP = preferences.getString("SQLIP", "");
        String strSQLPort = preferences.getString("SQLPort", "");
        String strLastUserName = preferences.getString("LastLogin", "");
        txtSQLIP.setText(strSQLIP);
        txtSQLPort.setText(strSQLPort);
        txtUserName.setText(strLastUserName);
        txtActivityTitle = (TextView) findViewById(R.id.txtAtivityTitle);
        txtActivityTitle.setText("Login");

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        if (strLastUserName!=""){
            txtPassword.requestFocus();
        }
        txtSQLIP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                SharedPreferences.Editor editor = preferences.edit();
                String strSQLIP=txtSQLIP.getText().toString();
                editor.putString("SQLIP",strSQLIP);
                editor.commit();
            }
        });
        txtSQLPort.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                SharedPreferences.Editor editor = preferences.edit();
                String strSQLPort=txtSQLPort.getText().toString();
                editor.putString("SQLPort",strSQLPort);
                editor.commit();
            }
        });
        cmdBack = (Button) findViewById(R.id.cmdci_Back);
        cmdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cmdSave = (Button) findViewById(R.id.cmdRcv_Save);

        cmdSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = preferences.edit();
                String strSQLPort=txtSQLPort.getText().toString();
                String strSQLIP=txtSQLIP.getText().toString();
                editor.putString("SQLIP",strSQLIP);
                editor.putString("SQLPort",strSQLPort);
                editor.commit();

                DBHelper myDBH = new DBHelper();

                Connection MyCon = myDBH.connectionclass(context);        // Connect to database
                int iCount,iPassword;
                String strUserName,strPassword,strPasscode;

                strUserName=txtUserName.getText().toString();
                strPassword=txtPassword.getText().toString();


                try{
                iPassword=Integer.parseInt(strPassword);}
                catch(NumberFormatException nfe){
                    //do Nothing;
                    iPassword=0;
                }
                strPasscode=Integer.toString(iPassword);
                iCount=utility_functions.getSingleIntValue("COUNT(*)","Users","WHERE UserName='"+strUserName+"' AND Password='"+strPassword+"'",context);
                if (iCount==0 && strPasscode!="" && iPassword!=0){
                    iCount=utility_functions.getSingleIntValue("COUNT(*)","Users","WHERE UserName='"+strUserName+"' AND NumericCode="+strPasscode,context);
                }
                if (iCount>0){
                    //Successfull...
                    editor.putString("LastLogin",strUserName);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }else{
                    Message.message(context,"Invalid User or Password");
                }
                //return utility_functions.getSingleStringValue(MyCon,strFieldName,strTableName,strCondition,context);

            }
        });
    }

}