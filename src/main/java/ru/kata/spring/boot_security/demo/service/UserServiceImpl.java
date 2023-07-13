package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername (String email) {
        return userRepository.findByUsername(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByUsername(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User `%s` not found", email));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),mapRolesToAuthority(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthority (Collection<Role> roles) {
        return roles.stream().map(r ->new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Transactional
    public void add (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void update (User user) {
        if (user.getRoles() == null) {
            user.setRoles(show(user.getId()).getRoles());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void delete (Long id) {
        userRepository.delete(userRepository.findById(id).get());
    }

    @Transactional
    public List<User> getAll () {
        return userRepository.findAll();
    }

    @Transactional
    public User show (Long id) {
        return userRepository.findById(id).get();
    }
}
