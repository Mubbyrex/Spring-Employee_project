package com.example.thymeleaf.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PaginationService {

    @Autowired
    private EmployeeService employeeService;

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
}