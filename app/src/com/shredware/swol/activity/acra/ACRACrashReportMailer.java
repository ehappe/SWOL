package com.shredware.swol.activity.acra;

import android.content.Context;
import android.util.Log;

import org.acra.ReportField;
import org.acra.data.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** 
 * Based on acra-mailer(https://github.com/d-a-n/acra-mailer) of d-a-n. 
 */
public class ACRACrashReportMailer implements ReportSender {
	private final static String BASE_URL = "http://skubware.de/de.skubware.opentraining/acra_crash.php";
	private final static String SHARED_SECRET = "my_on_github_with_everyone_shared_secret";
	private Map<String, String> custom_data = null;

	public ACRACrashReportMailer() {
	}

	public ACRACrashReportMailer(HashMap<String, String> custom_data) {
		this.custom_data = custom_data;
	}

	public void send(Context c, CrashReportData report) throws ReportSenderException {

		String url = getUrl();
		Log.e("xenim", url);

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			if (custom_data != null) {
				for (Map.Entry<String, String> entry : custom_data.entrySet()) {
					parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			parameters.add(new BasicNameValuePair("DATE", new Date().toString()));
			parameters.add(new BasicNameValuePair("REPORT_ID", report.getString(ReportField.REPORT_ID)));
			parameters.add(new BasicNameValuePair("APP_VERSION_CODE", report.getString(ReportField.APP_VERSION_CODE)));
			parameters.add(new BasicNameValuePair("APP_VERSION_NAME", report.getString(ReportField.APP_VERSION_NAME)));
			parameters.add(new BasicNameValuePair("PACKAGE_NAME", report.getString(ReportField.PACKAGE_NAME)));
			parameters.add(new BasicNameValuePair("FILE_PATH", report.getString(ReportField.FILE_PATH)));
			parameters.add(new BasicNameValuePair("PHONE_MODEL", report.getString(ReportField.PHONE_MODEL)));
			parameters.add(new BasicNameValuePair("ANDROID_VERSION", report.getString(ReportField.ANDROID_VERSION)));
			parameters.add(new BasicNameValuePair("BUILD", report.getString(ReportField.BUILD)));
			parameters.add(new BasicNameValuePair("BRAND", report.getString(ReportField.BRAND)));
			parameters.add(new BasicNameValuePair("PRODUCT", report.getString(ReportField.PRODUCT)));
			parameters.add(new BasicNameValuePair("TOTAL_MEM_SIZE", report.getString(ReportField.TOTAL_MEM_SIZE)));
			parameters.add(new BasicNameValuePair("AVAILABLE_MEM_SIZE", report.getString(ReportField.AVAILABLE_MEM_SIZE)));
			parameters.add(new BasicNameValuePair("CUSTOM_DATA", report.getString(ReportField.CUSTOM_DATA)));
			parameters.add(new BasicNameValuePair("STACK_TRACE", report.getString(ReportField.STACK_TRACE)));
			parameters.add(new BasicNameValuePair("INITIAL_CONFIGURATION", report.getString(ReportField.INITIAL_CONFIGURATION)));
			parameters.add(new BasicNameValuePair("CRASH_CONFIGURATION", report.getString(ReportField.CRASH_CONFIGURATION)));
			parameters.add(new BasicNameValuePair("DISPLAY", report.getString(ReportField.DISPLAY)));
			parameters.add(new BasicNameValuePair("USER_COMMENT", report.getString(ReportField.USER_COMMENT)));
			parameters.add(new BasicNameValuePair("USER_APP_START_DATE", report.getString(ReportField.USER_APP_START_DATE)));
			parameters.add(new BasicNameValuePair("USER_CRASH_DATE", report.getString(ReportField.USER_CRASH_DATE)));
			parameters.add(new BasicNameValuePair("DUMPSYS_MEMINFO", report.getString(ReportField.DUMPSYS_MEMINFO)));
			parameters.add(new BasicNameValuePair("DROPBOX", report.getString(ReportField.DROPBOX)));
			parameters.add(new BasicNameValuePair("LOGCAT", report.getString(ReportField.LOGCAT)));
			parameters.add(new BasicNameValuePair("EVENTSLOG", report.getString(ReportField.EVENTSLOG)));
			parameters.add(new BasicNameValuePair("RADIOLOG", report.getString(ReportField.RADIOLOG)));
			parameters.add(new BasicNameValuePair("IS_SILENT", report.getString(ReportField.IS_SILENT)));
			parameters.add(new BasicNameValuePair("DEVICE_ID", report.getString(ReportField.DEVICE_ID)));
			parameters.add(new BasicNameValuePair("INSTALLATION_ID", report.getString(ReportField.INSTALLATION_ID)));
			parameters.add(new BasicNameValuePair("USER_EMAIL", report.getString(ReportField.USER_EMAIL)));
			parameters.add(new BasicNameValuePair("DEVICE_FEATURES", report.getString(ReportField.DEVICE_FEATURES)));
			parameters.add(new BasicNameValuePair("ENVIRONMENT", report.getString(ReportField.ENVIRONMENT)));
			parameters.add(new BasicNameValuePair("SETTINGS_SYSTEM", report.getString(ReportField.SETTINGS_SYSTEM)));
			parameters.add(new BasicNameValuePair("SETTINGS_SECURE", report.getString(ReportField.SETTINGS_SECURE)));
			parameters.add(new BasicNameValuePair("SHARED_PREFERENCES", report.getString(ReportField.SHARED_PREFERENCES)));
			parameters.add(new BasicNameValuePair("APPLICATION_LOG", report.getString(ReportField.APPLICATION_LOG)));
			parameters.add(new BasicNameValuePair("MEDIA_CODEC_LIST", report.getString(ReportField.MEDIA_CODEC_LIST)));
			parameters.add(new BasicNameValuePair("THREAD_DETAILS", report.getString(ReportField.THREAD_DETAILS)));

			httpPost.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			httpClient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getUrl() {
		String token = getToken();
		String key = getKey(token);
		return String.format("%s?token=%s&key=%s&", BASE_URL, token, key);
	}

	private String getKey(String token) {
		return md5(String.format("%s+%s", SHARED_SECRET, token));
	}

	private String getToken() {
		return md5(UUID.randomUUID().toString());
	}

	public static String md5(String s) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(s.getBytes(), 0, s.length());
		String hash = new BigInteger(1, m.digest()).toString(16);
		return hash;
	}
}
