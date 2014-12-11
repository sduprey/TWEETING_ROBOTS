package com.test;

import com.factory.AccountTwitterFactory;

import twitter4j.*;


public final class LookUpFriendshipTest {
	/**
	 * Usage: java twitter4j.examples.user.LookupFriendships [screen name[,screen name..]]
	 *
	 * @param args message
	 */
	public static void main(String[] args) {
		String friends_to_look = "@stefanduprey,@MuratDemir";
		try {
			Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
            User user = twitter.verifyCredentials();
            System.out.println("Successfully verified credentials of " + user.getScreenName());
			ResponseList<Friendship> friendships = twitter.lookupFriendships(friends_to_look.split(","));
			for (Friendship friendship : friendships) {
				System.out.println("@" + friendship.getScreenName()
						+ " following: " + friendship.isFollowing()
						+ " followed_by: " + friendship.isFollowedBy());
			}
			System.out.println("Successfully looked up friendships [" + args[0] + "].");
			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to lookup friendships: " + te.getMessage());
			System.exit(-1);
		}
	}
}

