package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewWhiskey extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewLocation;
    private TextView textViewAlcCont;
    private TextView textViewDescription;
    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitCommentButton;
    private Button viewCommentsButton;

    private int whiskeyId;
    private String currentUsername;
    private Whiskey whiskey;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_whiskey);

        currentUsername = getIntent().getStringExtra("username");
        whiskeyId = getIntent().getIntExtra("whiskeyId", -1);
        db = new DatabaseHandler(this);
        whiskey = db.getWhiskey(whiskeyId);

        textViewName = (TextView) findViewById(R.id.viewWhiskeyNameText);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        textViewAlcCont = (TextView) findViewById(R.id.textViewAC);
        textViewDescription = (TextView) findViewById(R.id.viewWhiskeyDescText);
        ratingBar = (RatingBar) findViewById(R.id.viewWhiskeyRatingBar);
        commentEditText = (EditText) findViewById(R.id.editTextEnterComment);
        submitCommentButton = (Button) findViewById(R.id.commentSubmitButton);
        viewCommentsButton = (Button) findViewById(R.id.viewCommentsListButton);

        ratingBar.setFocusable(false);

        populateInformationFields();

        submitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString();
                if (TextUtils.isEmpty(commentText)) {
                    Toast.makeText(ViewWhiskey.this, "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    WhiskeyComment whiskeyComment = new WhiskeyComment();
                    whiskeyComment.setUserId(db.getUser(currentUsername).getId());
                    whiskeyComment.setWhiskeyId(whiskeyId);
                    whiskeyComment.setCommentText(commentEditText.getText().toString());
                    db.addWhiskeyComment(whiskeyComment);
                    db.close();
                    Toast.makeText(ViewWhiskey.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                    commentEditText.setText("");
                }
            }
        });

        viewCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewWhiskey.this, WhiskeyCommentListActivity.class);
                i.putExtra("whiskeyId", whiskeyId);
                startActivity(i);
            }
        });
    }

    private void populateInformationFields() {
        textViewName.setText(whiskey.getName());
        textViewLocation.setText(whiskey.getLocation());
        textViewAlcCont.setText(Integer.toString(whiskey.getProofLevel()));
        textViewDescription.setText(whiskey.getDescription());
        ratingBar.setRating(whiskey.getRating());
    }
}
