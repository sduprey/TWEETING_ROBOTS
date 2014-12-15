package com.urlutilities;

import twitter4j.Status;

public class RankingItem implements Comparable<RankingItem>{
	private Status twittingStatus;
	private QuotesInfo quoteInfos;
	private Double adequation;
	public QuotesInfo getQuoteInfos() {
		return quoteInfos;
	}
	public void setQuoteInfos(QuotesInfo quoteInfos) {
		this.quoteInfos = quoteInfos;
	}
	public Status getTwittingStatus() {
		return twittingStatus;
	}
	public void setTwittingStatus(Status twittingStatus) {
		this.twittingStatus = twittingStatus;
	}
	public Double getAdequation() {
		return adequation;
	}
	public void setAdequation(Double adequation) {
		this.adequation = adequation;
	}
	@Override
	public int compareTo(RankingItem o) {
		return this.getAdequation().compareTo(o.getAdequation());
	}
}
