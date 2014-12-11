package com.test;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.factory.AccountTwitterFactory;

public class GetFollowerTest {
    public static void main(String[] args) {
        try {
			Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
            long cursor = -1;
            IDs ids;
            System.out.println("Listing followers's ids.");
            do {
                if (0 < args.length) {
                    ids = twitter.getFollowersIDs(args[0], cursor);
                } else {
                    ids = twitter.getFollowersIDs(cursor);
                }
                for (long id : ids.getIDs()) {
                    System.out.println(id);
                }
            } while ((cursor = ids.getNextCursor()) != 0);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get followers' ids: " + te.getMessage());
            System.exit(-1);
        }
    }
}
