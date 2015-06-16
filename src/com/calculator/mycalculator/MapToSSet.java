package com.calculator.mycalculator;

public class MapToSSet {
	int id;
	KeypadButton keypadButton;
	public MapToSSet() {
		// TODO Auto-generated constructor stub
	}
	public MapToSSet(int id,KeypadButton keypadButton){
		this.id=id;
		this.keypadButton=keypadButton;
		
	}
	
	public int getId() {
		return id;
	}
	public KeypadButton getKeypadButton() {
		return keypadButton;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id+" "+keypadButton;
	}
	


}
