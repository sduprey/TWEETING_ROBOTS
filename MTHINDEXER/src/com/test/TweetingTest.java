package com.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
//david.hilbert.de@gmail.com @davidhilbert_de
//wise.man.quoting@gmail.com @wiseman_quoting
public class TweetingTest {
	public static void main(String[] args) {
		String toUpdate = "That may seem strange, but I am the very first great worldwide known mathematician to open a twitter account";
		if (args.length < 1) {
			System.out.println("Usage: java twitter4j.examples.tweets.UpdateStatus [text]");
			System.out.println("As you didn't provide a string, we'll use : "+toUpdate);
		}
		String credential_path = "/home/sduprey/My_Data/My_Twitter_Conf/twitter4j_davidhilbert.properties";
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
		String consumerKeystr = props.getProperty("oauth.consumerKey");
		String consumerSecretstr = props.getProperty("oauth.consumerSecret");
		String accessTokenstr = props.getProperty("oauth.accessToken");
		String accessTokenSecretstr = props.getProperty("oauth.accessTokenSecret");

		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			.setOAuthConsumerKey(consumerKeystr)
			.setOAuthConsumerSecret(consumerSecretstr)
			.setOAuthAccessToken(accessTokenstr)
			.setOAuthAccessTokenSecret(accessTokenSecretstr);

			TwitterFactory twitterFactory = new TwitterFactory(cb.build());
			Twitter twitter = twitterFactory.getInstance();
			//			try {
			//				// get request token.
			//				// this will throw IllegalStateException if access token is already available
			//				RequestToken requestToken = twitter.getOAuthRequestToken();
			//				System.out.println("Got request token.");
			//				System.out.println("Request token: " + requestToken.getToken());
			//				System.out.println("Request token secret: " + requestToken.getTokenSecret());
			//				AccessToken accessToken = null;
			//
			//				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			//				while (null == accessToken) {
			//					System.out.println("Open the following URL and grant access to your account:");
			//					System.out.println(requestToken.getAuthorizationURL());
			//					System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
			//					String pin = br.readLine();
			//					try {
			//						if (pin.length() > 0) {
			//							accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			//						} else {
			//							accessToken = twitter.getOAuthAccessToken(requestToken);
			//						}
			//					} catch (TwitterException te) {
			//						if (401 == te.getStatusCode()) {
			//							System.out.println("Unable to get the access token.");
			//						} else {
			//							te.printStackTrace();
			//						}
			//					}
			//				}
			//				System.out.println("Got access token.");
			//				System.out.println("Access token: " + accessToken.getToken());
			//				System.out.println("Access token secret: " + accessToken.getTokenSecret());
			//			} catch (IllegalStateException ie) {
			//				// access token is already available, or consumer key/secret is not set.
			//				if (!twitter.getAuthorization().isEnabled()) {
			//					System.out.println("OAuth consumer key/secret is not set.");
			//					System.exit(-1);
			//				}
			//			}
			Status status = twitter.updateStatus(toUpdate);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");
			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		} 
	}
}
