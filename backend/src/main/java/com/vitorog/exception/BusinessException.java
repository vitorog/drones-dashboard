package com.vitorog.exception;

/** Exceptions thrown in the business (service) layer. */
public class BusinessException extends RuntimeException {

  public BusinessException(String message) {
    super(message);
  }
}
