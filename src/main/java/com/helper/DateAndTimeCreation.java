package com.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateAndTimeCreation {

	public  String dateAndTimeCreator(String whichTypeOfDate) {
		
		Date currentDate = new Date();
		if(whichTypeOfDate.equals("Date")) {
			
		return  formatDate(currentDate);}
		else if(whichTypeOfDate.equals("Time")) {
		
		return  formatTime(currentDate);}
		
			return formatDateTime(currentDate);
	

}
	private  String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}

	private  String formatTime(Date date) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
		return timeFormat.format(date);
	}
	private  String formatDateTime(Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return dateTimeFormat.format(date);
    }
}
