package com.example.user.foodforthought.adapter;


import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.foodforthought.R;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter
{

    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;

    private ArrayList<Pair<String, Integer>> messageList;
    private LayoutInflater layoutInflater;

    public MessageAdapter(Activity activity) {
        layoutInflater = activity.getLayoutInflater();
        messageList = new ArrayList<Pair<String, Integer>>();
    }
    public void addMessage(String message, int direction) {
        messageList.add(new Pair(message, direction));
        notifyDataSetChanged();
    }

    public boolean clearList(){
        messageList.clear();

        if (messageList.size() == 0)
            return true;
        else
            return false;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }
    @Override
    public Pair<String, Integer> getItem(int i) {
        return messageList.get(i);
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
        return messageList.get(i).second;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int direction = getItemViewType(i);
        //show message on left or right, depending on if
        //it's incoming or outgoing
        if (convertView == null) {
            int res = 0;
            if (direction == DIRECTION_INCOMING) {
                res = R.layout.simple_text_left;
            } else if (direction == DIRECTION_OUTGOING) {
                res = R.layout.simple_text_right;
            }
            convertView = layoutInflater.inflate(res, viewGroup, false);
        }
        String message = messageList.get(i).first;

        TextView txtMessage = (TextView) convertView.findViewById(R.id.textMessage);
        Log.d("The message is: ", message);
        Log.d("The direction is: ", "" + direction);
        txtMessage.setText(message);
        return convertView;

    }

}