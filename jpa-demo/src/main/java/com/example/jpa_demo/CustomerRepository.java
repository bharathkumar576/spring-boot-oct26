package com.example.jpa_demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
// JPARepository for Entity: Customer, Primary Keytype: Integer
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    

}
