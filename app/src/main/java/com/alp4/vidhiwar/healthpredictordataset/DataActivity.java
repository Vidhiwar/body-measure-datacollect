package com.alp4.vidhiwar.healthpredictordataset;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataActivity extends AppCompatActivity {

    private EditText editTextHeight;
    private EditText editTextShoulders;
    private EditText editTextWaist;
    private EditText editTextHip;
    private EditText editTextArms;
    private EditText editTextWrist;
    private EditText editTextThigh;
    private EditText editTextAge;
    private EditText editTextWeight;
    private Spinner spinnerSex;
    private Spinner spinnerBodyType;
    private Button buttonSubmit;
    private ImageButton buttonImageCapture;
    private ImageView imageBody;
    private String imageFilePath;
    private String imageUrl;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dataSet;
    private StorageReference imageStore;
    private StorageReference filePath;

    private Uri imageUri;
    private ProgressDialog mProgress;

    private static  final int CAMERA_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        mProgress = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        dataSet = FirebaseDatabase.getInstance().getReference(user.getUid());
        imageStore = FirebaseStorage.getInstance().getReference();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        editTextShoulders = (EditText) findViewById(R.id.editTextShoulders);
        editTextWaist = (EditText) findViewById(R.id.editTextWaist);
        editTextHip = (EditText) findViewById(R.id.editTextHip);
        editTextArms = (EditText) findViewById(R.id.editTextArms);
        editTextWrist = (EditText) findViewById(R.id.editTextWrist);
        editTextThigh = (EditText) findViewById(R.id.editTextThigh);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        spinnerBodyType = (Spinner) findViewById(R.id.spinnerBodyType);
        spinnerSex = (Spinner)findViewById(R.id.spinnerSex);
        buttonSubmit = (Button)findViewById(R.id.buttonSubmit);
        buttonImageCapture = (ImageButton) findViewById(R.id.buttonImageCapture);
        imageBody = (ImageView) findViewById(R.id.imageBody);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addNewEntry();
            }
        });

        buttonImageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();
            }
        });
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "BodyMeasureImage_" + timeStamp + "_";
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

    private void addNewEntry(){

        String height = editTextHeight.getText().toString();
        String shoulders = editTextShoulders.getText().toString();
        String waist = editTextWaist.getText().toString();
        String hip = editTextHip.getText().toString();
        String arms = editTextArms.getText().toString();
        String wrist = editTextWrist.getText().toString();
        String thigh = editTextThigh.getText().toString();
        String age = editTextAge.getText().toString();
        String weight = editTextWeight.getText().toString();
        String sex = spinnerSex.getSelectedItem().toString();
        String bodyType = spinnerBodyType.getSelectedItem().toString();

        if(!TextUtils.isEmpty(height) && !TextUtils.isEmpty(shoulders) && !TextUtils.isEmpty(waist) && !TextUtils.isEmpty(hip) && !TextUtils.isEmpty(arms) && !TextUtils.isEmpty(wrist)&& !TextUtils.isEmpty(thigh) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(weight) && !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(bodyType))
        {
            if(filePath == null)
            {
                Toast.makeText(this,"Insert Image",Toast.LENGTH_SHORT).show();
            }
            if(filePath != null) {
                imageFilePath = filePath.toString();
                String id = dataSet.push().getKey();
                measurementData Data = new measurementData(imageUrl,imageFilePath, id, Integer.parseInt(height), Integer.parseInt(shoulders), Integer.parseInt(waist), Integer.parseInt(hip), Integer.parseInt(arms), Integer.parseInt(wrist), Integer.parseInt(thigh), Integer.parseInt(age), Integer.parseInt(weight), sex, bodyType);
                dataSet.child(id).setValue(Data);

                Toast.makeText(this, "Entry Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
        {
            Toast.makeText(this,"Don't leave any column blank",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK)
        {

            File f = new File(mCurrentPhotoPath);
            imageUri = Uri.fromFile(f);
            imageBody.setImageURI(imageUri);

            mProgress.setMessage("Uploading Data...");
            mProgress.show();

            if(imageUri == null){
                Toast.makeText(DataActivity.this, "Image Capture Failed \nUri Null", Toast.LENGTH_SHORT).show();
            }

            if(imageUri != null) {

                filePath = imageStore.child("BodyMeasurePhotos").child(imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(DataActivity.this, "Image Upload Success", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                imageUrl = "No URL";
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(DataActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                });
            }
        }

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putParcelable("myURI", imageUri);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        imageUri = savedInstanceState.getParcelable("myURI");

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}
