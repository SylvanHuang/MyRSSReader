package com.huangxw.rss;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

public class ItemPullService {
	/**
	 * pull����XML�ļ�
	 * @param inputStream
	 * @return
	 * @throws Throwable
	 */
	 public List<Item> getItemList(InputStream inputStream) throws Throwable  
	    {  
	        List<Item> itemList=null;  
	        Item item=null;  
	        XmlPullParser parser=Xml.newPullParser();  
	        parser.setInput(inputStream, "utf-8");  
	        int event=parser.getEventType();  
	        //����ѭ����ʽ��ֱ����ȡ��XML�ļ���ĩβ
	        while(event!=XmlPullParser.END_DOCUMENT)  
	        {  
	            switch (event)  
	            {  
	                case XmlPullParser.START_DOCUMENT:  //��ʼ�ĵ�
	                	itemList=new ArrayList<Item>();  
	                    break;  
	                case XmlPullParser.START_TAG:  				//��ʼ�ڵ�
	                    if("item".equals(parser.getName()))  
	                    {  
	                        item=new Item();  
	                    }  
	                    if(item!=null)  
	                    {  
	                    	//parser.getName()���õ���ǰ�ڵ�����
	                        if("title".equals(parser.getName()))  
	                        {  
	                            String title =parser.nextText().toString();  
	                            item.setTitle(title);
	                        }  
	                        else if("link".equals(parser.getName()))  
	                        {  
	                           String link = parser.nextText().toString();
	                           item.setLink(link);
	                        }
	                        else if("description".equals(parser.getName()))  
	                        {  
		                         String description = parser.nextText().toString();
		                         item.setDescription(description);
		                    }   
	                    }  
	                    break;  
	                case XmlPullParser.END_TAG:   //�����ڵ�
	                    if("item".equals(parser.getName()))  
	                    {  
	                    	// ȥ������Ϊ�ջ��߽�β��Ϊ.html���߲�Ϊ.htm���߼��Ϊ�յ�Item
	                    	if((item.getLink()!=null)
	                    			&&(item.getDescription().trim()!=null)
	                    			&&(item.getLink().endsWith(".shtml")||item.getLink().endsWith(".html"))){
	                    		itemList.add(item);  
	                    		item=null;  
	                    	}
	                    }  
	                    break;  
	                default:  
	                    break;  
	            }  
	            event=parser.next();  
	        }  
	        return itemList;  
	    }  
}
