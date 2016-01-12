package com.sensu.android.zimaogou.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.LruCache;
import com.sensu.android.zimaogou.encrypt.MD5Utils;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by winter on 2015/9/22.
 * 文件操作工具
 *
 * @author winter
 */
public class FileUtils {
    //趣拍视频工具方法
    /**
     * 保存路径的文件夹名称
     */
    public static  String DIR_NAME = "qupai_video_test";

    /**
     * 给指定的文件名按照时间命名
     */
    private static SimpleDateFormat OUTGOING_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");

    /**
     * 得到指定的Video保存路径
     * @return
     */
    public static File getDoneVideoPath() {
        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR_NAME);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /**
     * 得到输出的Video保存路径
     * @return
     */
    public static String newOutgoingFilePath() {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        String fileName = getDoneVideoPath() + File.separator
                + "video_" + str + ".mp4";
        return fileName;
    }










    /**
     * 对文件排序，文件夹在前，文件在后，同类型按照名称排序
     *
     * @param files
     */
    public static void sort(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                int value;
                if (lhs.isDirectory() && rhs.isFile()) {
                    value = -1;
                } else if (lhs.isFile() && rhs.isDirectory()) {
                    value = 1;
                } else {
                    value = Collator.getInstance(Locale.CHINESE).compare(lhs.getName(), rhs.getName());
                }
                return value;
            }
        });
    }

    public static String translateFileName(String str) {
        return sTranslateFileNameCache.get(str);
    }

    private static LruCache<String, String> sTranslateFileNameCache = new LruCache<String, String>(1000) {
        @Override
        protected String create(String key) {
            return translateMD5FileName(key);
        }
    };

    public static String translateMD5FileName(String str) {
        try {
            return MD5Utils.md5(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return validateFilePath(str);
    }


    /**
     * 取出文件名中的无效字符
     *
     * @param srcStr
     * @return 去除文件名后的字符
     */
    public static String validateFilePath(String srcStr) {
        return TextUtils.isEmpty(srcStr) ? srcStr : srcStr.replaceAll("[\\\\/:\"*?<>|]+", "_");
    }

    /**
     * 判断路径是文件，且存在
     *
     * @param path 文件路径，如果传入null字符串，则认为文件不存在
     * @return 如果条件成立，返回true；
     */
    public static boolean fileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean exists(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    /**
     * <p> 由文件夹路径字符串创建一个文件夹
     * <p> 1.路径已经存在，不创建
     * <p> 2.如果路径是个文件，删除后创建
     *
     * @param path 文件夹路径字符串
     */
    public static void createPath(String path) {
        File f = new File(path);
        if (f.exists()) {
            if (!f.isDirectory()) {
                f.delete();
                f.mkdirs();
            }
        } else {
            f.mkdirs();
        }
    }

    /**
     * <p> 由文件路径创建文件
     * <p> 1.文件已经存在则不创建
     *
     * @param filePath
     * @return
     * @throws java.io.IOException
     */
    public static File createFile(String filePath) throws IOException {
        File f = new File(filePath);
        if (f.exists()) {
            if (f.isFile()) {
                return f;
            } else {
                return null;
            }
        }

        File parentFile = f.getParentFile();
        if (parentFile != null && (parentFile.isDirectory() || parentFile.mkdirs())) {
            f.createNewFile();
            return f;
        }
        return null;
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return deleteFile(file);
    }

    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 重命名文件
     *
     * @param srcPath 原名
     * @param dstPath 重命名后的文件名
     * @return 成功为true
     */
    public static boolean rename(String srcPath, String dstPath) {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (srcFile.isFile()) {
            dstFile.getParentFile().mkdirs();
            return srcFile.renameTo(dstFile);
        }
        return false;
    }

    /**
     * <p> Reader reader1 = new FileReader(new File("sample"));
     * <p> Reader reader2 = new InputStreamReader(new FileInputStream("sample"), "utf-8");
     * <p> Reader reader3 = new StringReader("adcde...xyz...");
     * <p> Reader reader4 = new CharArrayReader(new char[] {'a', 'b', 'c'});
     * <p> Reader reader5 = new BufferedReader(reader1);
     * <p> Reader reader6 = new LineNumberReader(reader2);
     *
     * @param reader
     * @return
     */
    public static StringBuilder readUseReader(Reader reader) {
        return readUseReader(reader, null);
    }

    /**
     * StringUtils.stringFromInputStream()类似
     */
    public static StringBuilder readUseReader(Reader reader, StringBuilder error) {
        try {
            StringBuilder content = new StringBuilder();
            char[] charArray = new char[1024];
            int charCount;
            while ((charCount = reader.read(charArray)) != -1) {
                content.append(charArray, 0, charCount);
            }
            return content;
        } catch (IOException e) {
            if (error != null) {
                error.append(e.toString());
            }
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p> Reader reader1 = new FileReader(new File("sample"));
     * <p> Reader reader2 = new InputStreamReader(new FileInputStream("sample"), "utf-8");
     * <p> Reader reader3 = new StringReader("adcde...xyz...");
     * <p> Reader reader4 = new CharArrayReader(new char[] {'a', 'b', 'c'});
     * <p> Reader reader5 = new BufferedReader(reader1);
     * <p> Reader reader6 = new LineNumberReader(reader2);
     *
     * @param reader
     * @return
     */
    public static StringBuilder readUseBufferReader(Reader reader) {
        return readUseBufferReader(reader, null);
    }

    public static StringBuilder readUseBufferReader(Reader reader, StringBuilder error) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder content = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            return content;
        } catch (IOException e) {
            PromptUtils.showTestToast("FileUtils" + e);
            if (error != null) {
                error.append(e);
            }
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p> Writer writer1 = new FileWriter("sample", true);
     * <p> Writer writer2 = new OutputStreamWriter(new FileOutputStream("sample", true), "utf-8");
     * <p> Writer writer3 = new StringWriter();
     * <p> Writer writer4 = new CharArrayWriter();
     * <p> Writer writer5 = new BufferedWriter(writer1);
     * <p> Writer writer6 = new LogWriter("tag");
     *
     * @param writer
     * @param str
     * @return
     */
    public static boolean writeUseWriter(Writer writer, String str) {
        return writeUseWriter(writer, str, null);
    }

    public static boolean writeUseWriter(Writer writer, String str, StringBuilder error) {
        try {
            writer.write(str);
            writer.flush();
            return true;
        } catch (IOException e) {
            PromptUtils.showTestToast("FileUtils" + e);
            if (error != null) {
                error.append(e + "\n");
            }
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                if (error != null) {
                    error.append(e);
                }
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void writeByteToFile(InputStream inputStream, String filePath) throws IOException {
        writeByteToFile(inputStream, createFile(filePath));
    }

    public static void writeByteToFile(InputStream inputStream, File file) throws IOException {
        writeByteToFile(inputStream, new FileOutputStream(file));
    }

    public static void writeByteToFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        int length = 1000;
        int size;
        byte[] bs = new byte[length];
        while ((size = inputStream.read(bs, 0, length)) > 0) {
            outputStream.write(bs, 0, size);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public static Object readObject(String path) {
        return readObject(path, null);
    }

    public static Object readObject(String path, StringBuilder error) {
        File file = new File(path);
        if (file.isFile()) {
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(file));
                return objectInputStream.readObject();
            } catch (Throwable e) {
                PromptUtils.showTestToast("FileUtils" + e);
                LogUtils.i("FileUtils", e.toString());
                if (error != null) {
                    error.append(e + "\n");
                }
                e.printStackTrace();
            } finally {
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e) {
                        if (error != null) {
                            error.append(e + "\n");
                        }
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static boolean writeObject(String path, Object object) {
        return writeObject(path, object, null);
    }

    public static boolean writeObject(String path, Object object, StringBuilder error) {
        if (object instanceof Serializable) {
            try {
                File file = createFile(path);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                objectOutputStream.close();
                return true;
            } catch (IOException e) {
                PromptUtils.showTestToast("FileUtils" + e);
                LogUtils.i("FileUtils", e.toString());
                if (error != null) {
                    error.append(e + "\n");
                }
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean copy(String originPath, String targetPath) {
        try {
            writeByteToFile(new FileInputStream(originPath), targetPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



}
