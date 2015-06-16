package com.Converter.myConverter.Unit;

import android.content.Context;
import android.content.res.Resources;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EnoteUpravljalec {




	private static String IDENTIFIER_NAME_REGEX = "[a-z0-9_]+";

	// regular expression za iskanje floating pointa
	private static String FLOATING_POINT_REGEX ="[-+]?(?:(?:[0-9]+(?:\\.[0-9]*)?)|(?:\\.[0-9]+))(?:[eE][-+]?[0-9]+)?";

	// Regular expresion za iskanje inta
	private static String INTEGER_REGEX = "[0-9]+";

	public static ArrayList<Enota> getUnits(String category, Context context,  int maxVisibilityLevel)
	{
		Resources resources = context.getResources();
		String filename = category + ".csv";
		ArrayList<Enota> units = new ArrayList<Enota>();
		String pkgname = context.getPackageName();



		// Open the configuration file.
		int id = resources.getIdentifier(category, "raw", pkgname);
		InputStream is;
		try {
			is = resources.openRawResource(id);
		} catch (Resources.NotFoundException e) {

			return units;
		}
		BufferedReader rdr;
		try {
			rdr = new BufferedReader(new InputStreamReader(is, "US-ASCII"));
		} catch (UnsupportedEncodingException e) {

			try {
				is.close();
			} catch (IOException e2) { }
			return units;
		}


		Pattern pat = Pattern.compile("^\\s*(" + IDENTIFIER_NAME_REGEX + ")\\s*," +
									   "\\s*(" + FLOATING_POINT_REGEX  + ")\\s*," +
									   "\\s*(" + INTEGER_REGEX		   + ")\\s*$");
		try {
			String line;


			while ((line = rdr.readLine()) != null) {

				// Skip empty lines and lines containing all whitespace.
				boolean all_whitespace = true;
				for (int i = 0; i < line.length(); i++) {
					if (!Character.isWhitespace(line.charAt(i))) {
						all_whitespace = false;
						break;
					}
				}
				if (all_whitespace)
					continue;


				if (line.charAt(0) == '#')
					continue;

				Matcher m = pat.matcher(line);
				if (m.matches()) {
					String str_identifier_name  = m.group(1);
					String str_normalized_value = m.group(2);

						Enota unit = new Enota(context, str_identifier_name,
											 Double.parseDouble(str_normalized_value)
											);
						units.add(unit);


				}
			}
		} catch (IOException e) {


		}
		try {
			is.close();
		} catch (IOException e) { }
		return units;
	}
};
