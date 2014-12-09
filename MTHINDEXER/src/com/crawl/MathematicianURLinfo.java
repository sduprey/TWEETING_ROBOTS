package com.crawl;

import java.util.Date;

public class MathematicianURLinfo {
	private String url="";
	private String text="";
	private String name="";
	private String birth_location="";
	private Date birth_date;
	private String death_location="";
	private Date death_date;
	private String out_links="";
	private String title="";
	private String short_description="";
	private String h1="";
	private int status_code=0;
	private int depth=0;
	private int links_size=0;
	
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
	public String getBirth_location() {
		return birth_location;
	}
	public void setBirth_location(String birth_location) {
		this.birth_location = birth_location;
	}
	public Date getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}
	public String getDeath_location() {
		return death_location;
	}
	public void setDeath_location(String death_location) {
		this.death_location = death_location;
	}
	public Date getDeath_date() {
		return death_date;
	}
	public void setDeath_date(Date death_date) {
		this.death_date = death_date;
	}
	public int getLinks_size() {
		return links_size;
	}
	public void setLinks_size(int links_size) {
		this.links_size = links_size;
	}
	public String getOut_links() {
		return out_links;
	}
	public void setOut_links(String out_links) {
		this.out_links = out_links;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getH1() {
		return h1;
	}
	public void setH1(String h1) {
		this.h1 = h1;
	}
	public int getStatus_code() {
		return status_code;
	}
	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getShort_description() {
		return short_description;
	}
	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}
}		
