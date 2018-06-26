package com.star.one_playerpong;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class BouncingBallView extends View {

    private static final float BALL_SIZE = 80;
    private static final float BALL_MAX_VELOCITY = 20;

    private static final float PADDLE_WIDTH = 300;
    private static final float PADDLE_HEIGHT = 40;

    private static final float PADDLE_OFFSET_BOTTOM = 240;

    private Sprite mBall;
    private Paddle mPaddle;
    private DrawingThread mDrawingThread;

    private int mScore;

    public BouncingBallView(Context context, AttributeSet attrs) {

        super(context, attrs);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        float width = windowManager.getDefaultDisplay().getWidth();
        float height = windowManager.getDefaultDisplay().getHeight();

        mBall = new Sprite();
        mBall.setLocation(width / 2, height / 2);
        mBall.setSize(BALL_SIZE, BALL_SIZE);
        mBall.setVelocity(
                (float) ((Math.random() - 0.5) * 2 * BALL_MAX_VELOCITY),
                (float) ((Math.random() - 0.5) * 2 * BALL_MAX_VELOCITY)
        );

        Paint ballPaint = new Paint();
        ballPaint.setARGB(255, 255, 0, 0);

        mBall.setPaint(ballPaint);

        mPaddle = new Paddle();
        mPaddle.setLocation((width - PADDLE_WIDTH) / 2, height - PADDLE_OFFSET_BOTTOM);
        mPaddle.setSize(PADDLE_WIDTH, PADDLE_HEIGHT);

        Paint paddlePaint = new Paint();
        paddlePaint.setARGB(255, 0, 0, 0);

        mPaddle.setPaint(paddlePaint);

        mDrawingThread = new DrawingThread(this, 50);
        mDrawingThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < PADDLE_WIDTH / 2) {
                    mPaddle.setLocation(0, getHeight() - PADDLE_OFFSET_BOTTOM);
                } else if (x > getWidth() - PADDLE_WIDTH / 2) {
                    mPaddle.setLocation(getWidth() - PADDLE_WIDTH, getHeight() - PADDLE_OFFSET_BOTTOM);
                } else {
                    mPaddle.setLocation(x - PADDLE_WIDTH / 2, getHeight() - PADDLE_OFFSET_BOTTOM);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (x < PADDLE_WIDTH / 2) {
                    mPaddle.setLocation(0, getHeight() - PADDLE_OFFSET_BOTTOM);
                } else if (x > getWidth() - PADDLE_WIDTH / 2) {
                    mPaddle.setLocation(getWidth() - PADDLE_WIDTH, getHeight() - PADDLE_OFFSET_BOTTOM);
                } else {
                    mPaddle.setLocation(x - PADDLE_WIDTH / 2, getHeight() - PADDLE_OFFSET_BOTTOM);
                }
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(mBall.getRectF(), mBall.getPaint());
        canvas.drawRect(mPaddle.getRectF(), mPaddle.getPaint());

        Paint scorePaint = new Paint();
        scorePaint.setARGB(255, 0, 0, 0);
        scorePaint.setTextSize(50);
        canvas.drawText("Score: " + mScore, 50, 50, scorePaint);

        updateSprites();
    }

    private void updateSprites() {
        mBall.move();

        if (mBall.getRectF().left < 0 || mBall.getRectF().right > getWidth()) {
            mBall.setDx(-mBall.getDx());
        }

        if (mBall.getRectF().top < 0 || mBall.getRectF().bottom > getHeight()) {
            mBall.setDy(-mBall.getDy());
        }

        if (mBall.getRectF().top < 0) {
            mScore++;
        }

        if (mBall.getRectF().bottom > getHeight()) {
            mScore--;
        }

        if (RectF.intersects(mBall.getRectF(), mPaddle.getRectF()) && mBall.getDy() > 0 && !
                (((mBall.getRectF().top + BALL_SIZE / 2) >= mPaddle.getRectF().top) &&
                        ((mBall.getRectF().left + BALL_SIZE / 2) >= mPaddle.getRectF().left) &&
                        ((mBall.getRectF().left + BALL_SIZE / 2) <= mPaddle.getRectF().right))) {
            mBall.setDy(-mBall.getDy());
        }

    }
}
