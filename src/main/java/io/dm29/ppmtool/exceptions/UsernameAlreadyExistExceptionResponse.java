package io.dm29.ppmtool.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameAlreadyExistExceptionResponse {
    private String username;

    public UsernameAlreadyExistExceptionResponse(String username) {
        this.username = username;
    }
}
