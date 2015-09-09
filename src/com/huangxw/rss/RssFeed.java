package com.huangxw.rss;

import java.util.List;
import java.util.Vector;
/**
 * @author huangxw
 * @total rss file construct
 */
public class RssFeed {
    private String channel;
    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String lastBuildDate;
    private List<Item> itemList;
    private int itemCount = 0;
    
    public RssFeed(){
    	itemList = new Vector<Item>(0);
    }
    public int addItem(Item item){
    	itemList.add(item);
    	itemCount++;
    	return itemCount;
    }
    public Item getItem(int location){
    	return itemList.get(location);
    }
    public List<Item> getAllItems(){
    	return itemList;
    }
    public String getChannel(){
    	return channel;
    }
    public void setChannel(String channel){
    	this.channel = channel;
    }
    public String getTitle(){
    	return title;
    }
    public void setTitle(String title){
    	this.title = title;
    }
    public String getLink(){
    	return link;
    }
    public void setLink(String link){
    	this.link = link;
    }
    public String getDescription(){
    	return description;
    }
    public void setDescription(String description){
    	this.description = description;
    }
    public String getPubDate(){
    	return pubDate;
    }
    public void setPubDate(String pubDate){
    	this.pubDate = pubDate;
    }
    public String getLastBuildDate(){
    	return lastBuildDate;
    }
    public void setLastBuildDate(String lastBuildDate){
    	this.lastBuildDate = lastBuildDate;
    }
}
