package com.devfelix.authservice.service;

import com.devfelix.authservice.dto.AccessTokenWrapper;
import com.devfelix.authservice.dto.UserLogin;
import com.devfelix.authservice.exception.AuthenticationException;
import com.devfelix.authservice.model.User;
import com.devfelix.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationImpl implements UserAuthentication{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProviderService tokenProviderService;
    @Override
    public Mono<AccessTokenWrapper> loginUser(UserLogin userLogin) {
        return Mono.fromCallable(()-> {
            //Check if user exists with email
            User movieUser= userRepository.findByEmail(userLogin.email())
                    .orElseThrow(()-> new AuthenticationException("Invalid credentials"));

            if(!passwordEncoder.matches(userLogin.password(),movieUser.getPassword())){
                throw new AuthenticationException("Invalid credentials");
            }
            return tokenProviderService.generateToken(movieUser.getEmail(), movieUser.getId());
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Map<String,Long>> validateToken(String token) {
        return Mono.fromCallable(()->{
            try{
                long userId= tokenProviderService.validateToken(token);
                return Map.of("userId",userId);
            }catch(Exception e){
               throw new RuntimeException();
            }
        }).publishOn(Schedulers.boundedElastic());

    }
}
