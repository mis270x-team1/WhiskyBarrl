package edu.wpi.mis270xteam1.whiskybarrl;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class RegistrationActivity extends Activity {

    private EditText editTextUsername;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private EditText editTextPassword;
    private EditText editTextReEnterPassword;
    private EditText editTextGender;
    private EditText editTextCountry;

    private Button registerButton;

    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextFirstName = (EditText) findViewById(R.id.editTextFN);
        editTextLastName = (EditText) findViewById(R.id.editTextLN);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhoneNumber = (EditText) findViewById(R.id.editTextPN);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextReEnterPassword = (EditText) findViewById(R.id.editTextCpass);
        editTextGender = (EditText) findViewById(R.id.editTextGender);
        editTextCountry = (EditText) findViewById(R.id.editTextCountry);

        registerButton = (Button) findViewById(R.id.buttonRegister);

        dbHandler = new DatabaseHandler(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRegistrationInfoValid()) {
                    User user = createNewUser();
                    dbHandler.addUser(user);
                    Toast.makeText(RegistrationActivity.this, "Account successfully registered.", Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = getRegistrationErrorMessage();
                    Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private User createNewUser() {
        String usernameEntry = editTextUsername.getText().toString();
        String firstNameEntry = editTextFirstName.getText().toString();
        String lastNameEntry = editTextLastName.getText().toString();
        String genderEntry = editTextGender.getText().toString();
        String countryEntry = editTextCountry.getText().toString();
        int ageEntry = Integer.parseInt(editTextAge.getText().toString());
        String passwordEntry = editTextPassword.getText().toString();
        String emailEntry = editTextEmail.getText().toString();
        String phoneNumberEntry = editTextPhoneNumber.getText().toString();

        User user = new User();
        user.setUsername(usernameEntry);
        user.setFirstName(firstNameEntry);
        user.setLastName(lastNameEntry);
        user.setGender(genderEntry);
        user.setCountry(countryEntry);
        user.setAge(ageEntry);
        user.setPassword(passwordEntry);
        user.setEmail(emailEntry);
        user.setPhoneNumber(phoneNumberEntry);

        return user;
    }

    private boolean isRegistrationInfoValid() {
        return allFieldsNotEmpty() && isUserNameValid()
                && isPasswordValid() && isAgeValid()
                && isEmailValid() && isPhoneNumberValid();
    }

    private boolean allFieldsNotEmpty() {
        String usernameEntry = editTextUsername.getText().toString();
        String firstNameEntry = editTextFirstName.getText().toString();
        String lastNameEntry = editTextLastName.getText().toString();
        String genderEntry = editTextGender.getText().toString();
        String countryEntry = editTextCountry.getText().toString();
        String ageEntry = editTextAge.getText().toString();
        String emailEntry = editTextEmail.getText().toString();
        String phoneNumberEntry = editTextPhoneNumber.getText().toString();

        return !TextUtils.isEmpty(usernameEntry)
                && !TextUtils.isEmpty(firstNameEntry)
                && !TextUtils.isEmpty(lastNameEntry)
                && !TextUtils.isEmpty(genderEntry)
                && !TextUtils.isEmpty(countryEntry)
                && !TextUtils.isEmpty(ageEntry)
                && !TextUtils.isEmpty(emailEntry)
                && !TextUtils.isEmpty(phoneNumberEntry);
    }

    private boolean isUserNameValid() {
        return dbHandler.getUser(editTextUsername.getText().toString()) == null;
    }

    private boolean isAgeValid() {
        try {
            return Integer.parseInt(editTextAge.getText().toString()) >= 21;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPasswordValid() {
        String initialPWEntry = editTextPassword.getText().toString();
        String reEnterPWEntry = editTextReEnterPassword.getText().toString();

        return initialPWEntry.equals(reEnterPWEntry);
    }

    private boolean isEmailValid() {
        String emailEntry = editTextEmail.getText().toString();
        return Patterns.EMAIL_ADDRESS.matcher(emailEntry).matches();
    }

    private boolean isPhoneNumberValid() {
        String phoneNumberEntry = editTextPhoneNumber.getText().toString();
        return Patterns.PHONE.matcher(phoneNumberEntry).matches();
    }

    private String getRegistrationErrorMessage() {
        if (!allFieldsNotEmpty()) {
            return "One or more fields are blank.";
        } else if (!isUserNameValid()) {
            return "Username is already taken.";
        } else if (!isAgeValid()) {
            return "You must be at least 21 years of age to use this app.";
        } else if (!isPasswordValid()) {
            return "Passwords do not match.";
        } else if (!isEmailValid()) {
            return "You must enter a valid email address.";
        } else if (!isPhoneNumberValid()) {
            return "You must enter a valid phone number.";
        }
        return "";
    }
}
