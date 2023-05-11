package com.devfelix.authservice.controller;

import com.devfelix.authservice.dto.AccessTokenWrapper;
import com.devfelix.authservice.dto.UniversalResponse;
import com.devfelix.authservice.dto.UserLogin;
import com.devfelix.authservice.service.UserAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserAuthentication userAuthentication;
    @PostMapping("/login")
    public Mono<ResponseEntity<UniversalResponse>> loginUser(@RequestBody UserLogin userLogin){
        return userAuthentication.loginUser(userLogin)
                .map(res-> UniversalResponse.builder().status(200).message("Successful login")
                        .data(res).build())
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PostMapping("/validate")
    public Mono<ResponseEntity<?>> validateToken(@RequestParam String token){
        return userAuthentication.validateToken(token)
                .map(ResponseEntity::ok);
    }
}
