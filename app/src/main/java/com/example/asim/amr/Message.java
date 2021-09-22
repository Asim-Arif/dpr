package com.example.asim.amr;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Asim on 2/27/2016.
 */
public class Message {
    public static void message(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
