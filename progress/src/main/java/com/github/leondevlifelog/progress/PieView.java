package com.github.leondevlifelog.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PieView extends View {
    private static final String TAG = "Pie";
    private Paint paint;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int contentWidth;
    private int contentHeight;
    private int maxRadius;
    private int minRadius = 120;
    private Paint arcPaint;
    private RectF rectF;
    private Region globalRegion;
    private List<PieData> data = Collections.emptyList();
    private int totalSize = 0;
    private int defaultStartAngle = 90;
    private float pieRadiusGap;
    private Rect globalRect;
    private Paint globalPaint;
    private Paint linePaint;
    private Paint countTextPaint;
    private Paint labelPaint;
    private int dotRadius;
    private int lineWidth;
    private int labelTextSize;
    private int countTextSize;
    private int countTextColor;
    private List<Region> clickableRegions = new ArrayList<>();

    public PieView(Context context) {
        super(context);
        init(null, 0);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PieView, defStyle, 0);
        dotRadius = a.getDimensionPixelSize(R.styleable.PieView_dotRadius, 10);
        lineWidth = a.getDimensionPixelSize(R.styleable.PieView_lineWidth, 2);
        maxRadius = a.getDimensionPixelSize(R.styleable.PieView_maxRadius, getWidth() / 4);
        labelTextSize = a.getDimensionPixelSize(R.styleable.PieView_labelTextSize, 26);
        countTextSize = a.getDimensionPixelSize(R.styleable.PieView_countTextSize, 60);
        countTextColor = a.getColor(R.styleable.PieView_countTextColor, Color.parseColor("#D9000000"));
        a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        countTextPaint = new Paint();
        countTextPaint.setTextSize(countTextSize);
        countTextPaint.setAntiAlias(true);
        countTextPaint.setColor(countTextColor);
        countTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        globalPaint = new Paint();
        globalPaint.setAntiAlias(true);
        globalPaint.setColor(Color.GREEN);
        globalPaint.setStyle(Paint.Style.STROKE);

        labelPaint = new Paint();
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setAntiAlias(true);
        labelPaint.setColor(Color.parseColor("#D9000000"));
        labelPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        globalRegion = new Region();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        drawPie(canvas);
    }

    private void drawPie(Canvas canvas) {
        pieRadiusGap = (maxRadius - minRadius) * 1.0f / data.size();
        float startAngle = -90;
        if (data.size() == 0) {
            canvas.drawCircle(0, 0, maxRadius, arcPaint);
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getCount() == 0) {
                continue;
            }
            arcPaint.setColor(data.get(i).getPieColor());
            float radius = minRadius + ((i + 1) * pieRadiusGap);
            arcPaint.setShadowLayer(6, 0, 0, data.get(i).getPieColor());
            rectF.set(-radius, -radius, radius, radius);
            float sweepAngle = 360.0f * data.get(i).getCount() / totalSize;
            if (sweepAngle > 360f - ((data.size() - 1))) {
                sweepAngle = 360f - ((data.size() - 1));
            } else if (sweepAngle < 1) {
                sweepAngle = 1;
            }
            canvas.drawArc(rectF, startAngle, sweepAngle, true, arcPaint);
            startAngle += sweepAngle;
            drawLableAndLine(i, canvas, startAngle - (sweepAngle / 2.0f), radius, data.get(i));
        }
    }

    private void drawLableAndLine(int index, Canvas canvas, float angle, float radius, PieData pieData) {
        String countText = String.valueOf(pieData.getCount());
        Double startX = (radius - 60) * Math.cos(angle * Math.PI / 180f);
        Double startY = (radius - 60) * Math.sin(angle * Math.PI / 180f);
        Double endX = (radius + 80) * Math.cos(angle * Math.PI / 180f);
        Double endY = (radius + 80) * Math.sin(angle * Math.PI / 180f);
        linePaint.setColor(pieData.getLineColor());
        canvas.drawLine(startX.floatValue(), startY.floatValue(),
                endX.floatValue(), endY.floatValue(), linePaint);
        Double lineEnd = endX > 0 ? endX + 80 : endX - 80;
        countTextPaint.setTextAlign(endX > 0 ? Paint.Align.LEFT : Paint.Align.RIGHT);
        canvas.drawText(countText, endX.floatValue(), endY.floatValue() - 20, countTextPaint);
        linePaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(endX > 0 ? lineEnd.floatValue() + dotRadius : lineEnd.floatValue() - dotRadius, endY.floatValue(), dotRadius, linePaint);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawLine(endX.floatValue(), endY.floatValue(), lineEnd.floatValue(), endY.floatValue(), linePaint);
        Rect textBounds = new Rect();
        countTextPaint.getTextBounds(countText, 0, countText.length(), textBounds);
        labelPaint.setColor(pieData.getLineColor());
        labelPaint.setTextAlign(endX > 0 ? Paint.Align.LEFT : Paint.Align.RIGHT);
        canvas.drawText(pieData.getLable(), 0, pieData.getLable().length(), endX.floatValue(), endY.floatValue() - (2 * textBounds.height()), labelPaint);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_right_arrow);
        if (drawable == null || drawable.getConstantState() == null || drawable.getConstantState().newDrawable() == null) {
            return;
        }
        Drawable tmpDrawable = drawable.getConstantState().newDrawable().mutate();
        int drawableLeft = endX.intValue() > 0 ? endX.intValue() + Float.valueOf(labelPaint.measureText(pieData.getLable())).intValue() : endX.intValue();
        int drawableTop = endY.intValue() - countTextSize - (labelTextSize / 2) - tmpDrawable.getIntrinsicHeight();
        int drawableRight = drawableLeft + tmpDrawable.getIntrinsicWidth();
        int drawableBottom = endY.intValue() - countTextSize - (labelTextSize / 2);
        tmpDrawable.setBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
        DrawableCompat.setTint(tmpDrawable, pieData.getLineColor());
        tmpDrawable.draw(canvas);
        clickableRegions.get(index).set(endX > 0 ? endX.intValue() : lineEnd.intValue(), drawableTop, drawableRight, endY.intValue());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: " + "event = [" + event + "]");
                for (int i = 0; i < clickableRegions.size(); i++) {
                    if (clickableRegions.get(i).contains(Float.valueOf(event.getX() - (getWidth() / 2f)).intValue(), Float.valueOf(event.getY() - (getWidth() / 2f)).intValue())) {
                        if (data.get(i).getOnClickListener() != null) {
                            data.get(i).getOnClickListener().onClick(this);
                        }
                    }
                }
                break;
            default:
                return super.onTouchEvent(event);
        }
        return true;
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

        if (minRadius >= maxRadius) {
            minRadius = maxRadius / 2;
        }
        rectF = new RectF(-maxRadius, -maxRadius, maxRadius, maxRadius);
        globalRect = new Rect(0, 0, getWidth(), getBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(widthSize, heightSize);
        setMeasuredDimension(size, size);
    }

    public List<PieData> getData() {
        return data;
    }

    public void setData(List<PieData> data) {
        this.data = data;
        Collections.sort(data, (o1, o2) -> o1.getCount() - o2.getCount());
        prepareDraw();
        invalidate();
    }

    private void prepareDraw() {
        totalSize = 0;
        clickableRegions.clear();
        for (int i = 0; i < data.size(); i++) {
            totalSize += data.get(i).getCount();
            clickableRegions.add(new Region());
        }
    }
}
