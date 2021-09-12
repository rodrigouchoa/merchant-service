package com.merchant.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String uuid) {
        super("Transaction with uuid '" + uuid + "' not found");
    }
}
