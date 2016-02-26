package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class UserFavoritesActivity extends Activity {
    private ListView userFavoritesListView;
    private DatabaseHandler db;
    private String username;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favorites);

        username = getIntent().getStringExtra("username");

        userFavoritesListView = (ListView) findViewById(R.id.userFavoritesListView);
        db = new DatabaseHandler(this);
        user = db.getUser(username);

        updateFavoritesList();
    }

    private void updateFavoritesList() {
        List<Whiskey> favoriteWhiskeys = db.getUserFavorites(user);
        db.close();

        if (favoriteWhiskeys.size() == 0) {
            // Indicate to the user if there are no comments.
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.userFavoritesViewLayout);
            layout.removeView(userFavoritesListView);

            TextView noFavoritesText = new TextView(this);

            noFavoritesText.setText(R.string.no_favorites);
            RelativeLayout.LayoutParams layoutParamsTextView = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTextView.addRule(RelativeLayout.CENTER_HORIZONTAL);
            noFavoritesText.setLayoutParams(layoutParamsTextView);

            layout.addView(noFavoritesText);
        } else {
            Whiskey[] favoriteWhiskeysArray = favoriteWhiskeys.toArray(new Whiskey[favoriteWhiskeys.size()]);

            WhiskeyListAdapter favoriteWhiskeyListAdapter = new WhiskeyListAdapter(this, favoriteWhiskeysArray);
            userFavoritesListView.setAdapter(favoriteWhiskeyListAdapter);
        }
    }

}
