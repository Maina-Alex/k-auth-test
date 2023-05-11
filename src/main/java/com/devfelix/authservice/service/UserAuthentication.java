package com.devfelix.authservice.service;

import com.devfelix.authservice.dto.AccessTokenWrapper;
import com.devfelix.authservice.dto.UserLogin;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserAuthentication {
    Mono<AccessTokenWrapper> loginUser(UserLogin userLogin);

    Mono<Map<String,Long>> validateToken(String token);

}
