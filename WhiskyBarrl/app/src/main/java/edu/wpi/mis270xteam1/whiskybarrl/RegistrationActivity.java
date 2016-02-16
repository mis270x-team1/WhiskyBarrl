package edu.wpi.mis270xteam1.whiskybarrl;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

    EditText editTextUsername;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextAge;
    EditText editTextEmail;
    EditText editTextPhoneNumber;
    EditText editTextPassword;
    EditText editTextReEnterPassword;
    EditText editTextGender;
    EditText editTextCountry;

    Button registerButton;

    DatabaseHandler dbHandler;

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

                    Toast.makeText(
                            RegistrationActivity.this,
                            "Account successfully registered.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            RegistrationActivity.this,
                            "Unable to create a user with the given information.",
                            Toast.LENGTH_SHORT).show();
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
        String usernameEntry = editTextUsername.getText().toString();
        String firstNameEntry = editTextFirstName.getText().toString();
        String lastNameEntry = editTextLastName.getText().toString();
        String genderEntry = editTextGender.getText().toString();
        String countryEntry = editTextCountry.getText().toString();

        return isPasswordValid() && isAgeValid() && isEmailValid()
                && isPhoneNumberValid() && !TextUtils.isEmpty(usernameEntry)
                && !TextUtils.isEmpty(firstNameEntry)
                && !TextUtils.isEmpty(lastNameEntry)
                && !TextUtils.isEmpty(genderEntry)
                && !TextUtils.isEmpty(countryEntry);
    }

    private boolean isAgeValid() {
        String ageEntry = editTextAge.getText().toString();
        return Integer.parseInt(ageEntry) >= 21
                && !TextUtils.isEmpty(ageEntry);
    }

    private boolean isPasswordValid() {
        String initialPWEntry = editTextPassword.getText().toString();
        String reEnterPWEntry = editTextReEnterPassword.getText().toString();

        return !TextUtils.isEmpty(initialPWEntry)
                && !TextUtils.isEmpty(reEnterPWEntry)
                && initialPWEntry.equals(reEnterPWEntry);
    }

    private boolean isEmailValid() {
        String emailEntry = editTextEmail.getText().toString();
        return Patterns.EMAIL_ADDRESS.matcher(emailEntry).matches()
                && !TextUtils.isEmpty(emailEntry);
    }

    private boolean isPhoneNumberValid() {
        String phoneNumberEntry = editTextPhoneNumber.getText().toString();
        return Patterns.PHONE.matcher(phoneNumberEntry).matches()
                && !TextUtils.isEmpty(phoneNumberEntry);
    }

}
