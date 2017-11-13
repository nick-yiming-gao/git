package com.octo.techtest.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.octo.techtest.exception.ConvertException;

public abstract class ColumnFormatter {

	protected String name;
	protected int length;
	protected String origin;

	public static List<ColumnFormatter> initFormatters(File metadata) throws ConvertException {
		List<ColumnFormatter> formatters = new ArrayList<>();
		BufferedReader br = null;
		try {
			// open input data file;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(metadata), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] definitions = line.split(",");
				try {
					if (definitions.length != 3) {
						throw new IllegalArgumentException("Unexpected column definition: " + line);
					}
					int length = Integer.parseInt(definitions[1]);
					formatters.add(getInstance(definitions[0], length, definitions[2]));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Unexpected column definition: " + line);
				}
			}
		} catch (IOException e) {
			throw new ConvertException("Cannot read metadata file in UTF-8 format: " + metadata.getAbsolutePath(), e);
		} catch (Exception e) {
			throw new ConvertException("Error happened when reading metadata file. ", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("Failed to close input file: " + metadata.getAbsolutePath());
					e.printStackTrace();
				}
			}
		}

		return formatters;
	}

	private static ColumnFormatter getInstance(String name, int length, String type) {
		if (type.equalsIgnoreCase("date")) {
			return new DateColumnFormatter(name, length);
		}
		if (type.equalsIgnoreCase("string")) {
			return new StringColumnFormatter(name, length);
		}
		if (type.equalsIgnoreCase("numeric")) {
			return new NumericColumnFormatter(name, length);
		}
		throw new IllegalArgumentException(type + " is not supported!");
	}

	protected ColumnFormatter(String name, int length) {
		super();
		// trim off the double quotations if there are at the beginning and the
		// end as name may be " escaped.
		// this is a bit tricky as it can be done by using apache common csv lib
		// but I don't want to use any third-party library.
		if(name.startsWith("\"") && name.endsWith("\""))
			name = name.substring(1, name.length()-1);
		this.name = name;
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public String input(String content) throws ConvertException {
		if (content.length() < length) {
			throw new ConvertException("Unexpected data length.");
		}
		origin = content.substring(0, length);
		return content.substring(length);
	}

	public abstract String output() throws ConvertException;

}
