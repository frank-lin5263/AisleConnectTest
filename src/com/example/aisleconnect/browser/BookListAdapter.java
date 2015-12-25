package com.example.aisleconnect.browser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.example.aisleconnect.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookListAdapter extends ArrayAdapter<ProductCategory> {
	private final Context mContext;

	public BookListAdapter(Context context) {
		super(context, 0);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		Bitmap bm;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ProductCategory product = getItem(position);
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.browse_row, (ViewGroup)null);
			holder = new ViewHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.textView1);
			holder.publisherView = (TextView) convertView.findViewById(R.id.textView2);
			holder.imageUrlView = (ImageView) convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.nameView.setText(product.getBookName());
		holder.publisherView.setText(product.getPublisher());
		URL url;
		try {
			url = new URL(product.getImageUrl());
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			bm = thumbnailForStream(urlConnection.getInputStream());
			holder.imageUrlView.setImageBitmap(bm);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	public void setData(List<ProductCategory> data) {
		clear();
		if (data != null)
		{
			for (ProductCategory item : data) {
				add(item);
			}
		}
	}
	
	private static Bitmap thumbnailForStream(InputStream is) throws IOException
	{
	    Bitmap b = BitmapFactory.decodeStream(is);
	    is.close();
	    return b;
	}

	private class ViewHolder {
		TextView nameView;
		TextView publisherView;
		ImageView imageUrlView;
	}
}
