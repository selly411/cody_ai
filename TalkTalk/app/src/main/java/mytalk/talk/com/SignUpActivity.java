package mytalk.talk.com;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText email_join;
    private EditText pwd_join;
    private Button btn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar a = getSupportActionBar();
        a.setDisplayShowHomeEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);
        a.setTitle(" ");
        Drawable d=getResources().getDrawable(R.drawable.actionbar_1);
        a.setBackgroundDrawable(d);
        setContentView(R.layout.activity_signup);

        email_join = (EditText) findViewById(R.id.sign_up_email);
        pwd_join = (EditText) findViewById(R.id.sign_up_pwd);
        btn = (Button) findViewById(R.id.sign_up_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_join.getText().toString().trim();
                String pwd = pwd_join.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }
}
