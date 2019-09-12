package com.github.leondevlifelog.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

public class Progress extends View {
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int contentWidth;
    private int contentHeight;
    private int widthHeightOffset;
    private int progress;
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private int centerX;
    private int centerY;
    private int outerCircleRadius;
    private int innerCicleRadius;
    private float outerCicleStarLeftY;
    private float outerCicleStarRightY;
    private float outerCicleStarLeftX;
    private float outerCicleStarRightX;
    private Paint outerCircleStarPaint;
    private int progressOuterRadius;
    private int progressInnerRadius;
    private Paint progressIndectorPaint;
    private Paint innerSweepPaint;
    private int defaultColor = Color.parseColor("#FF209331");
    private Paint outerSweepPaint;

    public Progress(Context context) {
        super(context);
        init(null, 0);
    }

    public Progress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Progress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private static int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.Progress, defStyle, 0);

        a.recycle();
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.parseColor("#4013C2C2"));
        outerCirclePaint.setStrokeWidth(2);
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setStyle(Paint.Style.STROKE);
        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.parseColor("#A6209331"));
        innerCirclePaint.setStrokeWidth(2);
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setStyle(Paint.Style.STROKE);
        outerCircleStarPaint = new Paint();
        outerCircleStarPaint.setColor(Color.parseColor("#4013C2C2"));
        outerCircleStarPaint.setAntiAlias(true);
        outerCircleStarPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        progressIndectorPaint = new Paint();
        progressIndectorPaint.setColor(Color.parseColor("#1A000000"));
        progressIndectorPaint.setAntiAlias(true);
        progressIndectorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        progressIndectorPaint.setStrokeWidth(6);
        progressIndectorPaint.setStrokeCap(Paint.Cap.ROUND);
        innerSweepPaint = new Paint();
        innerSweepPaint.setAntiAlias(true);
        outerSweepPaint = new Paint();
        outerSweepPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, outerCircleRadius, outerCirclePaint);
        canvas.drawCircle(centerX, centerY, innerCicleRadius, innerCirclePaint);
        canvas.drawCircle(outerCicleStarLeftX, outerCicleStarLeftY, 6, outerCircleStarPaint);
        canvas.drawCircle(outerCicleStarRightX, outerCicleStarRightY, 6, outerCircleStarPaint);
        int save = canvas.save();
        Double outerX = (progressOuterRadius * Math.sin(3.6)) + centerX;
        Double outerY = (progressOuterRadius * Math.cos(3.6)) + centerY;
        Double innerX = (progressInnerRadius * Math.sin(3.6)) + centerX;
        Double innerY = (progressInnerRadius * Math.cos(3.6)) + centerY;
        for (int i = 0; i < 100; i++) {
            canvas.drawLine(innerX.floatValue(), innerY.floatValue(), outerX.floatValue(), outerY.floatValue(), progressIndectorPaint);
            canvas.rotate(3.6f, centerX, centerY);
        }
        canvas.restoreToCount(save);
        int save1 = canvas.save();
        canvas.rotate(-90, centerX, centerY);
        canvas.drawCircle(centerX, centerY, innerCicleRadius - 30, innerSweepPaint);
        canvas.drawCircle(centerX, centerY, outerCircleRadius - 30, outerSweepPaint);
        canvas.restoreToCount(save1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();
        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;
        widthHeightOffset = Math.abs(contentHeight - contentWidth);
        centerX = contentWidth / 2;
        centerY = contentHeight / 2;
        outerCircleRadius = contentHeight / 2 - 2;
        innerCicleRadius = (contentWidth / 2) - 100;
        outerCicleStarLeftX = Double.valueOf(Math.sin(40) * outerCircleRadius + centerX).floatValue();
        outerCicleStarLeftY = Double.valueOf(Math.cos(40) * outerCircleRadius + centerX).floatValue();
        outerCicleStarRightX = Double.valueOf(centerX - (Math.sin(40) * outerCircleRadius)).floatValue();
        outerCicleStarRightY = Double.valueOf(centerX - (Math.cos(40) * outerCircleRadius)).floatValue();
        progressOuterRadius = outerCircleRadius - 50;
        progressInnerRadius = innerCicleRadius + 20;
        Shader shader = new SweepGradient(centerX, centerY, new int[]{changeAlpha(defaultColor, 0), changeAlpha(defaultColor, 0),
                changeAlpha(defaultColor, 180)},
                new float[]{0f, 0.6f, 1}
        );
        innerSweepPaint.setShader(shader);
        SweepGradient outerShader = new SweepGradient(centerX, centerY, new int[]{changeAlpha(defaultColor, 0), changeAlpha(defaultColor, 0),
                changeAlpha(defaultColor, 64)},
                new float[]{0f, 0.6f, 1});
        outerSweepPaint.setShader(outerShader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(widthSize, heightSize);
        setMeasuredDimension(size, size);
    }
}
