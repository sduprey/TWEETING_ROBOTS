package com.test;

import java.sql.SQLException;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.factory.AccountTwitterFactory;
import com.urlutilities.QuotesInfo;
import com.urlutilities.QuotesUtilities;

import crawl4j.vsm.CorpusCache;

public class CheckingHomeTimeLineForRelevantQuotes {
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
            for (Status status : statuses) {
            	String incomingTwittingText = status.getText();
                System.out.println("@" + status.getUser().getScreenName() + " - " + incomingTwittingText);
    			QuotesInfo relevantQuote = QuotesUtilities.findMostPertinentQuote(incomingTwittingText);
    			System.out.println("Twitting incoming : " + incomingTwittingText);
    			System.out.println("Relevant quote : " + relevantQuote.getQuotes() + relevantQuote.getAdequation());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
	}
}
