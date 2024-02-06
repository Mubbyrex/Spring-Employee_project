package com.example.thymeleaf.project;
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
