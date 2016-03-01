package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;
    private Button registerButton;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String savedUsername = preferences.getString("username", "");
        String savedPassword = preferences.getString("password", "");

        // Attempt an automatic login if there are saved preferences.
        if (!("".equals(savedUsername) && "".equals(savedPassword))) {
            attemptLogin(savedUsername, savedPassword, true);
        }

        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.buttonLogIn);
        registerButton = (Button) findViewById(R.id.buttonRegister);
        rememberMeCheckbox = (CheckBox) findViewById(R.id.rememberMeCheckbox);

        getLoginCredentials();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextUsername.setText("");
                editTextPassword.setText("");
                Intent registerIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registerIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginUsername = editTextUsername.getText().toString();
                String loginPassword = editTextPassword.getText().toString();
                boolean rememberCredentials = rememberMeCheckbox.isChecked();
                attemptLogin(loginUsername, loginPassword, rememberCredentials);
            }
        });
    }

    private void attemptLogin(String username, String password, boolean rememberCredentials) {
        if (isSuccessfulLogin(username, password)) {
            if (rememberCredentials) {
                // Save the login information if the user wants it.
                saveLoginCredentials(username, password);
            } else {
                // Clear the saved login information if the user does not want it remembered.
                preferences.edit().clear().apply();
            }

            Intent i = new Intent(LoginActivity.this, MainTabbedActivity.class);
            i.putExtra("username", username);
            startActivity(i);
            finish();
        } else {
            // Create an alert dialog and show it to the user.
            AlertDialog.Builder loginFailedDialog = new AlertDialog.Builder(LoginActivity.this);
            loginFailedDialog.setTitle("Login Failed");

            if (allLoginFieldsEntered(username, password)) {
                loginFailedDialog.setMessage("Username/password combination does not exist");
            } else {
                loginFailedDialog.setMessage("Please enter a username and password");
            }

            loginFailedDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
    }

    private boolean allLoginFieldsEntered(String username, String password) {
        return username.trim().length() > 0 && password.trim().length() > 0;
    }

    private boolean isSuccessfulLogin(String username, String password) {
        // Get the username/password attempted and compare it to the one in the database.
        if (!allLoginFieldsEntered(username, password)) {
            return false;
        }

        DatabaseHandler db = new DatabaseHandler(this);
        User loginUser = db.getUser(username);

        if (loginUser == null) {
            // No user with this username exists, so login failed.
            return false;
        }

        String realUsername = loginUser.getUsername();
        String realPassword = loginUser.getPassword();

        return realUsername.equals(username) && realPassword.equals(password);
    }

    private void saveLoginCredentials(String username, String password) {
        preferences.edit().putString("username", username).apply();
        preferences.edit().putString("password", password).apply();
    }

    private void getLoginCredentials() {
        String savedUsername = preferences.getString("username", "");
        String savedPassword = preferences.getString("password", "");

        editTextUsername.setText(savedUsername);
        editTextPassword.setText(savedPassword);
        rememberMeCheckbox.setChecked(true);
    }
}
