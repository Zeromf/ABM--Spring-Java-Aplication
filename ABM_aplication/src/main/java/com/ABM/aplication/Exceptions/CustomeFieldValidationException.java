package com.ABM.aplication.Exceptions;

public class CustomeFieldValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3899956411477508010L;
	
	private String fieldName;
	
	public CustomeFieldValidationException(String message,String fieldName) {
		super(message);
		
		this.fieldName=fieldName;
		
	}
	
	public String getFieldName() {
		
		return this.fieldName;
		
		
	}
	
}
