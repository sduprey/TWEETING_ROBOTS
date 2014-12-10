package com.crawl;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.urlutilities.QuoteURL_Utilities;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class QuotesContinuousCrawler extends WebCrawler {
	// size of the local data cache
	private static int bulk_size = 100;
	private static int counter=0;

	private static String quotes_author= "http://www.worldofquotes.com/author/";
	private static String whole_site ="http://www.worldofquotes.com/";
	Pattern filters = Pattern.compile(".*(\\.(css|js|bmp|gif|jpeg" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|ico|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	QuotesCrawlDataManagement myCrawlDataManager;

	public QuotesContinuousCrawler() {
		myCrawlDataManager = new QuotesCrawlDataManagement();
	}

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !filters.matcher(href).matches() && href.startsWith(whole_site);
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println(url);
		if (url.startsWith(quotes_author)){
			System.out.println(Thread.currentThread()+": Visiting URL : "+url);
			String author = QuoteURL_Utilities.getAuthor(url);
			List<WebURL> links = null;
			if ((page.getParseData() instanceof HtmlParseData)&& (author.length() > 1)) {
				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				String html = htmlParseData.getHtml();
				// parsing here our html to get the content we wish to have
				org.jsoup.nodes.Document doc =  Jsoup.parse(html);
				Elements paragraphs = doc.select("p");
				for (Element paragraph : paragraphs){
					Elements quotes = paragraph.getElementsByAttribute("itemprop");
					if (quotes != null){
						String toAdd = quotes.text();
						if (!"".equals(toAdd)) {			
							QuoteInfo infoquote = new QuoteInfo();
							infoquote.setName(author);
							infoquote.setUrl(url);
							infoquote.setText(toAdd);
							System.out.println("Adding to the cache : "+infoquote.getText());
							myCrawlDataManager.getCrawledContent().add(infoquote);
							myCrawlDataManager.incProcessedPages();	
						}
					}
				}
				links = htmlParseData.getOutgoingUrls();
				myCrawlDataManager.incTotalLinks(links.size());
				myCrawlDataManager.incTotalTextSize(htmlParseData.getText().length());	
				counter++;
				System.out.println("Adding URL : "+url + " to the cache");
				System.out.println("The cache has now  : "+counter + " elements ");
				System.out.println("The cache has now  : "+myCrawlDataManager.getTotalProcessedPages() + " elements ");

			}
			// We save this crawler data after processing every bulk_sizes pages
			if ((myCrawlDataManager.getTotalProcessedPages() != 0 ) && (myCrawlDataManager.getTotalProcessedPages() % bulk_size == 0)) {
				System.out.println("Saving the cache : ");
				saveData();
			}
		}
	}



	// This function is called by controller to get the local data of this
	// crawler when job is finished
	@Override
	public Object getMyLocalData() {
		return myCrawlDataManager;
	}

	// This function is called by controller before finishing the job.
	// You can put whatever stuff you need here.
	@Override
	public void onBeforeExit() {
		saveData();
		try {
			myCrawlDataManager.getCon().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveData(){
		int id = getMyId();
		// This is just an example. Therefore I print on screen. You may
		// probably want to write in a text file.
		System.out.println("Crawler " + id + "> Processed Pages: " + myCrawlDataManager.getTotalProcessedPages());
		System.out.println("Crawler " + id + "> Total Links Found: " + myCrawlDataManager.getTotalLinks());
		System.out.println("Crawler " + id + "> Total Text Size: " + myCrawlDataManager.getTotalTextSize());
		myCrawlDataManager.saveData();	
	}
}