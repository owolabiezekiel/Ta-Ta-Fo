package klover.com.ta_ta_fo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FinalConfirmation extends AppCompatActivity {
    private TextView nameTextView;
    private TextView locationTextView;
    private TextView agencyTextView;
    private ImageView crimeImage;
    private String emailPicPath;
    private String emailSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_confirmation);

        nameTextView = findViewById(R.id.nameTextView);
        locationTextView = findViewById(R.id.locationTextView);
        agencyTextView = findViewById(R.id.agencyTextView);
        crimeImage = findViewById(R.id.crimeImage);

        Intent populate = getIntent();
        Bundle bundle = populate.getExtras();
        if(bundle != null){
            nameTextView.setText("Anonymous");
            String agency = (String) bundle.get("agency");
            agencyTextView.setText(agency);
            String image = (String) bundle.get("image");
            emailPicPath = image;
            Glide.with(this).load(image).into(crimeImage);
            String desc = (String) bundle.get("description");
            emailSubject = desc;
            locationTextView.setText(desc);

        }
    }

    public void tatafoIt(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("image/png");
// set the email subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Crime Submission");
// set the email image path for the attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + emailPicPath));
// set the body of the email
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"owo.ezekiel@gmail.com"});
        try {
            startActivity(Intent.createChooser(emailIntent, "Choose email provider"));
        } catch (android.content.ActivityNotFoundException ex) {
            //do something else
        }

        startActivity(new Intent(FinalConfirmation.this, ReportActivity.class));
        finish();
    }


}
