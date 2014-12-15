package com.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.urlutilities.QuotesUtilities;

public class QuotesParsingTest {
	public static void main(String[] args){

		String my_url_to_fetch ="http://www.worldofquotes.com/author/Bob+Dylan/1/index.html";

		// fetching data using jQuery
		org.jsoup.nodes.Document doc;
		try{
			// we wait between 30 and 70 seconds
			doc =  Jsoup.connect(my_url_to_fetch)
					.userAgent("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)")
					.referrer("accounterlive.com")
					.ignoreHttpErrors(true)
					.timeout(0)
					.get();
			String author = QuotesUtilities.getAuthor(my_url_to_fetch);
			System.out.println(author);
			List<String> quotesList = new ArrayList<String>();
			Elements paragraphs = doc.select("p");
			for (Element paragraph : paragraphs){
				Elements quotes = paragraph.getElementsByAttribute("itemprop");
				if (quotes != null){
					String toAdd = quotes.text();

					if (!"".equals(toAdd)) {
						quotesList.add(toAdd);
					}
				}
			}
			System.out.println(quotesList);
			//			if (dates.size() == 1){
			//				Element date = dates.get(0);
			//				Elements fontElements = date.getElementsByAttribute("color");
			//				Element birthElement = fontElements.get(0);
			//				String birthText = birthElement.text();
			//				System.out.println(birthText);
			//				Element deathElement = fontElements.get(1);
			//				String deathText = deathElement.text();
			//				DeathBirthInfo birthInfo = MathURL_Utilities.parseBirthDeathInformation(birthText,deathText);
			//				System.out.println(birthInfo);
			//			}


		}
		catch (IOException e) {
			e.printStackTrace();
		} 

	}

}
