package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.Authentication.AuthResponseDTO;
import com.cars.carSaleWebsite.dto.Authentication.RegisterDto;
import com.cars.carSaleWebsite.dto.Authentication.RoleDto;
import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.exceptions.NotFoundException;
import com.cars.carSaleWebsite.models.entities.user.Role;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.repository.user.RoleRepository;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.config.security.JWTGenerator;
import com.cars.carSaleWebsite.service.UserEntityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.management.relation.RoleNotFoundException;
import javax.management.relation.RoleStatus;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    private UserEntityRepository userEntityRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private JWTGenerator jwtGenerator;

    @Autowired
    public UserEntityServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder,
                                 RoleRepository roleRepository, JWTGenerator jwtGenerator) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtGenerator = jwtGenerator;
    }


    @Override
    public UserEntityDto findUserByUsername(String username) {
        UserEntity user = userEntityRepository.findByUserByUsername(username);

        return mapToDto(user);
    }

    @Override
    public Boolean existsEmailByEmail(String email) {
        Boolean find = userEntityRepository.existsByEmail(email);

        return find;
    }

    @Override
    public Boolean existsNameByName(String username) {
        Boolean find = userEntityRepository.existsByUsername(username);

        return find;
    }

    @Override
    public Boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        String upperCaseRegex = String.format(".*[A-Z]{%d,}.*", 1);
        String lowerCaseRegex = String.format(".*[a-z]{%d,}.*", 1);
        String digitRegex = String.format(".*\\d{%d,}.*", 1);
        String specialCharRegex = String.format(".*[^a-zA-Z0-9]{%d,}.*", 1);

        return password.matches(upperCaseRegex) &&
               password.matches(lowerCaseRegex) &&
               password.matches(digitRegex) &&
               password.matches(specialCharRegex);
    }

    @Override
    public String createUser(RegisterDto user) throws RoleNotFoundException {
        UserEntity newUser = new UserEntity();

        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());

        Role roles = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RoleNotFoundException("There is no such role"));
        newUser.setRoles(Collections.singleton(roles));

        userEntityRepository.save(newUser);

        return "User was registered successfully";
    }

    @Override
    public AuthResponseDTO getUserInfo() {
        String token = getJWTFromRequest();
        String userId = getCurrentUserId();
        UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new NotFoundException("User was not found"));

        return mapToAuth(user, token);
    }

    @Override
    public HashSet<RoleDto> getUserRoles(UUID id) {
        HashSet<Object[]> roles = userEntityRepository.findRolesByUserId(id);
        HashSet<RoleDto> roleDtos = new HashSet<>();

        for(Object[] role : roles){
            RoleDto newRole = new RoleDto();

            newRole.setId((UUID) role[0]);
            newRole.setName((String) role[1]);

            roleDtos.add(newRole);
        }

        return roleDtos;
    }


    private AuthResponseDTO mapToAuth(UserEntity user, String token){
        AuthResponseDTO response = new AuthResponseDTO();
        Set<Role> roles = user.getRoles();

        HashSet<RoleDto> mappedRoles = (HashSet<RoleDto>) roles.stream().map(r -> {
            return toRoleDto(r);
        }).collect(Collectors.toSet());

        response.setUserId(user.getId());
        response.setPhone(user.getPhone());
        response.setAccessToken(token);
        response.setEmail(user.getEmail());
        response.setFristName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setUsername(user.getUsername());
        response.setRoles(mappedRoles);

        return response;

    }

    private RoleDto toRoleDto(Role role){
        RoleDto mapped = new RoleDto();

        mapped.setName(role.getName());

        return mapped;
    }

    private UserEntityDto mapToDto(UserEntity user){
        UserEntityDto userDto = new UserEntityDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());

        return userDto;
    }

    private String getCurrentUserId(){
        String token = (String) getJWTFromRequest();
        return jwtGenerator.getUserIdFromJWT(token);
    }

    private String getJWTFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String bearerToken = request.getHeader("Authorization");
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
        }
        return null;
    }
}
