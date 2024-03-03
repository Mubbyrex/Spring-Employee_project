package com.example.thymeleaf.project.Service;

import com.example.thymeleaf.project.PrincipalUserAccess;
import com.example.thymeleaf.project.Security.JWTGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class AuthService {
    private EmployeeService employeeService;
    private PrincipalUserAccess principalUserAccess;
    private PaginationService paginationService;
    private AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;

    private JWTBlacklistService jwtBlacklistService;

    @Autowired
    public AuthService(EmployeeService employeeService, PrincipalUserAccess principalUserAccess, PaginationService paginationService, AuthenticationManager authenticationManager, JWTGenerator jwtGenerator, JWTBlacklistService jwtBlacklistService) {

        this.employeeService = employeeService;
        this.principalUserAccess = principalUserAccess;
        this.paginationService = paginationService;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest req, HttpServletResponse res){
        String token;

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication: "+authentication);
            token = jwtGenerator.generateToken(authentication);
        } catch (UsernameNotFoundException e) {

            System.out.println("Bad Credentials: "+e.getMessage());
            return "redirect:/auth/login";

        } catch(BadCredentialsException e)
        {
            System.out.println("Bad Credentials: "+e.getMessage());
            return "redirect:/auth/login";
        }

        if (token != null) {
            // Create and configure the cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setMaxAge(36000);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            res.addCookie(cookie);
            System.out.println("token: " + token);

            return "redirect:/list";
        }
        System.out.println("token: " + token);
        return "redirect:/auth/login";
    }

    public String logout(HttpServletRequest request,HttpServletResponse res,HttpSession session){
        String msg = null;

        Cookie[] cookies = request.getCookies();
        for(int i = 0; i < cookies.length; i++)
        {
            if (cookies[i].getName().equals("token"))
            {
                jwtBlacklistService.blacklistToken(cookies[i].getValue());

                cookies[i].setMaxAge(0);
                cookies[i].setValue("");
                res.addCookie(cookies[i]);

                System.out.println("Token age: "+ cookies[i].getMaxAge());
                System.out.println("Token value: "+cookies[i].getValue());
                msg = "Logout successfully";

            }
        }
        session.setAttribute("msg", msg);

        return "redirect:/auth/login";
    }
}
