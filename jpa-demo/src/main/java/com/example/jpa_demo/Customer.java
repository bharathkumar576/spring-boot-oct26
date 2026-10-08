package com.example.jpa_demo;

import java.util.Date;
import jakarta.persistence.*;

@Entity 
@Table(name="customers")
public class Customer {
    @Id
    @Column(name="cust_id")
    private Integer id;

    @Column(name="first_name", length = 20)
    private String firstName;

    @Column(name="last_name", length = 30)
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Column(name="dateofbirth")
    private Date birthDate;

    public Customer(){ }

    public Customer(Integer id, String firstName, String lastName, Date birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }


}
