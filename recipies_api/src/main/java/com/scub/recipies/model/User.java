package com.scub.recipies.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Data
@Table(name = "users")
public class User {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id")
    List<Recipes> recipes = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    private Long id;
    @Column(name = "name", unique = true, length = 100)
    private String name;
    @Column(name = "email", unique = true, length = 100)
    private String email;
    @Column(name = "password", length = 100)
    private String password;

}