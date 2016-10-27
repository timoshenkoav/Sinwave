package com.views.sinwave;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 7/14/16.
 */
public class SinWave extends View {
    public static final int LINE_COUNT = 6;
    private Paint mPaint;
    private Paint mCenterPaint;
    private float mCoef;
    private float mWidthCoef;
    private float mStrokeWidth = 5;
    private float mGlobalCoef = 1;
    private Random mRandom;
    private boolean mCoefChanged = true;
    private Paint mShadowPaint;
    private float[] mLineCoefs;
    private int[] mLineWidths;

    public SinWave(Context context) {
        super(context);
    }

    public SinWave(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SinWave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SinWave(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    {
        init();
    }

    private void init() {
        mRandom = new Random(System.currentTimeMillis());
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#62AFF7"));
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);


        mShadowPaint = new Paint();
        mShadowPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setColor(Color.parseColor("#62AFF7"));
        mShadowPaint.setStrokeWidth(10);
        mShadowPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint.setAlpha(10);
        mPaint.setMaskFilter(new BlurMaskFilter(3, BlurMaskFilter.Blur.OUTER));


        mCenterPaint = new Paint();
        mCenterPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(Color.BLACK);
        mCenterPaint.setStrokeWidth(5);
        mCenterPaint.setStyle(Paint.Style.STROKE);

        mLineCoefs = new float[LINE_COUNT];
        mLineWidths = new int[LINE_COUNT];
        for (int i = 0; i < LINE_COUNT; ++i) {
            mLineCoefs[i] = mRandom.nextFloat();
            mLineWidths[i] = Math.round(1 + mRandom.nextInt(5));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        drawInternal(canvas, canvas.getWidth());

        canvas.restore();
    }

    private void drawInternal(Canvas pCanvas, int pWidth) {
        pCanvas.drawLine(0, pCanvas.getHeight() / 2, pWidth, pCanvas.getHeight() / 2, mCenterPaint);

        mPaint.setStrokeWidth(mStrokeWidth * 1.1f);
        mShadowPaint.setStrokeWidth(mStrokeWidth * 1.1f * 3);

//        drawPath(pCanvas, getPath(pCanvas, mCoef, pWidth));

        for (int i = 0; i < mLineWidths.length; ++i) {
            mPaint.setStrokeWidth(mLineWidths[i]);
            mShadowPaint.setStrokeWidth(mLineWidths[i] * 3);
            drawPath(pCanvas, getPath(pCanvas, mLineCoefs[i], pWidth));
        }
    }

    private void drawPath(Canvas pCanvas, Path pPath) {
        pCanvas.drawPath(pPath, mShadowPaint);
        pCanvas.drawPath(pPath, mPaint);
    }

    private float[] WIDTH_CORD = new float[]{0, 0.2f, 0.5f, 1f};

    @NonNull
    private Path getPath(Canvas canvas, float coef, int pWidth) {
        Path lPath = new Path();
        lPath.moveTo(0, canvas.getHeight() / 2);

        boolean top = true;

        for (int i = 0; i < WIDTH_CORD.length - 1; ++i) {
            quad(canvas, lPath, WIDTH_CORD[i], WIDTH_CORD[i + 1], coef, top, pWidth);
            top = !top;
        }

        return lPath;
    }

    private void quad(Canvas pCanvas, Path pPath, float xStartCoef, float xEndCoef, float pHeightCoef, boolean top, int pWidth) {
        float yPos = pCanvas.getHeight() * pHeightCoef;

        pPath.quadTo(
                pWidth * xStartCoef + (pWidth * xEndCoef - pWidth * xStartCoef) / 2,
                (top ?
                        (pCanvas.getHeight() - yPos)
                        :
                        (yPos)
                ),
                pWidth * xEndCoef,
                pCanvas.getHeight() / 2);
    }

    public void setCoef(float pCoef) {
        mCoef = pCoef;
        postInvalidate();
    }

    public void setWidthCoef(float pWidthCoef) {
        mWidthCoef = pWidthCoef;
        postInvalidate();
    }

    public void setGlobalCoef(float coef) {
        if (Float.compare(0, coef) == 0) {
            mCoefChanged = true;
        } else {
            mCoefChanged = false;
        }
        mGlobalCoef = coef;

        postInvalidate();
    }

    private boolean mRunning;

    public void setLineCoef(int[] vals) {
        for (int i = 0; i < LINE_COUNT; ++i) {
            mLineCoefs[i] = vals[i]/1000.f;
        }
        postInvalidate();
    }

    public void toggle() {
        start();
    }

    private void start() {
        List<Animator> lObjectAnimatorList = new ArrayList<>();
        int[]startAr = new int[LINE_COUNT];
        int[]endAr = new int[LINE_COUNT];
        for (int i=0;i<LINE_COUNT;++i){
            startAr[i] = mRandom.nextInt(1000);
            endAr[i] = mRandom.nextInt(1000);
        }
        ValueAnimator animAnim = ObjectAnimator.ofObject(this, "lineCoef", new IntArrayEvaluator(),startAr, endAr);
        lObjectAnimatorList.add(animAnim);
        final AnimatorSet lAnimatorSet = new AnimatorSet();
        lAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                start();
            }
        });
        lAnimatorSet.playSequentially(lObjectAnimatorList);
        lAnimatorSet.setDuration(100);
        lAnimatorSet.start();
    }

    private class AnimationRunnable implements Runnable {

        @Override
        public void run() {
            if (mRunning) {

            }
        }
    }

    private class IntArrayEvaluator implements TypeEvaluator<int[]> {
        private int[] mArray;


        public IntArrayEvaluator() {
        }

        /**
         * Create a FloatArrayEvaluator that reuses <code>reuseArray</code> for every evaluate() call.
         * Caution must be taken to ensure that the value returned from
         * {@link android.animation.ValueAnimator#getAnimatedValue()} is not cached, modified, or
         * used across threads. The value will be modified on each <code>evaluate()</code> call.
         *
         * @param reuseArray The array to modify and return from <code>evaluate</code>.
         */
        public IntArrayEvaluator(int[] reuseArray) {
            mArray = reuseArray;
        }

        @Override
        public int[] evaluate(float fraction, int[] startValue, int[] endValue) {
            int[] array = mArray;
            if (array == null) {
                array = new int[startValue.length];
            }

            for (int i = 0; i < array.length; i++) {
                float start = startValue[i];
                float end = endValue[i];
                array[i] = Math.round(start + (fraction * (end - start)));
            }
            return array;
        }

    }
}
