package com.devfelix.authservice.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserLogin(
        @NotEmpty(message = "email is required")
        String email,
        @NotEmpty(message = "password is required")
        String password) {
}
