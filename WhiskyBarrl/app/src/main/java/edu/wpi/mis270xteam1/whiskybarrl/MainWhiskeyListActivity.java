package edu.wpi.mis270xteam1.whiskybarrl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainWhiskeyListActivity extends AppCompatActivity {

    private ListView whiskeyListView;
    private DatabaseHandler db;
    private ArrayAdapter<Whiskey> whiskeyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_whiskey_list);

        whiskeyListView = (ListView) findViewById(R.id.whiskeyListView);
        db = new DatabaseHandler(this);

        loadWhiskeys();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWhiskeys();
    }

    private void loadWhiskeys() {
        List<Whiskey> whiskeys = db.getAllWhiskeys();
        Whiskey[] whiskeyArray = whiskeys.toArray(new Whiskey[whiskeys.size()]);

        whiskeyListAdapter = new WhiskeyListAdapter(this, whiskeyArray);
        whiskeyListView.setAdapter(whiskeyListAdapter);
    }
}
