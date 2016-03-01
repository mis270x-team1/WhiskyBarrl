package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class UserFavoritesFragment extends Fragment {
    private ListView userFavoritesListView;
    private DatabaseHandler db;
    private String username;
    private User user;
    private EditText favoriteSearchEditText;
    private WhiskeyListAdapter favoriteWhiskeyListAdapter;
    private List<Whiskey> favoriteWhiskeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getArguments().getString("username");

        db = new DatabaseHandler(getActivity());
        user = db.getUser(username);
        favoriteWhiskeys = db.getUserFavorites(user);
        Whiskey[] favoriteWhiskeysArray = favoriteWhiskeys.toArray(new Whiskey[favoriteWhiskeys.size()]);
        favoriteWhiskeyListAdapter = new WhiskeyListAdapter(getActivity(), favoriteWhiskeysArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_favorites, container, false);
        userFavoritesListView = (ListView) view.findViewById(R.id.userFavoritesListView);
        userFavoritesListView.setTextFilterEnabled(true);
        favoriteSearchEditText = (EditText) view.findViewById(R.id.favoriteSearchEditText);

        userFavoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Whiskey selectedWhiskey = (Whiskey) parent.getItemAtPosition(position);
                int whiskeyId = selectedWhiskey.getId();
                Intent i = new Intent(getActivity(), ViewWhiskeyActivity.class);
                i.putExtra("username", username);
                i.putExtra("whiskeyId", whiskeyId);
                startActivity(i);
            }
        });

        favoriteSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (favoriteWhiskeyListAdapter != null) {
                    favoriteWhiskeyListAdapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        updateFavoritesList(view, favoriteWhiskeyListAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteWhiskeys = db.getUserFavorites(user);
        Whiskey[] favoriteWhiskeysArray = favoriteWhiskeys.toArray(new Whiskey[favoriteWhiskeys.size()]);
        favoriteWhiskeyListAdapter = new WhiskeyListAdapter(getActivity(), favoriteWhiskeysArray);
        updateFavoritesList(getView(), favoriteWhiskeyListAdapter);
    }

    private void updateFavoritesList(final View view, final WhiskeyListAdapter adapter) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (favoriteWhiskeys.size() == 0) {
                    // Indicate to the user if there are no comments.
                    RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.userFavoritesViewLayout);
                    layout.removeView(favoriteSearchEditText);
                    layout.removeView(userFavoritesListView);

                    TextView noFavoritesText = new TextView(getActivity());

                    noFavoritesText.setText(R.string.no_favorites);
                    RelativeLayout.LayoutParams layoutParamsTextView = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParamsTextView.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    noFavoritesText.setLayoutParams(layoutParamsTextView);

                    layout.addView(noFavoritesText);
                } else {
                    userFavoritesListView.setAdapter(adapter);
                }
            }
        });
    }

}
