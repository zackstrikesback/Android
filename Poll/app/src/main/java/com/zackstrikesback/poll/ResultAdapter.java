package com.zackstrikesback.poll;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;



public class ResultAdapter extends ArrayAdapter<DBPoll> {
    private ArrayList<DBPoll> polls;

    public ResultAdapter(Context context, ArrayList<DBPoll> polls) {
        super(context, 0, polls);
        this.polls = polls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        View v = convertView;
        DBPoll poll = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.poll_result, parent, false);
        }
        // Populate the data into the template view using the data object
        TextView rName = (TextView) v.findViewById(R.id.rName);
        TextView rQuery = (TextView) v.findViewById(R.id.rQuery);
        ImageView rImage = (ImageView) v.findViewById(R.id.rImage);
        TextView yCount = (TextView) v.findViewById(R.id.yCount);
        TextView nCount = (TextView) v.findViewById(R.id.nCount);
        TextView number = (TextView) v.findViewById(R.id.number);
        if (poll.getImage()!= null){
            Uri uri = Uri.parse(poll.getImage());
            Log.d("uri", uri.toString());
            rImage.setImageBitmap(BitmapFactory.decodeFile(poll.getImage()));
            rImage.setVisibility(View.VISIBLE);
        }
        rName.setText(poll.getName());
        rQuery.setText(poll.getQuestion());
        yCount.setText(Integer.toString(poll.getYes()));
        nCount.setText(Integer.toString(poll.getNo()));
        number.setText("Poll #" + Integer.toString(poll.getId()));
        // Return the completed view to render on screen
        return v;
    }
}
