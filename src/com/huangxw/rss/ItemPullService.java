package com.huangxw.rss;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

public class ItemPullService {
	/**
	 * pull解析XML文件
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
	        //采用循环方式，直到读取到XML文件的末尾
	        while(event!=XmlPullParser.END_DOCUMENT)  
	        {  
	            switch (event)  
	            {  
	                case XmlPullParser.START_DOCUMENT:  //开始文档
	                	itemList=new ArrayList<Item>();  
	                    break;  
	                case XmlPullParser.START_TAG:  				//开始节点
	                    if("item".equals(parser.getName()))  
	                    {  
	                        item=new Item();  
	                    }  
	                    if(item!=null)  
	                    {  
	                    	//parser.getName()：得到当前节点名称
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
	                case XmlPullParser.END_TAG:   //结束节点
	                    if("item".equals(parser.getName()))  
	                    {  
	                    	// 去除链接为空或者结尾不为.html或者不为.htm或者简介为空的Item
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
