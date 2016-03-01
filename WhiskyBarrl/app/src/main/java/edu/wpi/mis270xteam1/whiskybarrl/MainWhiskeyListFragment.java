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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MainWhiskeyListFragment extends Fragment {

    private ListView whiskeyListView;
    private EditText whiskeySearchEditText;
    private DatabaseHandler db;
    private String currentUsername;
    private WhiskeyListAdapter whiskeyListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUsername = getArguments().getString("username");
        db = new DatabaseHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_whiskey_list, container, false);
        whiskeyListView = (ListView) view.findViewById(R.id.whiskeyListView);
        whiskeySearchEditText = (EditText) view.findViewById(R.id.whiskeySearchEditText);
        loadWhiskeys(view);

        whiskeyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Whiskey selectedWhiskey = (Whiskey) parent.getItemAtPosition(position);
                int whiskeyId = selectedWhiskey.getId();
                Intent i = new Intent(getActivity(), ViewWhiskeyActivity.class);
                i.putExtra("username", currentUsername);
                i.putExtra("whiskeyId", whiskeyId);
                startActivity(i);
            }
        });

        whiskeySearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (whiskeyListAdapter != null) {
                    whiskeyListAdapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWhiskeys(getView());
    }

    private void loadWhiskeys(final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // Part of this taken from http://viralpatel.net/blogs/convert-arraylist-to-arrays-in-java/
                List<Whiskey> whiskeys = db.getAllWhiskeys();

                if (whiskeys.size() == 0) {
                    RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.whiskeyViewLayout);
                    layout.removeView(whiskeySearchEditText);
                    layout.removeView(whiskeyListView);

                    TextView noWhiskeysText = new TextView(getActivity());

                    noWhiskeysText.setText(R.string.no_whiskeys);
                    RelativeLayout.LayoutParams layoutParamsTextView = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParamsTextView.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    noWhiskeysText.setLayoutParams(layoutParamsTextView);

                    layout.addView(noWhiskeysText);
                } else {
                    Whiskey[] whiskeyArray = whiskeys.toArray(new Whiskey[whiskeys.size()]);
                    whiskeyListAdapter = new WhiskeyListAdapter(getActivity(), whiskeyArray);
                    whiskeyListView.setAdapter(whiskeyListAdapter);
                }
            }
        });
    }
}
