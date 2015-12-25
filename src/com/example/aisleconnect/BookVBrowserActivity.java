package com.example.aisleconnect;

import com.example.aisleconnect.browser.BookBrowserListFragmnet;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class BookVBrowserActivity extends FragmentActivity {
	private static final String TAG = "BookVBrowserActivity";
	private Bundle mExtras;
	private String mAccount;
	private String mPassword;
	private FragmentManager mFragmentMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate: ");
		setContentView(R.layout.book_browser);
		mFragmentMgr = getSupportFragmentManager();
		mExtras = getIntent().getBundleExtra("Extras");
		if (mExtras != null)
		{
			mAccount = mExtras.getString("Account");
			mPassword = mExtras.getString("Password");
		}
		Log.d(TAG, "Account: " + mAccount);
		Log.d(TAG, "Password: " + mPassword);
		FragmentTransaction ft = mFragmentMgr.beginTransaction();
		ft.replace(R.id.browse, new BookBrowserListFragmnet(), "BookBrowserList");
		ft.commit();
	}
	
	public String getAccount() {
		return mAccount;
	}
	
	public String getPassword() {
		return mPassword;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume: ");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy: ");
	}

}
