package org.ziegelbauer.homepage.models.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "A user with that username already exists")
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super(String.format("A user with username: %s already exists", email));
        log.error("A user with username: {} already exists", email);
    }
}
