package com.example.aisleconnect.browser;

import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class BookItemLoader extends AsyncTaskLoader<List<ProductCategory>> {
	private static final String TAG = "BookItemLoader";
	private final String mUrl;
	private String mAccount;
	private String mPassword;
	
	public BookItemLoader(Context context, String url, String accout, String password) {
		super(context);
		// TODO Auto-generated constructor stub
		mUrl = url;
		mAccount = accout;
		mPassword = password;
	}

	@Override
	public List<ProductCategory> loadInBackground() {
		// TODO Auto-generated method stub
		try {
			return BookProvider.buildProduct(mUrl, mAccount, mPassword);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Failed to fetch checklit");
			return null;
		}
	}

	@Override
	protected void onStartLoading() {
		// TODO Auto-generated method stub
		super.onStartLoading();
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		// TODO Auto-generated method stub
		super.onStopLoading();
		cancelLoad();
	}

}
