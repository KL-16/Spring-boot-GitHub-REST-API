package com.interview.task.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.interview.task.Model.GitHubBranch;
import com.interview.task.Model.Response;
import com.interview.task.Repositories.GitHubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;

    @Autowired
    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> listNonForkRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos?type=all";

        try {
            return getGitHubRepositories(username, url);
        } catch (HttpClientErrorException.NotFound notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .message("GitHub user does not exist")
                            .build().toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity<String> getGitHubRepositories(String username, String url) throws JsonProcessingException {
        ResponseEntity<GitHubRepository[]> response = restTemplate.getForEntity(url, GitHubRepository[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return fetchRepositories(username, response);
        } else {
            throw new RuntimeException("Failed to fetch repositories: " + response.getStatusCode());
        }
    }

    private ResponseEntity<String> fetchRepositories(String username, ResponseEntity<GitHubRepository[]> response) throws JsonProcessingException {
        GitHubRepository[] repositories = response.getBody();
        assert repositories != null;
        List<GitHubRepository> nonForkRepositories = getNonForkGitHubRepositories(repositories);

        for (GitHubRepository repo:nonForkRepositories) {
            getBranches(username, repo);
        }
        try {
            return ResponseEntity.status(HttpStatus.OK).body(Arrays.toString(repositories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error formatting JSON array");
        }
    }

    private void getBranches(String username, GitHubRepository repo) {
        String repoName = repo.getName();
        String branchesUrl = "https://api.github.com/repos/" + username + "/" + repoName + "/branches";
        ResponseEntity<GitHubBranch[]> branchesResponse = restTemplate.getForEntity(branchesUrl, GitHubBranch[].class);

        if (branchesResponse.getStatusCode().is2xxSuccessful()) {
            setRepoBranches(repo, branchesResponse);
        }
    }

    private static void setRepoBranches(GitHubRepository repo, ResponseEntity<GitHubBranch[]> branchesResponse) {
        GitHubBranch[] branches = branchesResponse.getBody();
        assert branches != null;
        List<GitHubBranch> branchList = Arrays.asList(branches);
        repo.setBranches(branchList);
    }

    private static List<GitHubRepository> getNonForkGitHubRepositories(GitHubRepository[] repositories) {
        return Arrays.stream(repositories)
                .filter(repo -> !repo.isFork())
                .collect(Collectors.toList());
    }
}