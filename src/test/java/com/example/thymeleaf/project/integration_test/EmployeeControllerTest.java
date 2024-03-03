package com.example.thymeleaf.project.integration_test;

import com.example.thymeleaf.project.Employee;
import com.example.thymeleaf.project.Service.EmployeeDetailService;
import com.example.thymeleaf.project.Service.EmployeeService;
import com.example.thymeleaf.project.Service.PaginationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    EmployeeDetailService employeeDetailService;

    @Autowired
    PaginationService paginationService;

    @Autowired
    MockMvc mockMvc;



    @Test
    void accessHomepageTest() throws Exception {

        // Build the GET request
        MvcResult result = mockMvc.perform(get("/list").with(user("john").password("password").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
    }

    @Test
    void unauthorizedAccess() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/list"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void accessingEmployeeDetail() throws Exception {
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee("John", "Doe", "Johndoe@seaico.com", 1, "CEO", "password", "ADMIN"));

        // Build the GET request
        MvcResult result = this.mockMvc.perform(get("/employee-details/1").with(user("john").password("password").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
    }

    @Test
    void createFormTest() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/createForm"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
    }


    @Test
    @WithMockUser(roles="ADMIN")
    void registerEmployeeTest() throws Exception {
        Employee employee = new Employee("jim","bones","jim@gmail.com",200,"BO","password","BO");
        Employee mockEmployee = Mockito.mock(Employee.class);

        MvcResult result = this.mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", employee.getFirstName())
                        .param("lastName", employee.getLastName())
                        .param("email", employee.getEmail())
                        .param("password", employee.getPassword())
                        .param("position", employee.getPosition()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void accessingAdminPage() throws Exception {
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee("John", "Doe", "Johndoe@seaico.com", 1, "CEO", "password", "ADMIN"));

        MvcResult result = this.mockMvc.perform(get("/admin")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @WithMockUser(roles="USER")
    void accessingAdminPagewithoutAuthorization() throws Exception {
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee("John", "Doe", "Johndoe@seaico.com", 1, "CEO", "password", "ADMIN"));

        MvcResult result = this.mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void canDeleteEmployee() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/employees/delete/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void editEmployee() throws Exception {
        Employee employee = new Employee("jim","bones","jim@gmail.com",200,"BO","password","BO");
        Employee mockEmployee = Mockito.mock(Employee.class);

        MvcResult result = this.mockMvc.perform(post("/editEmployee/200")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", employee.getFirstName())
                        .param("lastName", "banes")
                        .param("email", "newEmail@gmail.com")
                        .param("password", employee.getPassword())
                        .param("position", employee.getPosition()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();
    }
}
