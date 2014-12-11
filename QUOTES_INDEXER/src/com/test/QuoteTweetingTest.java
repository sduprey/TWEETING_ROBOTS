package com.test;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.factory.AccountTwitterFactory;
//david.hilbert.de@gmail.com @davidhilbert_de
//wise.man.quoting@gmail.com @wiseman_quoting

public class QuoteTweetingTest {
	public static void main(String[] args) {
		//String toUpdate = "I am a wise man and I'll distill to you my knowledge, day by day.";
		String toUpdate = "\"The question is, said Humpty Dumpty, which is to be master  that's all.\"Lewis Carroll";

		if (args.length < 1) {
			System.out.println("Usage: java twitter4j.examples.tweets.UpdateStatus [text]");
			System.out.println("As you didn't provide a string, we'll use : "+toUpdate);
		}
	
		try {
			Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
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
