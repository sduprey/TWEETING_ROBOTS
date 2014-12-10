package com.urlutilities;

public class QuoteURL_Utilities {
	
	private static String radical = "http://www.worldofquotes.com/author/";
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
