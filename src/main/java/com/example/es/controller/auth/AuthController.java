package com.example.es.controller.auth;

import com.example.es.entity.Users;
import com.example.es.service.UsersService;
import com.example.es.vo.UserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthController", description = "Authentication related operations")
public class AuthController {

    @Autowired
    private UsersService usersService;


    @PostMapping("/login")
    @Operation(summary = "Login", description = "User login endpoint")
    public ResponseEntity<String> login(@RequestBody UserDetail userDetail) {
        return usersService.login(userDetail);
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "User register endpoint")
    public ResponseEntity<String> register(@RequestBody UserDetail userDetail) {
        return usersService.register(userDetail);
    }
}

