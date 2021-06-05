package com.douglas.jointlyapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.douglas.jointlyapp.R;

public class NoConnectionActivity extends AppCompatActivity {

    private ImageView ivNoConnection;
    private TextView TvNoConnection;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        ivNoConnection = findViewById(R.id.ivNoConnection);
        TvNoConnection = findViewById(R.id.tvNoConnection);
        btnExit = findViewById(R.id.btnExit);

        btnExit.setOnClickListener(v -> {
            finish();
        });
    }
}