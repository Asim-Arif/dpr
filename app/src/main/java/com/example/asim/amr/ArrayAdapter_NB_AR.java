package com.example.asim.amr;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ArrayAdapter_NB_AR extends ArrayAdapter<String> {

    Context myContext;
    int layoutResourceId;
    public ArrayAdapter_NB_AR(Context context, int resource,int to, String[] from) {
        super(context, resource, to,from);
        myContext=context;
        layoutResourceId=resource;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        String strColor,strColor_Android;
        //Map<String,String> data =(HashMap<String,String>)super.getItem(position);

        strColor=super.getItem(position).toString();
        strColor_Android=utility_functions.covertToAndroidColor(strColor);

        try {
            view.setBackgroundColor(Color.parseColor(strColor_Android));
        }
        catch(IllegalArgumentException IAE)
        {
            view.setBackgroundColor(0x30FF0000);

        }

        return view;
    }


}
