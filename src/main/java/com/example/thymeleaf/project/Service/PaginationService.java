package com.example.thymeleaf.project.Service;

import com.example.thymeleaf.project.Employee;
import com.example.thymeleaf.project.PrincipalUserAccess;
import com.example.thymeleaf.project.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PrincipalUserAccess principalUserAccess;

    public Page<Employee> findPaginated(Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Employee> list;

        List<Employee> employees = this.employeeService.fetchEmployees();
        if (employees.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, employees.size());
            list = employees.subList(startItem, toIndex);
        }

        Page<Employee> employeePage
                = new PageImpl<Employee>(list, PageRequest.of(currentPage, pageSize), employees.size());

        return employeePage;
    }

    public Page<Employee> implementPaginated(Optional<Integer> page, Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Employee> employees;
        return employees = this.findPaginated(PageRequest.of(currentPage - 1, pageSize));
    }

    public List<Integer> pageNum(int totalPages) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            return pageNumbers;
        }

    public String EmployeesWithPagination(Model model, Optional<Integer> page, Optional<Integer> size ){

        Page<Employee> employees = this.implementPaginated(page,size);
        model.addAttribute("employees", employees);


        int totalPages = employees.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = this.pageNum(totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }

//      current logged-in user
        Map<String, String> principal = principalUserAccess.userInfoCollector();
        model.addAttribute("principal", principal);

        return "homepage";
    }
}