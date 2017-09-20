package com.zackstrikesback.a9patch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText email = (EditText)findViewById(R.id.email);
        final EditText password = (EditText)findViewById(R.id.pw);
        Button submit = (Button)findViewById(R.id.sub);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isValidEmail(email.getText())){
                    email.setError("Invalid Email Address");
                    email.requestFocus();
                } else if(!isValidPassword(password.getText())) {
                    password.setError("Invalid Password");
                    password.requestFocus();
                } else {
                    Toast.makeText(MainActivity.this, "Submission Success!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected boolean isValidPassword(CharSequence target) {
        if(target != null && target.length() > 7){
            return true;
        } else {
            return false;
        }
    }

    protected boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
