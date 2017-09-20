package com.zackstrikesback.poll;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class PollAdapter extends ArrayAdapter<DBPoll> {
    private ArrayList<DBPoll> polls;

    public PollAdapter(Context context, ArrayList<DBPoll> polls) {
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
            v = LayoutInflater.from(getContext()).inflate(R.layout.img_poll, parent, false);
        }
        // Populate the data into the template view using the data object
        TextView pName = (TextView) v.findViewById(R.id.pName);
        TextView pQuery = (TextView) v.findViewById(R.id.pQuery);
        ImageView pImage = (ImageView) v.findViewById(R.id.pImage);
        pName.setText(poll.getName());
        pQuery.setText(poll.getQuestion());
        if (poll.getImage()!= null){
            Uri uri = Uri.parse(poll.getImage());
            Log.d("uri", uri.toString());
            pImage.setImageBitmap(BitmapFactory.decodeFile(poll.getImage()));
            pImage.setVisibility(View.VISIBLE);
        }
        // Return the completed view to render on screen
        return v;
    }
}
