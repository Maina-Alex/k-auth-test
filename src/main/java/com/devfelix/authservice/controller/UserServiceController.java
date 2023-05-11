package com.devfelix.authservice.controller;

import com.devfelix.authservice.dto.UniversalResponse;
import com.devfelix.authservice.dto.UserDto;
import com.devfelix.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserServiceController {
    private final UserService userService;

    @PostMapping("/create")
    public Mono<ResponseEntity<UniversalResponse>> createUser(@RequestBody UserDto userDto){
        return  userService.createUser(userDto)
                .map(res-> UniversalResponse.builder().status(200).message("User created successfully")
                        .data(res).build())
                .map(res->ResponseEntity.ok().body(res));
    }

    @GetMapping("/get/{userId}")
    public Mono<ResponseEntity<?>> getUser(@PathVariable long userId){
        return userService.getUser(userId)
                .map(res-> UniversalResponse.builder().status(200)
                        .data(res).build())
                .map(res-> ResponseEntity.ok().body(res));
    }

    @GetMapping("/get/users")
    public Mono<ResponseEntity<?>> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size ){
        Pageable pageable= PageRequest.of(page, size);

        return userService.getUsers(pageable)
                .map(res-> UniversalResponse.builder().status(200)
                        .data(res).build())
                .map(res-> ResponseEntity.ok().body(res));
    }

    @PostMapping("/update/{userId}")
    public Mono<ResponseEntity<?>> updateUser(@RequestBody UserDto userDto, @PathVariable long userId){
        return userService.updateUser(userDto,userId)
                .map(res-> UniversalResponse.builder().status(200)
                        .data(res).build())
                .map(res-> ResponseEntity.ok().body(res));
    }


}
