package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.*;
import com.cars.carSaleWebsite.repository.RoleRepository;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import com.cars.carSaleWebsite.security.JWTGenerator;
import com.cars.carSaleWebsite.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserEntityRepository userEntityRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    private UserEntityService userEntityService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserEntityRepository userEntityRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator,
                          UserEntityService userEntityService) {
        this.authenticationManager = authenticationManager;
        this.userEntityRepository = userEntityRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.userEntityService = userEntityService;

    }

    @PostMapping("register")
    public ResponseEntity<RegisterDto> register(@RequestBody RegisterDto registerDto) throws RoleNotFoundException {
        Boolean error = false;
        RegisterDto register = new RegisterDto();

        if(userEntityService.existsNameByName(registerDto.getUsername())){
            register.setUsername("Username is taken");
            error = true;
        }
        if(userEntityService.existsEmailByEmail(registerDto.getEmail())){
            register.setEmail("Email is already taken");
            error = true;
        }
        if(!(userEntityService.isValidPassword(registerDto.getPassword()))){
            register.setPassword("Your password must contain at least 1 Upper case and special character");
            error = true;
        }

        if(error){
            return new ResponseEntity<>(register, HttpStatus.BAD_REQUEST);
        }

        String message = userEntityService.createUser(registerDto);
        register.setMessage(message);

        return new ResponseEntity<>(register, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto){
        UserEntityDto user = userEntityService.findUserByUsername(loginDto.getUsername());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<>(new AuthResponseDTO(token, user.getPhone(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getId()), HttpStatus.OK);
    }
}
