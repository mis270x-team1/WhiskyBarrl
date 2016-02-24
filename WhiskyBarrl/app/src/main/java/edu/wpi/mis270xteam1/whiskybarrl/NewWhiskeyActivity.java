package edu.wpi.mis270xteam1.whiskybarrl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

public class NewWhiskeyActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextLocation;
    private EditText editTextAlcCont;
    private EditText editTextDesc;

    private ImageButton imageButton;
    private RatingBar ratingBar;
    private Button buttonSub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_whiskey);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextAlcCont = (EditText) findViewById(R.id.editTextAlcCont);
        editTextDesc = (EditText) findViewById(R.id.editTextDesc);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        buttonSub = (Button) findViewById(R.id.buttonSub);

        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String location = editTextLocation.getText().toString();
                int alcoholContent = Integer.parseInt(editTextAlcCont.getText().toString());
                String description = editTextDesc.getText().toString();
                float rating = ratingBar.getRating();

                Whiskey whiskey = new Whiskey();
                whiskey.setName(name);
                whiskey.setLocation(location);
                whiskey.setProofLevel(alcoholContent);
                whiskey.setDescription(description);
                whiskey.setRating(rating);

                DatabaseHandler dbHandler = new DatabaseHandler(NewWhiskeyActivity.this);
                boolean whiskeyAdded = dbHandler.addWhiskey(whiskey);

                if (whiskeyAdded) {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_whiskey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
