package com.interview.task.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commit {
    private String sha;

    @Override
    public String toString() {
        return "last commit sha=" + sha;
    }
}
