package com.example.aisleconnect.browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class BookProvider {
	private static final String TAG = "BookProvider";
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String TAG_COUNT = "count";
	private static final String TAG_DATA = "data";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_PUBLISHER = "publisher";
	private static final String TAG_IMAGEURL = "imageUrl";
	private static List<ProductCategory> productList;
	
	protected JSONObject parseUrl(String url, String account, String password) {
		InputStream is = null;
		is = getInputStream(url, account, password);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer response = new StringBuffer();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			String json = response.toString();
			return new JSONObject(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	public static List<ProductCategory> buildProduct(String url, String account, String password) throws JSONException {
		if (productList != null)
			return productList;
		productList = new ArrayList<ProductCategory>();
		JSONObject jsonObj = new BookProvider().parseUrl(url, account, password);
		String count = jsonObj.getString(TAG_COUNT);
		Log.d(TAG, "count: "+ count);
		JSONArray datas = jsonObj.getJSONArray(TAG_DATA);
		if (datas != null)
		{
			Log.d(TAG, "datas.length: "+ datas.length());
			JSONObject data = datas.getJSONObject(0);
			JSONArray products = data.getJSONArray(TAG_PRODUCTS);
			if (products != null)
			{
				Log.d(TAG, "products.length: "+ products.length());
				for (int i = 0; i < products.length(); i += 1) {
					JSONObject product = products.getJSONObject(i);
					int id = product.getInt(TAG_ID);
					Log.d(TAG, "id: "+ id);
					String bookName = product.getString(TAG_NAME);
					Log.d(TAG, "bookName: "+ bookName);
					String publisher = product.getString(TAG_PUBLISHER);
					Log.d(TAG, "publisher: "+ publisher);
					String imageUrl = product.getString(TAG_IMAGEURL);
					Log.d(TAG, "imageUrl: "+ imageUrl);
					productList.add(buildProductInfo(id, bookName, publisher, imageUrl));
				}
			}			
		}
		return productList;
	}
	
	private static ProductCategory buildProductInfo(int id, String bookName, String publisher, String imageUrl) {
		ProductCategory product = new ProductCategory();
		product.setProductID(id);
		product.setBookName(bookName);
		product.setPublisher(publisher);
		product.setImageUrl(imageUrl);
		return product;
	}
	
	private void trustEveryone() { 
        try { 
        	HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){ 
        	@Override
        	public boolean verify(String hostname, SSLSession session) {
        		// TODO Auto-generated method stub
        		return true;
			}}); 
        	SSLContext context = SSLContext.getInstance("TLS"); 
        	context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
        	public X509Certificate[] getAcceptedIssuers() { 
        		return new X509Certificate[0]; 
        	}
        	@Override
        	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        		// TODO Auto-generated method stub		
        	}
        	@Override
        	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        		// TODO Auto-generated method stub
			}}}, new SecureRandom()); 
        	HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory()); 
        } catch (Exception e) { // should never happen 
        	e.printStackTrace(); 
        } 
    } 
    
    private InputStream getInputStream(String urlStr, String user, String password)
    {
    	InputStream in = null;
    	trustEveryone();

        // Add any data you wish to post here
        try {
        	URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // Use this if you need SSL authentication
            String userpass = user + ":" + password;
            String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
            conn.setRequestProperty("Authorization", basicAuth);

            // set Timeout and method
            conn.setReadTimeout(7000);
            conn.setConnectTimeout(7000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);        	
        	conn.connect();
        	in = conn.getInputStream();
        } catch (IOException e) { // should never happen
        	Log.e(TAG, "getInputStream:IOException: ");
        	return null;
        }
        return in;
    }
}
