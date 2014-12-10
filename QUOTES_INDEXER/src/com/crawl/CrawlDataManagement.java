package com.crawl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CrawlDataManagement {

	private int totalProcessedPages;
	private long totalLinks;
	private long totalTextSize;
	//private HttpSolrServer solr_server;
	private Connection con;

	private Map<String, QuoteInfo> crawledContent = new HashMap<String, QuoteInfo>();
	private static String insert_statement="INSERT INTO MATHS_BIO(URL,WHOLE_TEXT,TITLE,SHORT_DESCRIPTION,"
			+ "H1,NAME,BIRTH_LOCATION,BIRTH_DATE,DEATH_LOCATION,DEATH_DATE)"
			+ " VALUES(?,?,?,?,?,?,?,?,?,?)";

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
		String url="jdbc:postgresql://localhost/MATHSDB";
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

//	public void updateSolrData() {
//		try{
//			Iterator<Entry<String, MathematicianURLinfo>> it = crawledContent.entrySet().iterator();
//			int local_counter = 0;
//			if (it.hasNext()){
//				local_counter++;
//				do {
//					local_counter ++;
//					Map.Entry<String, MathematicianURLinfo> pairs = it.next();
//					String url=(String)pairs.getKey();
//					MathematicianURLinfo info = (MathematicianURLinfo)pairs.getValue();
//					SolrInputDocument doc = new SolrInputDocument();
//					doc.addField("id",url.replace("http://www-history.mcs.st-andrews.ac.uk/",""));
//					doc.addField("url",url);
//					doc.addField("whole_text",info.getText());
//					doc.addField("title",info.getTitle());
//					doc.addField("links_size",info.getLinks_size());
//					doc.addField("links",info.getOut_links());
//					doc.addField("h1",info.getH1());
//					doc.addField("short_description",info.getShort_description());
//					doc.addField("birth_date",info.getBirth_date());
//					doc.addField("birth_place",info.getBirth_location());
//					doc.addField("death_date",info.getDeath_date());
//					doc.addField("death_place",info.getDeath_location());
//					java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
//					doc.addField("last_update", sqlDate.toString());	
//					try{
//						solr_server.add(doc);
//					}catch (Exception e){
//						System.out.println("Trouble inserting : "+url);
//						e.printStackTrace();  
//					}
//				}while (it.hasNext());	
//				solr_server.commit();
//				System.out.println(Thread.currentThread()+"Committed " + local_counter + " updates");
//			}
//		} catch (Exception e){
//			//System.out.println("Line already inserted : "+nb_lines);
//			e.printStackTrace();  
//		}
//	}
//
//	// we here perform upsert to keep up to date our crawl referential
//	public void updateData(){
//		updateSolrData();
//		// clear cache
//		crawledContent.clear();
//	}

	public void saveData(){
		try{
			Iterator<Entry<String, QuoteInfo>> it = crawledContent.entrySet().iterator();
			int local_counter = 0;
			if (it.hasNext()){
				local_counter++;
				con.setAutoCommit(false);
				PreparedStatement st = con.prepareStatement(insert_statement);
				do {
					local_counter ++;
					Map.Entry<String, QuoteInfo> pairs = (Map.Entry<String, QuoteInfo>)it.next();
					String url=pairs.getKey();
					QuoteInfo info = pairs.getValue();
					//					String prepared_string = "("+url+","
					//					                            +(String)list_values[0]+","
					//					                            +(String)list_values[1]+","
					//             					                +(String)list_values[1]+","
					//             					                +(int)list_values[2]+","
					//					                            +(String)list_values[3]+","
					//             					                +(String)list_values[4]+","
					//             					                +(String)list_values[5]+","
					//					                            +(String)list_values[6]+","
					//             					                +(String)list_values[7]+","
					//             					                +(String)list_values[8]+","
					//					                            +(String)list_values[9]+","
					//             					                +(int)list_values[10]+","
					//               					                +(int)list_values[11]+","
					//   					                            +(String)list_values[12]+")";
				
					//MATHS_BIO(URL,WHOLE_TEXT,TITLE,SHORT_DESCRIPTION,H1,NAME,BIRTH_LOCATION,BIRTH_DATE,DEATH_LOCATION,DEATH_DATE)"
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
					st.addBatch();
				}while (it.hasNext());	
				st.executeBatch();		 
				con.commit();
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

	public Map<String, QuoteInfo> getCrawledContent() {
		return crawledContent;
	}

	public void setCrawledContent(Map<String, QuoteInfo> crawledContent) {
		this.crawledContent = crawledContent;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
}