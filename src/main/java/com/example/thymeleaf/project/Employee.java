package com.example.thymeleaf.project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employees")
public class Employee {
    @NotNull
    @Size(min=2, max=30)
    @Column(name = "firstname")
    private String firstName;
    @NotNull
    @Size(min=2, max=30)
    @Column(name = "lastname")
    private String lastName;
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;
    @Column(name = "id")
    @Id
     private Integer id;
    @NotNull
    @Column(name = "position")
     private String position;
    @NotNull
    @Column(name = "password")
     private String password;
    @NotNull
    @Column(name = "roles")
     private String role;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String email, Integer id, String position, String password, String role){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.id=id;
        this.position = position;
        this.password = password;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
