package com.interview.task.Repositories;

import com.interview.task.Model.GitHubBranch;
import com.interview.task.Model.Owner;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GitHubRepository {
    private String name;
    private Owner owner;
    private List<GitHubBranch> branches;

    private boolean fork;

    @Override
    public String toString() {
        return "GitHubRepository{\n" +
                "Repository name=" + name + "," + '\n'
                + owner + ',' + '\n' +
                "branches=" + branches +
                "}\n";
    }
}