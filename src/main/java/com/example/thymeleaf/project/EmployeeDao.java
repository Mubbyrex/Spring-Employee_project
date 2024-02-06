package com.example.thymeleaf.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Employee> listEmployees() {
        try {
            String query = "select * from sonoo.employees ORDER BY id";
            return jdbcTemplate.query(query, (rs, rowNum) -> {
                Integer id = rs.getInt("id");
                String position = rs.getString("position");
                String password = rs.getString("password");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");
                String role = rs.getString("roles");

                // Use the constructor to create the Employee object with all values
                Employee e = new Employee(firstName,lastName,email,id, position, password, role);
                return e;
            });
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to retrieve employees", ex);
        }
    }
        public int saveEmployee(Employee e) {
            try {
                String query = "insert into employees(position, password, firstName, lastName, email,roles) values(?, ?,?,?,?,?)";
                return jdbcTemplate.update(query, e.getPosition(), e.getPassword(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getRole());
            } catch (DataAccessException ex) {
                // Handle database-related errors appropriately
                throw new RuntimeException("Failed to save employee", ex);
            }
    }
//    public int updateEmployee(Employee e) {
//        try {
//            String query = "update employee set name = ?, salary = ? where id = ?";
//            return jdbcTemplate.update(query, e.getName(), e.getSalary(), e.getId());
//        } catch (DataAccessException ex) {
//            throw new RuntimeException("Failed to update employee", ex);
//        }
//    }
    public int deleteEmployee(int id) {
        try {
            String query = "delete from employees where id = ?";
            return jdbcTemplate.update(query, id);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to delete employee", ex);
        }
    }

}
