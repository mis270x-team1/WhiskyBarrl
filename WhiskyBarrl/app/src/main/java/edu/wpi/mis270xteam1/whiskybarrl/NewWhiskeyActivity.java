package edu.wpi.mis270xteam1.whiskybarrl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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

    private static final int NEW_WHISKEY_IMG_REQUEST_CODE = 1;
    private static final int GET_WHISKEY_IMG_FROM_GALLERY_REQUEST_CODE = 2;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_WHISKEY_IMG_REQUEST_CODE) {
                Bitmap imgBitmap = (Bitmap) data.getExtras().get("data");
                newWhiskeyImageView.setImageBitmap(imgBitmap);
                currentImgPath = getRealUriPath(getImgUri(getApplicationContext(), imgBitmap));
            } else if (requestCode == GET_WHISKEY_IMG_FROM_GALLERY_REQUEST_CODE) {
                Cursor c = null;
                try {
                    Uri imgUri = data.getData();
                    String [] pathColumn = { MediaStore.Images.Media.DATA };
                    c = getContentResolver().query(imgUri, pathColumn, null, null, null);
                    if (c.moveToFirst()) {
                        String imgString = c.getString(c.getColumnIndex(pathColumn[0]));
                        newWhiskeyImageView.setImageBitmap(BitmapFactory.decodeFile(imgString));
                        currentImgPath = getRealUriPath(imgUri);
                    }
                } catch (Exception e) {
                    Toast.makeText(
                            NewWhiskeyActivity.this,
                            "An error occurred trying to fetch the image.",
                            Toast.LENGTH_SHORT).show();
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_whiskey, menu);
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

    private Uri getImgUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private String getRealUriPath(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        int index = c.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return c.getString(index);
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
                            startActivityForResult(capturePicIntent, NEW_WHISKEY_IMG_REQUEST_CODE);
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
