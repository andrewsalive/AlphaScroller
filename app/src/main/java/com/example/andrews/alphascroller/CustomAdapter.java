package com.example.andrews.alphascroller;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
 * Created by Apple on 17.07.15.
 */
public class CustomAdapter extends ArrayAdapter<ContentValues> implements SectionIndexer {
    private LayoutInflater layoutInflater_;
    private int separetorResourceId = R.layout.listview_sep;
    private final String SEP_FLAG = "sepFlag";
    private HashMap<String, Integer> indexer = new HashMap<String, Integer>();
    private String[] sections;

    // Constructor
    public CustomAdapter(Context context, int textViewResourceId, List<ContentValues> objects) {
        super(context, textViewResourceId, new ArrayList<ContentValues>());
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int listLength = objects.size();
        String pre_initial = ""; int sep_num = 0;
        for(int index=0; index<listLength; index++){
            ContentValues cv = objects.get(index);

            String initial = cv.getAsString("name").substring(0, 1);
            if(!initial.equalsIgnoreCase(pre_initial)){
                ContentValues cv_sep = new ContentValues();
                cv_sep.put(SEP_FLAG, true); cv_sep.put("text", initial);
                this.indexer.put(initial, index + sep_num);
                add(cv_sep); sep_num++;
                pre_initial = initial;
            }
            add(cv);
        }

        ArrayList<String> sectionList = new ArrayList<String>(indexer.keySet());
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
    }

    static class ViewHolder{
        TextView textView_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final ContentValues cv = (ContentValues)getItem(position);
        ViewHolder holder;

        if (null == convertView || convertView.getId() != R.id.listview_name) {
            convertView = layoutInflater_.inflate(R.layout.listview_item, null);
            holder = new ViewHolder();
            holder.textView_name = (TextView)convertView.findViewById(R.id.listview_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(!isEnabled(position)){
            convertView =  layoutInflater_.inflate(separetorResourceId, null);
            TextView sep_text = (TextView)convertView.findViewById(R.id.listView_initial);
            sep_text.setText(cv.getAsString("text"));
        }else{
            holder.textView_name.setText(cv.getAsString("name"));
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position){
        ContentValues cv = getItem(position);
        if(cv.containsKey(SEP_FLAG)){
            return !cv.getAsBoolean(SEP_FLAG);
        }else{
            return true;
        }
    }

    @Override
    public int getPositionForSection(int section) {
        return indexer.get(sections[section]);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 1;
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

}