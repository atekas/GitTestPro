package com.sensu.android.zimaogou.widget;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.LogUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;

import java.io.IOException;

/**
 * Created by winter on 2015/7/7.
 * 只提供预览功能
 */
public class CameraPreviewView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private boolean mHasSurface;
    private boolean mIsResume;
    private float mHeightWidthRatio = 1;
    private Camera mCamera;

    public CameraPreviewView(Context context) {
        this(context, null);
    }

    public CameraPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        detectCamera();
    }

    private void detectCamera() {
        Camera camera = null;
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (RuntimeException e) {
            LogUtils.i("CameraPreviewView", "detectCamera() " + e.toString());
        }
        if (camera == null) {
            return;
        }
        Camera.Size size = camera.getParameters().getPreviewSize();
        mHeightWidthRatio = (float)size.width / size.height;
        camera.release();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            width = DisplayUtils.dp2px(120);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = MeasureSpec.makeMeasureSpec((int)(width * mHeightWidthRatio), MeasureSpec.EXACTLY);
        width = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        super.onMeasure(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHasSurface = true;
        open();
        setWillNotDraw(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
        close();
    }

    public void onResume() {
        mIsResume = true;
        open();
    }

    public void onPause() {
        mIsResume = false;
        close();
    }

    private void open() {
        if (!mHasSurface) {
            return;
        }
        if (!mIsResume) {
            return;
        }
        if (mCamera != null) {
            return;
        }
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (RuntimeException e) {
            LogUtils.i("CameraPreviewView", "open() " + e.toString());
        }
        if (mCamera == null) {
            return;
        }
        setDisplayOrientation();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    private void setDisplayOrientation() {
        if (mCamera == null) {
            return;
        }

        int rotation = ((Activity)getContext()).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        mCamera.setDisplayOrientation(result);
    }

    private void close() {
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
}
