package mytalk.talk.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity{
    ImageView imageView;
    private View mainview;

    ImageButton registration;
    ImageButton cancle;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("images");

    FirebaseStorage storage;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar a = getSupportActionBar();
        a.setDisplayShowHomeEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);
        a.setTitle(" ");
        Drawable d=getResources().getDrawable(R.drawable.actionbar_1);
        a.setBackgroundDrawable(d);
        setContentView(R.layout.activity_main);
        mainview = findViewById(R.id.main_activity);
        registration = findViewById(R.id.registration_btn);
        cancle = findViewById(R.id.cancle_btn);

        intent = getIntent();
        final String absoluteImagePath = intent.getStringExtra("path");


        imageView = findViewById(R.id.imageView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(absoluteImagePath, options);
        imageView.setImageBitmap(originalBm);

        storage = FirebaseStorage.getInstance();

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri file = Uri.fromFile(new File(absoluteImagePath));


                //??????????????? ????????? ?????? ?????? ??????
                final StorageReference riversRef = storage.getReference().child("images/" + file.getLastPathSegment());
                //images/filename.jpg
                UploadTask uploadTask = riversRef.putFile(file);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(imageView.getContext(), "????????? ??????", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(imageView.getContext(), "????????? ??????", Toast.LENGTH_SHORT).show();
                    }
                });

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            //Uri uri = task.getResult();
                            //db??? url ??????(???????????????)
                            String res = riversRef.toString().substring(riversRef.toString().lastIndexOf("com") + 4);
                            conditionRef.setValue(res);
                            final Snackbar snackbar = Snackbar.make(mainview, "??????????????? ????????????????", Snackbar.LENGTH_INDEFINITE);

                            snackbar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(new Intent(MainActivity.this, CodyActivity.class));

                                }
                            });

                            snackbar.show();

                            //startActivity(new Intent(CameraActivity.this, CodyActivity.class));
                        }
                        else {
                            //
                        }
                    }
                });

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
                finish();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }


}
