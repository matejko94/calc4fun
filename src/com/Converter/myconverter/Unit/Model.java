package com.Converter.myConverter.Unit;

public class Model {

//	private int icon;
	private String title;
	private String counter;
	
	private boolean isGroupHeader = false;

/*	public Model(String title) {
		this(-1, title, null);
		isGroupHeader = true;
	}:*/

	public Model( String title, String counter) {
	//	super();
	//	this.icon = icon;
		this.title = title;
		this.counter = counter;
		
	}

	public String getCounter() {
		return counter;
	}

/*	public int getIcon() {
		return icon;
	}
*/
	public String getTitle() {
		return title;
	}
	


	// gettters & setters...
}