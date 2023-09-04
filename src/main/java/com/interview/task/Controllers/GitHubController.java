package com.interview.task.Controllers;

import com.interview.task.Model.Response;
import com.interview.task.Services.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/github/repositories")
public class GitHubController {

    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubApiService) {
        this.gitHubService = gitHubApiService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<String> listNonForkRepositories(@PathVariable String username, @RequestHeader("Accept") String customHeader) {
        if (customHeader.equals("application/xml")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Response.builder()
                            .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                            .message("XML header not supported")
                            .build().toString());
        }
        return gitHubService.listNonForkRepositories(username);
    }
}