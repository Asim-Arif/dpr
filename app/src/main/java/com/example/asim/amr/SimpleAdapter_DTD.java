package com.example.asim.amr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SimpleAdapter_DTD extends SimpleAdapter {
    Context myContext;
    int layoutResourceId;
    Boolean[] chkValues;
    public SimpleAdapter_DTD (Context context, List<Map<String, String>> items, int resource, String[] from, int[] to,Boolean[] chkValues) {
        super(context, items, resource, from, to);
        myContext=context;
        layoutResourceId=resource;
        this.chkValues=chkValues;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //View view = super.getView(position, convertView, parent);

        final ViewHolder myviewholder;
        if (convertView==null ){
            LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            final TextView txtDate=(TextView) convertView.findViewById(R.id.txtDTD);
            final CheckBox chk=(CheckBox) convertView.findViewById(R.id.chkDTD);
            ViewHolder viewholder=new ViewHolder(txtDate,chk);
            convertView.setTag(viewholder);
            myviewholder=viewholder;
        }
        else {
            final ViewHolder viewholder=(ViewHolder) convertView.getTag();
            myviewholder=viewholder;
        }


        myviewholder.chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v,position,0);
            }
        });

        View view = super.getView(position, convertView, parent);
        String strCheckBoxValue;
        Map<String,String> data =(HashMap<String,String>)super.getItem(position);
        strCheckBoxValue=data.get("CheckBox");
        CheckBox myChkBox=(CheckBox) view.findViewById(R.id.chkDTD);
        myviewholder.chk.setChecked(chkValues[position]);

        /*if (strCheckBoxValue=="1")
            myviewholder.chk.setChecked(true);
        else
            myviewholder.chk.setChecked(false);*/
        return view;
    }
    private class ViewHolder {

        private final TextView txtDate;
        private final CheckBox chk;

        public ViewHolder(TextView txtDate, CheckBox chk)
        {
            this.txtDate=txtDate;
            this.chk=chk;
        }
    }
}