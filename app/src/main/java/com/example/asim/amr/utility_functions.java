package com.example.asim.amr;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by asim on 4/11/2017.
 */

public class utility_functions {
    public static int getBackgroundColor(TextView textView) {
        ColorDrawable drawable = (ColorDrawable) textView.getBackground();
        if (Build.VERSION.SDK_INT >= 11) {
            return drawable.getColor();
        }
        try {
            Field field = drawable.getClass().getDeclaredField("mState");
            field.setAccessible(true);
            Object object = field.get(drawable);
            field = object.getClass().getDeclaredField("mUseColor");
            field.setAccessible(true);
            return field.getInt(object);
        } catch (Exception e) {
            // TODO: handle exceptionlic
        }
        return 0;
    }
    public static String covertToAndroidColor(String strColor){

        int iColor=Integer.parseInt(strColor);
        strColor=Integer.toHexString(iColor);
        int iLen=strColor.length();
        if (iLen<6)
        {
            for (int i = iLen; i < 6; i++) {
                strColor="0"+strColor;
            }
        }

        String s1,s2,s3;

        s1=strColor.substring(iLen-2);
        s2=strColor.substring(2,4);
        s3=strColor.substring(0,2);
        strColor="#"+s1+s2+s3;

        return strColor;
    }

    public static int getSingleIntValue(String strFieldName, String strTableName, String strCondition, Context context){
        DBHelper myDBH = new DBHelper();

        Connection MyCon = myDBH.connectionclass(context);        // Connect to database
        return utility_functions.getSingleIntValue(MyCon,strFieldName,strTableName,strCondition,context);

    }
    public static int getSingleIntValue(Connection MyCon,String strFieldName, String strTableName, String strCondition, Context context){
        DBHelper myDBH = new DBHelper();
        //Connection MyCon = myDBH.connectionclass(context);        // Connect to database

        String query = "SELECT "+strFieldName +" FROM "+strTableName+" "+strCondition;
        int ReturnValue=0;
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ReturnValue=rs.getInt(1);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
            return 0;
        }
        return ReturnValue;
    }
    public static double getSingleDoubleValue(String strFieldName, String strTableName, String strCondition, Context context){
        DBHelper myDBH = new DBHelper();

        Connection MyCon = myDBH.connectionclass(context);        // Connect to database
        return utility_functions.getSingleDoubleValue(MyCon,strFieldName,strTableName,strCondition,context);

    }
    public static double getSingleDoubleValue(Connection MyCon,String strFieldName, String strTableName, String strCondition, Context context){

        String query = "SELECT "+strFieldName +" FROM "+strTableName+" "+strCondition;
        double ReturnValue=0;
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ReturnValue=rs.getDouble(1);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
            return 0;
        }
        return ReturnValue;
    }

    public static String getSingleStringValue(String strFieldName, String strTableName, String strCondition, Context context){
        DBHelper myDBH = new DBHelper();

        Connection MyCon = myDBH.connectionclass(context);        // Connect to database
        return utility_functions.getSingleStringValue(MyCon,strFieldName,strTableName,strCondition,context);

    }
    public static String getSingleStringValue(Connection MyCon,String strFieldName, String strTableName, String strCondition, Context context){
        DBHelper myDBH = new DBHelper();
        //Connection MyCon = myDBH.connectionclass(context);        // Connect to database

        String query = "SELECT "+strFieldName +" FROM "+strTableName+" "+strCondition;
        String ReturnValue="";
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ReturnValue=rs.getString(1);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
            return "";
        }
        return ReturnValue;
    }
    public static Boolean getSingleBooleanValue(String strFieldName, String strTableName, String strCondition, Context context){
        DBHelper myDBH = new DBHelper();

        Connection MyCon = myDBH.connectionclass(context);        // Connect to database
        return utility_functions.getSingleBooleanValue(MyCon,strFieldName,strTableName,strCondition,context);

    }
    public static Boolean getSingleBooleanValue(Connection MyCon,String strFieldName, String strTableName, String strCondition, Context context){
        DBHelper myDBH = new DBHelper();
        //Connection MyCon = myDBH.connectionclass(context);        // Connect to database

        String query = "SELECT "+strFieldName +" FROM "+strTableName+" "+strCondition;
        Boolean ReturnValue=false;
        try {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = MyCon.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ReturnValue=rs.getBoolean(1);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            Message.message(context,e.toString());
            return false;
        }
        return ReturnValue;
    }
    public static java.sql.Date convertStringToSQLDate(String strDT,Context context){

        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");

        java.util.Date date=null;
        try{
            date = sdf1.parse(strDT);
        } catch (java.text.ParseException PE){
            Message.message(context,PE.toString());
        }

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }
    public static String getNextLotNo(Context context){
        int iRcvLotNo,iIssLotNo,iLotNo;
        String strLotNo;
        iRcvLotNo=utility_functions.getSingleIntValue("MAX(CAST(RIGHT(LotNo,LEN(LotNo)-LEN(REPLACE(CONVERT(VARCHAR(50),getDate(),3),'/',''))) AS INT))","VendRcvdDetail"," WHERE LEFT(LotNo,6)=REPLACE(CONVERT(VARCHAR(50),getDate(),3),'/','')",context);
        iIssLotNo=utility_functions.getSingleIntValue("MAX(CAST(RIGHT(LotNo,LEN(LotNo)-LEN(REPLACE(CONVERT(VARCHAR(50),getDate(),3),'/',''))) AS INT))","VendIssdDetail"," WHERE LEFT(LotNo,6)=REPLACE(CONVERT(VARCHAR(50),getDate(),3),'/','')",context);
        if (iRcvLotNo>iIssLotNo){
            iLotNo=iRcvLotNo;
        }
        else{
            iLotNo=iIssLotNo;
        }
        iLotNo=iLotNo+1;
        strLotNo=convertDateToString(getCurrentDate(context),"ddMMyy")+ String.format("%03d",iLotNo);
        return strLotNo;
    }

    public static Date getCurrentDate(Context context){
        final java.util.Calendar c=java.util.Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String myDT;
        myDT=String.format("%02d",month)+'-'+String.format("%02d",day)+'-'+Integer.toString(year);
        java.util.Date date=null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        try{
            date = sdf1.parse(myDT);
        } catch (java.text.ParseException PE){
            Message.message(context,PE.toString());
        }
        return date;
    }
    public static String convertDateToString(Date myDT,String strDateFormat){
        SimpleDateFormat sdf1 = new SimpleDateFormat(strDateFormat);
        return sdf1.format(myDT).toString();
    }
}
