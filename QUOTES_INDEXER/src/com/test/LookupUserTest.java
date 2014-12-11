package com.test;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.factory.AccountTwitterFactory;

public class LookupUserTest {
	   public static void main(String[] args) {
			String friends_to_look = "@stefanduprey";
			//String friends_to_look = "33531571";
			try {
				Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
	            ResponseList<User> users = twitter.lookupUsers(friends_to_look.split(","));
	            for (User user : users) {
	                if (user.getStatus() != null) {
	                    System.out.println("@" + user.getScreenName() + " - " + user.getStatus().getText());
	                } else {
	                    // the user is protected
	                    System.out.println("@" + user.getScreenName());
	                }
	            }
	            System.out.println("Successfully looked up users [" + args[0] + "].");
	            System.exit(0);
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to lookup users: " + te.getMessage());
	            System.exit(-1);
	        }
	    }
}
