package com.github.leondevlifelog.progress;

import android.view.View;

public class PieData {
    private String lable;
    private int count;
    private int pieColor;
    private int lineColor;
    private View.OnClickListener onClickListener;

    public PieData() {
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int getPieColor() {
        return pieColor;
    }

    public void setPieColor(int pieColor) {
        this.pieColor = pieColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    @Override
    public String toString() {
        return "PieData{" +
                "lable='" + lable + '\'' +
                ", count=" + count +
                ", pieColor=" + pieColor +
                ", lineColor=" + lineColor +
                '}';
    }
}
