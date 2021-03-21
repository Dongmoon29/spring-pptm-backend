package io.dm29.ppmtool.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectNotFoundExceptionResponse {
    private String projectIdentifier;

    public ProjectNotFoundExceptionResponse(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
