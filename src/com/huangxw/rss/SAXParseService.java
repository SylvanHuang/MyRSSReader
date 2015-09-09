package com.huangxw.rss;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
/**
 * SAX解析xml文件，不过由于无法完全解析从网络上下载下来的xml文件，因此 ，改用pull方式解析
 * huangxw20141010
 */
public class SAXParseService {
	
	public  List<Item> getItemList(InputStream is)throws Exception{
        SAXParserFactory factory = SAXParserFactory.newInstance();  
        SAXParser parser = factory.newSAXParser();
        RssHandler handler = new RssHandler();
        parser.parse(is, handler);
        return handler.getList();
	}
  
	private final class RssHandler extends DefaultHandler{
        private List<Item> list = null;	//保存XML解析的结果
        private Item item;
        private String tag;
        private StringBuffer buffer;
          
        public List<Item> getList() {
            return list;
        }
        //XML文件开始解析时调用此方法
        @Override
        public void startDocument() throws SAXException {
            list = new ArrayList<Item>();//开始解析时，实例化list
        }
        //解析到Element的开头时调用此方法
        @Override
        public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {

        	if("channel".equals(localName)){
        	  	return;
        	}
        	if("item".equals(localName)){
                item = new Item();
                item.setId(attributes.getValue(0));//提取item节点id属性值
            }
            tag = localName;
            buffer = new StringBuffer();
        }
        //取得Element的开头结尾中间夹的字符串
        @Override
        public void characters(char[] ch, int start, int length)throws SAXException {
            String textdata = new String(ch,start,length);
            if(buffer != null) 
            	buffer.append(textdata); //此种方式可处理含CDATA的标签
        }
        @Override
        public void endElement(String uri, String localName, String qName)throws SAXException {
        	if(tag != null){
        		String contentValue = buffer.toString();
                if("title".equals(tag)){		
                    item.setTitle(contentValue.trim());  //提取标题名称
                }else if("description".equals(tag)){
                	item.setDescription(contentValue.trim());  //提取详细信息
                }else if("link".equals(tag)){
                	item.setLink(contentValue.trim()); //提取链接信息
                }
        	}    
        	tag = null;
            if("item".equals(localName)){
                list.add(item);
                item = null;
            }
        }
  	}
}
