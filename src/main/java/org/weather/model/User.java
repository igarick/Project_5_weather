package org.weather.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table (name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Login must be between 2 and 50 characters")
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 3, max = 8, message = "Password must be between 3 and 8 characters")
    @Column(name = "password")
    private String password;
}
