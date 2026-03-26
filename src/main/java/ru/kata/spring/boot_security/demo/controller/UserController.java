package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.UserServices;

@Controller
public class UserController {

    private final UserServices userServices;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserServices userServices, PasswordEncoder passwordEncoder) {
        this.userServices = userServices;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/admin")
    public String allUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listUser", this.userServices.getAllUsers());
        return "admin";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("user") User user) {
        if (user.getId() == null || user.getId() == 0) {
            // Проверка на существующий email
            if (userServices.findByEmail(user.getEmail()) != null) {
                return "redirect:/admin?error=Email+already+exists";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userServices.addUser(user);
        } else {
            User existingUser = userServices.getUser(user.getId());
            // Проверка, что email не занят другим пользователем
            User userWithSameEmail = userServices.findByEmail(user.getEmail());
            if (userWithSameEmail != null && !userWithSameEmail.getId().equals(user.getId())) {
                return "redirect:/admin?error=Email+already+exists";
            }

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(existingUser.getPassword());
            }
            this.userServices.updateUser(user);
        }
        return "redirect:/admin?success=true";
    }

    @GetMapping("/admin/remove")
    public String removeUser(@RequestParam("id") int id) {
        this.userServices.removeUser(id);
        return "redirect:/admin";
    }

}