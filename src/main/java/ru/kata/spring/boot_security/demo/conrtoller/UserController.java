package ru.kata.spring.boot_security.demo.conrtoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.security.Principal;

@Controller
public class UserController {
    private final UserService userServiceImp;

    @Autowired
    public UserController(UserService userServiceImp) {
        this.userServiceImp = userServiceImp;
    }

    @GetMapping("/user")
    public String onlyForUser (Principal principal, ModelMap model) {
        User user = userServiceImp.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

}
