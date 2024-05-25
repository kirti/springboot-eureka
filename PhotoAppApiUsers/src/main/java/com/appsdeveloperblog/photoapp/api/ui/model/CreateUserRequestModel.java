package com.appsdeveloperblog.photoapp.api.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserRequestModel {
	
	@NotNull(message="First name cannot be null")
	@Size(min=2,message="First Name must not be less than two characters")
	private String firstName;
	
	@NotNull(message="last name cannot be null")
	@Size(min=2,message="last Name must not be less than two characters")
	private String lastName;
	
	@NotNull(message="last name cannot be null")
	@Size(min=4,max=6,message="password must be equal or greater than 4 characters and less than 6 characters")
	private String password;
	
	@NotNull(message="email name cannot be null")
	@Email(message="must be valid email ")

	private String email;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
