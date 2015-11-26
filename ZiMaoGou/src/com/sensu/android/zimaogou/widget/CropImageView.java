package com.sensu.android.zimaogou.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import com.sensu.android.zimaogou.handler.SoftReferenceHandler;
import com.sensu.android.zimaogou.utils.DisplayUtils;

/**
 * Created by k on 14-9-29.
 * <p/>
 * 用于缩放裁剪的自定义ImageView视图
 *
 * @author vinson
 */
public class CropImageView extends ImageView implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener, SoftReferenceHandler.MsgCallback {

    public static final float DEFAULT_MAX_SCALE = 4.0f;
    public static final float DEFAULT_MID_SCALE = 2.0f;
    public static final float DEFAULT_MIN_SCALE = 1.0f;

    private MultiGestureDetector mMultiGestureDetector;

    private int mFrameWidth;
    private int mFrameHeight;

    private boolean mIsAdjusted;

    private boolean mShowFullPic;

    private long mDownTime;
    private float mX;
    private float mY;
    private final float mMinMove;
    private boolean mEnable;
    private static final int CLICK_PRESS_TIME = 250;
    private static final int DOUBLE_CLICK_INTERVAL_TIME = 350;
    private static final int TRIGGER_CLICK = 1;
    private static final int TRIGGER_CLICK_DELAY = 200;
    private SoftReferenceHandler mHandler;
    private OnClickListener mClick;

    private final Matrix mBaseMatrix = new Matrix();
    private final Matrix mDrawMatrix = new Matrix();
    private final Matrix mSuppMatrix = new Matrix();
    private final RectF mDisplayRect = new RectF();
    private final float[] mMatrixValues = new float[9];

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public CropImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        setScaleType(ScaleType.MATRIX);

        setOnTouchListener(this);

        mMultiGestureDetector = new MultiGestureDetector(context);

        mFrameWidth = DisplayUtils.getDisplayWidth();
        mFrameHeight = mFrameWidth;

