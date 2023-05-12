package org.ziegelbauer.homepage.models.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No matching blog found")
public class BlogNotFoundException extends RuntimeException {
    public BlogNotFoundException(Integer id) {
        super(String.format("Blog with Id: %d not found", id));
        log.error("No blog matching {} found", id);
    }
}
