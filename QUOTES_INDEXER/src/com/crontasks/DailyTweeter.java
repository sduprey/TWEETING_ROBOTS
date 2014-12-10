package com.crontasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.crawl.QuoteInfo;

public class DailyTweeter {
	
	//david.hilbert.de@gmail.com @davidhilbert_de
	//wise.man.quoting@gmail.com @wiseman_quoting

	//SELECT ... WHERE to_char(contact.dob, 'MMDD') between '0615' and '0630'...
	//SELECT * from  MATHS_BIO where to_char(MATHS_BIO.birth_date, 'MM') = '12' and to_char(MATHS_BIO.birth_date, 'DD') = '19'
	private static String birthday_selecting_string ="SELECT url, whole_text, title, short_description, h1, name, birth_location, birth_date, death_location, death_date from  MATHS_BIO where to_char(MATHS_BIO.birth_date, 'MM') = ? and to_char(MATHS_BIO.birth_date, 'DD') = ?";

	private static Connection con;

	private static List<QuoteInfo> mathematicians_to_tweet = new ArrayList<QuoteInfo>();
	
	public static void main(String[] args){
		try {
			fetching_all_mathematicians_born_today();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fetching mathematicians from database failed, we quit here.");
			System.exit(0);
		}
	}

	public static void fetching_all_mathematicians_born_today() throws SQLException{
		// here is the links daemon starting point
		// getting all URLS and out.println links for each URL
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		System.out.println("Getting all mathematicans born today : "+today);
		PreparedStatement pst = con.prepareStatement(birthday_selecting_string);
		pst.setInt(1,month);
		pst.setInt(2,day);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			//url, whole_text, title, short_description, h1, name, birth_location, birth_date, death_location, death_date
			QuoteInfo result = new QuoteInfo();
			String url = rs.getString(1);
			String whole_text = rs.getString(2);
			String title = rs.getString(3);
			String short_description = rs.getString(4);
			String h1 = rs.getString(5);
			String name = rs.getString(6);
			String birth_location = rs.getString(7);
			Date birth_date = rs.getDate(8);
			String death_location = rs.getString(9);
			Date death_date = rs.getDate(10);		
			result.setUrl(url);
			result.setName(name);
			result.setBirth_date(birth_date);
			result.setBirth_location(birth_location);
			result.setDeath_date(death_date);
			result.setDeath_location(death_location);
			result.setH1(h1);
			result.setTitle(title);
			result.setShort_description(short_description);
			result.setText(whole_text);
			mathematicians_to_tweet.add(result);
			System.out.println("Putting into cache mathematician  :"+name);
		}
		pst.close();
	}

}
