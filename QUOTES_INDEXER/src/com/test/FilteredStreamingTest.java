package com.test;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

import com.factory.AccountTwitterFactory;

public class FilteredStreamingTest {
	public static void main(String[] args){
	    StatusListener listener = new StatusListener(){
	        public void onStatus(Status status) {
	            System.out.println(status.getUser().getName() + " : " + status.getText());
	        }
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
	    };
		
	    TwitterStream twitterStream = AccountTwitterFactory.getWiseManTwitterStream();

	    // old plain output
	    //	    twitterStream.addListener(listener);
        // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
	    //	    twitterStream.sample(); 
	    
	      FilterQuery fq = new FilterQuery();        
          String keywords[] = {"sport", "politics", "health"};
          fq.track(keywords);        
          twitterStream.addListener(listener);
          twitterStream.filter(fq);       
	}
}
