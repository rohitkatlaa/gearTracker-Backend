package com.geartracker.geartracker_backend;

import java.time.LocalDate;

public class Utils {
	/*
		Class for common functions.
	*/

	public static LocalDate string_to_date(String s_date) {
		/*
			Function to cast parse string to fetch the date.
		*/
		LocalDate localDate = LocalDate.parse(s_date);
		return localDate;
	}
	
	public static String date_to_string(LocalDate s_date) {
		/*
			Function to case the date to string.
		*/
		String date = s_date.toString();
		return date;
	}
}
