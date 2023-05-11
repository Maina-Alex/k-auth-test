package com.devfelix.authservice.service;

import com.devfelix.authservice.dto.UserDto;
import com.devfelix.authservice.exception.UserServiceException;
import com.devfelix.authservice.model.User;
import com.devfelix.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Mono<User> createUser(UserDto userDto) {
        return Mono.fromCallable(()-> {
            //validate if user exists
            boolean isUserPresent= userRepository.existsByEmail(userDto.email());
            if(isUserPresent){
                // throw exception
                throw new UserServiceException("User already exists");
            }
            User newUser= User.builder()
                    .firstName(userDto.firstName())
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .phone(userDto.phone())
                    .password(passwordEncoder.encode(userDto.password()))
                    .build();

            return userRepository.save(newUser);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<User> getUser(long userId) {
        return Mono.fromCallable(()-> userRepository.findById(userId)
                .orElseThrow(()-> new UserServiceException("User not found"))).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Page<User>> getUsers(Pageable pageable) {
        return Mono.fromCallable(()-> userRepository.findAll(pageable)).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<User> updateUser(UserDto userDto, long id ) {
        return Mono.fromCallable(()-> {
            User user= userRepository.findById(id)
                    .orElseThrow(()-> new UserServiceException("User not found"));
            if(userDto.firstName()!=null){
                user.setFirstName(userDto.firstName());
            }

            if(userDto.lastName()!=null){
                user.setLastName(userDto.lastName());
            }

            if(userDto.phone()!=null){
                user.setPhone(userDto.phone());
            }
            return userRepository.save(user);
        }).publishOn(Schedulers.boundedElastic());
    }

}
