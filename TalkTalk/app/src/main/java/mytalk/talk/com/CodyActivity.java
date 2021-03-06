package mytalk.talk.com;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class CodyActivity extends AppCompatActivity {
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private ImageButton confirm_btn;
    int i;
    int j = 0;
    public String[] strings = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar a = getSupportActionBar();
        a.hide();

        setContentView(R.layout.activity_cody);

        // 데이터베이스 읽기 #1. ValueEventListener
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    strings[j] = (String) snapshot.getValue();
                    Log.d("MainActivity", strings[j]);
                    j++;


                }

                imageView1 = (ImageView)findViewById(R.id.imageView1);
                imageView2 = (ImageView)findViewById(R.id.imageView2);
                imageView3 = (ImageView)findViewById(R.id.imageView3);


                // Reference to an image file in Cloud Storage



                for(i=1; i<4; i++){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageReference.child(strings[i]);
                    Log.d(this.getClass().getName(), imageRef.toString());
                    switch (i){
                        case 1:
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        Glide.with(CodyActivity.this).load(task.getResult()).into(imageView1);
                                    }else{
                                    }
                                }
                            });
                            break;
                        case 2:
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        Glide.with(CodyActivity.this).load(task.getResult()).into(imageView2);
                                    }else{
                                    }
                                }
                            });
                            break;
                        case 3:
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        Glide.with(CodyActivity.this).load(task.getResult()).into(imageView3);
                                    }else{
                                    }
                                }
                            });
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Log.d("MainActivity", strings[0]);
        //loadWithGlide();

        confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodyActivity.this, CameraActivity.class));
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                startActivity(new Intent(CodyActivity.this, CameraActivity.class));
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
}
