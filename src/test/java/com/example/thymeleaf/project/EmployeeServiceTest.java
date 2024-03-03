package com.example.thymeleaf.project;

import com.example.thymeleaf.project.Service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmployeeServiceTest {

    @MockBean
    private EmployeeDao employeeDao;
    @Autowired
    @InjectMocks
    private EmployeeService employeeServiceTest;

    @Captor
    ArgumentCaptor<Employee> employeeCaptor;

    @Test
    void fetchEmployeesTest() {

        // Mock behavior of employeeDao.listEmployees()
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee("John", "Doe", "Johndoe@seaico.com", 1, "CEO", "password", "ADMIN"));
        when(employeeDao.listEmployees()).thenReturn(expectedEmployees);

//        when
        List<Employee> actualEmployees = employeeServiceTest.fetchEmployees();
//        then
        assertThat(actualEmployees).isNotNull();
        assertThat(actualEmployees).contains(expectedEmployees.get(0));
    }


    @Test
    void createEmployeeTest() {

        Employee employee = new Employee("jim","bones","jim@gmail.com",200,"BO","password","BO");

//        when
        employeeServiceTest.createEmployee(employee);

        verify(employeeDao).saveEmployee(employeeCaptor.capture());
        Employee employeeCaptorValue = employeeCaptor.getValue();
//        then
        assertThat(employeeCaptorValue.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employeeCaptorValue.getEmail()).isEqualTo(employee.getEmail());
        assertThat(employeeCaptorValue.getRole()).isEqualTo(employee.getRole());
    }

    @Test
    void fetchSingleEmployeeTest() {

        // Mock behavior of employeeDao.listEmployees()
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee("John", "Doe", "Johndoe@seaico.com", 1, "CEO", "password", "ADMIN"));
        when(employeeDao.listEmployees()).thenReturn(expectedEmployees);

//        when
        Employee actualEmployees = employeeServiceTest.singleEmployee("John");

//        then
        assertThat(actualEmployees).isNotNull();
    }

    @Test
    void deleteEmployeeTest() {
//        given
        int id = 1;
//        when
        employeeServiceTest.deleteEmployee(id);
//        then
        verify(employeeDao).deleteEmployee(id);
    }

    @Test
    void updateEmployeeTest() {
        Employee employee = new Employee("jim","bones","jim@gmail.com",200,"BO","password","BO");

//        when
        employeeServiceTest.updateEmployee(employee);

        verify(employeeDao).updateEmployee(employeeCaptor.capture());
        Employee employeeCaptorValue = employeeCaptor.getValue();
//        then
        assertThat(employeeCaptorValue.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employeeCaptorValue.getEmail()).isEqualTo(employee.getEmail());
        assertThat(employeeCaptorValue.getRole()).isEqualTo(employee.getRole());
    }


//    Exceptions
    @Test
    void emptyDatabaseExceptionTest(){
//        given
        List<Employee> expectedEmployees = new ArrayList<>();
        when(employeeDao.listEmployees()).thenReturn(expectedEmployees);

//        when
        List<Employee> actualEmployees = employeeDao.listEmployees();
//        then
        assertThatThrownBy(() -> employeeServiceTest.fetchEmployees())
                .isInstanceOf(EmptyResultDataAccessException.class)
                .hasMessageContaining("Failed to retrieve employees");
    }

    @Test
    void userDoesNotExistTest(){
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee("John", "Doe", "Johndoe@seaico.com", 1, "CEO", "password", "ADMIN"));
        when(employeeDao.listEmployees()).thenReturn(expectedEmployees);

//        when
//        then
        assertThatThrownBy(() -> employeeServiceTest.singleEmployee("Jude"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found: " + "Jude");
    }
}