        mMinMove = ViewConfiguration.get(context).getScaledTouchSlop();
        mHandler = new SoftReferenceHandler(this);
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case TRIGGER_CLICK:
                if (mClick != null) {
                    mClick.onClick(CropImageView.this);
                }
                break;
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClick = l;
    }

    public void showFullPic(boolean isFull) {
        mShowFullPic = isFull;
    }

    public void setArea(int width, int height) {
        mFrameWidth = width;
        mFrameHeight = height;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    @Override
    public void onGlobalLayout() {
        if (mIsAdjusted) {
            return;
        }
        // 调整视图位置
        configPosition();
        mHasLayout = true;
    }

    private boolean mHasLayout;

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (mHasLayout) {
            configPosition();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (mHasLayout) {
            configPosition();
        }
    }

    /**
     * 依据图片宽高比例，设置图像初始缩放等级和位置
     */
    private void configPosition() {
        super.setScaleType(ScaleType.MATRIX);
        Drawable d = getDrawable();
        if (d == null) {
            return;
        }
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        final float drawableWidth = d.getIntrinsicWidth();
        final float drawableHeight = d.getIntrinsicHeight();
        if (mShowFullPic) {
            float viewRatio = viewWidth / viewHeight;
            float picRatio = drawableWidth / drawableHeight;
            if (viewRatio > picRatio) {
                mFrameHeight = (int)viewHeight;
                mFrameWidth = (int)(mFrameHeight * picRatio);
            } else {
                mFrameWidth = (int)viewWidth;
                mFrameHeight = (int)(mFrameWidth / picRatio);
            }
        }

        // 图片缩放
        mBaseMatrix.reset();
        float scale = Math.max(mFrameWidth / drawableWidth, mFrameHeight / drawableHeight);
        mBaseMatrix.postScale(scale, scale);
        // 移动居中
        mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2, (viewHeight - drawableHeight * scale) / 2);

        resetMatrix();
        mIsAdjusted = true;
    }

    /**
     * Resets the Matrix back to FIT_CENTER, and then displays it.s
     */
    private void resetMatrix() {
        mSuppMatrix.reset();
        setImageMatrix(getDisplayMatrix());
    }

    protected Matrix getDisplayMatrix() {
        mDrawMatrix.set(mBaseMatrix);
        mDrawMatrix.postConcat(mSuppMatrix);
        return mDrawMatrix;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(TRIGGER_CLICK);
                mEnable = false;
                long time = System.currentTimeMillis();
                if (mDownTime == 0 || (time - mDownTime) > DOUBLE_CLICK_INTERVAL_TIME) {
                    mDownTime = time;
                    mX = event.getX();
                    mY = event.getY();
                    mEnable = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEnable) {
                    float x = event.getX();
                    float y = event.getY();
                    if (Math.abs(x - mX) > mMinMove || Math.abs(y - mY) > mMinMove) {
                        mEnable = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mEnable) {
                    time = System.currentTimeMillis();
                    if (time - mDownTime < CLICK_PRESS_TIME) {
                        mHandler.removeMessages(TRIGGER_CLICK);
                        mHandler.sendEmptyMessageDelayed(TRIGGER_CLICK, TRIGGER_CLICK_DELAY);
                    }
                }
                break;
        }
        return mMultiGestureDetector.onTouchEvent(event);
    }

    private class MultiGestureDetector extends GestureDetector.SimpleOnGestureListener implements
            ScaleGestureDetector.OnScaleGestureListener {

        private final ScaleGestureDetector scaleGestureDetector;
        private final GestureDetector gestureDetector;
        private final float scaledTouchSlop;

        private VelocityTracker velocityTracker;
        private boolean isDragging;

        private float lastTouchX;
        private float lastTouchY;
        private float lastPointerCount;

        public MultiGestureDetector(Context context) {
            scaleGestureDetector = new ScaleGestureDetector(context, this);

            gestureDetector = new GestureDetector(context, this);
            gestureDetector.setOnDoubleTapListener(this);

            final ViewConfiguration configuration = ViewConfiguration.get(context);
            scaledTouchSlop = configuration.getScaledTouchSlop();
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = getScale();
            float scaleFactor = detector.getScaleFactor();
            if (getDrawable() != null && ((scale < DEFAULT_MAX_SCALE && scaleFactor > 1.0f) || (scale > DEFAULT_MIN_SCALE && scaleFactor < 1.0f))) {
                if (scaleFactor * scale < DEFAULT_MIN_SCALE) {
                    scaleFactor = DEFAULT_MIN_SCALE / scale;
                }
                if (scaleFactor * scale > DEFAULT_MAX_SCALE) {
                    scaleFactor = DEFAULT_MAX_SCALE / scale;
                }
                mSuppMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
                checkAndDisplayMatrix();
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }

            scaleGestureDetector.onTouchEvent(event);

            /*
             * Get the center x, y of all the pointers
             */
            float x = 0, y = 0;
            final int pointerCount = event.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                x += event.getX(i);
                y += event.getY(i);
            }
            x = x / pointerCount;
            y = y / pointerCount;

            /*
             * If the pointer count has changed cancel the drag
             */
            if (pointerCount != lastPointerCount) {
                isDragging = false;
                if (velocityTracker != null) {
                    velocityTracker.clear();
                }
                lastTouchX = x;
                lastTouchY = y;
            }
            lastPointerCount = pointerCount;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (velocityTracker == null) {
                        velocityTracker = VelocityTracker.obtain();
                    } else {
                        velocityTracker.clear();
                    }
                    velocityTracker.addMovement(event);

                    lastTouchX = x;
                    lastTouchY = y;
                    isDragging = false;
                    break;

                case MotionEvent.ACTION_MOVE: {
                    final float dx = x - lastTouchX, dy = y - lastTouchY;

                    if (!isDragging) {
                        // Use Pythagoras to see if drag length is larger than
                        // touch slop
                        isDragging = Math.sqrt((dx * dx) + (dy * dy)) >= scaledTouchSlop;
                    }

                    if (isDragging) {
                        if (getDrawable() != null) {
                            mSuppMatrix.postTranslate(dx, dy);
                            checkAndDisplayMatrix();
                        }

                        lastTouchX = x;
                        lastTouchY = y;

                        if (velocityTracker != null) {
                            velocityTracker.addMovement(event);
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    lastPointerCount = 0;
                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                        velocityTracker = null;
                    }
                    break;
            }

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            try {
                float scale = getScale();
                float x = getWidth() / 2;
                float y = getHeight() / 2;

                if (scale < DEFAULT_MID_SCALE) {
                    post(new AnimatedZoomRunnable(scale, DEFAULT_MID_SCALE, x, y));
                } else if ((scale >= DEFAULT_MID_SCALE) && (scale < DEFAULT_MAX_SCALE)) {
                    post(new AnimatedZoomRunnable(scale, DEFAULT_MAX_SCALE, x, y));
                } else {
                    post(new AnimatedZoomRunnable(scale, DEFAULT_MIN_SCALE, x, y));
                }
            } catch (Exception e) {
                // Can sometimes happen when getX() and getY() is called
            }

            return true;
        }
    }

    private class AnimatedZoomRunnable implements Runnable {
        // These are 'postScale' values, means they're compounded each iteration
        static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07f;
        static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93f;

        private final float focalX, focalY;
        private final float targetZoom;
        private final float deltaScale;

        public AnimatedZoomRunnable(final float currentZoom, final float targetZoom,
                                    final float focalX, final float focalY) {
            this.targetZoom = targetZoom;
            this.focalX = focalX;
            this.focalY = focalY;

            if (currentZoom < targetZoom) {
                deltaScale = ANIMATION_SCALE_PER_ITERATION_IN;
            } else {
                deltaScale = ANIMATION_SCALE_PER_ITERATION_OUT;
            }
        }

        public void run() {
            mSuppMatrix.postScale(deltaScale, deltaScale, focalX, focalY);
            checkAndDisplayMatrix();

            final float currentScale = getScale();

            if (((deltaScale > 1f) && (currentScale < targetZoom))
                    || ((deltaScale < 1f) && (targetZoom < currentScale))) {
                // We haven't hit our target scale yet, so post ourselves
                // again
                postOnAnimation(CropImageView.this, this);

            } else {
                // We've scaled past our target zoom, so calculate the
                // necessary scale so we're back at target zoom
                final float delta = targetZoom / currentScale;
                mSuppMatrix.postScale(delta, delta, focalX, focalY);
                checkAndDisplayMatrix();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.postOnAnimation(runnable);
        } else {
            view.postDelayed(runnable, 16);
        }
    }

    /**
     * Returns the current scale value
     *
     * @return float - current scale value
     */
    public final float getScale() {
        mSuppMatrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MSCALE_X];
    }

    /**
     * Helper method that simply checks the Matrix, and then displays the result
     */
    private void checkAndDisplayMatrix() {
        checkMatrixBounds();
        setImageMatrix(getDisplayMatrix());
    }

    private void checkMatrixBounds() {
        final RectF rect = getDisplayRect(getDisplayMatrix());
        if (null == rect) {
            return;
        }

        float deltaX = 0, deltaY = 0;
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        // 判断移动或缩放后，图片显示是否超出裁剪框边界
        if (rect.top > (viewHeight - mFrameHeight) / 2) {
            deltaY = (viewHeight - mFrameHeight) / 2 - rect.top;
        }
        if (rect.bottom < (viewHeight + mFrameHeight) / 2) {
            deltaY = (viewHeight + mFrameHeight) / 2 - rect.bottom;
        }
        if (rect.left > (viewWidth - mFrameWidth) / 2) {
            deltaX = (viewWidth - mFrameWidth) / 2 - rect.left;
        }
        if (rect.right < (viewWidth + mFrameWidth) / 2) {
            deltaX = (viewWidth + mFrameWidth) / 2 - rect.right;
        }
        // Finally actually translate the matrix
        mSuppMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * Helper method that maps the supplied Matrix to the current Drawable
     *
     * @param matrix - Matrix to map Drawable against
     * @return RectF - Displayed Rectangle
     */
    private RectF getDisplayRect(Matrix matrix) {
        Drawable d = getDrawable();
        if (null != d) {
            mDisplayRect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(mDisplayRect);
            return mDisplayRect;
        }
        return null;
    }

    /**
     * 选择的区域在图片的位置的百分比
     */
    public RectF getClipPicArea() {
        RectF rect = getDisplayRect(getDisplayMatrix());
        if (rect != null) {
            float left = (getWidth() - mFrameWidth) / 2 - rect.left;
            float top = (getHeight() - mFrameHeight) / 2 - rect.top;
            float right = (getWidth() + mFrameWidth) / 2 - rect.left;
            float bottom = (getHeight() + mFrameHeight) / 2 - rect.top;
            float width = rect.width();
            float height = rect.height();
            return new RectF(left / width, top / height, right / width, bottom / height);
        }
        return null;
    }

    /**
     * 剪切图片，返回剪切后的bitmap对象
     */
    public Bitmap clip() {
        Bitmap bitmap = Bitmap.createBitmap(mFrameWidth, mFrameHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(-(getWidth() - mFrameWidth) / 2, -(getHeight() - mFrameHeight) / 2);
        draw(canvas);
        return bitmap;
    }
}
