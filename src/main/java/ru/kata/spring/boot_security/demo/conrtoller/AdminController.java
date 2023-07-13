package ru.kata.spring.boot_security.demo.conrtoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;


import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    private RoleService roleService;

    @Autowired
    public AdminController (UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/")
    public String printUsers (Principal principal, ModelMap model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        List<User> listOfUsers = userService.getAll();
        model.addAttribute("listOfUsers", listOfUsers);
        return "Users";
    }

    @GetMapping("/addNewUser")
    public String addNewUser (ModelMap model, @ModelAttribute("user") User user) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "Users";
    }
    @PostMapping("/")
    public String saveUser (User user) {
        userService.add(user);
        return "redirect:/admin/";
    }

    @GetMapping("/{id}/edit")
    public String editUser (ModelMap model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.show(id));
        List<Role> listOfRoles = roleService.getAllRoles();
        model.addAttribute("listOfRoles", listOfRoles);
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute ("user") User user, @PathVariable("id") Long id) {
        userService.update(user);
        return "redirect:/admin/";
    }
    @DeleteMapping("/{id}")
    public String  deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/";
    }

}
