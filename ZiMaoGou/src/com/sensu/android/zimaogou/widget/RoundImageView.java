package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by cg on 2015/2/2.
 * 存圆的
 */
public class RoundImageView extends ImageView {

    private Paint mMaskPaint;
    private Path mMaskPath;

    public RoundImageView(Context context) {
        super(context);
        init();
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mMaskPaint = new Paint();
        mMaskPaint.setAntiAlias(true);
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mMaskPath = new Path();
        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w - getPaddingLeft() - getPaddingRight();
        int radius = width / 2;
        mMaskPath.reset();
        mMaskPath.addCircle(getPaddingLeft() + radius, getPaddingTop() + radius, radius, Path.Direction.CCW);
        mMaskPath.setFillType(Path.FillType.INVERSE_WINDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 保存当前layer的透明橡树到离屏缓冲区。并新创建一个透明度爲255的新layer
        int saveCount = canvas.saveLayerAlpha(0.0F, 0.0F, canvas.getWidth(), canvas.getHeight(),
                255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        super.onDraw(canvas);
        if (mMaskPath != null) {
            canvas.drawPath(mMaskPath, mMaskPaint);
        }
        canvas.restoreToCount(saveCount);
    }
}
