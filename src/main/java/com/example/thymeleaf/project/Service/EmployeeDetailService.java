package com.example.thymeleaf.project.Service;
import com.example.thymeleaf.project.Employee;
import com.example.thymeleaf.project.EmployeeDetails;
import com.example.thymeleaf.project.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class EmployeeDetailService implements UserDetailsService{

        @Autowired
        private EmployeeRepository employeeRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            Employee user = employeeRepository.findByFirstName(username);
            if(user ==null) {
                throw new UsernameNotFoundException("User Not Found");
            }

            return new EmployeeDetails(user);
        }

}
