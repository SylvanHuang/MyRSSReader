package com.huangxw.rss;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 解析跳转链接，以text文本形式呈现内容
 * @author huangxw
 * @date 20141012
 */
public class NewsActivity extends Activity {

	private String TAG = "NewsActivity";
	private String title = null;
	private TextView tvTitle;
	private TextView tvContent;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// To do sth. here...
		super.onCreate(savedInstanceState);
		// 需要对html文件进行解析，提取新闻文本内容，最后付给textview
		setContentView(R.layout.news_activity);
		tvTitle = (TextView)findViewById(R.id.news_title);
		tvContent = (TextView)findViewById(R.id.news_content);
		//tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
		Log.d(TAG,"onCreate");
		Intent intent = getIntent();
		//Log.d(TAG,"link="+intent.getStringExtra("link"));
		title = intent.getStringExtra("title");
		new ParseHtmlAsyncTask().execute(intent.getStringExtra("link"));
	}
	/**
	 * @author huangxw
	 * @param url html链接
	 * @return 文章标题和内容，以Article对象返回
	 */
	private Article parseHtml(String url){
		// read html by url
		Log.d(TAG, "parseHtml...");
		StringBuffer result = new StringBuffer();//返回结果
		//Document doc = Jsoup.parse(url);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//article_title
//		Element titleDiv = doc.getElementById("article");
//		String articleTitle = null;
//		if(titleDiv != null){
//			Elements h1_ele = titleDiv.getElementsByTag("h1");
//			articleTitle = h1_ele.get(0).text();
//			Log.d(TAG, "TITLE="+articleTitle);
//		}
//		Log.d(TAG, "TITLE="+articleTitle);
		//main_content
		Element content = doc.getElementById("main_content");//main_content start
		if(content != null){
			Elements ps = content.getElementsByTag("p");
			for(Element pText : ps){
				//String style = pText.data();//样式、渲染
				String text = pText.text();//文本
				if(text != null){
					text = "\t\t" + text.trim() + "\n";
					result.append(text);
				}
			}
		}else{
			//部分凤凰页面不带div id=main_content，只带div id=article_real
			content = doc.getElementById("artical_real");
			Log.d(TAG, "article_real content = "+content);
			if(content != null){
				Log.d(TAG, "if content = "+content);
				Elements ps = content.getElementsByTag("p");
				for(Element pText : ps){
					//String style = pText.data();//样式、渲染
					String text = pText.text();//文本
					if(text != null){
						text = "\t\t" + text.trim() + "\n";
						result.append(text);
					}
				}
			}
		}
		Article article = new Article();
		article.setTitle(title);
		article.setContent(result.toString());
		return article;
	}
	
	//使用异步任务处理耗时操作
    private class ParseHtmlAsyncTask extends AsyncTask<String, Void, Article>{
    	ProgressDialog tipDialog;
    	@Override 
    	protected void onPreExecute(){
    		super.onPreExecute();
    		tipDialog = ProgressDialog.show(NewsActivity.this, null, "正在加载新闻...", false, true);
    	}
    	@Override
    	protected Article doInBackground(String... params) {
    		Log.d(TAG,"doInBackground...");
    		if(params[0] != null)
    			return parseHtml(params[0]);
    		else
    			return null;
    	}
    	@Override
    	protected void onPostExecute(Article result) {
    		Log.d(TAG,"onPostExecute");
    		//set textview here...
    		if(tipDialog != null)
    			tipDialog.dismiss();
    		if(result != null){
    			//title textview
    			tvTitle.setText(result.getTitle());
    			//content textview
    			tvContent.setText(result.getContent());
    		}else{
    			Toast.makeText(NewsActivity.this, "加载失败......", Toast.LENGTH_SHORT).show();
    		}

    	}
    }
    /**
     * @brief Article类，包含标题和正文内容
     * @author huangxw
     */
    private class Article{
    	private String title = null;
    	private String content = null;
    	
    	public String getTitle(){
    		return title;
    	}
    	public void setTitle(String title){
    		this.title = title;
    	}
    	public String getContent(){
    		return content;
    	}
    	public void setContent(String content){
    		this.content = content;
    	}
    }
}
