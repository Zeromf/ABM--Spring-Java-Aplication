package com.ABM.aplication.Exceptions;

public class UsernameOrIdNotFound extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1284527391881890620L;


	public UsernameOrIdNotFound() {
		super("Usario o Id no encontrado");
		
		
	}
	
	
	public UsernameOrIdNotFound(String message) {
		
		super(message);
		
		
	}
	
	
	
	
	
	
	
	
	
}
