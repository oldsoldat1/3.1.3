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
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userServices.addUser(user);
        } else {
            User existingUser = userServices.getUser(user.getId());
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

    @GetMapping("/admin/edit")
    public String editUser(@RequestParam("id") int id, Model model) {
        User user = this.userServices.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("listUser", this.userServices.getAllUsers());
        return "admin";
    }

    @GetMapping("/user")
    public String userData(@AuthenticationPrincipal User authenticatedUser,
                           @RequestParam(required = false) Integer id,
                           Model model) {
        User user;
        if (id != null) {
            user = this.userServices.getUser(id);
            if (user == null) {
                return "redirect:/admin?error=Пользователь+не+найден";
            }
        } else {

            user = authenticatedUser;
        }
        model.addAttribute("user", user);
        return "userData";
    }
}