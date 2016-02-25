package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class WhiskeyCommentListAdapter extends ArrayAdapter<WhiskeyComment> {
    private Context context;
    private WhiskeyComment[] whiskeyComments;
    private DatabaseHandler db;

    public WhiskeyCommentListAdapter(Context context, WhiskeyComment[] whiskeyComments) {
        super(context, R.layout.whiskey_comment_list_entry, whiskeyComments);
        this.context = context;
        this.whiskeyComments = whiskeyComments;
        db = new DatabaseHandler(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Part of this taken from http://www.vogella.com/tutorials/AndroidListView/article.html
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listItemView = inflater.inflate(R.layout.whiskey_comment_list_entry, parent, false);

        TextView usernameView = (TextView) listItemView.findViewById(R.id.textViewWhiskeyCommentUsername);
        TextView whiskeyCommentTextView = (TextView) listItemView.findViewById(R.id.textViewWhiskeyCommentText);

        WhiskeyComment whiskeyComment = whiskeyComments[position];
        User user = db.getUserById(whiskeyComment.getUserId());
        db.close();

        usernameView.setText(user.getUsername());
        whiskeyCommentTextView.setText(whiskeyComment.getCommentText());

        return listItemView;
    }
}
