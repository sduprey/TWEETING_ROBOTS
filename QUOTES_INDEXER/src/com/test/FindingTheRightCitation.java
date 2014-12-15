package com.test;

import java.sql.SQLException;

import com.urlutilities.QuotesInfo;
import com.urlutilities.QuotesUtilities;

import crawl4j.vsm.CorpusCache;

public class FindingTheRightCitation {

	public static void main(String[] args){
		String[] incoming_twitter = {"Police say it is \"evolving into a negotiation\" situation between police & the armed person",
		                             "Between some new hardware (yes), and Apple TV (no), here's what Apple watchers are predicting for the year ahead."};
		try {
			// loading the semantics english corpus to compute tf idf
			CorpusCache.load();
			// loading the quotes cache
			QuotesUtilities.fillUpQuotesCache();
			// browsing to find the most pertinent quote

			for (String incomingTwitting : incoming_twitter){
				QuotesInfo relevantQuote = QuotesUtilities.findMostPertinentQuote(incomingTwitting);
				System.out.println("Twitting incoming : " + incomingTwitting);
				System.out.println("Relevant quote : " + relevantQuote.getQuotes() + relevantQuote.getAdequation());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Trouble getting all quotes cache : either with the database itself or with the database being badly filled");
		}
	}

	//	public static void main(String args[]){
	//        try {
	//            // we log in as wise man
	//			Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
	//            User user = twitter.verifyCredentials();
	//            // we get wise man time line
	//            List<Status> statuses = twitter.getHomeTimeline();
	//            // we hereby display the list of the wise man's home timeline
	//            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
	//            for (Status status : statuses) {
	//                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
	//                String twitting_text = status.getText();
	//            }
	//        } catch (TwitterException te) {
	//            te.printStackTrace();
	//            System.out.println("Failed to get timeline: " + te.getMessage());
	//            System.exit(-1);
	//        }
	//	}
}
