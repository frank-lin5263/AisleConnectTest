package com.example.aisleconnect.browser;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductCategory implements Parcelable {
	private int productID;
	private String mName;
	private String mImageUrl;
	private String mPublisher;
	
	public static final Parcelable.Creator<ProductCategory> CREATOR = new Parcelable.Creator<ProductCategory>(){
		public ProductCategory createFromParcel(Parcel src) {
            return new ProductCategory(src);
        }

        public ProductCategory[] newArray(int size) {
            return new ProductCategory[size];
        }		
	};
	
	public ProductCategory() {
		productID = -1;
		mName = "Unknown";
		mImageUrl = "Unknown";
		mPublisher = "Unknown";
	}
	
	private ProductCategory(Parcel source)
	{
		productID = source.readInt();
		mName = source.readString();
		mImageUrl = source.readString();
		mPublisher = source.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(productID);
		dest.writeString(mName);
		dest.writeString(mImageUrl);
		dest.writeString(mPublisher);
	}
	
	public int getProductID() {
		return productID;
	}

	public String getBookName() {
		return mName;
	}
	
	public String getImageUrl() {
		return mImageUrl;
	}
	
	public String getPublisher() {
		return mPublisher;
	}
	
	public void setProductID(int id) {
		productID = id;
	}
	
	public void setBookName(String bookName) {
		mName = bookName;
	}
	
	public void setImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}
	
	public void setPublisher(String publisher) {
		mPublisher = publisher;
	}
}
