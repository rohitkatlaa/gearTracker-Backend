package com.geartracker.geartracker_backend;

import java.time.LocalDate;

public class Utils {
	public static LocalDate string_to_date(String s_date) {
		LocalDate localDate = LocalDate.parse(s_date);
		return localDate;
	}
}
