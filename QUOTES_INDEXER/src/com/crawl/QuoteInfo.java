package com.crawl;

public class QuoteInfo {
	private String url="";
	private String text="";
	private String name="";

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return text.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof QuoteInfo))
			return false;
		if (obj == this)
			return true;
		QuoteInfo rhs = (QuoteInfo) obj;
		return this.text.equals(rhs.getText());
	}

}		
