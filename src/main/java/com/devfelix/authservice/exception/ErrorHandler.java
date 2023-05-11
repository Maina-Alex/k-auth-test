package com.devfelix.authservice.exception;

import com.devfelix.authservice.dto.UniversalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Maina
 * @created 01/08/2022
 **/
@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({AuthenticationException.class})
    public Mono<ResponseEntity<Map<String,String>>> handleAuthException(Exception ex){
        return Mono.fromCallable (()-> {
            log.error ("==============> Debug error :  =======> \n  ", ex);
            Map<String,String> errorBody= new HashMap<>();
            errorBody.put("error",ex.getMessage());
            return new ResponseEntity<> (errorBody, HttpStatus.UNAUTHORIZED);
        }).publishOn (Schedulers.boundedElastic ());
    }

    @ExceptionHandler({UserServiceException.class})
    public Mono<ResponseEntity<UniversalResponse>> handleUserServiceException(Exception ex){
        return  Mono.fromCallable(()-> {
            UniversalResponse universalResponse=UniversalResponse.builder().status(400).message(ex.getMessage()).build();
            return new ResponseEntity<>(universalResponse,HttpStatus.OK);
        });

    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<UniversalResponse> handleWebBindException(WebExchangeBindException e) {
        var errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(UniversalResponse.builder().status(400).message("bad request").data(errors)
                .build());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<UniversalResponse>> handleAllServerErrors(Exception ex){
        return Mono.fromCallable (()-> {
            log.error ("==============> Debug error :  =======> \n  ", ex);
            UniversalResponse response= UniversalResponse.builder().status (500).message ("An error occurred").build();
            return new ResponseEntity<> (response, HttpStatus.INTERNAL_SERVER_ERROR);
        }).publishOn (Schedulers.boundedElastic ());
    }

}
