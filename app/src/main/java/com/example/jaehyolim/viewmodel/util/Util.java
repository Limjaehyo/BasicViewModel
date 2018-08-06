package com.example.jaehyolim.viewmodel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;


public class Util {





    /**
     * Android SharedPreferences 에 data 를 저장한다.
     *
     * @param key   저장할 Key
     * @param value 저장할 Value
     * @param <T>   Value 값에 따라 저장을 달리한다.
     */
    public static <T> void setSharedPreferences(Context context,String key, T value) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.apply();
    }


    /**
     * Android SharedPreferences 의 data 를 가져온다.
     *
     * @param key 가져올 Key
     * @param <R> 가져올 Key 에 저장된 Value 값에따라 리턴값이 달라진다.
     * @return 제네릭 값의 Data 값.
     */
    public static <R extends Object> R getSharedPreferences(Context context,String key) throws ClassCastException {
        final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Object value = settings.getAll().get(key);
        if (value != null) {
            return  (R) value;
        }else{
            return (R) "";
        }

    }


    /**
     * Android SharedPreferences 의 data 를 지운다.
     *
     * @param key 가져올 Key
     */
    public static void removesSharedPreferences(Context context ,String key) {
        final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        settings.edit().remove(key).apply();
    }

    /**
     * @param context CacheDir에 접근하기 위한 Context
     * @param bitmap  저장할 Bitmap
     * @param name    저장할 BitmapName
     * @return 저장한 경로
     */
    public static String saveCacheBitmapToJpeg(Context context, Bitmap bitmap, String name) {
        File storage = context.getCacheDir(); // 이 부분이 임시파일 저장 경로
        String fileName = name + ".png";  // 파일이름 지정
        File tempFile = new File(storage, fileName);

        try {
            tempFile.createNewFile();  // 파일을 생성해주고
            FileOutputStream out = new FileOutputStream(tempFile);
            File.createTempFile(fileName, null, context.getCacheDir());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);  // 넘겨 받은 bitmap을 jpeg(손실압축)으로 저장해줌
            out.close(); // 마무리로 닫아줍니다.

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();   // 임시파일 저장경로를 리턴!
    }







    public static Object deepCopy(Object o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);

        byte[] buff = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(buff);
        ObjectInputStream os = new ObjectInputStream(bais);
        Object copy = os.readObject();
        return copy;
    }

    public static boolean isStringEmpty(@Nullable CharSequence str) {
        return TextUtils.isEmpty(str);
    }


}
