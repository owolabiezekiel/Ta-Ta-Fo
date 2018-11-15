package klover.com.ta_ta_fo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    private EditText crimeDesc;
    public Spinner agency_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        crimeDesc = findViewById(R.id.editText);


        dispatchTakePictureIntent();

        agency_spinner = findViewById(R.id.agency);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> agencies = ArrayAdapter.createFromResource(this,
                R.array.agencies, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        agencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        agency_spinner.setAdapter(agencies);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context = getApplicationContext();
        try {
            // Check which request we're responding to and make sure the request was successful
            if (requestCode == REQUEST_TAKE_PHOTO) {
                // The user took a picture
                if (resultCode == RESULT_OK) {
                    ImageView incidentPreview = findViewById(R.id.crime_image);
                    Glide.with(this).load(mCurrentPhotoPath).into(incidentPreview);

                } else {
                    // User Cancelled the action
                    CharSequence text = "Please take the picture again!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast.makeText(context, text, duration).show();

                }

            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "klover.com.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    public void confirmInfo(View view) {
        Intent intent = new Intent(ReportActivity.this, FinalConfirmation.class);
        intent.putExtra("image", mCurrentPhotoPath);
        intent.putExtra("description", crimeDesc.getText().toString());
        String spinnerItemSelected = agency_spinner.getItemAtPosition(agency_spinner.getSelectedItemPosition()).toString();
        intent.putExtra("agency", spinnerItemSelected);
        Toast.makeText(ReportActivity.this, spinnerItemSelected, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
