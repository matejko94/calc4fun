package com.example.mycalc;

public class Item {
	private int id;
	private String title;
	private String description;

	public Item(int id, String title, String description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}

	// getters and setters...

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id+"**"+title+"**"+description;
	}
	
	
}