package com.smartiky.smartpoll.misc;

public class Log {
	
	public static boolean loggingEnabled = false;
	public static boolean verboseLoggingEnabled = false;
	public static boolean debugLoggingEnabled = true;
	public static boolean infoLoggingEnabled = true;
	public static boolean warnLoggingEnabled = true;
	public static boolean errorLoggingEnabled = true;
	public static boolean assertLoggingEnabled = true;
	
	public static void v(String tag, String msg) {
		if (loggingEnabled && verboseLoggingEnabled)
			android.util.Log.v(tag, msg);
	}
	
	public static void d(String tag, String msg) {
		if (loggingEnabled && debugLoggingEnabled)
			android.util.Log.d(tag, msg);
	}
	
	public static void i(String tag, String msg) {
		if (loggingEnabled && infoLoggingEnabled)
			android.util.Log.i(tag, msg);
	}
	
	public static void w(String tag, String msg) {
		if (loggingEnabled && warnLoggingEnabled)
			android.util.Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (loggingEnabled && errorLoggingEnabled)
			android.util.Log.e(tag, msg);
	}

	public static void wtf(String tag, String msg) {
		if (loggingEnabled && assertLoggingEnabled)
			android.util.Log.wtf(tag, msg);
	}

}
