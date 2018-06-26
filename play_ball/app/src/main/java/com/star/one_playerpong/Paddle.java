package com.star.one_playerpong;


import android.graphics.Paint;
import android.graphics.RectF;

public class Paddle {

    private RectF mRectF = new RectF();
    private Paint mPaint;

    public Paddle() {

    }

    public Paddle(float x, float y, float width, float height) {
        setLocation(x, y);
        setSize(width, height);
    }

    public void setLocation(float x, float y) {
        mRectF.offsetTo(x, y);
    }

    public void setSize(float width, float height) {
        mRectF.right = mRectF.left + width;
        mRectF.bottom = mRectF.top + height;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public RectF getRectF() {
        return mRectF;
    }

    public Paint getPaint() {
        return mPaint;
    }
}
