package com.crawl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrawlDataManagement {

	private int totalProcessedPages;
	private long totalLinks;
	private long totalTextSize;
	//private HttpSolrServer solr_server;
	private Connection con;

	private List<QuoteInfo> crawledContent = new ArrayList<QuoteInfo>();
	private static String insert_statement="INSERT INTO QUOTES(QUOTE,AUTHOR)"
			+ " VALUES(?,?)";

	public CrawlDataManagement() {
		//		Properties props = new Properties();
		//		FileInputStream in = null;      
		//		try {
		//			in = new FileInputStream("database.properties");
		//			props.load(in);
		//		} catch (IOException ex) {
		//			Logger lgr = Logger.getLogger(BenchmarkingController.class.getName());
		//			lgr.log(Level.FATAL, ex.getMessage(), ex);
		//		} finally {
		//			try {
		//				if (in != null) {
		//					in.close();
		//				}
		//			} catch (IOException ex) {
		//				Logger lgr = Logger.getLogger(BenchmarkingController.class.getName());
		//				lgr.log(Level.FATAL, ex.getMessage(), ex);
		//			}
		//		}
		// the following properties have been identified
		//		String url = props.getProperty("db.url");
		//		String user = props.getProperty("db.user");
		//		String passwd = props.getProperty("db.passwd");
		String url="jdbc:postgresql://localhost/QUOTESDB";
		String user="postgres";
		String passwd="mogette";

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
		try{
			int local_counter = 0;
			con.setAutoCommit(false);
			PreparedStatement st = con.prepareStatement(insert_statement);
			for (QuoteInfo info : crawledContent)	{	
				local_counter++;
				st.setString(1,info.getText());
				st.setString(2,info.getName());
				st.addBatch();
			}	
			st.executeBatch();		 
			con.commit();
			System.out.println(Thread.currentThread()+"Committed " + local_counter + " updates");
		} catch (SQLException e){
			//System.out.println("Line already inserted : "+nb_lines);
			e.printStackTrace();  
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex1) {
					ex1.printStackTrace();
				}
			}
		}	
		crawledContent.clear();
	}

	public List<QuoteInfo> getCrawledContent() {
		return crawledContent;
	}

	public void setCrawledContent(List<QuoteInfo> crawledContent) {
		this.crawledContent = crawledContent;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}