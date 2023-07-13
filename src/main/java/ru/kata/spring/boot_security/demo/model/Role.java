package ru.kata.spring.boot_security.demo.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "roles",
        uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role () {}

    public Role(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  name ;
    }
}
