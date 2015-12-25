package com.example.aisleconnect;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final int ACTIVITY_CODE_BROWSER = 100;
	private static final String USER_AGENT = "Mozilla/5.0";
	private EditText accountEdit;
	private EditText passwordEdit;
	private Button btnSign;
	private static final String hostURL = "https://apistage2.aisleconnect.us/ac.api/rest/v2.0/checklist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(policy);
    	btnSign = (Button) findViewById(R.id.button);
    	accountEdit = (EditText) findViewById(R.id.editText1);
    	passwordEdit = (EditText) findViewById(R.id.editText2);
    	
    	btnSign.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
            	String account = accountEdit.getText().toString();
            	String password = passwordEdit.getText().toString();

				InputStream is = getInputStream(hostURL, account, password);
				if (is != null)
				{
					startBookVBrowser(account, password);
				}
				else
				{
					Toast.makeText(MainActivity.this, R.string.sign_error, Toast.LENGTH_LONG).show();
				}  	
    		}
    	});
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult: " + requestCode);
		finish();
	}

	private void startBookVBrowser(String account, String password)
	{
		Intent intent = new Intent(this, BookVBrowserActivity.class);
		Bundle extras = new Bundle();
        extras.putString("Account", account);
        extras.putString("Password", password);
        intent.putExtra("Extras", extras);
		startActivityForResult(intent, ACTIVITY_CODE_BROWSER);
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
