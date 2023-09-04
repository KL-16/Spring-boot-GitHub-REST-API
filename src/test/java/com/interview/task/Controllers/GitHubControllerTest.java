package com.interview.task.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.task.Services.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GitHubControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    ObjectMapper objectMapper = new ObjectMapper();

    final static String USER_NAME = "KL-16";
    final static String NON_EXISTING_USER_NAME = "KL-166666";

    @Test
    void shouldReturnStatusOKWhenUserExistsAndHeaderSupported() throws Exception {
        mockMvc.perform(get("/github/repositories/" + USER_NAME)
                        .content(objectMapper.writeValueAsString(USER_NAME))
                        .accept("application/json"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void shouldReturnNotAcceptableStatusWhenXMLHeader() throws Exception {
        mockMvc.perform(get("/github/repositories/" + USER_NAME)
                        .content(objectMapper.writeValueAsString(USER_NAME))
                        .accept("application/xml"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldReturnNotFoundStatusWhenUserDoesNotExist() throws Exception {
        Mockito.when(gitHubService.listNonForkRepositories(NON_EXISTING_USER_NAME))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/github/repositories/" + NON_EXISTING_USER_NAME)
                        .content(objectMapper.writeValueAsString(NON_EXISTING_USER_NAME))
                        .accept("application/json"))
                .andExpect(status().isNotFound());
    }
}
