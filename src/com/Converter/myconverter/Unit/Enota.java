package com.Converter.myConverter.Unit;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

// Represents a unit that's available in the unit converter.
public class Enota {

	//Ime ki bo prikazano uprabniku
	private String ime;

	// osnovna enota
	private double pretvorniFaktor;

	private String okrajsavaIme;

	public Enota(Context context, String identifier_name, double normalizedValue)
	{
		Resources resources = context.getResources();
		String pkgname = context.getPackageName();
		try {
			int nameId = resources.getIdentifier(identifier_name, "string", pkgname);
			this.ime = resources.getString(nameId);
		} catch (Resources.NotFoundException e) {
			this.ime = identifier_name;
		}
		
		try {
			int abbrevId = resources.getIdentifier(identifier_name + ".abbrev",
												   "string", pkgname);
			this.okrajsavaIme = resources.getString(abbrevId);
		} catch (Resources.NotFoundException e) {
			Log.e("SIT", "Can't find unit abbreviation: " + identifier_name, e);
			this.okrajsavaIme = identifier_name;
		}

		this.pretvorniFaktor = normalizedValue;

	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Enota))
			return false;
		return getLocalizedName().equals(((Enota)other).getLocalizedName());
	}

	public String getLocalizedName() {
		return ime;
	}


	public double getNormalizedValue() {
		return pretvorniFaktor;
	}
	
	
	public String getAbbreviatedName() {
		return okrajsavaIme;
	}


}
