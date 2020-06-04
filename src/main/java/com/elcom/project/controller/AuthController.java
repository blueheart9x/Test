package com.elcom.project.controller;

import com.elcom.project.auth.CustomUserDetails;
import com.elcom.project.auth.LoginRequest;
import com.elcom.project.auth.LoginResponse;
import com.elcom.project.exception.ValidationException;
import com.elcom.project.auth.jwt.JwtTokenProvider;
import com.elcom.project.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author anhdv
 */
@RestController
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController() {
    }

    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        new UserValidation().validateLogin(loginRequest.getUsername(), loginRequest.getPassword());
        
        // Xác thực thông tin người dùng Request lên, nếu không xảy ra exception tức là thông tin hợp lệ
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
        }catch(AuthenticationException ex) {
            LOGGER.error(ex.toString());
            throw new ValidationException("Sai username/password.");
        }
        
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new LoginResponse(jwt);
    }
}
