package com.crontasks;

import java.sql.SQLException;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.factory.AccountTwitterFactory;
import com.urlutilities.QuotesInfo;
import com.urlutilities.QuotesUtilities;
import com.urlutilities.RankingItem;

import crawl4j.vsm.CorpusCache;

public class QuotesDailyResponding {

	private static int retweetLimit = 140;
	public static void main(String[] args){
		// initializing all the caches, quotes, semantics corpus
		try {
			// loading the semantics english corpus to compute tf idf
			CorpusCache.load();
			// loading the quotes cache
			QuotesUtilities.fillUpQuotesCache();
			// browsing to find the most pertinent quote

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Trouble getting all quotes cache : either with the database itself or with the database being badly filled");
		}
		try {
			// gets Twitter instance with default credentials
			Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
			User user = twitter.verifyCredentials();
			List<Status> statuses = twitter.getHomeTimeline();
			System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
			RankingItem winningItem = QuotesUtilities.checkingHomeTimeLineForRelevantQuotes(statuses);
			if (winningItem != null){
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
					twitter.updateStatus(stat);
				}
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}
}
