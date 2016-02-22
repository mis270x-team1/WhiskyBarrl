package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.buttonLogIn);
        registerButton = (Button) findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainMenuActivity.this, RegistrationActivity.class);
                startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(isSuccessfulLogin());
                System.out.println(editTextUsername.getText().toString());
                System.out.println(editTextPassword.getText().toString());

                if (isSuccessfulLogin()) {
                    Toast.makeText(MainMenuActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainMenuActivity.this, NewWhiskeyActivity.class);
                    startActivity(i);
                } else {
                    AlertDialog.Builder loginFailedDialog = new AlertDialog.Builder(MainMenuActivity.this);
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
                    });
                    loginFailedDialog.create().show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
