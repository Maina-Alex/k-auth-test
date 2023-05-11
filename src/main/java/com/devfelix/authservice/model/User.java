package com.devfelix.authservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="movie_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean softDelete;
    private Date createdOn;

    @PrePersist
    public void persist(){
        createdOn= new Date();
        softDelete=false;
    }
}
