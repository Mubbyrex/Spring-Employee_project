package com.example.thymeleaf.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public int updateEmployee(Employee e) {
        try {
            String query = "update employees set firstName = ?, lastName = ?, position= ?, email=? where id = ?;";

//            debug
//            PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(query);
//
            // Set parameter values in the prepared statement
//            ps.setString(1, e.getFirstName());
//            ps.setString(2, e.getLastName());
//            ps.setString(3, e.getPosition());
//            ps.setString(4, e.getEmail());
//            ps.setInt(5, e.getId());

            // Print the query before execution for debugging purposes
//            System.out.println("Prepared statement query: " + ps.toString());
//
//            // Execute the update statement
//            int rowsUpdated = ps.executeUpdate();
//
//            // Close the prepared statement
//            ps.close();

            return jdbcTemplate.update(query, e.getFirstName(), e.getLastName(), e.getPosition(), e.getEmail(), e.getId());

        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to update employee", ex);
        }
    }

    public int deleteEmployee(int id) {
        try {
            String query = "delete from employees where id = ?";
            return jdbcTemplate.update(query, id);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to delete employee", ex);
        }
    }

}
