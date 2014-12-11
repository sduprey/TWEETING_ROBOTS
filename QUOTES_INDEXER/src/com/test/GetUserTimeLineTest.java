package com.test;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.factory.AccountTwitterFactory;

public class GetUserTimeLineTest {
	public static void main(String[] args) {
        // gets Twitter instance with default credentials
		Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
        try {
            List<Status> statuses;
            String user = "@stefanduprey";
            statuses = twitter.getUserTimeline(user);
            System.out.println("Showing @" + user + "'s user timeline.");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
}
