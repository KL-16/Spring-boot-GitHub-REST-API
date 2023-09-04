package com.interview.task.Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class Response {

    private HttpStatus httpStatus;

    private String message;

    @Override
    public String toString() {
        return "{" +
                "\nstatus = " + httpStatus +
                ", \nMessage = '" + message + '\'' +
                "\n}";
    }
}
