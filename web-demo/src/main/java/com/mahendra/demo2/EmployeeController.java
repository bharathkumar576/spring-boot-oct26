package com.mahendra.demo2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService service;
	
	  @PostMapping
	    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
	        Employee saved = service.create(employee);
	        return ResponseEntity.ok(saved);
	    }

	    @GetMapping
	    public List<Employee> getAll() {
	        return service.getAll();
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Employee> getById(@PathVariable Integer id) {
	        Employee emp = service.getById(id);
	        if (emp != null ) {
	        		return ResponseEntity.ok(emp);
	        }
	        return ResponseEntity.notFound().build();
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Employee employee) {
	        return ResponseEntity.ok(service.update(id, employee));
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> delete(@PathVariable Integer id){
	        service.delete(id);
	        return ResponseEntity.noContent().build();
	    }
}
