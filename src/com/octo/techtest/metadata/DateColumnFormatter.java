package com.octo.techtest.metadata;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.octo.techtest.exception.ConvertException;

public class DateColumnFormatter extends ColumnFormatter {
	
	// Note: SimpleDateFormat is NOT thread safe, this is only for single thread test.
	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");

	public DateColumnFormatter(String name, int length) {
		super(name, length);
	}


	@Override
	public String output() throws ConvertException{
		try{
			Date d = yyyyMMdd.parse(origin.trim());
			return ddMMyyyy.format(d);
		}catch(ParseException e){
			throw new ConvertException(origin + " is not a date format (yyyy-mm-dd).");
		}
	}

}
