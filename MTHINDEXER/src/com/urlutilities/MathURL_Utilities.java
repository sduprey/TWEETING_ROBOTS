package com.urlutilities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.crawl.DeathBirthInfo;

public class MathURL_Utilities {

	public static DeathBirthInfo parseBirthDeathInformation(String birthTextToParse, String deathTextToParse){
		DeathBirthInfo deathbirthInfo = new DeathBirthInfo();
		String[] birthPieces = birthTextToParse.split(" |,");
		if (birthPieces.length > 3){
			int daynumber = Integer.valueOf(birthPieces[0]);
			int monthnumber = getMonthNumber(birthPieces[1]);
			int yearnumber = Integer.valueOf(birthPieces[2]);
			Calendar calendar = new GregorianCalendar(yearnumber,monthnumber,daynumber);
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");	
			//System.out.println(" Birth date " + sdf.format(calendar.getTime()));
			deathbirthInfo.setBirthDate(calendar.getTime());		
			StringBuilder birthLocation = new StringBuilder();
			for (int i=3; i<birthPieces.length;i++){
				if (i!=(birthPieces.length-1)){
					if (!(("".equals(birthPieces[i]))||("in".equals(birthPieces[i])))){
						birthLocation.append(birthPieces[i]+", ");
					}
				} else {
					birthLocation.append(birthPieces[i]);
				}
			}
			deathbirthInfo.setBirthLocation(birthLocation.toString());	
		}
		String[] deathPieces = deathTextToParse.split(" |,");
		if (deathPieces.length > 3){
			int daynumber = Integer.valueOf(deathPieces[0]);
			int monthnumber = getMonthNumber(deathPieces[1]);
			int yearnumber = Integer.valueOf(deathPieces[2]);
			Calendar calendar = new GregorianCalendar(yearnumber,monthnumber,daynumber);
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");	
			//System.out.println(" Death date " + sdf.format(calendar.getTime()));
			deathbirthInfo.setDeathDate(calendar.getTime());		
			StringBuilder deathLocation = new StringBuilder();
			for (int i=3; i<deathPieces.length;i++){
				if (i!=(deathPieces.length-1)){
					if (!(("".equals(deathPieces[i]))||("in".equals(deathPieces[i])))){
						deathLocation.append(deathPieces[i]+", ");
					}
				} else {
					deathLocation.append(deathPieces[i]);
				}
			}
			deathbirthInfo.setDeathLocation(deathLocation.toString());	
		}
		return deathbirthInfo;
	}

	public static int getMonthNumber(String month){
		switch (month) {
		case "January" : 
			return 0;
		case "February" : 
			return 1;
		case "March" : 
			return 2;
		case "April" : 
			return 3;
		case "May" : 
			return 4;
		case "June" : 
			return 5;
		case "July" : 
			return 6;
		case "August" : 
			return 7;
		case "September" : 
			return 8;
		case "October" : 
			return 9;
		case "November" : 
			return 10;
		case "December" : 
			return 11;
		default :
			return 0;
		}
	}
}
