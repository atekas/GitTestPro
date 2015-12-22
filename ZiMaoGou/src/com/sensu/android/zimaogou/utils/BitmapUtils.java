package com.sensu.android.zimaogou.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import java.io.*;

/**
 * Created by CG on 14-5-31.
 * @author CG
 */
public class BitmapUtils {

    private static final int SIZE_LIMIT = 4096;

	public static class Size {
		private int mWidth;
		private int mHeight;

		public Size(int width, int height) {
			mWidth = width;
			mHeight = height;
		}

		public int getWidth() {
			return mWidth;
		}

		public int getHeight() {
			return mHeight;
		}
	}

	public static class BitmapInfo {
		private Bitmap mBitmap;
		private int mInSampleSize;
		private Size mOriginalSize;

		public BitmapInfo(Bitmap bitmap, int inSampleSize, Size originalSize) {
			mBitmap = bitmap;
			mInSampleSize = inSampleSize;
			mOriginalSize = originalSize;
		}

		public Bitmap getBitmap() {
			return mBitmap;
		}

		public int getInSampleSize() {
			return mInSampleSize;
		}

		public Size getOriginalSize() {
			return mOriginalSize;
		}
	}

	public static BitmapInfo getSampleBitmap(String originalFilePath, int reqWidth, int reqHeight) {
        return getSampleBitmap(originalFilePath, reqWidth, reqHeight, false);
    }

    public static BitmapInfo getSampleBitmap(String originalFilePath) {
        return getSampleBitmap(originalFilePath, 0, 0, true);
    }

    private static BitmapInfo getSampleBitmap(String originalFilePath, int reqWidth, int reqHeight, boolean useTextureSize) {
		Bitmap bitmap = null;
		int inSampleSize = 1;
		Size size = null;

		if (FileUtils.fileExists(originalFilePath)) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(originalFilePath, options);
            if (useTextureSize) {
                inSampleSize = getSample(options.outWidth, options.outHeight);
            } else {
                inSampleSize = getSample(options.outWidth, options.outHeight, reqWidth, reqHeight);
            }
			size = new Size(options.outWidth, options.outHeight);
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            try {
                bitmap = BitmapFactory.decodeFile(originalFilePath, options);
                if (bitmap == null) {
                    FileUtils.deleteFile(originalFilePath);
                }
            } catch (OutOfMemoryError e) { }
		}

		if (bitmap != null) {
			return new BitmapInfo(bitmap, inSampleSize, size);
		}
		return null;
	}

    /**
     * 计算采样率
     */
    public static int getSample(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (reqWidth > 0 && reqHeight > 0 && (width > reqWidth || height > reqHeight)) {
            int widthRate = width / reqWidth;
            int heightRate = height / reqHeight;
            inSampleSize = Math.max(widthRate, heightRate);
        }
        return inSampleSize;
    }

    /**
     * 计算采样率(根据最大纹理大小及手机分辨率)
     */
    public static int getSample(int width, int height) {
        int maxWidth;
        int maxHeight;
        int textureLimit = Math.min(getMaxTextureSize(), SIZE_LIMIT);
        if (textureLimit > 0) {
            maxWidth = textureLimit;
            maxHeight = textureLimit;
        } else {
            maxWidth = DisplayUtils.getDisplayWidth() * 2;
            maxHeight = DisplayUtils.getDisplayHeight() * 2;
        }
        int sampleSize = 1;
        while (width / sampleSize > maxWidth || height / sampleSize > maxHeight) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    public static int getMaxTextureSize() {
        // The OpenGL texture size is the maximum size that can be drawn in an ImageView
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    /**
     * 没有图片，只有采样信息
     */
	public static BitmapInfo getSampleBitmapInfo(String originalFilePath, int reqWidth, int reqHeight) {
		if (FileUtils.fileExists(originalFilePath)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(originalFilePath, options);
            int inSampleSize = getSample(options.outWidth, options.outHeight, reqWidth, reqHeight);
            Size size = new Size(options.outWidth, options.outHeight);
            return new BitmapInfo(null, inSampleSize, size);
		}
		return null;
	}

    /**
     * 获取缩放的图片
     */
    public static Bitmap getScaleBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        int bmpWidth  = bitmap.getWidth();
        int bmpHeight  = bitmap.getHeight();
        float scaleRate = getScaleRate(bmpWidth, bmpHeight, reqWidth, reqHeight);
        if (scaleRate - 1 < 0.01) { // 优化 ： 如果只是大了一点（1%），就不再压缩
            float scaleWidth  = bmpWidth / scaleRate;
            float scaleHeight = bmpHeight / scaleRate;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
        }
        return bitmap;
    }

    /**
     * 计算缩放比例
     */
    public static float getScaleRate(float width, float height, float reqWidth, float reqHeight) {
        float scaleRate = 1;
        if (reqWidth > 0 && reqHeight > 0 && (width > reqWidth || height > reqHeight)) {
            float widthRate = width / reqWidth;
            float heightRate = height / reqHeight;
            scaleRate = Math.max(widthRate, heightRate);
        }
        return scaleRate;
    }

    /**
     * 获取图片的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
		int size = 0;
		if (bitmap != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
				size = bitmap.getByteCount();
			} else {
				size = bitmap.getRowBytes() * bitmap.getHeight();
			}
		}
		return size;
	}

    public static void saveScreen(Activity activity, String filePath) {
        saveBitmap(getScreen(activity), filePath);
    }

    /**
     * 将bitmap保存成图片
     * @param bitmap
     * @param filePath
     */
    public static void saveBitmap(Bitmap bitmap, String filePath) {
        if (bitmap != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, new FileOutputStream(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getScreen(Activity activity) {
        return getShowBitmap(activity.getWindow().getDecorView());
    }

    public static void saveShowBitmap(View view, String filePath) {
        saveBitmap(getShowBitmap(view), filePath);
    }

    public static Bitmap getShowBitmap(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }

    public void saveBitmapFile(Bitmap bitmap,String name){
        File file=new File(name);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
