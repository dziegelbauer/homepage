package org.ziegelbauer.homepage.models.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No matching user found")
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id) {
        super(String.format("No user matching %s found", id));
        log.error("No user matching {} found", id);
    }
}
