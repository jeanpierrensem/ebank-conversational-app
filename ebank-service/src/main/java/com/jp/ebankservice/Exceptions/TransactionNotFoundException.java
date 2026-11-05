package com.jp.ebankservice.Exceptions;

public class TransactionNotFoundException extends RuntimeException  {
    public TransactionNotFoundException(String message) {
    super(message);}
}
