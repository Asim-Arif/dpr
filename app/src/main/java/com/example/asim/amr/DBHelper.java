package com.example.asim.amr;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Asim on 3/6/2017.
 */

public class DBHelper {
    private static final String DATABASE_NAME="Impulse.db";
    private static final int DATABASE_VERSION=11;
    private Context context;
    private SQLiteDatabase db;
    private Connection con;
    SharedPreferences preferences;

    public Connection connectionclass(Context context)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        preferences= PreferenceManager.getDefaultSharedPreferences(context);

        String strSQLIP = preferences.getString("SQLIP", "");
        String strSQLPort = preferences.getString("SQLPort", "");
        //Vision Settings
        //strSQLIP="192.168.0.22";
        //strSQLPort="50801";


        //strSQLIP="192.168.8.105";
        //strSQLIP="192.168.43.242";

        //Allah Malak Settings..
        //strSQLIP="192.168.1.116";
        //strSQLPort="1433";
        //AMR
        //strSQLIP="192.168.1.15";
        //strSQLPort="1433";

        //Cafe
        //strSQLIP="192.168.1.16";
        //strSQLPort="1433";

        //Event Hall
        strSQLIP="192.168.1.17";
        strSQLPort="1433";

        //

        //Laptop Settings
        //strSQLIP="192.168.10.24";
        //strSQLPort="51178";
        //

        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";databaseName="+database + ";user=" + user+ ";password=" + password + ";";

            ConnectionURL = "jdbc:jtds:sqlserver://"+strSQLIP+":"+strSQLPort+";databaseName=Rest_Man;user=sa;password=awm";
            //ConnectionURL = "jdbc:jtds:sqlserver://192.168.0.22:50592;databaseName=ImiTex;user=sa;password=awm";
            DriverManager.setLoginTimeout(15);
            connection = DriverManager.getConnection(ConnectionURL);

        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
            //Message.message(this.context,se.getMessage().toString());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }


}




