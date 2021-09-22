package com.example.asim.amr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SimpleAdapter_NBD extends SimpleAdapter {
    Context mycontext;
    public SimpleAdapter_NBD(Context context, List<Map<String, String>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);
        mycontext=context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        if (convertView!=null) {
            TextView txtRemarks = (TextView) convertView.findViewById(R.id.txtNB_Remarks);
            final TextView txtDate = (TextView) convertView.findViewById(R.id.txtNBD_Date);

            txtDate.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    /*((ListView) parent).performItemClick(view,position,0);
                    */
                    /*txtDate.setVisibility(view.GONE);
                    txtRemarks.setVisibility(view.VISIBLE);*/

                    return false;
                }
            });
            txtRemarks.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((ListView) parent).performItemClick(view,position,0);
                    //txtRemarks.setVisibility(view.GONE);
                    //txtDate.setVisibility(view.VISIBLE);
                    //Message.message(mycontext,"OK");
                    return false;
                }
            });

        }

        String strColor;
        Map<String,String> data =(HashMap<String,String>)super.getItem(position);

        strColor=data.get("Color");
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
        try {
            view.setBackgroundColor(Color.parseColor(strColor));
        }
        catch(IllegalArgumentException IAE)
        {
            view.setBackgroundColor(0x30FF0000);
        }
        return view;
    }
}