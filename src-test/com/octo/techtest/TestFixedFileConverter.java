package com.octo.techtest;

import java.io.File;

import com.octo.techtest.exception.ConvertException;

public class TestFixedFileConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// all files need to be UTF-8 without BOM encoded.
		test("normal");
		test("special-chars");
		test("big-data");
		test("wrong-metadata-type");
		test("wrong-data-type");
	}
	
	
	public static void test(String type){
		File metadata = new File("resources/"+type+"/metadata.csv");
		File input = new File("resources/"+type+"/input.csv");
		File output = new File("resources/"+type+"/output.csv");
		
		try {
			new FixedFileConverter().convert(metadata, input, output);
		} catch (ConvertException e) {
			e.printStackTrace();
		}
	}
}
