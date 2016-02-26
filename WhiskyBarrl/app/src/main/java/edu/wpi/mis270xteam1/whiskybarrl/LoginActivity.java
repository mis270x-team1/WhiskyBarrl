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
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.buttonLogIn);
        registerButton = (Button) findViewById(R.id.buttonRegister);
        rememberMeCheckbox = (CheckBox) findViewById(R.id.rememberMeCheckbox);
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        getLoginCredentials();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextUsername.setText("");
                editTextPassword.setText("");
                Intent registerIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSuccessfulLogin()) {
                    if (rememberMeCheckbox.isChecked()) {
                        // Save the login information if the user wants it.
                        saveLoginCredentials();
                    } else {
                        // Clear the saved login information if the user does not want it remembered.
                        preferences.edit().clear().apply();
                    }
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainWhiskeyListActivity.class);
                    i.putExtra("username", editTextUsername.getText().toString());
                    startActivity(i);
                } else {
                    // Create an alert dialog and show it to the user.
                    AlertDialog.Builder loginFailedDialog = new AlertDialog.Builder(LoginActivity.this);
                    loginFailedDialog.setTitle("Login Failed");

                    if (allLoginFieldsEntered()) {
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
        });
    }

    private boolean allLoginFieldsEntered() {
        String loginUsername = editTextUsername.getText().toString();
        String loginPassword = editTextPassword.getText().toString();

        return loginUsername.trim().length() > 0 && loginPassword.trim().length() > 0;
    }

    private boolean isSuccessfulLogin() {
        // Get the username/password attempted and compare it to the one in the database.
        String loginUsername = editTextUsername.getText().toString();
        String loginPassword = editTextPassword.getText().toString();

        if (!allLoginFieldsEntered()) {
            return false;
        }

        DatabaseHandler db = new DatabaseHandler(this);
        User loginUser = db.getUser(loginUsername);

        if (loginUser == null) {
            // No user with this username exists, so login failed.
            return false;
        }

        String realUsername = loginUser.getUsername();
        String realPassword = loginUser.getPassword();

        return realUsername.equals(loginUsername) && realPassword.equals(loginPassword);
    }

    private void saveLoginCredentials() {
        String enteredUsername = editTextUsername.getText().toString();
        String enteredPassword = editTextPassword.getText().toString();
        preferences.edit().putString("username", enteredUsername).apply();
        preferences.edit().putString("password", enteredPassword).apply();
        preferences.edit().putBoolean("rememberMe", rememberMeCheckbox.isChecked()).apply();
    }

    private void getLoginCredentials() {
        String savedUsername = preferences.getString("username", "");
        String savedPassword = preferences.getString("password", "");
        boolean rememberMe = preferences.getBoolean("rememberMe", false);

        editTextUsername.setText(savedUsername);
        editTextPassword.setText(savedPassword);
        rememberMeCheckbox.setChecked(rememberMe);
    }
}
