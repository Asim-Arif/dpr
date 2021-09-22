package com.example.asim.amr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SimpleAdapter_Tasks extends SimpleAdapter {

    Context myContext;
    int layoutResourceId;
    int[] RadioButtonValues;
    String[] strRemarks;
    public SimpleAdapter_Tasks(Context context, List<Map<String, String>> items, int resource, String[] from, int[] to,int[] RadioButtonValues,String[] strRemarks) {
        super(context, items, resource, from, to);
        myContext=context;
        layoutResourceId=resource;
        this.RadioButtonValues=RadioButtonValues;
        this.strRemarks=strRemarks;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //View view = super.getView(position, convertView, parent);
        final ViewHolder myviewholder;
        if (convertView==null ){
            LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            final TextView txtDescription=(TextView) convertView.findViewById(R.id.txtTasks_Description);
            final EditText txtRemarks=(EditText) convertView.findViewById(R.id.txtTasks_Remarks);
            final RadioGroup RG_Tasks=(RadioGroup) convertView.findViewById(R.id.RG_Tasks);
            final RadioButton RDYes=(RadioButton) convertView.findViewById(R.id.RD_Yes);
            final RadioButton RDNo=(RadioButton) convertView.findViewById(R.id.RD_No);
            final Button cmdDelete=(Button) convertView.findViewById(R.id.cmdDelete);
            final Button cmdPost=(Button) convertView.findViewById(R.id.cmdPost);
            ViewHolder viewholder=new ViewHolder(txtDescription,txtRemarks,RG_Tasks,RDYes,RDNo,cmdDelete,cmdPost);
            convertView.setTag(viewholder);
            myviewholder=viewholder;
        }
        else {
            final ViewHolder viewholder=(ViewHolder) convertView.getTag();
            myviewholder=viewholder;
        }

        myviewholder.RDNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view,position,0);
            }
        });

        myviewholder.RDYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view,position,0);
            }
        });
        myviewholder.cmdDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view,position,0);
            }
        });
        myviewholder.cmdPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view,position,0);
            }
        });
        myviewholder.txtRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                strRemarks[position]=editable.toString();
            }
        });
        //String strRemarks;

        View view = super.getView(position, convertView, parent);

        Map<String,String> data =(HashMap<String,String>)super.getItem(position);
        String strYes,strNo;
        //strRemarks=data.get("Remarks");
        strYes=data.get("Yes");
        strNo=data.get("No");

        if (RadioButtonValues[position]==1){
            myviewholder.RDYes.setChecked(true);
        } else if (RadioButtonValues[position]==0){
            myviewholder.RDNo.setChecked(true);
        }
        myviewholder.txtRemarks.setText(strRemarks[position]);
        /*if (strYes=="1") {
            myviewholder.RDYes.setChecked(true);
        }else{
            //myviewholder.RDYes.setChecked(false);
        }
        if (strNo=="1") {
            myviewholder.RDNo.setChecked(true);
        }else{
            myviewholder.RDNo.setChecked(false);
        }*/
        //myviewholder.txtRemarks.setText(strRemarks);
        //return convertView;
        return view;
    }

    private class ViewHolder {

        private final TextView txtDescription;
        private final EditText txtRemarks;
        private final RadioGroup RG_Tasks;
        private final RadioButton RDYes,RDNo;
        private final Button cmdDelete,cmdPost;

        public ViewHolder(TextView txtDescription, EditText txtRemarks,RadioGroup RG_Tasks, RadioButton RDYes,RadioButton RDNo,Button cmdDelete,Button cmdPost)
        {
            this.txtDescription=txtDescription;
            this.txtRemarks=txtRemarks;
            this.RG_Tasks=RG_Tasks;
            this.RDYes=RDYes;
            this.RDNo=RDNo;
            this.cmdDelete=cmdDelete;
            this.cmdPost=cmdPost;
        }
    }

}
