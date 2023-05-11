package com.devfelix.authservice.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(
        @NotEmpty(message = "firstName is required")
        String firstName,
        @NotEmpty(message = "lastName is required")
        String lastName,
        @NotEmpty(message = "email is required ")
        String email,
        String phone,
        @NotEmpty(message = "Password is required")
        String password
        ) {
}
