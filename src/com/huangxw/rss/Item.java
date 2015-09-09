package com.huangxw.rss;
/*
 * huangxw
 */
public class Item {
	private String id;          //id
	private String title;	    //����
	private String description; //����
	private String link;        //����
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		// need to truncate the <a href http.../>
		int index = -1;
		index = description.indexOf("<a href=");
		if(index != -1){
			//��ȡ�ӿ�ͷ��description��"<a href="�����ַ��Ӵ�
			String subDescription = description.substring(0, index-1);
			subDescription = subDescription.trim();//ȥ����β�ո�
			int cssIndex = -1;
			cssIndex = subDescription.indexOf(".vPly{");
			if(cssIndex == -1){
				this.description = "\t\t" + subDescription + "[����鿴����]" + "\n";
				return;
			}
			subDescription = "";
			this.description = subDescription;
			return;
		}
		this.description = "\t\t" + description.trim() + "[����鿴����]" + "\n";
	}
	public String getLink(){
		return link;
	}
	public void setLink(String link){
		this.link = link;
	}
//	@Override
//	public String toString() {
//		return "*" + title + "\n" + "\t\t" + description;
//	}
}