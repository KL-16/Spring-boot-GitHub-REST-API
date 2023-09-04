package com.interview.task.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Owner {
    private String login;

    @Override
    public String toString() {
        return "Owner login=" + login;
    }
}
