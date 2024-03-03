package com.example.thymeleaf.project.Controller;

import com.example.thymeleaf.project.PrincipalUserAccess;
import com.example.thymeleaf.project.Security.JWTGenerator;
import com.example.thymeleaf.project.Service.EmployeeService;
import com.example.thymeleaf.project.Service.JWTBlacklistService;
import com.example.thymeleaf.project.Service.AuthService;
import com.example.thymeleaf.project.Service.PaginationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

@Controller
@RequestMapping("/auth")
public class AuthController {



    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService){

        this.authService = authService;
    }

    //  login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

//    Login
    @PostMapping(value = "/login")
    public String customLogin(@RequestParam String username, @RequestParam String password,HttpServletRequest req, HttpServletResponse res){
        return authService.login(username,password,req,res);
    }

    @GetMapping("/log_out")
    public String logout(HttpServletRequest request,HttpServletResponse res,HttpSession session) {
        return authService.logout(request,res,session);
    }

}
