package com.octo.techtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.octo.techtest.exception.ConvertException;
import com.octo.techtest.metadata.ColumnFormatter;

public class FixedFileConverter {

	public void convert(File metadata, File input, File output) throws ConvertException {
		long start = System.currentTimeMillis();
		System.out.println("=====================================");
		System.out.println("Start converting ...");

		List<ColumnFormatter> formatters = ColumnFormatter.initFormatters(metadata);

		PrintWriter pw = null;
		try {
			// open output file;
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
		} catch (IOException e) {
			throw new ConvertException("Cannot open output file in UTF-8 format: " + output.getAbsolutePath(), e);
		}

		BufferedReader br = null;
		int lineNo = 0;
		try {
			// open input data file;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));

			// prepare data for each line;
			String[] data = new String[formatters.size()];
			// output headers;
			int i = 0;
			for (ColumnFormatter formatter : formatters) {
				data[i++] = formatter.getName();
			}
			println(pw, data);
			// read each line from input data file;
			String line = null;
			while ((line = br.readLine()) != null) {
				lineNo++;
				i = 0;
				for (ColumnFormatter formatter : formatters) {
					// each formatter cuts what it needs from the origin string
					// so what the current formatter needs always starts from
					// the beginning;
					line = formatter.input(line);
					// formatting...
					data[i++] = formatter.output();
				}
				// print formatted data to output file;
				println(pw, data);
			}

		} catch (ConvertException e) {
			throw new ConvertException("Error happened in converting line no. " + lineNo + " : " + e.getMessage(), e);
		} catch (IOException e) {
			throw new ConvertException("Cannot read data file in UTF-8 format: " + input.getAbsolutePath(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("Failed to close input file: " + input.getAbsolutePath());
					e.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		System.out.println("Finished converting ...");
		System.out.println("Total " + lineNo + " lines converted in " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("=====================================");
	}

	private static void println(PrintWriter pw, String[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			sb.append(data[i]);
			if (i < data.length - 1) {
				sb.append(",");
			}
		}
		pw.println(sb.toString());
	}

}
