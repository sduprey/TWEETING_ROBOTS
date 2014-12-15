package com.urlutilities;

public class QuotesInfo implements Comparable<QuotesInfo>{
	private String wholeTwitterQuotes;
	private String author;
	private String quotes;
	private Double adequation = new Double(0);
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getQuotes() {
		return quotes;
	}
	public void setQuotes(String quotes) {
		this.quotes = quotes;
	}
	public Double getAdequation() {
		return adequation;
	}
	public void setAdequation(Double adequation) {
		this.adequation = adequation;
	}
	public String getWholeTwitterQuotes() {
		return wholeTwitterQuotes;
	}
	public void setWholeTwitterQuotes(String wholeTwitterQuotes) {
		this.wholeTwitterQuotes = wholeTwitterQuotes;
	}
	@Override
	public int compareTo(QuotesInfo o) {
		return this.adequation.compareTo(o.getAdequation());
	}
}
