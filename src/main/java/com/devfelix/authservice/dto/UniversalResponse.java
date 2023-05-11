package com.devfelix.authservice.dto;


import lombok.Builder;

@Builder
public record UniversalResponse (int status, String message, Object data){
    public static UniversalResponseBuilder builder(){
        return new UniversalResponseBuilder ();
    }
}

