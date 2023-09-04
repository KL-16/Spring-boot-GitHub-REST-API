package com.interview.task.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubBranch {
    private String name;
    private Commit commit;

    @Override
    public String toString() {
        return "{" +
                "name=" + name + "," + '\n'
                + commit +
                "}\n";
    }
}
