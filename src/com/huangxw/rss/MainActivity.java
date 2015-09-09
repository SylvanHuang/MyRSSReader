package com.huangxw.rss;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * RSS�����Ķ���
 * huangxw20141010
 */
public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private Spinner rssSelectSpinner;
	private ListView myListView;//������ʾ�������ص��ı����ݵ��ı��ؼ�
	private String[] rssUrlArray = null;
	private String[] rssNameArray = null;
	private RssUrl rss = new RssUrl(); 
	private List<Item> itemList;
	private String urlString = "http://news.ifeng.com/rss/index.xml";
	
	private OnItemClickListener itemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Log.d(TAG,"onItemClick");
			//so we can start a new activity to show the details 
			//Intent intent = new Intent(MainActivity.this, DetailsActivity.class);//webview����
			Intent intent = new Intent(MainActivity.this, NewsActivity.class);//textview����
			intent.putExtra("link", itemList.get(position).getLink());
			intent.putExtra("title", itemList.get(position).getTitle());
			startActivity(intent);
		}};
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rssSelectSpinner = (Spinner)findViewById(R.id.rss_source_spinner);
        initRssUrl();
        loadSpinner();
        myListView = (ListView)findViewById(R.id.my_list);//�ҵ�idΪmy_textview���ı��ؼ�
        myListView.setOnItemClickListener(itemListener);
        //Ĭ�ϼ��ص�ҳ��
        loadRss(urlString);
    }

    /**
     * ����rss
     */
    private void loadRss(String url){
        //ִ���첽����
    	if(url != null)
    		new DownloadTextAsyncTask().execute(url);//��������ҳ����
    	else
    		Toast.makeText(this, "��ѡ��rssԴ", Toast.LENGTH_SHORT).show();
    }
    /**
     * init rss sources spinner
     */
    public void initRssUrl(){
    	this.rssUrlArray = getResources().getStringArray(R.array.rss_source_urls);
    	this.rssNameArray = getResources().getStringArray(R.array.rss_source_name);
    	rss.setName(rssNameArray);
    	rss.setLink(rssUrlArray);
    }
    /**
     * ��ʼ��rssԴ�������б�
     */
    public void loadSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this
        		, android.R.layout.simple_spinner_item, rss.getName());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rssSelectSpinner.setAdapter(spinnerAdapter);
        rssSelectSpinner.setVisibility(View.VISIBLE);
        rssSelectSpinner
         	.setOnItemSelectedListener(new OnItemSelectedListener() {
     
                   public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        //parent.setVisibility(View.VISIBLE);
                	    MainActivity.this.urlString = MainActivity.this.rss.getLink()[pos];
                        loadRss(urlString);//ѡ��һ����RSSԴ���Զ����½���
                   }
                   public void onNothingSelected(AdapterView<?> parent) {
                	   // Do nothing.
                   }
         	});
     }
    /**
     * ����URL��ַ��������Ӧ������������
     */
    private InputStream openHttpConnection(String urlString) throws IOException{
    	InputStream in = null;
    	int response = -1 ;
    	
    	URL url = new URL(urlString);//����urlString����URL����
    	URLConnection conn = url.openConnection();//��http����
    	if( !(conn instanceof HttpURLConnection)){
    		throw new IOException("Not an HTTP Connection");
    	}
    	try{
    		HttpURLConnection httpConn = (HttpURLConnection)conn;
    		httpConn.setInstanceFollowRedirects(true);//���Խ�����ת
    		httpConn.setRequestMethod("GET");//get����ʽ
    		httpConn.connect();						
    		response = httpConn.getResponseCode();//��÷���˵ķ�����
    		//�����������200��˵��http������������
    		if (response == HttpURLConnection.HTTP_OK) {
    			in = httpConn.getInputStream();
    		}
    	}catch(Exception e){
    		Log.e(TAG, "occurs errors:" + e.getLocalizedMessage());
    		throw new IOException("Error connecting");
    	}
    	return in;
    }
    //�����ı�����
    private String downloadText(String url) throws UnsupportedEncodingException{
    	InputStream in = null;
    	try{
    		in = openHttpConnection(url);
    	}catch(Exception e){
    		Log.e(TAG, "Open Http Connection Occurs Errors:" + e.getLocalizedMessage());
    		return "";	//���openHttpConnection�׳��쳣���򷵻ؿ��ַ���
    	}
    	//��ȡ������//�ɸ���xmlͷ���ж���utf-8����gb2312...
    	//BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"gb2312"));
    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
    	String line = null;
    	StringBuffer result = new StringBuffer();//���ؽ��
    	try {
			while((line = bufferedReader.readLine()) != null){
				result.append(line);
			}
		} catch (IOException e) {
			Log.d(TAG, "Read InputStream occurs errors:" + e.getLocalizedMessage());
			return "";
		}
    	return result.toString();
    }
        
    //ʹ���첽�������ʱ����
    private class DownloadTextAsyncTask extends AsyncTask<String, Void, String>{
    	ProgressDialog tipDialog;
    	@Override 
    	protected void onPreExecute(){
    		super.onPreExecute();
    		tipDialog = ProgressDialog.show(MainActivity.this, null, "���ڼ�������...", false, true);
    	}
    	@Override
    	protected String doInBackground(String... params) {
    		Log.d(TAG,"doInBackground...");
    		try {
				return downloadText(params[0]);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
    	}
    	@Override
    	protected void onPostExecute(String result) {
    		Log.d(TAG,"onPostExecute");
    		if(tipDialog != null)
    			tipDialog.dismiss();
    		Log.d(TAG,"result= "+result.toString());	
    		//����ֱ�ӽ�stringת��Ϊinputstream��ʽʵ��
    		InputStream inputStream = null;
			try {
				inputStream = new ByteArrayInputStream(result.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//pull parse...good and easy
    		try {
    			if(inputStream != null)
    				itemList = new ItemPullService().getItemList(inputStream);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		ArrayList<HashMap<String, Object>> myList = new ArrayList<HashMap<String, Object>>();
    		for(int i=0;i<itemList.size();i++){
    			HashMap<String, Object> map = new HashMap<String, Object>();
    			map.put("image", R.drawable.icon_fenghuang);
    			map.put("title", itemList.get(i).getTitle());
    			map.put("description", itemList.get(i).getDescription());
    			myList.add(map);
    		}
    		SimpleAdapter mSchedule = null;
    		if(itemList != null)
    			mSchedule = new SimpleAdapter(MainActivity.this
    					, myList
    					, R.layout.my_listitem
    					,new String[]{"image","title","description"}
    					, new int[]{R.id.itemimage, R.id.itemtitle, R.id.itemtext});
    		myListView.setAdapter(mSchedule);
    		
			//sax parse...but it's not good enough!
//          try {
//            	InputStream inputStream = MainActivity.this.getAssets().open("news.xml");
//            	itemList = new SAXParseService().getItemList(inputStream);
//            	Log.d(TAG, "itemList"+itemList.toString());
//    			myListView.setAdapter(new ArrayAdapter<Item>(MainActivity.this, android.R.layout.simple_list_item_1, itemList));
//    		} catch (Exception e) {
//    			e.printStackTrace();
//    		}
    	}
    }
    /**
     * ����RssUrl�࣬����spinner�����б�������֣���ƥ��url����
     * @author huangxw
     *
     */
    private class RssUrl{
    	private String[] name;
    	private String[] link;
    	
    	public RssUrl(){
    		this.name = null;
    		this.link = null;
    	}
    	public String[] getName(){
    		return name;
    	}
    	public void setName(String[] name){
    		this.name = name;
    	}
    	public String[] getLink(){
    		return link;
    	}
    	public void setLink(String[] link){
    		this.link = link;
    	}
    }
}
