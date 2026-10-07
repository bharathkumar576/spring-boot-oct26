package com.mahendra.demo2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
	
	private List<Employee> employees = new LinkedList<>();
	
	public EmployeeService() {
	}
	
	
	public Employee create(Employee emp) {
		employees.add(emp);
		return emp;
	}
	
	public Employee getById(Integer id) {
		Optional<Employee> emp = employees.stream().filter(x -> x.getEmpId() == id).findFirst();
		
		if(emp.isPresent()) {
			return emp.get();
		}else {
			throw new RuntimeException("Employee with id "+id+" doesn't exist!" );
		}
		
	}
	
	public List<Employee> getAll(){
		return Collections.unmodifiableList(employees);
	}
	
	public String update(Integer id, Employee empUpdate) {
		try {
		Employee oldEmp = getById(id);
		oldEmp.setDesignation(empUpdate.getDesignation());
		oldEmp.setFirstName(empUpdate.getFirstName());
		oldEmp.setLastName(empUpdate.getLastName());
		return "Employee updated !";
		
		}catch(RuntimeException ex) {
			return ex.getMessage();
		}
	}
	
	
	public void delete(Integer id) {
		
		Employee oldEmp = getById(id);
		employees.remove(oldEmp);
		
	}
	
	
}
