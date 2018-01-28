package com.example.wautel_l.raspberry_chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by wautel_l on 25/01/2018.
 */

public class MyAdapter extends BaseAdapter{
    private ArrayList<String> mListItems;
    private LayoutInflater myLayout;

    public MyAdapter(Context context, ArrayList<String> arrayList)
    {
        mListItems = arrayList;
        myLayout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount(){
        return mListItems.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup  viewGroup)
    {
        if (view == null)
            view = myLayout.inflate(R.layout.list_item, null);

        String stringItem = mListItems.get(position);
        if (stringItem != null)
        {
            TextView itemName = (TextView) view.findViewById(R.id.list_item_textview);
            if (itemName != null)
            {
                itemName.setText(stringItem);
            }
        }

        return view;
    }
}