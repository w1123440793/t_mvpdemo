package demo.cn.t_mvpdemo.base.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/4.
 * Email wcc@jusfoun.com
 * Description
 */

public class IOUtil {

    public static final String FS = File.separator;

    // SD卡缓存
    private static final String JUSOUN = "jusfoun";
    // private static final String CACHE = "cache";
    private static final String IMAGE = "image";
    private static final String DOWNLOAD = "download";
    // private static final String LOG = "log";

    // 拍照缓存
    private static final String UPLOAD = "upload";
    private static final String UPLOAD_CAMERA_FILE = "camera_file.png";
    private static final String UPLOAD_IMAGE_FILE = "camera_file";

    private static final String UPLOAD_IMAGE_AVATAR_FILE = "avatar.png";

    private static final String UPLOAD_ZIP_FILE = "update.zip";
    private static final String PNG = ".png";

    private static final String VIDEO = "video";

    private static final String DOWNLOADIMAGEPATH = "jusfouns";

    public static String convertStreamToJson(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8 * 1024);
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e("convertStreamToString", "convertStreamToString error");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }


    public static boolean getExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return false;
        } else {
            return false;
        }
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().toString();
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean isParentDirExist(String filePath) {
        File file = new File(filePath);
        return file.getParentFile().exists();
    }

    public static boolean isDirExist(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public static boolean makeDirs(String path) {
        File file = new File(path);
        return file.mkdirs();
    }

    public static boolean makeParentDirs(String filePath) {
        File file = new File(filePath);

        return file.getParentFile().mkdirs();
    }

    public static String getBaseLocalLocation(Context context) {
        boolean isSDCanRead = IOUtil.getExternalStorageState();
        String baseLocation = "";
        if (isSDCanRead) {
            baseLocation = IOUtil.getSDCardPath();
        } else {
            baseLocation = context.getFilesDir().getAbsolutePath();
        }
        return baseLocation;
    }

    public static String getCacheLocation(Context context) {
        boolean isSDCanRead = IOUtil.getExternalStorageState();
        String baseLocation = "";
        if (isSDCanRead) {
            baseLocation = IOUtil.getSDCardPath();
        } else {
            baseLocation = context.getCacheDir().getAbsolutePath();
        }
        return baseLocation;
    }

    // 获取文件大小
    @SuppressWarnings("resource")
    public static int getFileSize(File file) {
        FileInputStream fis = null;
        int size = 0;
        try {
            fis = new FileInputStream(file);
            size = fis.available();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    //
    // // 添加到系统相册
    // public static boolean addToSysGallery(Context context, String path,
    // Bitmap bitmap) {
    // try {
    // if (context == null)
    // return false;
    // MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap,
    // "title", "description");
    // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
    // Uri.parse("file://" + path)));
    // return true;
    // } catch (Exception e) {
    // return false;
    // }
    //
    // }
    // 添加到系统相册
    public static void saveImageToGallery(final Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(IOUtil.getBaseLocalLocation(context), "Jusfoun");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        final String path = file.getAbsolutePath();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                + file.getAbsolutePath())));

        Handler handler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                        + path)));
            };
        };
        handler.sendEmptyMessageDelayed(100, 1000);
        Toast.makeText(context, "保存图片为：" + IOUtil.getBaseLocalLocation(context) + "/Jusfoun/", Toast.LENGTH_SHORT).show();
    }

    private static MediaScannerConnection msc = null;

    public static void addGallery(Context context, final String fileAbsolutePath, String fileName) {

        msc = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                msc.scanFile(fileAbsolutePath, "image/jpeg");
                // msc.disconnect();
            }

            public void onScanCompleted(String path, Uri uri) {
                // Log.v(TAG, "scan completed");
                msc.disconnect();
            }
        });
    }

    public static String createFileDir(String path) {
        if (!isDirExist(path)) {
            boolean isMakeSucc = makeDirs(path);
            if (!isMakeSucc) {
                return null;
            }
        }
        return path;
    }

    // 图片路径
    public static String getImagePath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + IMAGE + FS;
        return createFileDir(path);
    }

    // 默认路径
    public static String getDefaultPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS;
        return createFileDir(path);
    }

    // 默认压缩zip路径
    public static String getDefaultUploadZipPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + UPLOAD_ZIP_FILE;
        if (!isParentDirExist(path)) {
            makeParentDirs(path);
        }
        return path;
    }

    // 缓存路径
    // public static String getCachePath(Context context) {
    // String basePath = getBaseLocalLocation(context);
    // String pkgName = AppUtil.getPackageName(context);
    // String path = basePath + FS + COMMUNITY + FS + pkgName + FS + CACHE + FS;
    // return createFileDir(path);
    // }

    // 下载路径
    public static String getDownloadPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + DOWNLOAD + FS;
        return createFileDir(path);
    }

    // 视频路径
    public static String getVideoPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + VIDEO + FS;
        return createFileDir(path);
    }

    // 获取上传照片地址
    public static String getUploadPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + UPLOAD + FS;
        return createFileDir(path);
    }

    // 获取上传照片的路径，之后路径名字
    public static String getUploadOnlyPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + UPLOAD + FS;
        return path;
    }

    // 获取Log地址
    // public static String getLogPath(Context context) {
    // String basePath = getBaseLocalLocation(context);
    // String pkgName = AppUtil.getPackageName(context);
    // String path = basePath + FS + COMMUNITY + FS + pkgName + FS + LOG + FS;
    // return createFileDir(path);
    // }

    // 获取拍照图片地址
    public static String getUploadCameraPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + UPLOAD + FS + UPLOAD_CAMERA_FILE;
        if (!isParentDirExist(path)) {
            makeParentDirs(path);
        }
        return path;
    }

    // 获取拍照图片地址
    public static String getUploadCameraPath(Context context, String name) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + UPLOAD + FS + name + PNG;
        if (!isParentDirExist(path)) {
            makeParentDirs(path);
        }
        return path;
    }

    // 获取上传文件夹下第几个图片的地址
    public static String getUploadCameraPath(Context context, int index) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + UPLOAD + FS + UPLOAD_IMAGE_FILE + index + PNG;
        if (!isParentDirExist(path)) {
            Log.e("tag", "执行添加文件夹操作");
            makeParentDirs(path);
        }
        return path;
    }

    public static String getUploadCameraAcatarPath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String pkgName = AppUtil.getPackageName(context);
        String path = basePath + FS + JUSOUN + FS + pkgName + FS + UPLOAD + FS + UPLOAD_IMAGE_AVATAR_FILE;
        if (!isParentDirExist(path)) {
            makeParentDirs(path);
        }
        return path;
    }

    /**
     * 获取文件夹大小
     *
     * @param file
     *            File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static long getFolderSize(java.io.File file) throws Exception {
        long size = 0;
        java.io.File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    /**
     * 文件大小单位转换
     *
     * @param size
     * @return
     */
    public static String setFileSize(long size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "MB";
        }
    }

    // 移动文件
    public static boolean moveFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        boolean success = srcFile.renameTo(new File(destPath));
        return success;
    }

    // 删除文件夹
    public static boolean deleteFolder(File folder) {
        String childs[] = folder.list();
        if (childs == null || childs.length <= 0) {
            return folder.delete();
        }
        for (int i = 0; i < childs.length; i++) {
            String childName = childs[i];
            String childPath = folder.getPath() + File.separator + childName;
            File filePath = new File(childPath);
            if (filePath.exists() && filePath.isFile()) {
                filePath.delete();
            } else if (filePath.exists() && filePath.isDirectory()) {
                deleteFolder(filePath);
            }
        }

        return folder.delete();
    }

    // 下载 图片地址
    public static String getDowdloadImagePath(Context context) {
        String basePath = getBaseLocalLocation(context);
        String path = basePath + FS + DOWNLOADIMAGEPATH + FS;
        return createFileDir(path);
    }
}
