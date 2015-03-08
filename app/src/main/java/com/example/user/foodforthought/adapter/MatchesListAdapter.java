package com.example.user.foodforthought.adapter;

import android.app.Activity;
import android.media.Image;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.foodforthought.R;


import java.util.ArrayList;

/**
 * Created by Ty Giacalone on 3/6/2015.
 */
public class MatchesListAdapter extends BaseAdapter {

    private ArrayList<Pair<String, Integer>> matchList;
    private LayoutInflater layoutInflater;

    public MatchesListAdapter(Activity activity) {
        layoutInflater = activity.getLayoutInflater();
        matchList = new ArrayList<Pair<String, Integer>>();
    }
    public void addMessage(String name, int direction) {
        matchList.add(new Pair(name, direction));
        notifyDataSetChanged();
    }

    public boolean clearList(){
        matchList.clear();

        if (matchList.size() == 0)
            return true;
        else
            return false;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }
    @Override
    public Object getItem(int i) {
        return matchList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int i) {
        return matchList.get(i).second;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int direction = getItemViewType(i);

        convertView = layoutInflater.inflate(R.layout.simple_match, viewGroup, false);

        String name = matchList.get(i).first;

        TextView txtName = (TextView) convertView.findViewById(R.id.matchName);
        Log.d("The name is: ", name);
        Log.d("The direction is: ", "" + direction);
        txtName.setText(name);
        return convertView;

    }

}

