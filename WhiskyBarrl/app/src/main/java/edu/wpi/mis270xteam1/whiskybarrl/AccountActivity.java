package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {
    String currentUsername;
    User currentUser;
    DatabaseHandler db;

    Button editProfileButton;
    TextView textViewFullName;
    TextView textViewUsername;
    TextView textViewEmail;
    TextView textViewPhoneNumber;
    TextView textViewGender;
    TextView textViewCountry;

    private static final int UPDATE_USER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        currentUsername = getIntent().getStringExtra("username");
        db = new DatabaseHandler(this);
        currentUser = db.getUser(currentUsername);

        editProfileButton = (Button) findViewById(R.id.buttonEP);
        textViewFullName = (TextView) findViewById(R.id.textViewFullName);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewPhoneNumber = (TextView) findViewById(R.id.textViewPN);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);

        showUserInfo();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountActivity.this, EditProfile.class);
                i.putExtra("username", currentUsername);
                startActivityForResult(i, UPDATE_USER_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_USER_REQUEST) {
            String newUsername = data.getStringExtra("newUsername");
            currentUser = db.getUser(newUsername);
            currentUsername = currentUser.getUsername();
            showUserInfo();
        }
    }

    private void showUserInfo() {
        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        textViewFullName.setText(fullName);
        textViewUsername.setText(currentUsername);
        textViewEmail.setText(currentUser.getEmail());
        textViewPhoneNumber.setText(currentUser.getPhoneNumber());
        textViewGender.setText(currentUser.getGender());
        textViewCountry.setText(currentUser.getCountry());
    }
}
