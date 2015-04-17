package com.factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class AccountTwitterFactory {

	private static String wiseman_credential_path = "/home/sduprey/My_Data/My_Twitter_Conf/twitter4j_wiseman.properties";

	public static TwitterStream getWiseManTwitterStream(){
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(wiseman_credential_path);
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
		String consumerKeystr = props.getProperty("oauth.consumerKey");
		String consumerSecretstr = props.getProperty("oauth.consumerSecret");
		String accessTokenstr = props.getProperty("oauth.accessToken");
		String accessTokenSecretstr = props.getProperty("oauth.accessTokenSecret");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(consumerKeystr)
		.setOAuthConsumerSecret(consumerSecretstr)
		.setOAuthAccessToken(accessTokenstr)
		.setOAuthAccessTokenSecret(accessTokenSecretstr);

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		return twitterStream;
	}

	public static Twitter getWiseManTwitter(){
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(wiseman_credential_path);
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
		String consumerKeystr = props.getProperty("oauth.consumerKey");
		String consumerSecretstr = props.getProperty("oauth.consumerSecret");
		String accessTokenstr = props.getProperty("oauth.accessToken");
		String accessTokenSecretstr = props.getProperty("oauth.accessTokenSecret");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(consumerKeystr)
		.setOAuthConsumerSecret(consumerSecretstr)
		.setOAuthAccessToken(accessTokenstr)
		.setOAuthAccessTokenSecret(accessTokenSecretstr);

		TwitterFactory twitterFactory = new TwitterFactory(cb.build());
		Twitter twitter = twitterFactory.getInstance();
		return twitter;
	}
}
