package com.zackstrikesback.poll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.create) Button create;
    @BindView(R.id.vote) Button vote;
    @BindView(R.id.data) Button data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.create, R.id.vote, R.id.data})
    public void onClick(Button button) {
        if(button == create) {
            Intent explicitIntent = new Intent(MainActivity.this, Create.class);
            startActivity(explicitIntent);
        }
        else if(button == vote) {
            Intent explicitIntent = new Intent(MainActivity.this, Polls.class);
            startActivity(explicitIntent);
        }
        else if(button == data) {
            Intent explicitIntent = new Intent(MainActivity.this, Results.class);
            startActivity(explicitIntent);
        }
    }
}
