package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class WhiskeyListAdapter extends ArrayAdapter<Whiskey> {
    private Context context;
    private Whiskey[] whiskeys;

    public WhiskeyListAdapter(Context context, Whiskey[] whiskeys) {
        super(context, R.layout.whiskey_list_entry, whiskeys);
        this.context = context;
        this.whiskeys = whiskeys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Part of this taken from http://www.vogella.com/tutorials/AndroidListView/article.html
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItemView = inflater.inflate(R.layout.whiskey_list_entry, parent, false);

        TextView whiskeyNameView = (TextView) listItemView.findViewById(R.id.textViewWN1);
        //TextView usernameView = (TextView) listItemView.findViewById(R.id.textViewUser1);
        RatingBar ratingBar = (RatingBar) listItemView.findViewById(R.id.ratingBar2);

        Whiskey whiskey = whiskeys[position];

        whiskeyNameView.setText(whiskey.getName());
        ratingBar.setRating(whiskey.getRating());

        return listItemView;
    }
}
