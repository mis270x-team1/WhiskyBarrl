package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ViewWhiskeyActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewLocation;
    private TextView textViewAlcCont;
    private TextView textViewDescription;
    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitCommentButton;
    private Button viewCommentsButton;
    private Button favoriteButton;
    private ImageView viewWhiskeyImg;

    private int whiskeyId;
    private String currentUsername;
    private User currentUser;
    private Whiskey whiskey;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_whiskey);

        currentUsername = getIntent().getStringExtra("username");
        whiskeyId = getIntent().getIntExtra("whiskeyId", -1);
        db = new DatabaseHandler(this);
        currentUser = db.getUser(currentUsername);
        whiskey = db.getWhiskey(whiskeyId);

        textViewName = (TextView) findViewById(R.id.viewWhiskeyNameText);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        textViewAlcCont = (TextView) findViewById(R.id.textViewAC);
        textViewDescription = (TextView) findViewById(R.id.viewWhiskeyDescText);
        ratingBar = (RatingBar) findViewById(R.id.viewWhiskeyRatingBar);
        commentEditText = (EditText) findViewById(R.id.editTextEnterComment);
        submitCommentButton = (Button) findViewById(R.id.commentSubmitButton);
        viewCommentsButton = (Button) findViewById(R.id.viewCommentsListButton);
        favoriteButton = (Button) findViewById(R.id.addFavoriteButton);
        viewWhiskeyImg = (ImageView) findViewById(R.id.viewWhiskeyImg);

        ratingBar.setFocusable(false);

        populateInformationFields();

        submitCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString();
                if (TextUtils.isEmpty(commentText)) {
                    Toast.makeText(ViewWhiskeyActivity.this, "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    WhiskeyComment whiskeyComment = new WhiskeyComment();
                    whiskeyComment.setUserId(db.getUser(currentUsername).getId());
                    whiskeyComment.setWhiskeyId(whiskeyId);
                    whiskeyComment.setCommentText(commentEditText.getText().toString());
                    db.addWhiskeyComment(whiskeyComment);
                    db.close();
                    Toast.makeText(ViewWhiskeyActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                    commentEditText.setText("");
                }
            }
        });

        viewCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewWhiskeyActivity.this, WhiskeyCommentListActivity.class);
                i.putExtra("whiskeyId", whiskeyId);
                startActivity(i);
            }
        });

        if (db.isFavorite(currentUser, whiskey)) {
            setFavoriteButtonToDelete();
        } else {
            setFavoriteButtonToAdd();
        }
    }

    private void populateInformationFields() {
        textViewName.setText(whiskey.getName());

        if (!"".equals(whiskey.getImgPath())) {
            viewWhiskeyImg.setImageURI(Uri.fromFile(new File(whiskey.getImgPath())));
        }

        textViewLocation.setText(whiskey.getLocation());
        textViewAlcCont.setText(Integer.toString(whiskey.getProofLevel()));
        textViewDescription.setText(whiskey.getDescription());
        ratingBar.setRating(whiskey.getRating());
    }

    private void setFavoriteButtonToAdd() {
        favoriteButton.setText(R.string.add_to_favorites);
        favoriteButton.setOnClickListener(addFavoriteListener());
    }

    private void setFavoriteButtonToDelete() {
        favoriteButton.setText(R.string.remove_from_favorites);
        favoriteButton.setOnClickListener(deleteFavoriteListener());
    }

    private View.OnClickListener addFavoriteListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = db.getUser(currentUsername);
                db.addFavorite(user, whiskey);
                Toast.makeText(
                        ViewWhiskeyActivity.this,
                        "Added " + whiskey.getName() + " to Favorites",
                        Toast.LENGTH_SHORT
                ).show();

                // Change the button functionality to remove a favorite.
                setFavoriteButtonToDelete();
            }
        };
    }

    private View.OnClickListener deleteFavoriteListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteFavorite(currentUser, whiskey);
                Toast.makeText(
                        ViewWhiskeyActivity.this,
                        "Removed " + whiskey.getName() + " from Favorites",
                        Toast.LENGTH_SHORT
                ).show();

                // Change the button functionality to add a favorite.
                setFavoriteButtonToAdd();
            }
        };
    }
}
