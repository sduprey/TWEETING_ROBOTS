package com.crawl;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class MathsBioCrawlDataManagement {
	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/mthindexer.properties";

	private int totalProcessedPages;
	private long totalLinks;
	private long totalTextSize;
	//private HttpSolrServer solr_server;
	private Connection con;

	private Map<String, MathematicianURLinfo> crawledContent = new HashMap<String, MathematicianURLinfo>();
	private static String insert_statement="INSERT INTO MATHS_BIO(URL,WHOLE_TEXT,TITLE,SHORT_DESCRIPTION,"
			+ "H1,NAME,BIRTH_LOCATION,BIRTH_DATE,DEATH_LOCATION,DEATH_DATE)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?)";

	public MathsBioCrawlDataManagement() {
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
		try{
			Iterator<Entry<String, MathematicianURLinfo>> it = crawledContent.entrySet().iterator();
			int local_counter = 0;
			if (it.hasNext()){
				local_counter++;
				//con.setAutoCommit(false);
				//PreparedStatement st = con.prepareStatement(insert_statement);
				do {
					local_counter ++;
					Map.Entry<String, MathematicianURLinfo> pairs = (Map.Entry<String, MathematicianURLinfo>)it.next();
					String url=pairs.getKey();
					MathematicianURLinfo info = pairs.getValue();
					PreparedStatement st = con.prepareStatement(insert_statement);
					st.setString(1,url);
					st.setString(2,info.getText());
					st.setString(3,info.getTitle());
					st.setString(4,info.getShort_description());
					st.setString(5,info.getH1());
					st.setString(6,info.getName());
					st.setString(7,info.getBirth_location());
					java.sql.Date birthDate = new java.sql.Date(info.getBirth_date().getTime());
					st.setDate(8,birthDate);
					st.setString(9,info.getDeath_location());
					java.sql.Date deathDate = new java.sql.Date(info.getBirth_date().getTime());
					st.setDate(10,deathDate);
					st.close();
					st.executeUpdate();
					//st.addBatch();
					System.out.println(Thread.currentThread()+"Committed " + url + " update");
				}while (it.hasNext());	
				//st.executeBatch();		 
				//con.commit();
				System.out.println(Thread.currentThread()+"Committed " + local_counter + " updates");
			}
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

	public Map<String, MathematicianURLinfo> getCrawledContent() {
		return crawledContent;
	}

	public void setCrawledContent(Map<String, MathematicianURLinfo> crawledContent) {
		this.crawledContent = crawledContent;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}