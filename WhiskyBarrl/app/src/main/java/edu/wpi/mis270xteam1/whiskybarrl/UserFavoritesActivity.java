package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class UserFavoritesActivity extends Fragment {
    private ListView userFavoritesListView;
    private DatabaseHandler db;
    private String username;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getArguments().getString("username");

        db = new DatabaseHandler(getActivity());
        user = db.getUser(username);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_favorites, container, false);
        userFavoritesListView = (ListView) view.findViewById(R.id.userFavoritesListView);
        populateFavoritesList(view);

        userFavoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Whiskey selectedWhiskey = (Whiskey) parent.getItemAtPosition(position);
                int whiskeyId = selectedWhiskey.getId();
                Intent i = new Intent(getActivity(), ViewWhiskey.class);
                i.putExtra("username", username);
                i.putExtra("whiskeyId", whiskeyId);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateFavoritesList(getView());
    }

    private void populateFavoritesList(View view) {
        List<Whiskey> favoriteWhiskeys = db.getUserFavorites(user);

        if (favoriteWhiskeys.size() == 0) {
            // Indicate to the user if there are no comments.
            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.userFavoritesViewLayout);
            layout.removeView(userFavoritesListView);

            TextView noFavoritesText = new TextView(getActivity());

            noFavoritesText.setText(R.string.no_favorites);
            RelativeLayout.LayoutParams layoutParamsTextView = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTextView.addRule(RelativeLayout.CENTER_HORIZONTAL);
            noFavoritesText.setLayoutParams(layoutParamsTextView);

            layout.addView(noFavoritesText);
        } else {
            Whiskey[] favoriteWhiskeysArray = favoriteWhiskeys.toArray(new Whiskey[favoriteWhiskeys.size()]);

            WhiskeyListAdapter favoriteWhiskeyListAdapter = new WhiskeyListAdapter(getActivity(), favoriteWhiskeysArray);
            userFavoritesListView.setAdapter(favoriteWhiskeyListAdapter);
        }
    }

}
