package com.example.thymeleaf.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

@Service

public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;

    public List<Employee> fetchEmployees(){
        List<Employee> allEmployees = employeeDao.listEmployees();
        return allEmployees;
    }

    public void createEmployee(Employee newEmployee){

//        Encoding the password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode(newEmployee.getPassword());
        newEmployee.setPassword(result);
        newEmployee.setRole(newEmployee.getPosition());

        employeeDao.saveEmployee(newEmployee);
        System.out.println("Done");
    }

    public Employee singleEmployee(String username){
        Employee matchingEmployee = this.fetchEmployees().stream()
                .filter(employee -> employee.getFirstName().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return matchingEmployee;
    }

    public void deleteEmployee(int employee_id){
        employeeDao.deleteEmployee(employee_id);
    }
}
