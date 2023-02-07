package com.adf.function;

import java.util.Date;


public class Employee {

	private int empId;
	private String firstname;
	private String lastname;
	private String address;
	private String city;
	
	
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", firstname=" + firstname + ", lastname=" + lastname + ", address="
				+ address + ", city=" + city + "]";
	}
	
	
	public static Employee createEmployee(String txt) {
		
		String[] empDetails = txt.split(",");

		Employee emp = new Employee();
		emp.setEmpId(Integer.parseInt(empDetails[0]));
		emp.setFirstname(empDetails[1]);
		emp.setLastname(empDetails[2]);
		emp.setAddress(empDetails[3]);
		emp.setCity(empDetails[4]);
		
		return emp;
		
	}
	
	
}