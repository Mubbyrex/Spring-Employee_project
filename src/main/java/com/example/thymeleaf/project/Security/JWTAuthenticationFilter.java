package com.example.thymeleaf.project.Security;

import com.example.thymeleaf.project.Service.EmployeeDetailService;

import com.example.thymeleaf.project.Service.JWTBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
@Component
@Configuration
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    public  JWTGenerator tokenGenerator;
    @Autowired
    private EmployeeDetailService employeeDetailService;
    @Autowired
    private JWTBlacklistService jwtBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        try {
            String token = getJWTFromRequest(request);
            if (StringUtils.hasText(token) && tokenGenerator.validateToken(token) && !jwtBlacklistService.isTokenBlacklisted(token)) {
                String username = tokenGenerator.getUsernameFromJWT(token);

                UserDetails userDetails = employeeDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch(Exception ex){
            System.out.println("Error logging in user: " + ex.getMessage());
//            response.sendRedirect("/auth/login");
            System.out.println("Redirection done");
        }
    }

    private String getJWTFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7,bearerToken.length());
//        }
//        return null;
//    }

        String t = null;
        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();

            for (int i = 0; i < rc.length; i++) {
                if (rc[i].getName().equals("token") == true) {

                    t = rc[i].getValue().toString();
                }
            }
        }

        return t;
    }
}
