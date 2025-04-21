package com.codenenda.codenenda.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Table(name="users")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    private String password;

    private String name;

    private String email;

    private String token;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified = false;

    @Builder
    public User(String username, String password, String name, String email, String token, boolean verified) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.token = token;
        this.verified = verified;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
