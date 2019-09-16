package com.github.leondevlifelog.progresssample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.leondevlifelog.progress.Progress;

public class MainActivity extends AppCompatActivity {

    private Progress progressView;
    private int progress = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = findViewById(R.id.progress);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setProgress(progress);
                progress = (MainActivity.this.progress + 10);
                if (progress > 100) {
                    progress -= 100;
                }
            }
        });
    }
}
