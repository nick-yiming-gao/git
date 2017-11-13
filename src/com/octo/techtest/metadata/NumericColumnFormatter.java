package com.octo.techtest.metadata;

import com.octo.techtest.exception.ConvertException;

public class NumericColumnFormatter extends ColumnFormatter {
	
	public NumericColumnFormatter(String name, int length) {
		super(name, length);
	}

	@Override
	public String output() throws ConvertException{
		try{
			double d = Double.parseDouble(origin.trim());
			return new Double(d).toString();
		}catch(NumberFormatException e){
			throw new ConvertException(origin + " is not a numeric.");
		}
	}

}
