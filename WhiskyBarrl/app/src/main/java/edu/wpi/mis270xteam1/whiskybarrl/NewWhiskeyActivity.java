package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewWhiskeyActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextLocation;
    private EditText editTextAlcCont;
    private EditText editTextDesc;

    private RatingBar ratingBar;
    private Button buttonSub;
    private ImageView newWhiskeyImageView;
    private ImageButton newWhiskeyImageButton;

    private int currentUserId;
    private String currentUsername;
    private String currentImgPath = "";
    private String formattedDate;

    private static final int NEW_WHISKEY_IMG_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_whiskey);

        currentUserId = getIntent().getIntExtra("userId", -1);
        currentUsername = getIntent().getStringExtra("username");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextAlcCont = (EditText) findViewById(R.id.editTextAlcCont);
        editTextDesc = (EditText) findViewById(R.id.editTextDesc);
        newWhiskeyImageView = (ImageView) findViewById(R.id.setWhiskeyImgView);
        newWhiskeyImageButton = (ImageButton) findViewById(R.id.setWhiskeyImgButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        buttonSub = (Button) findViewById(R.id.buttonSub);

        newWhiskeyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCaptureImgActivity();
            }
        });

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
                whiskey.setImgPath(currentImgPath);
                whiskey.setLocation(location);
                whiskey.setProofLevel(alcoholContent);
                whiskey.setDescription(description);
                whiskey.setRating(rating);
                whiskey.setWhiskeyUserId(currentUserId);
                whiskey.setWhiskeyUsername(currentUsername);

                DatabaseHandler dbHandler = new DatabaseHandler(NewWhiskeyActivity.this);
                boolean whiskeyAdded = dbHandler.addWhiskey(whiskey);

                if (whiskeyAdded) {
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable arrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow);
        arrow.mutate();
        getSupportActionBar().setHomeAsUpIndicator(arrow);
        getSupportActionBar().setTitle("Add Whiskey");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_WHISKEY_IMG_REQUEST_CODE && resultCode == RESULT_OK) {
            File extFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File file = null;
            if (extFilesDir != null) {
                file = new File(extFilesDir, "IMG_WHISKEY" + formattedDate + ".jpg");
            }
            if (file != null) {
                Uri imgUri = Uri.fromFile(file);
                newWhiskeyImageView.setImageURI(imgUri);
                currentImgPath = imgUri.toString();
            } else {
                Toast.makeText(
                        NewWhiskeyActivity.this,
                        "An error occurred while updating the image.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
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

    private void startCaptureImgActivity() {
        AlertDialog.Builder obtainImgOptionsDialog = new AlertDialog.Builder(NewWhiskeyActivity.this);
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
                                Calendar currentTime = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddkkmmssSS");
                                formattedDate = df.format(currentTime.getTime());
                                Uri uri = Uri.fromFile(new File(extFilesDir, "IMG_WHISKEY" + formattedDate + ".jpg"));
                                capturePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                startActivityForResult(capturePicIntent, NEW_WHISKEY_IMG_REQUEST_CODE);
                            } else {
                                Toast.makeText(
                                        NewWhiskeyActivity.this,
                                        "An error occurred with the camera.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                        break;
                    case 1:
                        Intent getImgFromGalleryIntent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(getImgFromGalleryIntent, NEW_WHISKEY_IMG_REQUEST_CODE);
                        break;
                    default:
                        break;
                }
            }
        });
        obtainImgOptionsDialog.create().show();
    }
}
