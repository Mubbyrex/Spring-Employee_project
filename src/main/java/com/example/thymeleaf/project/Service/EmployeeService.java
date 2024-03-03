package com.example.thymeleaf.project.Service;

import com.example.thymeleaf.project.Employee;
import com.example.thymeleaf.project.EmployeeDao;
import com.example.thymeleaf.project.PrincipalUserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private PrincipalUserAccess principalUserAccess;

    public List<Employee> fetchEmployees(){

            List<Employee> allEmployees = employeeDao.listEmployees();
        if (allEmployees.isEmpty() ){
            throw new EmptyResultDataAccessException("Failed to retrieve employees",1);
        }
            return allEmployees;
    }

    public Employee createEmployee(Employee newEmployee){

//        Encoding the password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode(newEmployee.getPassword());
        newEmployee.setPassword(result);
        newEmployee.setRole(newEmployee.getPosition());

        employeeDao.saveEmployee(newEmployee);
        System.out.println("Done");
        return null;
    }

    public Employee singleEmployee(String username){
        Employee matchingEmployee = this.fetchEmployees().stream()
                .filter(employee -> employee.getFirstName().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return matchingEmployee;
    }

    public Employee deleteEmployee(int employee_id){
        employeeDao.deleteEmployee(employee_id);
        return null;
    }

    public Employee updateEmployee(Employee employee){
        employeeDao.updateEmployee(employee);
        return null;
    }



    public String employeeDetails(@PathVariable Integer employeeId, Model model) {
        List<Employee> employeesList = this.fetchEmployees();
        Employee singleEmployee = null;
        for(Employee employee : employeesList){
            if (employee.getId() == employeeId){
                singleEmployee = employee;
            }
        }
        model.addAttribute("singleEmployee", singleEmployee);
        return "singleEmployee";
    }
}

