package com.example.thymeleaf.project.Controller;

import com.example.thymeleaf.project.*;
import com.example.thymeleaf.project.Service.EmployeeService;
import com.example.thymeleaf.project.Service.EmployeeDetailService;
import com.example.thymeleaf.project.Service.PaginationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EmployeeController {
    @Autowired
    private PaginationService paginationService;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeDetailService employeeDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PrincipalUserAccess principalUserAccess;



//  All users
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String homepage(Model model,Optional<Integer> page,Optional<Integer> size ){
        return paginationService.EmployeesWithPagination(model,page,size);
    }


//    Single Employee
    @GetMapping("/employee-details/{employeeId}")
    public String employee_details(@PathVariable Integer employeeId, Model model) {
        return employeeService.employeeDetails(employeeId, model);
    }



    //  admin page
    @GetMapping("/admin")
    public String employee_details(Model model) {
        List<Employee> employees = this.employeeService.fetchEmployees();

        model.addAttribute("employees", employees);
        return "admin";
    }




    @GetMapping("/success")
    public String showSuccess() {
        return "success";
    }



    @PostMapping(value = "/register")
    public String register(@Valid Employee employee, BindingResult bindingResult, Model model, HttpServletRequest request) {

//        Validation
        if (bindingResult.hasErrors()) {
            return "form";
        }

        employeeService.createEmployee(employee);

        Map<String, String> principal = principalUserAccess.userInfoCollector();
        if(principal.get("name") != null){
            //        Pagination logic
            int currentPage = 1;
            int pageSize = 5;
            Page<Employee> employees = paginationService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

            model.addAttribute("employees", employees);
            model.addAttribute("principal", principal);

            int totalPages = employees.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = paginationService.pageNum(totalPages);
                model.addAttribute("pageNumbers", pageNumbers);
            }
            return "homepage";
        }
        return "success";
    }


//    Delete
    @RequestMapping(value = "/employees/delete/{empId}", method = RequestMethod.POST)
    public String delete_employee(@PathVariable Integer empId, Model model){
        employeeService.deleteEmployee(empId);

        List<Employee> employees = this.employeeService.fetchEmployees();
        model.addAttribute("employees", employees);

        return "admin";
    }


//    Edit Employee
    @RequestMapping(value = "/employees/edit/{empId}", method = RequestMethod.POST)
    public String EditEmployee(@PathVariable Integer empId, Model model){

        List<Employee> employeesList = this.employeeService.fetchEmployees();
        Employee singleEmployee = null;
        for(Employee employee : employeesList){
            if (employee.getId() == empId){
                singleEmployee = employee;
            }
        }
        model.addAttribute("employee", singleEmployee);

        return "update";
    }

    @RequestMapping(value = "/editEmployee/{id}", method = RequestMethod.POST)
    public String updateEmployee(@ModelAttribute @Valid Employee employeeData, BindingResult bindingResult, Model model) {


//        Validation
        if (bindingResult.hasErrors() && !bindingResult.hasFieldErrors("password") ) {

            model.addAttribute("employee", employeeData);
            System.out.println(bindingResult.getAllErrors());

            return "update";
        }

        employeeService.updateEmployee(employeeData);

        List<Employee> employeesList = this.employeeService.fetchEmployees();
        model.addAttribute("employees", employeesList);
        return "admin";
    }

    //    Form
    @GetMapping("/createForm")
    public String createForm(Model model){
        model.addAttribute("employee", new Employee());
        return "form";
    }


}
