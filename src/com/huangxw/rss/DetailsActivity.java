package com.huangxw.rss;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
/*
 * huangxw
 */
public class DetailsActivity extends Activity{
	private WebView webView;
	protected static final String TAG = "DetailsActivity";

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailsactivity);
		Intent intent = getIntent();
		webView = (WebView)findViewById(R.id.web);
		//webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(intent.getStringExtra("link"));
		Log.d(TAG,"Link ��"+intent.getStringExtra("link"));
	    webView.setWebChromeClient(new WebChromeClient(){
	    	@Override
	    	public void onProgressChanged(WebView view, int newProgress){
	    		if(newProgress == 100){
	    			Log.d(TAG,"�������...");
	    			Toast.makeText(DetailsActivity.this, "�������......", Toast.LENGTH_SHORT).show();
	    		}else if(0==newProgress%20){
	    			Log.d(TAG,"������...");
	    			Toast.makeText(DetailsActivity.this, "������......", Toast.LENGTH_SHORT).show();
	    		}
	    	}
	    });
	    webView.setWebViewClient(new WebViewClient(){
	    	@Override
	    	public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
	    		return true;
	    	}
		    @Override
		    public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error){
		    	handler.proceed();
		    }
	    });
	}
//	@Override
//	public boolean onKeyDown(int keyCoder,KeyEvent event){
//        if(webView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
//              webView.goBack();   //goBack()��ʾ����webView����һҳ��
//              return true;
//        }
//        return false;
//	}
}
