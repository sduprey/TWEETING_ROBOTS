package com.test;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.factory.AccountTwitterFactory;

public class GetAvailableTrendsTest {
    public static void main(String[] args) {
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
                	    System.out.println(trends.getTrends()[i].getName());
                	}
                }
            }
            System.out.println("done.");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get trends: " + te.getMessage());
            System.exit(-1);
        }
    }
}
