package cn.com.myframe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pizhuang on 2015/6/25.
 */
public class Mysharedperferences {

    private static volatile Mysharedperferences mysharedperferences;

    private Mysharedperferences () {

    }

    public static Mysharedperferences getIinstance() {

        if (null == mysharedperferences) {
            synchronized (Mysharedperferences.class) {
                if (null == mysharedperferences) {
                    mysharedperferences = new Mysharedperferences();
                }
            }
        }

        return mysharedperferences;
    }



    /**
     * 存储数据
     * @param context
     * @param key
     * @param value
     */
    public void putString(Context context,String key,String value) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(getName(context), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getString(Context context,String key) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(getName(context), Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    /**
     * 向 SharedPreferences 中存入 int 数据
     * @param context
     * @param key
     * @param value
     */
    public void putInt(Context context,String key,int value) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(getName(context), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 从 SharedPreferences 中 获取 int 数据
     * @param context
     * @param key
     * @return
     */
    public int getInt(Context context,String key) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(getName(context), Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * 向 SharedPreferences 中存入 Boolean 数据
     * @param context
     * @param key
     * @param value
     */
    public void putBoolean(Context context,String key,boolean value) {

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(getName(context), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 从 SharedPreferences 中 获取 Boolean 数据
     * @param context
     * @param key
     * @return
     */
    public boolean getBoolean(Context context,String key) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(getName(context), Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 获得文件名字
     * @param context
     * @return
     */
    private String getName(Context context){
        String name;
        name = context.getPackageName();
        return name;
    }

}
