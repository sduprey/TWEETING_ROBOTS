package com.crontasks;

import java.sql.SQLException;
import java.util.List;

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

public class QuotesDailyRespondingTheRetweeting {
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
				System.out.println("Best ranking item to be twitted");
				Status winningStatus = winningItem.getTwittingStatus();
				QuotesInfo relevantQuote = winningItem.getQuoteInfos();
				System.out.println("@" + winningStatus.getUser().getScreenName() + " - " + winningStatus.getText());
				System.out.println("Relevant quote : " + relevantQuote.getQuotes()+" and adequation : "+winningItem.getAdequation());
				// retweeting the tweet		
	            Status retweetedStatus = twitter.retweetStatus(winningStatus.getId());
				String replyingString = "RT @" + winningStatus.getUser().getScreenName() + " " +relevantQuote.getWholeTwitterQuotes();
				if (replyingString.length() <=retweetLimit){ 
					System.out.println("Best ranking item to be twitted");
					StatusUpdate stat= new StatusUpdate(replyingString);
					stat.setInReplyToStatusId(retweetedStatus.getId());
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
