package org.teamSmurfs.backend.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityDeletionException extends RuntimeException {
    public EntityDeletionException(String message) { super(message); }
}
