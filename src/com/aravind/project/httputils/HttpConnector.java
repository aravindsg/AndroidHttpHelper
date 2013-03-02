package com.aravind.project.httputils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;
import android.os.Build;
import android.os.Handler;

/**
 */
public class HttpConnector {
	private static final String TAG = "HTTP_CALL";
	private static final int RETRY_LIMIT = 3;
	private static final int CONNECTION_TIMEOUT = 30;
	private static final int[] READ_TIMEOUT = { 10, 20, 30 };
	public static final int HTTP_GET = 1;
	public static final int HTTP_POST = 2;

	Handler mHandler = new Handler();
	String responseString;

	public HttpConnector() {
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
			System.setProperty("http.keepAlive", "false");
	}

	// /////////////////////////////////////////////////////////////////////////
	/**
	 * Method performRequest.
	 * @param context Context
	 * @param url String
	 * @param method int
	 * @param headers HashMap<String,String>
	 * @param params HashMap<String,String>
	 * @return String
	 */
	public String performRequest(Context context, final String url, final int method, final HashMap<String, String> headers, final HashMap<String, String> params) {

		StringBuilder b = null;
		HttpURLConnection urlConnection = null;
		long startTime = System.currentTimeMillis();
		try {
			final URL destUrl = new URL(url);

			for (int i = 0; i < RETRY_LIMIT; i++) {

				if (b != null) {
					break;
				}

				urlConnection = (HttpURLConnection) destUrl.openConnection();

				if (method == HTTP_GET)
					urlConnection.setRequestMethod("GET");
				if (method == HTTP_POST)
					urlConnection.setRequestMethod("POST");

				setupConnectionParameters(headers, urlConnection, i);

				String postHeaders = getParametersToPost(headers);
				if (method == HTTP_POST) {
					setupPostParameters(url, params, urlConnection, i, postHeaders);
				}
				if (method == HTTP_GET) {
					printInput(url, i, "", postHeaders);
				}

				int responseCode;
				try {
					responseCode = urlConnection.getResponseCode();
					Logger.d(TAG + url + " response code", responseCode + " ");
					if (responseCode == HttpURLConnection.HTTP_OK) {

						InputStream in = urlConnection.getInputStream();
						b = readStringResponse(b, urlConnection, destUrl, in);

					} else {
						InputStream in = urlConnection.getErrorStream();
						b = readStringResponse(b, urlConnection, destUrl, in);
					}
				} catch (IOException e) {
					b = null;
					Logger.e(TAG + url + " Attempt " + i + " Failed", e.getMessage());
				}

			}

		} catch (Exception e) {
			Logger.e(TAG + url + " Exception in Http Stack", e.getMessage());
		} finally {
			urlConnection.disconnect();
		}

		Logger.d(TAG + url + " timeTaken ", (System.currentTimeMillis() - startTime) / 1000 + " ");
		Logger.d(TAG + url + " - response ", b == null ? "" : b.toString());

		return b == null ? "" : b.toString();

	}

	/**
	 * Method setupPostParameters.
	 * @param url String
	 * @param params HashMap<String,String>
	 * @param urlConnection HttpURLConnection
	 * @param i int
	 * @param postHeaders String
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private void setupPostParameters(final String url, final HashMap<String, String> params, HttpURLConnection urlConnection, int i, String postHeaders) throws UnsupportedEncodingException,
			IOException {
		urlConnection.setDoOutput(true);
		String postParameters = getParametersToPost(params);
		printInput(url, i, postParameters, postHeaders);
		BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
		out.write(postParameters.getBytes());
		out.close();
	}

	/**
	 * Method readStringResponse.
	 * @param b StringBuilder
	 * @param urlConnection HttpURLConnection
	 * @param destUrl URL
	 * @param in InputStream
	 * @return StringBuilder
	 * @throws IOException
	 */
	private StringBuilder readStringResponse(StringBuilder b, HttpURLConnection urlConnection, final URL destUrl, InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		if (destUrl.getHost().equals(urlConnection.getURL().getHost())) {
			String line = null;
			b = new StringBuilder();
			while ((line = br.readLine()) != null) {
				b.append(line);
			}
		}
		br.close();
		return b;
	}

	/**
	 * Method setupConnectionParameters.
	 * @param headers HashMap<String,String>
	 * @param urlConnection HttpURLConnection
	 * @param i int
	 */
	private void setupConnectionParameters(final HashMap<String, String> headers, HttpURLConnection urlConnection, int i) {
		urlConnection.setConnectTimeout(CONNECTION_TIMEOUT * 1000);
		urlConnection.setReadTimeout(READ_TIMEOUT[i] * 1000);
		urlConnection.setDoInput(true);
		urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
		urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		setHeaders(urlConnection, headers);
		urlConnection.setUseCaches(false);
	}

	/**
	 * Method printInput.
	 * @param url String
	 * @param i int
	 * @param params String
	 * @param headers String
	 */
	private void printInput(final String url, int i, String params, String headers) {
		Logger.d(TAG + url + " parameters:", params);
		if (!headers.equalsIgnoreCase("")) {
			Logger.d(TAG + url + " headers " + i, headers);
		}
	}

	/**
	 * Method setHeaders.
	 * @param urlConnection URLConnection
	 * @param headers HashMap<String,String>
	 */
	private void setHeaders(URLConnection urlConnection, HashMap<String, String> headers) {

		if (headers == null)
			return;

		for (String header : headers.keySet()) {
			urlConnection.setRequestProperty(header, headers.get(header));
		}

	}

	/**
	 * Method getParametersToPost.
	 * @param params HashMap<String,String>
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	private String getParametersToPost(HashMap<String, String> params) throws UnsupportedEncodingException {

		if (params == null || params.size() == 0)
			return "";

		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (String key : params.keySet()) {
			if (first)
				first = false;
			else
				result.append('&');

			result.append(URLEncoder.encode(key, "UTF-8"));
			result.append('=');
			result.append(URLEncoder.encode(params.get(key), "UTF-8"));
		}

		return result.toString();
	}
}
