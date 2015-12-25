package com.example.aisleconnect.browser;

import java.util.List;

import com.example.aisleconnect.BookVBrowserActivity;
import com.example.aisleconnect.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

public class BookBrowserListFragmnet extends ListFragment implements LoaderManager.LoaderCallbacks<List<ProductCategory>> {
	private static final String TAG = "BookBrowserListFragmnet";
	private static final String checkListURL = "https://apistage2.aisleconnect.us/ac.api/rest/v2.0/checklist";
	private BookListAdapter mAdapter;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Log.d(TAG, "onCreate: ");
		getListView().setFastScrollEnabled(true);
		mAdapter = new BookListAdapter(getActivity());
		setEmptyText(getString(R.string.no_book_found));
		setListAdapter(mAdapter);
		setListShown(false);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onLoadFinished(Loader<List<ProductCategory>> arg0, List<ProductCategory> data) {
		// TODO Auto-generated method stub
		mAdapter.setData(data);
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}		
	}

	@Override
	public void onLoaderReset(Loader<List<ProductCategory>> arg0) {
		// TODO Auto-generated method stub
		mAdapter.setData(null);
	}

	@Override
	public Loader<List<ProductCategory>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		BookVBrowserActivity activity = (BookVBrowserActivity) getActivity();
		return new BookItemLoader(activity, checkListURL, activity.getAccount(), activity.getPassword());
	}
}
