 package com.example.mostafa.talents;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mostafa.talents.Models.post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class StartActivity extends AppCompatActivity {
    private Uri videouri;
    private static final int REQUEST__CODE = 101;
    private StorageReference videoref;
    Uri path_video;
    String path_Image;


    EditText ed1_name, ed2_desc;
    ImageView image;
    Button btn_rg;
    DatabaseReference myRef = null;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    // Creating URI.
    Uri FilePathUri;
    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    // Image request code for onActivityResult() .

    int Image_Request_Code = 7;
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ed2_desc = findViewById(R.id.desc);
        ed1_name= findViewById(R.id.name);
        btn_rg = findViewById(R.id.sub);
        image = findViewById(R.id.image);
        //  create user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


// Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        videoref=storageReference.child("/videos" + "/userIntro.mp4");

        database= FirebaseDatabase.getInstance();

        // Assign FirebaseStorage instance to storageReference.
        myRef=database.getReference();

        image .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });
        btn_rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadImageFileToFirebaseStorage(v);

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                image.setImageBitmap(bitmap);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }


            else if (requestCode == REQUEST__CODE&&resultCode == RESULT_OK) {

                    videouri = data.getData();
                    Toast.makeText(this, "Video saved to:\n" +
                            videouri, Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Video recording cancelled.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Failed to record video",
                            Toast.LENGTH_LONG).show();




        }

    }
    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

   /* public void upload(View view) {
        if (videouri != null) {
            UploadTask uploadTask = videoref.putFile(videouri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StartActivity.this,
                            "Upload failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            path_video=taskSnapshot.getDownloadUrl();
                            Toast.makeText(StartActivity.this, "Upload complete",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }*/



    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage( View view) {

        // Checking whether FilePathUri Is empty or not.
//        upload(view);
        if (FilePathUri != null) {



            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            // Showing toast message after done uploading.
                           // Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            path_Image =taskSnapshot.getDownloadUrl().toString();


                            myRef = database.getReference("posts");
                            if (videouri != null) {
                                UploadTask uploadTask = videoref.putFile(videouri);

                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StartActivity.this,
                                                "Upload failed: " + e.getLocalizedMessage(),
                                                Toast.LENGTH_LONG).show();

                                    }
                                }).addOnSuccessListener(
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                               // path_video=taskSnapshot.getDownloadUrl();

                                                String id = myRef.push().getKey();
                                                String name = ed1_name.getText().toString();
                                                String desc = ed2_desc.getText().toString();


                                                @SuppressWarnings("VisibleForTests")

                                                post date = new post(name,path_Image,desc,taskSnapshot.getDownloadUrl().toString());

                                                myRef.child(id).setValue(date);

                                                Toast.makeText(StartActivity.this, "Upload complete",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }






                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {



                            // Showing exception erro message.
                            Toast.makeText(StartActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


        }
        else {

            Toast.makeText(StartActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.back) {
            FirebaseAuth.getInstance().signOut();

            //  onBackPressed();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }

    public void next(View view) {
        startActivity(new Intent(getApplicationContext(),allposts.class));
    }

    public void record(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST__CODE);

    }
}


