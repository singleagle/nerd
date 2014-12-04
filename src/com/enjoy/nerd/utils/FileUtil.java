package com.enjoy.nerd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

public class FileUtil {
	

	public static final String IMAGE_FILE_NAME = "avatar.jpg";
	public static final String DIR_NAME = "/neard";
	
	/**
     * 检查是否存在SDCard
     * @return
     */
    public static boolean hasSdcard(){
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)){
                    return true;
            }else{
                    return false;
            }
    }
    
    public static String getAppExtDir(){
    	File file = new File(Environment.getExternalStorageDirectory().toString() + DIR_NAME);
    	if(!file.exists()){
    		file.mkdir();
    	}
    	
    	StringBuffer buffer = new StringBuffer(Environment.getExternalStorageDirectory().toString());
    	buffer.append(DIR_NAME);
    	return buffer.toString();
    }
    
    public static String getFriendAvatarDir(){
    	return FileUtil.getAppExtDir() + "/avatar/";
    }
    /**
     * 
     * @param url
     * @return: 将文件链接映射成本地文件路径的片断，该片断由上层目录和文件名组成
     */
    public static String mapToLocalFilePathSegment(String url){
		CRC32 crc32 = new CRC32();
		crc32.update(url.getBytes());
		int dir = (int) (crc32.getValue()%256);
		StringBuffer buffer = new StringBuffer();
		if(dir < 16){
			buffer.append("0");
		}
		buffer.append(Integer.toHexString(dir));
		buffer.append("/");
		buffer.append(md5(url));
		return buffer.toString();
	}
	
	private static String md5(String url){
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(url.getBytes());
			byte b[] = md5.digest();
			
			int value;
			StringBuffer buffer = new StringBuffer("");
			for (int index = 0; index < b.length; index++) {
				value = b[index];
				if(value < 0){
					value += 256;
				}
				if(value < 16){
					buffer.append("0");
				}
				buffer.append(Integer.toHexString(value));
			}
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return "";
	}
    
    public static int readPictureDegree(String path) {  
    	int degree  = 0;  
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
        }  
        return degree;  
    }
    
    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
      
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
    
	public static File creatWritableFile(String path) throws IOException{
		File file = new File(path);
		File director = file.getParentFile();
		if(!director.exists()){
			director.mkdirs();
		}
		if(!file.exists()){
			file.createNewFile();
		}
		return file;
	}
	
	//获取原图大小不压缩
	public static Bitmap getBitmapFromPathForOriginal(String path){
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}
	
    
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {  
	    int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);  
	    int roundedSize;  
	    if (initialSize <= 8) {  
	        roundedSize = 1;  
	        while (roundedSize < initialSize) {  
	            roundedSize <<= 1;  
	        }  
	    } else {  
	        roundedSize = (initialSize + 7) / 8 * 8;  
	    }  
	    return roundedSize;  
	}  
	  
	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
	    double w = options.outWidth;  
	    double h = options.outHeight;  
	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
	    int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.  
	        return lowerBound;  
	    }  
	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
	        return 1;  
	    } else if (minSideLength == -1) {  
	        return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	}
	
	public static void writeSerializableObject(Object serializable, String path){
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			File file = creatWritableFile(path);
			fileOutputStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(serializable);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	public static Object readSerializableObject(String path){
		File file = new File(path);
		Object object = null;
		if(!file.exists()){
			return null;
		}
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		
		try {
			fileInputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fileInputStream);
			object = objectInputStream.readObject();
		}catch (StreamCorruptedException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			
		} finally{
			if(objectInputStream != null){
				try {
					objectInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return object;
	}
	
}
