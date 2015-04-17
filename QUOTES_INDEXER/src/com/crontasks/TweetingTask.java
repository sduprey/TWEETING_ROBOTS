package com.crontasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.factory.AccountTwitterFactory;
import com.urlutilities.QuotesInfo;
import com.urlutilities.QuotesUtilities;
import com.urlutilities.RankingItem;

public class TweetingTask extends TimerTask {
	private Set<String> trendingKeywords;
	private Map<Integer,Status> pertinentStatus;
	private static int retweetLimit = 140;
	@Override
	public void run() {
		////		List<Status> toLoop = new ArrayList<Status>(pertinentStatus);
		//		List<Status> toLoop = new ArrayList<Status>();
		//		toLoop.addAll(pertinentStatus);
		//		for (Status status : pertinentStatus){
		//			System.out.println(status.getUser().getName() + " : " + status.getText());
		//		}

		List<Status> statuses_to_check = new ArrayList<Status>();
		Iterator<Map.Entry<Integer,Status>> cat_counter_it = pertinentStatus.entrySet().iterator();
		while (cat_counter_it.hasNext()) {
			Map.Entry<Integer,Status> pairs = (Map.Entry<Integer,Status>)cat_counter_it.next();
			// we are here just interested by our argument naming
			Status status = pairs.getValue();
			statuses_to_check.add(status);
		}

		if (statuses_to_check.size()>0){
			RankingItem winningItem = QuotesUtilities.checkingHomeTimeLineForRelevantQuotes(statuses_to_check);
			if (winningItem != null){
				Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
				Status winningStatus = winningItem.getTwittingStatus();
				QuotesInfo relevantQuote = winningItem.getQuoteInfos();
				System.out.println("@" + winningStatus.getUser().getScreenName() + " - " + winningStatus.getText());
				System.out.println("Relevant quote : " + relevantQuote.getQuotes()+" and adequation : "+winningItem.getAdequation());
				// responding to the tweet
				String replyingString = ".@" + winningStatus.getUser().getScreenName() + " " +relevantQuote.getWholeTwitterQuotes();
				if (replyingString.length() <=retweetLimit){ 
					System.out.println("Best ranking item to be twitted");
					StatusUpdate stat= new StatusUpdate(replyingString);
					stat.setInReplyToStatusId(winningStatus.getId());
					MediaEntity[] entities = winningStatus.getMediaEntities();
					if(entities.length > 0){
						long[] mediaIds = new long[entities.length];
						for (int i=0;i<entities.length;i++){
							mediaIds[i]=entities[i].getId();
						}
						stat.setMediaIds(mediaIds);
					}
					try {
						twitter.updateStatus(stat);
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Trouble sending the tweet "+stat);
					}
				}
			}
		}
	}

	public Set<String> getTrendingKeywords() {
		return trendingKeywords;
	}

	public void setTrendingKeywords(Set<String> trendingKeywords) {
		this.trendingKeywords = trendingKeywords;
	}

	public Map<Integer,Status> getPertinentStatus() {
		return pertinentStatus;
	}

	public void setPertinentStatus(Map<Integer,Status> pertinentStatus) {
		this.pertinentStatus = pertinentStatus;
	}
}
