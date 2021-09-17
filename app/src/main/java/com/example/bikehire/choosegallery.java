package com.example.bikehire;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.contentcapture.DataShareWriteAdapter;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.os.Bundle;

public class choosegallery extends AppCompatActivity {
    private ImageView imageView;
    private Button uploadbtn;
    private Uri imageUri;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private ProgressBar progressBar;

    //private FirebaseStorage storage;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosegallery);
        imageView = findViewById(R.id.profilePic);
        uploadbtn = findViewById(R.id.uploadbtn);
        progressBar = findViewById(R.id.progressbar);

        progressBar.setVisibility(View.INVISIBLE);

        //storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null){

                    uploadtoFirebase(imageUri);
                }else{

                    Toast.makeText(choosegallery.this, "Please Select an Image", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // private void choosePicture() {
    //   Intent intent = new Intent();
    // intent.setType("image/*");
    //ntent.setAction(Intent.ACTION_GET_CONTENT);
    //startActivityForResult(intent,1);
    //}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==2 && resultCode== RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            //uploadPicture();
        }
    }

    private void uploadtoFirebase( Uri uri) {
        // Create a reference to "mountains.jpg"
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Model model = new Model(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);
                        Toast.makeText(choosegallery.this, "Image Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(choosegallery.this, "Uploading Failed!", Toast.LENGTH_LONG).show();
            }
        });
// Create a reference to 'images/mountains.jpg'
        //  StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

// While the file names are the same, the references point to different files
        //mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        //mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false




    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

}
