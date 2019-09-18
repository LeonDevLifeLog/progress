package com.github.leondevlifelog.progresssample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.leondevlifelog.progress.PieData;
import com.github.leondevlifelog.progress.PieView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PieView pieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pieView = findViewById(R.id.pie);
        ArrayList<PieData> data = new ArrayList<>();
        PieData e = new PieData();
        e.setCount(50);
        e.setLable("敏感文件");
        e.setPieColor(Color.parseColor("#FA8C16"));
        e.setLineColor(Color.parseColor("#F5222D"));
        e.setOnClickListener(this);
        data.add(e);
        PieData e1 = new PieData();
        e1.setCount(30);
        e1.setLable("非敏感文件");
        e1.setOnClickListener(this);
        e1.setPieColor(Color.parseColor("#43C55F"));
        e1.setLineColor(Color.parseColor("#209331"));
        data.add(e1);
        PieData e2 = new PieData();
        e2.setCount(40);
        e2.setLable("未判定");
        e2.setOnClickListener(this);
        e2.setPieColor(Color.parseColor("#4D000000"));
        e2.setLineColor(Color.parseColor("#4D000000"));
        data.add(e2);
        pieView.setData(data);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
