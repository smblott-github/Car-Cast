package com.jadn.cc.ui;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jadn.cc.R;

public class Settings extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String version="";
		try {
			String ourPackage = CarCast.class.getPackage().getName();
			int lastDot = ourPackage.lastIndexOf('.');
			ourPackage = ourPackage.substring(0, lastDot);
			PackageInfo pInfo = getPackageManager().getPackageInfo(ourPackage, PackageManager.GET_META_DATA);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e("Settings","lookging up own version", e);
		}
		addPreferencesFromResource(R.xml.settings);
		
		setTitle(BaseActivity.getAppTitle()+": "+BaseActivity.getVersion()+" / "+version);
	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String accounts = app_preferences.getString("accounts", "");
		if (app_preferences.getBoolean("emailSecret", false)) {
			if (!accounts.startsWith("anon:")) {
				SharedPreferences.Editor editor = app_preferences.edit();
				editor.putString("accounts", "anon:" + System.currentTimeMillis());
				editor.commit();
			}
		} else {
			if (accounts.startsWith("anon:")) {
				SharedPreferences.Editor editor = app_preferences.edit();
				// We use null to mean ask google.
				editor.putString("accounts", null);
				editor.commit();
			}
		}

	}
}
