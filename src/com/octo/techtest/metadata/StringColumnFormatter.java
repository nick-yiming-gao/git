package com.octo.techtest.metadata;

public class StringColumnFormatter extends ColumnFormatter {
	
	public StringColumnFormatter(String name, int length) {
		super(name, length);
	}


	@Override
	public String output(){
		return "\"" + origin.trim() + "\"";
	}

}
