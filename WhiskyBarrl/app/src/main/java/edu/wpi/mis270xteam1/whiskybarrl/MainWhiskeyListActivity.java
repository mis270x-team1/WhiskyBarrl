package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainWhiskeyListActivity extends AppCompatActivity {

    private ListView whiskeyListView;
    private DatabaseHandler db;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_whiskey_list);

        currentUsername = getIntent().getStringExtra("username");
        whiskeyListView = (ListView) findViewById(R.id.whiskeyListView);
        db = new DatabaseHandler(this);

        loadWhiskeys();

        whiskeyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Whiskey selectedWhiskey = (Whiskey) parent.getItemAtPosition(position);
                int whiskeyId = selectedWhiskey.getId();
                Intent i = new Intent(MainWhiskeyListActivity.this, ViewWhiskey.class);
                i.putExtra("username", currentUsername);
                i.putExtra("whiskeyId", whiskeyId);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_whiskey_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_new_whiskey) {
            // Start a new activity to add a whiskey
            Intent i = new Intent(MainWhiskeyListActivity.this, NewWhiskeyActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWhiskeys();
    }

    private void loadWhiskeys() {
        // Part of this taken from http://viralpatel.net/blogs/convert-arraylist-to-arrays-in-java/
        List<Whiskey> whiskeys = db.getAllWhiskeys();
        Whiskey[] whiskeyArray = whiskeys.toArray(new Whiskey[whiskeys.size()]);

        ArrayAdapter<Whiskey> whiskeyListAdapter = new WhiskeyListAdapter(this, whiskeyArray);
        whiskeyListView.setAdapter(whiskeyListAdapter);
    }
}
