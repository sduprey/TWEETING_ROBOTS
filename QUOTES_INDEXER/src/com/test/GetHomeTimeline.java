package com.test;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class GetHomeTimeline {
	/**
	 * Usage: java twitter4j.examples.timeline.GetHomeTimeline
	 *
	 * @param args String[]
	 */
	public static void main(String[] args) {

		String credential_path = "/home/sduprey/My_Data/My_Twitter_Conf/twitter4j_wiseman.properties";

		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(credential_path);
			props.load(in);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		String consumerKey = props.getProperty("oauth.consumerKey");
		String consumerSecret = props.getProperty("oauth.consumerSecret");
		String accessToken = props.getProperty("oauth.accessToken");
		String accessTokenSecret = props.getProperty("oauth.accessTokenSecret");

		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey(consumerKey)
			  .setOAuthConsumerSecret(consumerSecret)
			  .setOAuthAccessToken(accessToken)
			  .setOAuthAccessTokenSecret(accessTokenSecret);
			
			TwitterFactory twitterFactory = new TwitterFactory(cb.build());
			Twitter twitter = twitterFactory.getInstance();
			User user = twitter.verifyCredentials();
			List<Status> statuses = twitter.getHomeTimeline();
			System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
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