package com.example.asim.amr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SimpleAdapter_NB extends SimpleAdapter {

    Context myContext;
    int layoutResourceId;
    public SimpleAdapter_NB(Context context, List<Map<String, String>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);
        myContext=context;
        layoutResourceId=resource;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //View view = super.getView(position, convertView, parent);
        final ViewHolder myviewholder;
        if (convertView==null ){
            LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            final TextView txtRM=(TextView) convertView.findViewById(R.id.txtNB_Remarks);
            final TextView txtIss=(TextView) convertView.findViewById(R.id.txtNB_IssNo);
            final ImageButton imgBtn=(ImageButton) convertView.findViewById(R.id.ImgNB);
            ViewHolder viewholder=new ViewHolder(txtRM,txtIss,imgBtn);
            convertView.setTag(viewholder);
            myviewholder=viewholder;
        }
        else {
            final ViewHolder viewholder=(ViewHolder) convertView.getTag();
            myviewholder=viewholder;
        }

        myviewholder.txtIss.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((GridView) parent).performItemClick(view,position,0);
                return false;
            }
        });
        myviewholder.txtRm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((GridView) parent).performItemClick(view,position,0);
                return false;
            }
        });
        myviewholder.imgBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((GridView) parent).performItemClick(view,position,0);
                return false;
            }
        });
        /*if (convertView!=null) {
            TextView txtIssNo = (TextView) convertView.findViewById(R.id.txtNB_IssNo);
            TextView txtRemarks = (TextView) convertView.findViewById(R.id.txtNB_Remarks);
            ImageButton Img = (ImageButton)  convertView.findViewById(R.id.ImgNB);
            txtIssNo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((GridView) parent).performItemClick(view,position,0);
                    return false;
                }
            });
            txtRemarks.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((GridView) parent).performItemClick(view,position,0);
                    return false;
                }
            });
            Img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((GridView) parent).performItemClick(view,position,0);
                    return false;
                }
            });
        }*/
        View view = super.getView(position, convertView, parent);
        String strColor,strPriority,strForeColor;
        Map<String,String> data =(HashMap<String,String>)super.getItem(position);

        strColor=data.get("Color");
        strPriority=data.get("Priority");

        strForeColor=data.get("ForeColor");
        strForeColor=utility_functions.covertToAndroidColor(strForeColor);

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

        GradientDrawable border = new GradientDrawable();
        GradientDrawable ForeGround= new GradientDrawable();

        //border.setColor(0xFFFFFFFF); //white background
         //black border with full opacity
        /*if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            TitleLayout.setBackgroundDrawable(border);
        } else {
            TitleLayout.setBackground(border);
        }*/
        if (strPriority=="1"){
            border.setStroke(5, 0xFF000000);
        }
        s1=strColor.substring(iLen-2);
        s2=strColor.substring(2,4);
        s3=strColor.substring(0,2);
        strColor="#"+s1+s2+s3;
        try {
            //view.setBackgroundColor(Color.parseColor(strColor));
            border.setColor(Color.parseColor(strColor));
            ForeGround.setColor(Color.parseColor(strForeColor));

        }
            catch(IllegalArgumentException IAE)
            {
                //view.setBackgroundColor(0x30FF0000);
                border.setColor(0x30FF0000);
            }

        view.setBackground(border);
        //view.setForeground(ForeGround);
        return view;
    }

    private class ViewHolder {

        private final TextView txtRm,txtIss;
        private final ImageButton imgBtn;

        public ViewHolder(TextView txtRM, TextView txtIss, ImageButton imgBtn)
        {
            this.txtRm=txtRM;
            this.txtIss=txtIss;
            this.imgBtn=imgBtn;
        }
    }

}
