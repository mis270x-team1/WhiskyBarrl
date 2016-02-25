package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.Activity;
import android.content.ReceiverCallNotAllowedException;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class WhiskeyCommentListActivity extends Activity {
    private ListView commentsListView;
    private DatabaseHandler db;
    private int whiskeyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiskey_comment_list);

        whiskeyId = getIntent().getIntExtra("whiskeyId", -1);
        commentsListView = (ListView) findViewById(R.id.commentsListView);
        db = new DatabaseHandler(this);

        updateCommentList();
    }

    private void updateCommentList() {
        List<WhiskeyComment> whiskeyComments = db.getWhiskeyComments(whiskeyId);
        db.close();

        if (whiskeyComments.size() == 0) {
            // Indicate to the user if there are no comments.
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.whiskeyCommentsViewLayout);
            layout.removeView(commentsListView);

            TextView noCommentsText = new TextView(this);

            noCommentsText.setText(R.string.no_comments);
            RelativeLayout.LayoutParams layoutParamsTextView = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTextView.addRule(RelativeLayout.CENTER_HORIZONTAL);
            noCommentsText.setLayoutParams(layoutParamsTextView);

            layout.addView(noCommentsText);
        } else {
            WhiskeyComment[] whiskeyCommentsArray = whiskeyComments.toArray(new WhiskeyComment[whiskeyComments.size()]);

            WhiskeyCommentListAdapter commentListAdapter = new WhiskeyCommentListAdapter(this, whiskeyCommentsArray);
            commentsListView.setAdapter(commentListAdapter);
        }
    }
}
