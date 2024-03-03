package com.example.thymeleaf.project.DTO;

import com.example.thymeleaf.project.Employee;
import com.example.thymeleaf.project.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProtectedController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/filtered")
    public String filtered(Model model){
        List<Employee> employeeList = this.employeeService.fetchEmployees();
        ArrayList<Employee_Protected> employees = filterDetails(employeeList);
        model.addAttribute("employees",employees);
        return "filteredPage";
    }

    public ArrayList<Employee_Protected> filterDetails(List<Employee> employees) {
        ArrayList<Employee_Protected> filteredEmployees = new ArrayList<>();

        for (Employee employee : employees) {
            Employee_Protected details = new Employee_Protected(employee.getFirstName(), employee.getEmail());
            filteredEmployees.add(details);
        }

        return filteredEmployees;
    }

}
