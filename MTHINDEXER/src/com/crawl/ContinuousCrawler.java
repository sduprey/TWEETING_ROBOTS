package com.crawl;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.urlutilities.MathURL_Utilities;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class ContinuousCrawler extends WebCrawler {
	// size of the local data cache
	private static int bulk_size = 100;

	private static String mathematician_biography= "http://www-history.mcs.st-andrews.ac.uk/Biographies/";
	private static String whole_site ="http://www-history.mcs.st-and.ac.uk/";
	Pattern filters = Pattern.compile(".*(\\.(css|js|bmp|gif|jpeg" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|ico|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	CrawlDataManagement myCrawlDataManager;

	public ContinuousCrawler() {
		myCrawlDataManager = new CrawlDataManagement();
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
		if (url.startsWith(mathematician_biography)){
			System.out.println(Thread.currentThread()+": Visiting URL : "+url);
			MathematicianURLinfo info =myCrawlDataManager.getCrawledContent().get(url);
			if (info == null){
				info =new MathematicianURLinfo();
			}		
			info.setUrl(url);
			info.setDepth((int)page.getWebURL().getDepth());
			myCrawlDataManager.incProcessedPages();	

			List<WebURL> links = null;

			if (page.getParseData() instanceof HtmlParseData) {
				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

				String html = htmlParseData.getHtml();
				// parsing here our html to get the content we wish to have
				org.jsoup.nodes.Document doc =  Jsoup.parse(html);
				Elements topoel = doc.select("p");
				
			
				StringBuilder textBuilder = new StringBuilder();
				for (Element paraph : topoel){
					Elements texParaph = paraph.getElementsByAttribute("align");
					textBuilder.append(texParaph.text());
				}
				info.setText(textBuilder.toString());
				System.out.println(textBuilder.toString());
				Elements dates = doc.select("h3");
				if (dates.size() == 1){
					Element date = dates.get(0);
					Elements fontElements = date.getElementsByAttribute("color");
					Element birthElement = fontElements.get(0);
					String birthText = birthElement.text();
					System.out.println(birthText);
					Element deathElement = fontElements.get(1);
					String deathText = deathElement.text();
					DeathBirthInfo birthdeathInfo = MathURL_Utilities.parseBirthDeathInformation(birthText,deathText);
					info.setBirth_date(birthdeathInfo.getBirthDate());
					info.setDeath_date(birthdeathInfo.getDeathDate());
					info.setBirth_location(birthdeathInfo.getBirthLocation());
					info.setDeath_location(birthdeathInfo.getDeathLocation());

					Elements titleel = doc.select("title");
					info.setTitle(titleel.text());
					info.setShort_description(titleel.text());
					// fetching the H1 element
					Elements h1el = doc.select("h1");
					info.setH1(h1el.text());
					info.setName(h1el.text());
				}

				links = htmlParseData.getOutgoingUrls();
				myCrawlDataManager.incTotalLinks(links.size());
				myCrawlDataManager.incTotalTextSize(htmlParseData.getText().length());	

				Set<String> filtered_links = filter_out_links(links);
				info.setLinks_size(filtered_links.size());
				info.setOut_links(filtered_links.toString());

			}

			myCrawlDataManager.getCrawledContent().put(url,info);

			// We save this crawler data after processing every bulk_sizes pages
			if (myCrawlDataManager.getTotalProcessedPages() % bulk_size == 0) {
				saveData();
			}
		}
	}

	public Set<String> filter_out_links(List<WebURL> links){
		Set<String> outputSet = new HashSet<String>();
		for (WebURL url_out : links){
			if ((shouldVisit(url_out)) && (getMyController().getRobotstxtServer().allows(url_out))){
				if (url_out.getURL().startsWith(mathematician_biography)){
					outputSet.add(url_out.getURL());
				}
			}
		}
		return outputSet;
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

	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		String url = webUrl.getURL();
		MathematicianURLinfo info =myCrawlDataManager.getCrawledContent().get(url);
		if (info == null){
			info =new MathematicianURLinfo();
		}	
		info.setStatus_code(statusCode);
		myCrawlDataManager.getCrawledContent().put(url,info);
		//		if (statusCode != HttpStatus.SC_OK) {
		//			if (statusCode == HttpStatus.SC_NOT_FOUND) {
		//				System.out.println("Broken link: " + webUrl.getURL() + ", this link was found in page with docid: " + webUrl.getParentDocid());
		//			} else {
		//				System.out.println("Non success status for link: " + webUrl.getURL() + ", status code: " + statusCode + ", description: " + statusDescription);
		//			}
		//		}
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