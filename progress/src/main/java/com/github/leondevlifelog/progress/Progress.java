package com.github.leondevlifelog.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
    private int progress = 45;
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
    private Paint progressColorPaint;
    private Paint innerSweepPaint;
    private int defaultColor = Color.parseColor("#FF209331");
    private Paint outerSweepPaint;
    private Paint progressPaint;
    private RectF reactProgressArc;

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
        progressColorPaint = new Paint();
        progressColorPaint.setAntiAlias(true);
        progressColorPaint.setStrokeWidth(6);
        progressColorPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(defaultColor);
        progressPaint.setStrokeWidth(6);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, outerCircleRadius, outerCirclePaint);
        canvas.drawCircle(centerX, centerY, innerCicleRadius, innerCirclePaint);
        canvas.drawCircle(outerCicleStarLeftX, outerCicleStarLeftY, 6, outerCircleStarPaint);
        canvas.drawCircle(outerCicleStarRightX, outerCicleStarRightY, 6, outerCircleStarPaint);
        int save = canvas.save();
        for (int i = 0; i < 100; i++) {
            canvas.drawLine(centerX, centerY + progressInnerRadius, centerX, centerY + progressOuterRadius, progressIndectorPaint);
            canvas.rotate(3.6f, centerX, centerY);
        }
        canvas.restoreToCount(save);
        int save1 = canvas.save();
        canvas.rotate(-90, centerX, centerY);
        canvas.drawCircle(centerX, centerY, innerCicleRadius - 30, innerSweepPaint);
        canvas.drawCircle(centerX, centerY, outerCircleRadius - 30, outerSweepPaint);
        canvas.restoreToCount(save1);
        int save2 = canvas.save();
        canvas.rotate(-180, centerX, centerY);
        for (int i = 0; i < 40; i++) {
            progressColorPaint.setColor(changeAlpha(defaultColor, Float.valueOf(255 - (i * (255.0f / 40.0f))).intValue()));
            canvas.drawLine(centerX, centerY + progressInnerRadius, centerX, centerY + progressOuterRadius, progressColorPaint);
            canvas.rotate(-3.6f, centerX, centerY);
        }
        canvas.restoreToCount(save2);
        canvas.drawArc(reactProgressArc, -90, progress * 360 / 100.0f, false, progressPaint);
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
        reactProgressArc = new RectF(contentWidth / 2 - innerCicleRadius, contentHeight / 2 - innerCicleRadius, contentWidth - (contentWidth / 2 - innerCicleRadius), contentHeight - (contentHeight / 2 - innerCicleRadius));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(widthSize, heightSize);
        setMeasuredDimension(size, size);
    }
}
