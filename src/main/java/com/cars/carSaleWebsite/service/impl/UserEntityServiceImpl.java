package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.RegisterDto;
import com.cars.carSaleWebsite.dto.UserEntityDto;
import com.cars.carSaleWebsite.models.entities.user.Role;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.repository.RoleRepository;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import com.cars.carSaleWebsite.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    private UserEntityRepository userEntityRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserEntityServiceImpl(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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
}
