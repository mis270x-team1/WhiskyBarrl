package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

public class EditProfileActivity extends AppCompatActivity {

    private String currentUsername;

    private EditText editTextUsername;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private Spinner spinnerGender;
    private EditText editTextCountry;
    private EditText editTextChangePassword;
    private EditText editTextConfirmNewPassword;
    private Button submitChangesButton;
    private ImageView currentProfilePic;
    private ImageButton changeProfilePicButton;
    private String newImgPath;
    private String currentGender;
    private ArrayAdapter<CharSequence> genderAdapter;

    private static final int NEW_PROFILE_IMAGE_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

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
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        editTextCountry = (EditText) findViewById(R.id.editTextEC);
        editTextChangePassword = (EditText) findViewById(R.id.editTextChangePassword);
        editTextConfirmNewPassword = (EditText) findViewById(R.id.editTextConfirmNewPassword);
        submitChangesButton = (Button) findViewById(R.id.buttonSC);
        currentProfilePic = (ImageView) findViewById(R.id.editProfilePicImgView);
        changeProfilePicButton = (ImageButton) findViewById(R.id.changeProfilePicButton);

        db = new DatabaseHandler(this);
        currentUser = db.getUser(currentUsername);

        genderAdapter = ArrayAdapter.createFromResource(this, R.array.genders_array,
                android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCaptureImgActivity();
            }
        });

        submitChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditValid()) {
                    updateUserInformation();
                    db.updateUser(currentUser);
                    Intent data = new Intent();
                    Bundle userBundle = new Bundle();
                    userBundle.putString("newUsername", editTextUsername.getText().toString());
                    userBundle.putString("newPassword", editTextChangePassword.getText().toString());
                    userBundle.putString("newFirstName", editTextFirstName.getText().toString());
                    userBundle.putString("newLastName", editTextLastName.getText().toString());
                    userBundle.putString("newImgPath", newImgPath);
                    userBundle.putInt("newAge", Integer.parseInt(editTextAge.getText().toString()));
                    userBundle.putString("newEmail", editTextEmail.getText().toString());
                    userBundle.putString("newPhoneNumber", editTextPhoneNumber.getText().toString());
                    userBundle.putString("newGender", currentGender);
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

        populateInfoFields();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable arrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow);
        arrow.mutate();
        getSupportActionBar().setHomeAsUpIndicator(arrow);
        getSupportActionBar().setTitle("Edit Profile");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == NEW_PROFILE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
                File extFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = null;
                if (extFilesDir != null) {
                    file = new File(extFilesDir, currentUsername + currentUser.getId() + ".jpg");
                }
                if (file != null) {
                    Uri imgUri = Uri.fromFile(file);
                    currentProfilePic.setImageURI(imgUri);
                    newImgPath = imgUri.toString();
                } else {
                    Toast.makeText(
                            EditProfileActivity.this,
                            "An error occurred while updating the image.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(
                    EditProfileActivity.this,
                    "An error occurred trying to fetch the image.",
                    Toast.LENGTH_SHORT
            ).show();
            e.printStackTrace();
        }
    }

    private boolean isEditValid() {
        return isNewAgeValid() && isNewUsernameValid()
                && newPasswordsMatch() && !currentGender.contains("Select One");
    }

    private boolean isNewAgeValid() {
        int enteredAge = Integer.parseInt(editTextAge.getText().toString());
        return enteredAge >= 21;
    }

    private void startCaptureImgActivity() {
        /*if (ContextCompat.checkSelfPermission(EditProfileActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditProfileActivity.this,
                    new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }*/

        AlertDialog.Builder obtainImgOptionsDialog = new AlertDialog.Builder(EditProfileActivity.this);
        String[] options = new String[] {"From Camera", "From Gallery"};

        obtainImgOptionsDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent capturePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (capturePicIntent.resolveActivity(getPackageManager()) != null) {
                            File extFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            if (extFilesDir != null) {
                                Uri uri = Uri.fromFile(new File(extFilesDir, currentUsername + currentUser.getId() + ".jpg"));
                                capturePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                startActivityForResult(capturePicIntent, NEW_PROFILE_IMAGE_REQUEST_CODE);
                            } else {
                                Toast.makeText(
                                        EditProfileActivity.this,
                                        "An error occurred with the camera.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                        break;
                    case 1:
                        Intent getImgFromGalleryIntent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(getImgFromGalleryIntent, NEW_PROFILE_IMAGE_REQUEST_CODE);
                        break;
                    default:
                        break;
                }
            }
        });
        obtainImgOptionsDialog.create().show();
    }

    private void populateInfoFields() {
        // Populate the EditText widgets with the current user information.
        editTextUsername.setText(currentUsername);
        editTextFirstName.setText(currentUser.getFirstName());
        editTextLastName.setText(currentUser.getLastName());

        if (!"".equals(currentUser.getImgPath())) {
            currentProfilePic.setImageURI(Uri.parse(currentUser.getImgPath()));
        }

        editTextAge.setText(Integer.toString(currentUser.getAge()));
        editTextEmail.setText(currentUser.getEmail());
        editTextPhoneNumber.setText(currentUser.getPhoneNumber());

        int selectionPosition = genderAdapter.getPosition(currentUser.getGender());
        spinnerGender.setSelection(selectionPosition);

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
        String newPassword = editTextChangePassword.getText().toString();
        String enteredEmail = editTextEmail.getText().toString();
        String enteredPhoneNumber = editTextPhoneNumber.getText().toString();
        String enteredCountry = editTextCountry.getText().toString();

        currentUsername = enteredUsername;
        currentUser.setUsername(enteredUsername);

        if (!TextUtils.isEmpty(newPassword)) {
            currentUser.setPassword(newPassword);
        }

        currentUser.setFirstName(enteredFirstName);
        currentUser.setLastName(enteredLastName);

        if (newImgPath != null) {
            currentUser.setImgPath(newImgPath);
        }

        currentUser.setEmail(enteredEmail);
        currentUser.setPhoneNumber(enteredPhoneNumber);
        currentUser.setGender(currentGender);
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
        } else if (currentGender.contains("Select One")) {
            return "You must enter a gender.";
        }
        return "";
    }
}
