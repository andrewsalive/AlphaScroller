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
import java.util.HashMap;
import java.util.List;

/*
 * Created by Apple on 17.07.15.
 */
public class CustomAdapter extends ArrayAdapter<ContentValues> implements SectionIndexer {
    private LayoutInflater layoutInflater_;
    private int separetorResourceId = R.layout.listview_sep;
    private final String SEP_FLAG = "sepFlag";
    private HashMap<String, Integer>
            indexer = new HashMap<String, Integer>();
    private String[] sections;
    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Constructor
    public CustomAdapter(Context context, int textViewResourceId, List<ContentValues> objects) {
        super(context, textViewResourceId, new ArrayList<ContentValues>());
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int listLength = objects.size();
        String pre_initial = "";
        int sep_num = 0;
        for(int index=0; index<listLength; index++){
            ContentValues cv = objects.get(index);

            String initial = cv.getAsString("name").substring(0, 1);
            if(!initial.equalsIgnoreCase(pre_initial)){
                ContentValues cv_sep = new ContentValues();
                cv_sep.put(SEP_FLAG, true);
                cv_sep.put("text", initial);
                this.indexer.put(initial, index + sep_num);
                add(cv_sep);
                sep_num++;
                pre_initial = initial;
            }
            add(cv);
        }
        sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));

//        ArrayList<String> sectionList = new ArrayList<String>(indexer.keySet());
//        Collections.sort(sectionList);
//        sections = new String[sectionList.size()];
//        sectionList.toArray(sections);
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
        // If there is no item for current section, previous section will be selected
        for (int i = section; i >= 0; i--) {
            if (indexer.get(sections[i]) != null)
                return indexer.get(sections[i]);
/*            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    // For numeric section
//                    for (int k = 0; k <= 9; k++) {
//                        if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k)))
//                            return j;
//                    }
                } else {
                    if (indexer.get(sections[i]) != null)
//                    if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(mSections.charAt(i))))
//                        return j;
                        return indexer.get(sections[i]);
                }
            }*/
        }
        return 0;

//        return indexer.get(sections[section]);
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