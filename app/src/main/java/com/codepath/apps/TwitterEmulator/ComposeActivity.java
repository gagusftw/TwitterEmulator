package com.codepath.apps.TwitterEmulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;


public class ComposeActivity extends AppCompatActivity {
    TextInputLayout inputCompose;
    EditText etCompose;
    Button btnTweet;
    public static final int TWEET_LIMIT = 280;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputCompose = findViewById(R.id.inputCompose);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            //Need to make sure that user cannot submit tweet if over 280 characters
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > TWEET_LIMIT)
                    btnTweet.setEnabled(false);
                else
                    btnTweet.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //Called if user clicks on items in actionbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Helper method to finish activity and send back status string
    public void postTweet(View v) {
        Intent data = new Intent();
        data.putExtra("tweet", etCompose.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}