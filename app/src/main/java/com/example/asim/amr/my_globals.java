package com.example.asim.amr;

import android.content.Context;
import android.content.Intent;

/**
 * Created by asim on 7/19/2016.
 */
public class my_globals {
    Context mContext;

    // constructor
    public my_globals(Context context){
        this.mContext = context;
    }

    public void goToHomeActivity()
    {
        Intent i = new Intent(mContext, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(i);
    }
}

