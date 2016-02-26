package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainWhiskeyListActivity extends Fragment {

    private ListView whiskeyListView;
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
        View view = inflater.inflate(R.layout.activity_main_whiskey_list, container, false);
        whiskeyListView = (ListView) view.findViewById(R.id.whiskeyListView);

        loadWhiskeys();

        whiskeyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Whiskey selectedWhiskey = (Whiskey) parent.getItemAtPosition(position);
                int whiskeyId = selectedWhiskey.getId();
                Intent i = new Intent(getActivity(), ViewWhiskey.class);
                i.putExtra("username", currentUsername);
                i.putExtra("whiskeyId", whiskeyId);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        whiskeyListAdapter.notifyDataSetChanged();
    }

    private void loadWhiskeys() {
        // Part of this taken from http://viralpatel.net/blogs/convert-arraylist-to-arrays-in-java/
        List<Whiskey> whiskeys = db.getAllWhiskeys();
        Whiskey[] whiskeyArray = whiskeys.toArray(new Whiskey[whiskeys.size()]);

        whiskeyListAdapter = new WhiskeyListAdapter(getActivity(), whiskeyArray);
        whiskeyListView.setAdapter(whiskeyListAdapter);
    }
}
