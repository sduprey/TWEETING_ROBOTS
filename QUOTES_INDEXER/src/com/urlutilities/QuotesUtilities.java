package com.urlutilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import twitter4j.Status;
import crawl4j.vsm.CorpusCache;

public class QuotesUtilities {
	private static String radical = "http://www.worldofquotes.com/author/";

	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/quotes_indexer.properties";
	private static Connection con;
	private static String select_all_statement = "select quote_author from QUOTES";
	private static List<QuotesInfo> cached_quotes = new ArrayList<QuotesInfo>();
	static {
		instantiate_connection();
	}

	public static void instantiate_connection(){
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(database_con_path);
			props.load(in);
		} catch (IOException ex) {
			System.out.println("Trouble fetching database configuration");
			ex.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				System.out.println("Trouble fetching database configuration");
				ex.printStackTrace();
			}
		}
		// the following properties have been identified
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");
		try{
			con = DriverManager.getConnection(url, user, passwd);
			// we'll index that in a next step
			//solr_server = new HttpSolrServer("http://localhost:8983/solr");
		} catch (Exception e){
			System.out.println("Error instantiating either database or solr server");
			e.printStackTrace();
		}
	}

	public static void fillUpQuotesCache() throws SQLException {
		PreparedStatement pst = con.prepareStatement(select_all_statement);
		ResultSet rs = pst.executeQuery();
		int counter =0;
		while (rs.next()) {
			counter++;
			String wholeQuote = rs.getString(1);
			QuotesInfo info = parseQuote(wholeQuote);
			cached_quotes.add(info);
			System.out.println("Adding to the cache quote number :"+counter + " : " +info.getQuotes());
			System.out.println("Adding to the cache author number :"+counter + " : " +info.getAuthor());
		}
	}

	public static QuotesInfo parseQuote(String twitterQuote) {
		QuotesInfo info = new QuotesInfo();
		String quote = twitterQuote.substring(twitterQuote.indexOf("\"")+1, twitterQuote.lastIndexOf("\""));
		String author = twitterQuote.substring(twitterQuote.lastIndexOf("\"")+1,twitterQuote.length());
		info.setAuthor(author);
		info.setQuotes(quote);	
		info.setWholeTwitterQuotes(twitterQuote);
		return info;
	}

	public static RankingItem checkingHomeTimeLineForRelevantQuotes(List<Status> statuses){
		List<RankingItem> rankingList = new ArrayList<RankingItem>();
        int counter = 0;
        int size =statuses.size();
		for (Status status : statuses) {
			counter++;
			System.out.println(status.getUser().getScreenName());
			if(!"wiseman_quoting".equals(status.getUser().getScreenName())){
				System.out.println("Status number : "+counter+" over "+size);
				String incomingTwittingText = status.getText();
				QuotesInfo relevantQuote = QuotesUtilities.findMostPertinentQuote(incomingTwittingText);
				RankingItem item = new RankingItem();
				item.setQuoteInfos(relevantQuote);
				item.setTwittingStatus(status);
				item.setAdequation(relevantQuote.getAdequation());
				rankingList.add(item);
				System.out.println("@" + status.getUser().getScreenName() + " - " + incomingTwittingText);
				System.out.println("Relevant quote : " + relevantQuote.getQuotes()+" and adequation : "+relevantQuote.getAdequation());
			}
		}
		Collections.sort(rankingList);
		Collections.reverse(rankingList);
		if (rankingList.size() > 0){
			RankingItem bestItem = rankingList.get(0);
			return bestItem;
		} else {
			return null;
		}
	}

	public static QuotesInfo findMostPertinentQuote(String incoming_tweet){
		for (QuotesInfo info : cached_quotes){
			Double adequation =CorpusCache.computeTFSIDFimilarity(info.getQuotes(), incoming_tweet);
			info.setAdequation(adequation);
		}
		Collections.sort(cached_quotes);
		Collections.reverse(cached_quotes);
		return cached_quotes.get(0);
	}


	public static String getAuthor(String url){
		String name = "";
		url=url.replace(radical, "");
		int index_next_slash = url.indexOf("/");
		if ( index_next_slash!= -1){
			name = url.substring(0, index_next_slash);
		}
		name=name.replace("+", " ");
		return name;
	}
}
