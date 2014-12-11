package com.test;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Retweets specified status.
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class RetweetStatus {
    /**
     * Usage: java twitter4j.examples.tweets.RetweetStatus [status id]
     *
     * @param args message
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.tweets.RetweetStatus [status id]");
            System.exit(-1);
        }
        System.out.println("Retweeting the status id - [" + args[0] + "].");
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            twitter.retweetStatus(Long.parseLong(args[0]));
            System.out.println("Successfully retweeted status [" + args[0] + "].");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to retweet: " + te.getMessage());
            System.exit(-1);
        }
    }
}