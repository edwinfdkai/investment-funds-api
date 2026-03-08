package com.test.bank.exception;

public class BusinessException  extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}
