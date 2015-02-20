package com.example.user.foodforthought;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter
{
    Activity context;
    String title[];
    String description[];
    String dates[];
    String info[];

    public ListViewAdapter(Activity context, String[] title, String[] description,
                           String[] dates, String[] info) {
        super();
        this.context = context;
        this.title = title;
        this.description = description;
        this.dates = dates;
        this.info = info;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDescription;
        TextView txtViewDates;
        TextView txtViewInfo;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.simple_row, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.companyName);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.companyTitle);
            holder.txtViewDates = (TextView) convertView.findViewById(R.id.companyDates);
            holder.txtViewInfo = (TextView) convertView.findViewById(R.id.companyInfo);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewTitle.setText(title[position]);
        holder.txtViewDescription.setText(description[position]);
        holder.txtViewDates.setText(dates[position]);
        holder.txtViewInfo.setText(info[position]);

        return convertView;
    }

}