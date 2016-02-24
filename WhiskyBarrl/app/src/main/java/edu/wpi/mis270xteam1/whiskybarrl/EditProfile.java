package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends Activity {

    private String currentUsername;

    private EditText editTextUsername;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private EditText editTextGender;
    private EditText editTextCountry;

    private Button submitChangesButton;
    private DatabaseHandler db;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        currentUsername = getIntent().getStringExtra("username");

        editTextUsername = (EditText) findViewById(R.id.editTextEU);
        editTextFirstName = (EditText) findViewById(R.id.editTextEFN);
        editTextLastName = (EditText) findViewById(R.id.editTextELN);
        editTextEmail = (EditText) findViewById(R.id.editTextEE);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextEPN);
        editTextGender = (EditText) findViewById(R.id.editTextEG);
        editTextCountry = (EditText) findViewById(R.id.editTextEC);

        submitChangesButton = (Button) findViewById(R.id.buttonSC);
        db = new DatabaseHandler(this);
        currentUser = db.getUser(currentUsername);

        populateInfoInTextFields();

        submitChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewUsernameValid()) {
                    updateUserInformation();
                    db.updateUser(currentUser);
                    Intent data = new Intent();
                    data.putExtra("newUsername", editTextUsername.getText().toString());
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(EditProfile.this, "Username is already taken.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateInfoInTextFields() {
        // Populate the EditText widgets with the current user information.
        editTextUsername.setText(currentUsername);
        editTextFirstName.setText(currentUser.getFirstName());
        editTextLastName.setText(currentUser.getLastName());
        editTextEmail.setText(currentUser.getEmail());
        editTextPhoneNumber.setText(currentUser.getPhoneNumber());
        editTextGender.setText(currentUser.getGender());
        editTextCountry.setText(currentUser.getCountry());
    }

    private boolean isNewUsernameValid() {
        String updatedUsername = editTextUsername.getText().toString();
        boolean validNewUsername = db.getUser(updatedUsername) == null
                && !currentUsername.equals(updatedUsername.trim());
        boolean unchangedUsername = currentUsername.equals(updatedUsername.trim());
        return validNewUsername || unchangedUsername;
    }

    private void updateUserInformation() {
        String enteredUsername = editTextUsername.getText().toString();
        String enteredFirstName = editTextFirstName.getText().toString();
        String enteredLastName = editTextLastName.getText().toString();
        String enteredEmail = editTextEmail.getText().toString();
        String enteredPhoneNumber = editTextPhoneNumber.getText().toString();
        String enteredGender = editTextGender.getText().toString();
        String enteredCountry = editTextCountry.getText().toString();

        currentUsername = enteredUsername;
        currentUser.setUsername(enteredUsername);
        currentUser.setFirstName(enteredFirstName);
        currentUser.setLastName(enteredLastName);
        currentUser.setEmail(enteredEmail);
        currentUser.setPhoneNumber(enteredPhoneNumber);
        currentUser.setGender(enteredGender);
        currentUser.setCountry(enteredCountry);
    }
}
