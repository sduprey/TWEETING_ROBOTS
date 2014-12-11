package com.test;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.factory.AccountTwitterFactory;

public class FindingTheRightCitation {
	public static void main(String args[]){
        try {
            // we log in as wise man
			Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
            User user = twitter.verifyCredentials();
            // we get wise man time line
            List<Status> statuses = twitter.getHomeTimeline();
            // we hereby display the list of the wise man's home timeline
            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                String twitting_text = status.getText();
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
	}
}
