package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.UserServices;


import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserServices userServices;

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PasswordEncoder passwordEncoder, UserServices userServices) {
        this.passwordEncoder = passwordEncoder;
        this.userServices = userServices;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userServices.findByEmail("admin@mail.com") == null) {
            User admin = new User();
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setFirstNameUser("Admin");
            admin.setSecondNameUser("Adminov");
            admin.setPhoneNumberUser("11111111111");
            admin.setAge(30);

            Set<Role> roles = new HashSet<>();
            roles.add(new Role("ADMIN"));
            roles.add(new Role("USER"));
            admin.setRoles(roles);

            userServices.addUser(admin);

        }
    }
}