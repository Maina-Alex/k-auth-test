package com.devfelix.authservice.service;

import com.devfelix.authservice.dto.UserDto;
import com.devfelix.authservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(UserDto userDto);
    Mono<User> getUser(long userId);
    Mono<Page<User>> getUsers(Pageable pageable);
    Mono<User> updateUser(UserDto userDto, long id);


}
