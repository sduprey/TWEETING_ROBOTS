package com.test;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.factory.AccountTwitterFactory;

public class VerifyCredentialsTest {
    public static void main(String[] args) {
        try {
    		Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
            User user = twitter.verifyCredentials();
            System.out.println("Successfully verified credentials of " + user.getScreenName());
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to verify credentials: " + te.getMessage());
            System.exit(-1);
        }
    }
}
