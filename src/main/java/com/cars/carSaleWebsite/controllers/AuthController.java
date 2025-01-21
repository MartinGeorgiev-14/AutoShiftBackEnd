package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.*;
import com.cars.carSaleWebsite.models.entities.user.Role;
import com.cars.carSaleWebsite.repository.RoleRepository;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import com.cars.carSaleWebsite.security.JWTGenerator;
import com.cars.carSaleWebsite.security.SecurityConstant;
import com.cars.carSaleWebsite.service.UserEntityService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.apache.el.parser.BooleanNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import javax.management.relation.RoleNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

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
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterDto registerDto) throws RoleNotFoundException {
        Boolean error = false;
        RegisterDto register = new RegisterDto();
        RegisterResponseDto response = new RegisterResponseDto();

        if(userEntityService.existsNameByName(registerDto.getUsername())){
            response.setMessage("Username is taken");
            error = true;
        }
        if(userEntityService.existsEmailByEmail(registerDto.getEmail())){
            response.setMessage("Email is already taken");
            error = true;
        }
        if(!(userEntityService.isValidPassword(registerDto.getPassword()))){
            response.setMessage("Your password must contain at least 1 Upper case and special character");
            error = true;
        }

        if(error){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.setMessage(userEntityService.createUser(registerDto));


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto){
        UserEntityDto user = userEntityService.findUserByUsername(loginDto.getUsername());
        HashSet<RoleDto> roles = userEntityService.getUserRoles(user.getId());


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, user.getId());
        return new ResponseEntity<>(new AuthResponseDTO(token, user.getPhone(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getId(), roles), HttpStatus.OK);
    }

    @PostMapping("getUserInfo")
    public ResponseEntity<AuthResponseDTO> getUserInfo(){
        AuthResponseDTO response = userEntityService.getUserInfo();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
