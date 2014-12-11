package com.test;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.factory.AccountTwitterFactory;

public class SendingDirectMessageTest {
    public static void main(String[] args) {
        String recipient = "@stefanduprey";
        String messageToSend = "Thank you for following me, that is so wise";
		Twitter twitter = AccountTwitterFactory.getWiseManTwitter();
        try {
            DirectMessage message = twitter.sendDirectMessage(recipient, messageToSend);
            System.out.println("Direct message successfully sent to " + message.getRecipientScreenName());
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to send a direct message: " + te.getMessage());
            System.exit(-1);
        }
    }
}
