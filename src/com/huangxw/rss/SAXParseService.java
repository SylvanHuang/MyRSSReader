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
 * SAX����xml�ļ������������޷���ȫ����������������������xml�ļ������ ������pull��ʽ����
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
        private List<Item> list = null;	//����XML�����Ľ��
        private Item item;
        private String tag;
        private StringBuffer buffer;
          
        public List<Item> getList() {
            return list;
        }
        //XML�ļ���ʼ����ʱ���ô˷���
        @Override
        public void startDocument() throws SAXException {
            list = new ArrayList<Item>();//��ʼ����ʱ��ʵ����list
        }
        //������Element�Ŀ�ͷʱ���ô˷���
        @Override
        public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {

        	if("channel".equals(localName)){
        	  	return;
        	}
        	if("item".equals(localName)){
                item = new Item();
                item.setId(attributes.getValue(0));//��ȡitem�ڵ�id����ֵ
            }
            tag = localName;
            buffer = new StringBuffer();
        }
        //ȡ��Element�Ŀ�ͷ��β�м�е��ַ���
        @Override
        public void characters(char[] ch, int start, int length)throws SAXException {
            String textdata = new String(ch,start,length);
            if(buffer != null) 
            	buffer.append(textdata); //���ַ�ʽ�ɴ���CDATA�ı�ǩ
        }
        @Override
        public void endElement(String uri, String localName, String qName)throws SAXException {
        	if(tag != null){
        		String contentValue = buffer.toString();
                if("title".equals(tag)){		
                    item.setTitle(contentValue.trim());  //��ȡ��������
                }else if("description".equals(tag)){
                	item.setDescription(contentValue.trim());  //��ȡ��ϸ��Ϣ
                }else if("link".equals(tag)){
                	item.setLink(contentValue.trim()); //��ȡ������Ϣ
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
