package com.crontasks;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import twitter4j.FilterQuery;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;

import com.factory.AccountTwitterFactory;
import com.urlutilities.QuotesUtilities;

import crawl4j.vsm.CorpusCache;

public class TrendListeningAndReplyingTaskLauncher {

	private static int nb_request_max = 23 ;
	private static int delayed_seconds = 3600;
	//private static int delayed_seconds = 30;

	private static int nb_tweets = 0;
	private static int max_size = 3000;
	private static Set<String> trendingKeywords = new HashSet<String>();
	//private static  List<Status> pertinentTweets = new ArrayList<Status>();
	private static  Map<Integer, Status> pertinentTweets = new  ConcurrentHashMap<Integer, Status>();
	
//	private static  List<Status> pertinentTweets = Collections.synchronizedList(new ArrayList<Status>());

	public static void main(String args[]) {
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
		System.out.println("Finding the Worldwide biggest trends");
		try {
			Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
			ResponseList<Location> locations;
			locations = twitter.getAvailableTrends();
			System.out.println("Showing available trends");
			for (Location location : locations) {
				System.out.println(location.getName() + " (woeid:" + location.getWoeid() + ")");
				if ("Worldwide".equals(location.getName())){
					System.out.println("Getting the world wide trends : "+location.getName());
					Trends trends = twitter.getPlaceTrends(location.getWoeid());
					for (int i = 0; i < trends.getTrends().length; i++) {
						System.out.println(" Trend : " + trends.getTrends()[i].getName());
						trendingKeywords.add(trends.getTrends()[i].getName());
					}
				}
			}
			System.out.println("done finding the biggest trends.");
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get trends: " + te.getMessage());
			System.exit(-1);
		}

		System.out.println("Launching the tweeting stream listener");
		StatusListener listener = new StatusListener(){
			public void onStatus(Status status) {
				//System.out.println(status.getUser().getName() + " : " + status.getText());
				nb_tweets++;
				pertinentTweets.put(nb_tweets, status);
				//pertinentTweets.add(status);
				if (pertinentTweets.size() > max_size){
					pertinentTweets.clear();
				}
			}
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub		
			}
		};

		if (trendingKeywords.size() > 0 ){
			System.out.println("Keywords launched : ");
			for (String keyword : trendingKeywords){
				System.out.println("Keyword : "+keyword);		
			}

			TwitterStream twitterStream = AccountTwitterFactory.getWiseManTwitterStream();

			// old plain output
			//	    twitterStream.addListener(listener);
			// sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
			//	    twitterStream.sample(); 

			FilterQuery fq = new FilterQuery();   
			String[] keywords = trendingKeywords.toArray(new String[trendingKeywords.size()]);
			fq.track(keywords);        
			twitterStream.addListener(listener);
			twitterStream.filter(fq);      


			System.out.println("Java tweeting timer is about to start");
			Timer timer = new Timer(); 
			int nb_request=0;
			while (nb_request < nb_request_max){
				TweetingTask task = new TweetingTask();
				task.setTrendingKeywords(trendingKeywords);
				task.setPertinentStatus(pertinentTweets);
				timer.schedule(task, nb_request*delayed_seconds*1000); //delay in milliseconds
				nb_request++;
			}
			System.out.println("All tweets for the day launched !");
		}
	}

}
