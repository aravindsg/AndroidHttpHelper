package com.aravind.project.httputils;

import android.util.Log;

/**
 */
public class Logger {
	static final boolean LOG = true;

	/**
	 * Method i.
	 * @param tag String
	 * @param string String
	 */
	public static void i(String tag, String string) {
		if (LOG) {
			try {

				if (string.length() < 3000)
					Log.i(tag, string + " ");
				else {
					while (string.length() > 3000) {
						Log.i(tag, string.substring(0, 3000) + " ");
						string = string.substring(3000);
					}
					Log.i(tag, string + " ");
				}
			} catch (Exception e) {
				Log.i(tag, e.getMessage());
			}
		}
	}

	/**
	 * Method e.
	 * @param tag String
	 * @param string String
	 */
	public static void e(String tag, String string) {
		if (LOG) {
			try {

				if (string.length() < 3000)
					Log.e(tag, string + " ");
				else {
					while (string.length() > 3000) {
						Log.e(tag, string.substring(0, 3000) + " ");
						string = string.substring(3000);
					}
					Log.e(tag, string + " ");
				}
			} catch (Exception e) {
				Log.e(tag, e.getMessage());
			}
		}
	}

	/**
	 * Method e.
	 * @param tag String
	 * @param num int
	 */
	public static void e(String tag, int num) {
		if (LOG) {
			android.util.Log.e(tag, Integer.toString(num) + " ");
		}
	}

	/**
	 * Method d.
	 * @param tag String
	 * @param string String
	 */
	public static void d(String tag, String string) {
		if (LOG) {
			try {

				if (string.length() < 3000)
					Log.d(tag, string + " ");
				else {
					while (string.length() > 3000) {
						Log.d(tag, string.substring(0, 3000) + " ");
						string = string.substring(3000);
					}
					Log.d(tag, string + " ");
				}
			} catch (Exception e) {
				Log.d(tag, e.getMessage());
			}
		}
	}

	/**
	 * Method v.
	 * @param tag String
	 * @param string String
	 */
	public static void v(String tag, String string) {
		if (LOG) {
			try {

				if (string.length() < 3000)
					Log.v(tag, string + " ");
				else {
					while (string.length() > 3000) {
						Log.v(tag, string.substring(0, 3000) + " ");
						string = string.substring(3000);
					}
					Log.v(tag, string + " ");
				}
			} catch (Exception e) {
				Log.v(tag, e.getMessage());
			}
		}
	}

	/**
	 * Method w.
	 * @param tag String
	 * @param string String
	 */
	public static void w(String tag, String string) {
		if (LOG) {
			try {

				if (string.length() < 3000)
					Log.w(tag, string + " ");
				else {
					while (string.length() > 3000) {
						Log.w(tag, string.substring(0, 3000) + " ");
						string = string.substring(3000);
					}
					Log.w(tag, string + " ");
				}
			} catch (Exception e) {
				Log.w(tag, e.getMessage());
			}
		}
	}
}