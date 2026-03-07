package com.acrevu.acrevu_backend.exception;



public class TokenIsExpired extends RuntimeException{
    public TokenIsExpired(String message){
        super(message);
    }
}
