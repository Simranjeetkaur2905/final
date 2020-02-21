package com.example.projectnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class detailofnotes extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURED_CODE = 1001;


    DatabaseNotes mDatabase;


    // for Camera Image
    Button mCaptureBtn;
    ImageView mImageView;
    Uri image_uri;

    public int position;

    EditText ET_category, ET_NoteTitle, ET_description;
   // ImageButton IB_location;
    Notesdata n;

    Button recording,locationbtn;


    //      Location where note has taken
    Location Nlocation;
    private GoogleMap mMap;
    private final int REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailofnotes);

        ET_category = findViewById(R.id.E_category);
        ET_NoteTitle = findViewById(R.id.E_noteTitle);
        ET_description = findViewById(R.id.E_description);
        locationbtn = findViewById(R.id.location_btn);

        recording = findViewById(R.id.btn_record);

        findViewById(R.id.btn_save).setOnClickListener(this);
        mDatabase = new DatabaseNotes(this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        buildLocationRequest();
        buildLocationCallBack();


        // Image

        mImageView = findViewById(R.id.image_view);
        mCaptureBtn = findViewById(R.id.capture_image_btn);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){


                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        requestPermissions(permission,PERMISSION_CODE);
                    }

                    else {
                        openCamera();
                    }

                }else {

                    openCamera();

                }
            }
        });


        Intent i = getIntent();
        n = (Notesdata) i.getSerializableExtra("object");



        if (n != null){
            ET_category.setText(n.category);
            ET_NoteTitle.setText(n.notesTitle);
            ET_description.setText(n.description);
        }



        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(detailofnotes.this,mapview.class);
                //intent.putExtra("loc",position);
                startActivity(intent);
            }
        });

        recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(detailofnotes.this,Recording.class);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        if(! checkPermissions())
            requestPermissions();
        else
        {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
    }



    private void openCamera() {


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);



        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURED_CODE);
    }



    // IMAGE REQUEST PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            }
            else
            {
                Toast.makeText(this, "noppp", Toast.LENGTH_SHORT).show();
            }
        }


        switch (requestCode){

            case PERMISSION_CODE: {

                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                    openCamera();

                }

                else {

                    Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();

                }

            }

        }

    }


    // IMAGE


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){

            mImageView.setImageURI(image_uri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                addNotes();
        }

    }

    private void addNotes(){
        String category = ET_category.getText().toString().trim();
        String noteTitle = ET_NoteTitle.getText().toString().trim();
        String description = ET_description.getText().toString().trim();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String date = df.format(calendar.getTime());

        if (category.isEmpty()) {
            ET_category.setError("name field is mandatory");
            ET_category.requestFocus();
            return;
        }

        if (noteTitle.isEmpty()) {
            ET_NoteTitle.setError("salary field cannot be empty");
            ET_NoteTitle.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            ET_description.setError("salary field cannot be empty");
            ET_description.requestFocus();
            return;
        }

        if (n == null){

            // add
            if(mDatabase.addNotes(category,noteTitle,description,date,Nlocation.getLatitude(),Nlocation.getLongitude()))
                Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Not Added",Toast.LENGTH_SHORT).show();

        }else{

            if(mDatabase.updateNote(n.id,category, noteTitle, description,Nlocation.getLatitude(),Nlocation.getLongitude()))
                Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Not updated",Toast.LENGTH_SHORT).show();
        }


    }


    private  boolean checkPermissions(){
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location: locationResult.getLocations()) {
                    Nlocation = location;
                }
            }
        };
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void getLastLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Nlocation = task.getResult();

                }
        }
    });
}

    }

