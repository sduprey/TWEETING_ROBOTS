package com.crawl;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class QuotesCrawlDataManagement {
	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/quotes_indexer.properties";

	private static int twitter_threshold=140;
	private int totalProcessedPages;
	private long totalLinks;
	private long totalTextSize;
	//private HttpSolrServer solr_server;
	private Connection con;

	private Set<QuoteInfo> crawledContent = new HashSet<QuoteInfo>();
	private static String insert_statement="INSERT INTO QUOTES(QUOTE_AUTHOR)"
			+ " VALUES(?)";

	public QuotesCrawlDataManagement() {
		// Reading the property of our database
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

	public int getTotalProcessedPages() {
		return totalProcessedPages;
	}

	public void setTotalProcessedPages(int totalProcessedPages) {
		this.totalProcessedPages = totalProcessedPages;
	}

	public void incProcessedPages() {
		this.totalProcessedPages++;
	}

	public long getTotalLinks() {
		return totalLinks;
	}

	public void setTotalLinks(long totalLinks) {
		this.totalLinks = totalLinks;
	}

	public long getTotalTextSize() {
		return totalTextSize;
	}

	public void setTotalTextSize(long totalTextSize) {
		this.totalTextSize = totalTextSize;
	}

	public void incTotalLinks(int count) {
		this.totalLinks += count;
	}

	public void incTotalTextSize(int count) {
		this.totalTextSize += count;
	}

	public void saveData(){
		//try{
		int local_counter = 0;
		//con.setAutoCommit(false);
		//PreparedStatement st = con.prepareStatement(insert_statement);
		for (QuoteInfo info : crawledContent)	{	
			String to_insert="\""+info.getText()+"\""+info.getName();
			if (to_insert.length()<= twitter_threshold){
				local_counter++;
				try{
					PreparedStatement st = con.prepareStatement(insert_statement);
					st.setString(1,to_insert);
					//st.addBatch();
					st.executeUpdate();
					st.close();
				}
				catch (SQLException e){
					//System.out.println("Line already inserted : "+nb_lines);
					e.printStackTrace();  
				}	
			}
		}	
		//st.executeBatch();		 
		//con.commit();
		System.out.println(Thread.currentThread()+"Committed " + local_counter + " updates");
		//		} catch (SQLException e){
		//			//System.out.println("Line already inserted : "+nb_lines);
		//			e.printStackTrace();  
		//			if (con != null) {
		//				try {
		//					con.rollback();
		//				} catch (SQLException ex1) {
		//					ex1.printStackTrace();
		//				}
		//			}
		//		}	
		crawledContent.clear();
	}

	public Set<QuoteInfo> getCrawledContent() {
		return crawledContent;
	}

	public void setCrawledContent(Set<QuoteInfo> crawledContent) {
		this.crawledContent = crawledContent;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}