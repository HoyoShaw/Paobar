package com.hoyo.paobar.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public final class SettingTools {
    private static final String TAG = "SettingTools";

    public static final String PREFERENCE_NAME = "paobar_preference";
    public static final String UPDATED_AT = "updated_at";
    public static final String LOADED_COUNT = "loaded_count";

    private SettingTools() {
    }

    public static void saveString( String name, String value) {
    	Context context = ContextUtil.getContext();
		assert( context != null );
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.putString(name, value);
            ed.commit();
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method saveString", e);
        }
    }

    public static String readString( String name, String dftValue) {
        String returnValue = "";
        Context context = ContextUtil.getContext();
		assert( context != null );
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            returnValue = sp.getString(name, dftValue);
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method readString", e);
        }
        return returnValue;
    }

    public static void saveLong( String name, long value) {
    	Context context = ContextUtil.getContext();
		assert( context != null );
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.putLong(name, value);
            ed.commit();
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method saveLong", e);
        }
    }

    public static long readLong( String name, long dftValue) {
    	Context context = ContextUtil.getContext();
		assert( context != null );
        long returnValue;
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            returnValue = sp.getLong(name, dftValue);
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method readLong", e);
            returnValue = 0L;
        }
        return returnValue;
    }

    public static void saveBoolean( String name, boolean value) {
    	Context context = ContextUtil.getContext();
		assert( context != null );
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.putBoolean(name, value);
            ed.commit();
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method saveBoolean", e);
        }
    }

    /**
     * 判断配置是否存在
     * @param name 配置项
     */
	public static boolean existsConfigItem(String name) {
		boolean returnValue = false;
		try {
			Context context = ContextUtil.getContext();
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
			if (sp != null) {
				return sp.contains(name);
			}
		} catch (Exception e) {
			Log.e(TAG, "Unexcepted Exception in method readBoolean", e);
		}
		return returnValue;
	}

    public static boolean readBoolean( String name, boolean dftValue) {
    	Context context = ContextUtil.getContext();
		assert( context != null );
        boolean returnValue;
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            returnValue = sp.getBoolean(name, dftValue);
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method readBoolean", e);
            returnValue = false;
        }
        return returnValue;
    }

    public static void saveInt( String name, int value) {
    	Context context = ContextUtil.getContext();
		assert( context != null );
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.putInt(name, value);
            ed.commit();
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method saveInt", e);
        }
    }

    public static int readInt(String name, int dftValue) {
        int returnVlaue;
        Context context = ContextUtil.getContext();
		assert( context != null );
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            returnVlaue = sp.getInt(name, dftValue);
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method readInt", e);
            returnVlaue = 0;
        }
        return returnVlaue;
    }

    public static void remove(String name) {
    	Context context = ContextUtil.getContext();
		assert( context != null );
        try {
            SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.remove(name);
            ed.commit();
        } catch (Exception e) {
            Log.e(TAG, "Unexcepted Exception in method remove", e);
        }
    }
}