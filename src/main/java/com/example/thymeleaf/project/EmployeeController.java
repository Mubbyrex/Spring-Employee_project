package com.example.thymeleaf.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Optional;

@Controller
public class EmployeeController {
    @Autowired
    private PaginationService paginationService;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PrincipalUserAccess principalUserAccess;

//  All users
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String iteration(Model model,Optional<Integer> page,Optional<Integer> size ){

    //       Pagination logic
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Employee> employees = paginationService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("employees", employees);

        int totalPages = employees.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

//      current logged-in user
        Map<String, String> principal = principalUserAccess.userInfoCollector();
        model.addAttribute("principal", principal);


        return "homepage";
    }

    @GetMapping("/employee-details/{employeeId}")
    public String employee_details(@PathVariable Integer employeeId, Model model) {
        List<Employee> employeesList = this.employeeService.fetchEmployees();
        Employee singleEmployee = null;
        for(Employee employee : employeesList){
            if (employee.getId() == employeeId){
                singleEmployee = employee;
            }
        }
        model.addAttribute("singleEmployee", singleEmployee);
        return "singleEmployee";
    }

//    Form
    @GetMapping("/createForm")
    public String createForm(){
        return "form";
    }


//    Form Submission
@RequestMapping(value = "/register", method = RequestMethod.POST)
public String register(@ModelAttribute("employee") Employee employee,Model model) {
    employeeService.createEmployee(employee);

    Map<String, String> principal = principalUserAccess.userInfoCollector();
    if(principal.get("name") != null){
        //        Pagination logic
        int currentPage = 1;
        int pageSize = 5;
        Page<Employee> employees = paginationService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("employees", employees);
        model.addAttribute("principal", principal);
        return "homepage";
    }
    return "success";
}


//  login page
@GetMapping("/login")
        public String showLoginForm() {
            return "login";
        }


//  admin page
    @GetMapping("/admin")
    public String employee_details(Model model) {
        List<Employee> employees = this.employeeService.fetchEmployees();

        model.addAttribute("employees", employees);
        return "admin";
    }


//    Delete
    @RequestMapping(value = "/employees/delete/{empId}", method = RequestMethod.POST)
    public String delete_employee(@PathVariable Integer empId, Model model){
        employeeService.deleteEmployee(empId);

        List<Employee> employees = this.employeeService.fetchEmployees();
        model.addAttribute("employees", employees);

        return "admin";
    }
}
