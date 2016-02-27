package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    private String currentUsername;

    private EditText editTextUsername;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private EditText editTextGender;
    private EditText editTextCountry;
    private EditText editTextChangePassword;
    private EditText editTextConfirmNewPassword;

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
        editTextAge = (EditText) findViewById(R.id.editTextEditAge);
        editTextLastName = (EditText) findViewById(R.id.editTextELN);
        editTextEmail = (EditText) findViewById(R.id.editTextEE);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextEPN);
        editTextGender = (EditText) findViewById(R.id.editTextEG);
        editTextCountry = (EditText) findViewById(R.id.editTextEC);
        editTextChangePassword = (EditText) findViewById(R.id.editTextChangePassword);
        editTextConfirmNewPassword = (EditText) findViewById(R.id.editTextConfirmNewPassword);

        submitChangesButton = (Button) findViewById(R.id.buttonSC);
        db = new DatabaseHandler(this);
        currentUser = db.getUser(currentUsername);

        populateInfoInTextFields();

        submitChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditValid()) {
                    updateUserInformation();
                    db.updateUser(currentUser);
                    Intent data = new Intent();
                    Bundle userBundle = new Bundle();
                    userBundle.putString("newUsername", editTextUsername.getText().toString());
                    userBundle.putString("newFirstName", editTextFirstName.getText().toString());
                    userBundle.putString("newLastName", editTextLastName.getText().toString());
                    userBundle.putInt("newAge", Integer.parseInt(editTextAge.getText().toString()));
                    userBundle.putString("newEmail", editTextEmail.getText().toString());
                    userBundle.putString("newPhoneNumber", editTextPhoneNumber.getText().toString());
                    userBundle.putString("newGender", editTextGender.getText().toString());
                    userBundle.putString("newCountry", editTextCountry.getText().toString());

                    if (!TextUtils.isEmpty(editTextChangePassword.getText().toString())) {
                        userBundle.putString("newPassword", editTextChangePassword.getText().toString());
                    }

                    data.putExtras(userBundle);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    String error = getUpdateUserErrorMessage();
                    Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEditValid() {
        return isNewAgeValid() && isNewUsernameValid() && newPasswordsMatch();
    }

    private boolean isNewAgeValid() {
        int enteredAge = Integer.parseInt(editTextAge.getText().toString());
        return enteredAge >= 21;
    }

    private void populateInfoInTextFields() {
        // Populate the EditText widgets with the current user information.
        editTextUsername.setText(currentUsername);
        editTextFirstName.setText(currentUser.getFirstName());
        editTextLastName.setText(currentUser.getLastName());
        editTextAge.setText(Integer.toString(currentUser.getAge()));
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

    private boolean newPasswordsMatch() {
        String newPassword = editTextChangePassword.getText().toString();
        String confirmNewPassword = editTextConfirmNewPassword.getText().toString();

        return newPassword.equals(confirmNewPassword)
                || (TextUtils.isEmpty(newPassword) && TextUtils.isEmpty(confirmNewPassword));
    }

    private boolean isNewEmailValid() {
        String emailEntry = editTextEmail.getText().toString();
        return Patterns.EMAIL_ADDRESS.matcher(emailEntry).matches();
    }

    private boolean isNewPhoneNumberValid() {
        String phoneNumberEntry = editTextPhoneNumber.getText().toString();
        return Patterns.PHONE.matcher(phoneNumberEntry).matches();
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

    private String getUpdateUserErrorMessage() {
        if (!isNewUsernameValid()) {
            return "Username is already taken.";
        } else if (!isNewAgeValid()) {
            return "You must be at least 21 years of age to use this app.";
        } else if (!newPasswordsMatch()) {
            return "Passwords do not match.";
        } else if (!isNewEmailValid()) {
            return "You must enter a valid email address.";
        } else if (!isNewPhoneNumberValid()) {
            return "You must enter a valid phone number.";
        }
        return "";
    }
}
